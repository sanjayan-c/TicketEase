package com.example.ticketease

import android.content.Intent
import android.content.pm.ActivityInfo
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.util.Log
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import com.google.zxing.integration.android.IntentIntegrator
import com.google.zxing.integration.android.IntentResult
import com.journeyapps.barcodescanner.DecoratedBarcodeView
import java.sql.SQLException
import kotlin.properties.Delegates

class QRScanner : AppCompatActivity() {

    private lateinit var sharedViewModel: SharedViewModel
    private lateinit var scheduleId: String
    private lateinit var vehicleType: String
    private val cusConSQL = CusConSQL()
    private var destination by Delegates.notNull<Double>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        scheduleId = intent.getStringExtra("scheduleId") ?: ""
        vehicleType = intent.getStringExtra("vehicleType") ?: ""
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT

        sharedViewModel = ViewModelProvider.AndroidViewModelFactory(application)
            .create(SharedViewModel::class.java)

        sharedViewModel.totalDistance.observeForever { distance ->
            destination = distance
            Log.d("TotalDistance", "Total Distance in AnotherClass: $distance")
        }

        startQRCodeScanner()
    }

    private fun startQRCodeScanner() {
        val integrator = IntentIntegrator(this)

        integrator.setDesiredBarcodeFormats(IntentIntegrator.QR_CODE)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        integrator.setPrompt(" ")
        integrator.setCameraId(0)
        integrator.setBeepEnabled(false)
        integrator.setOrientationLocked(false)

        integrator.initiateScan()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        val result: IntentResult? = IntentIntegrator.parseActivityResult(requestCode, resultCode, data)

        if (result != null) {
            if (result.contents != null) {
                val customerId = result.contents
                Log.d("QRScanner", "Scanned QR Code: $customerId")
                processQRCodeResult(customerId)
            }

            val intent = Intent(this, InspectorStartedTrip::class.java)
            startActivity(intent)
        } else {
            // Handle case where QR code could not be scanned
        }
    }

    private fun processQRCodeResult(customerId: String) {
        cusConSQL.conclass { connection ->
            val query: String

            query = if (vehicleType == "Bus") {
                """SELECT * from BusSeatBooking WHERE cusId='$customerId' AND bookingNo='$scheduleId'"""
            } else {
                """SELECT * from TrainSeatBooking WHERE cusId='$customerId' AND bookingNo='$scheduleId'"""
            }

            if (connection != null) {
                try {
                    val preparedStatement = connection.prepareStatement(query)
                    preparedStatement.setString(1, scheduleId)
                    preparedStatement.setString(2, customerId)

                    val rowsReturned = preparedStatement.executeQuery()

                    if (rowsReturned != null) {
                        Log.e("success", "customer identified")
                        updateDistanceTable(customerId)
                    } else {
                        val insertQuery = if (vehicleType == "Bus") {
                            """SET @maxSeatNo = (SELECT COALESCE(MAX(seatNo) + 1, 1) FROM BusSeatBooking WHERE bookingNo = '$scheduleId');
INSERT INTO BusSeatBooking (cusId, bookingNo, seatNo) VALUES ('$customerId', '$scheduleId', @maxSeatNo);"""
                        } else {
                            """SET @maxSeatNo = (SELECT COALESCE(MAX(seatNo) + 1, 1) FROM TrainSeatBooking WHERE bookingNo = '$scheduleId');
INSERT INTO TrainSeatBooking (cusId, bookingNo, seatNo) VALUES ('$customerId', '$scheduleId', @maxSeatNo);"""
                        }

                        try {
                            val insertStatement = connection.prepareStatement(insertQuery)
                            insertStatement.executeUpdate()
                            updateDistanceTable(customerId)
                        } catch (e: SQLException) {
                            e.printStackTrace()
                        } finally {
                            connection.close()
                        }
                    }

                    preparedStatement.close()
                } catch (e: SQLException) {
                    e.printStackTrace()
                } finally {
                    connection.close()
                }
            } else {
                // Handle the case where the database connection is null
            }
        }
    }

    private fun updateDistanceTable(customerId: String) {
        cusConSQL.conclass { connection ->
            val query: String

            query = if (vehicleType == "Bus") {
                """UPDATE BusSeatBooking b
JOIN Bus_schedule bs ON b.bookingNo = bs.BusScheduleId
SET 
    b.StartDistance = CASE 
        WHEN b.StartDistance IS NULL THEN '$destination'
        ELSE b.StartDistance 
    END,
    b.EndDistance = CASE 
        WHEN b.StartDistance IS NOT NULL THEN '$destination'
        ELSE b.EndDistance
    END,
    b.charge = CASE 
        WHEN b.StartDistance IS NOT NULL THEN ('$destination' - b.StartDistance) * 100
        ELSE b.charge
    END
WHERE 
    b.BookingNo = '$scheduleId' AND
    b.CusId = '$customerId' AND
    bs.TripStarted = TRUE;
"""
            } else {
                """UPDATE TrainSeatBooking b
JOIN Train_schedule bs ON b.bookingNo = bs.TrainScheduleId
SET 
    b.StartDistance = CASE 
        WHEN b.StartDistance IS NULL THEN '$destination'
        ELSE b.StartDistance 
    END,
    b.EndDistance = CASE 
        WHEN b.StartDistance IS NOT NULL THEN '$destination'
        ELSE b.EndDistance
    END,
    b.charge = CASE 
        WHEN b.StartDistance IS NOT NULL THEN ('$destination' - b.StartDistance) * 100
        ELSE b.charge
    END
WHERE 
    b.BookingNo = '$scheduleId' AND
    b.CusId = '$customerId' AND
    bs.TripStarted = TRUE;
"""
            }

            if (connection != null) {
                try {
                    val preparedStatement = connection.prepareStatement(query)
                    preparedStatement.setString(1, scheduleId)
                    preparedStatement.setString(2, customerId)

                    val rowsUpdated = preparedStatement.executeUpdate()

                    if (rowsUpdated > 0) {
                        Log.e("success", "customer identified")
                    } else {
                        // Handle accordingly
                    }

                    preparedStatement.close()
                } catch (e: SQLException) {
                    e.printStackTrace()
                } finally {
                    connection.close()
                }
            } else {
                // Handle the case where the database connection is null
            }
        }
    }
}
