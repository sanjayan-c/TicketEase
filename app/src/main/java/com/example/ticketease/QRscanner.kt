package com.example.ticketease

import android.content.Intent
import android.content.pm.ActivityInfo
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.util.Log
import android.view.Gravity
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import com.example.ticketease.data.DistanceDataSingleton
import com.example.ticketease.data.SharedViewModel
import com.example.ticketease.databinding.ActivityMainBinding
import com.google.zxing.integration.android.IntentIntegrator
import com.google.zxing.integration.android.IntentResult
import com.journeyapps.barcodescanner.DecoratedBarcodeView
import java.util.Date
import java.sql.SQLException
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import kotlin.properties.Delegates


//private var Any.destination: Double
//    get() {}
//    set() {}

class QRScanner : AppCompatActivity() {

    //private lateinit var sharedViewModel: SharedViewModel
    private lateinit var scheduleId: String
    private lateinit var vehicleType: String
    private lateinit var binding: QRScanner

    private  var destination :Double = 0.0
    private val cusConSQL = CusConSQL()
   // val totalDistance = MutableLiveData<Double>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        binding=TripService.inflate(layoutInflater)
//        setContentView(binding.root)
        scheduleId = intent.getStringExtra("scheduleId") ?: ""
        vehicleType = intent.getStringExtra("vehicleType") ?: ""
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT

        destination = DistanceDataSingleton.Distance!!
        Log.d("QRclass", "Total Distance : $destination")



//        val sharedViewModel = ViewModelProvider(this).get(SharedViewModel::class.java)
//// Observe changes in totalDistance
//        sharedViewModel.getTotalDistanceLiveData().observe(this) { distance ->
//            // Handle the updated distance in your Activity
//            destination = distance
//            Log.d("QRclass", "Total Distance : $distance")
//            // Now, 'destination' holds the value of 'distance' from your ViewModel
//            // You can use 'destination' as needed in your activity
//        }


//        sharedViewModel = ViewModelProvider.AndroidViewModelFactory(application)
//            .create(SharedViewModel::class.java)
//        val totalDistance = MutableLiveData<Double>()
//
//
//        sharedViewModel.totalDistance.observeForever { distance ->
//            destination = distance
//            Log.d("TotalDistance", "Total Distance in AnotherClass: $distance")
//        }

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
                val customerId: String = result.contents
                Log.d("QRScanner", "Scanned QR Code: $customerId")

                cusConSQL.conclass { connection ->
                    if (connection != null) {
                        try {

                            val query: String
                            query = if (vehicleType == "Bus") {
                                "SELECT * from BusSeatBooking WHERE cusId = ? AND bookingNo = ?"
                            } else {
                                "SELECT * from TrainSeatBooking WHERE cusId = ? AND bookingNo = ?"
                            }

                            val preparedStatement = connection.prepareStatement(query)
                            preparedStatement.setString(1, customerId)
                            preparedStatement.setString(2, scheduleId)

                            val rowsReturned = preparedStatement.executeQuery()

                            if (rowsReturned.next()) {
                                Log.e("success", "customer identified")
                                //   Toast.makeText(requireContext(), "customer identified", Toast.LENGTH_SHORT).show()

                                // updateDistanceTable(customerId)
                                /***************************************************************************************************************************************************/

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
                                            val preparedStatement =
                                                connection.prepareStatement(query)
//                                            preparedStatement.setString(1, scheduleId)
//                                            preparedStatement.setString(2, customerId)

                                            val rowsUpdated = preparedStatement.executeUpdate()

                                            if (rowsUpdated > 0) {
                                                Log.e(
                                                    "success",
                                                    "customer identified and diastance updated"
                                                )

                                                val Query: String
                                                if (vehicleType == "Bus") {
                                                    Query =
                                                        """ SELECT SUM(B.charge),S.StartLocation,S.EndLocation,S.busNo
                                                          FROM BusSeatBooking B,Bus_schedule S
                                                          WHERE B.bookingNo='$scheduleId' AND B.cusId = '$customerId' AND B.bookingNo=S.BusScheduleId
                                           """
                                                } else {
                                                    Query =
                                                        """SELECT SUM(B.charge),S.StartLocation,S.EndLocation,S.busNo
                                                          FROM TrainSeatBooking B,Train_schedule S
                                                          WHERE B.bookingNo='$scheduleId' AND B.cusId = '$customerId' AND B.bookingNo=S.BusScheduleId
                                                """

                                                }
                                                var charge: Double? = null
                                                var StartLocation: String = ""
                                                var EndLocation: String = ""
                                                var BusNo: String = ""


                                                val preparedStatement =
                                                    connection.prepareStatement(Query)
                                                val resultSet = preparedStatement.executeQuery()
                                                while (resultSet.next()) {
                                                    charge = resultSet.getDouble(1)
                                                    if (charge == null) {
                                                        // Handle the case where charge is null
                                                        break  // This will exit the loop
                                                    }
                                                    StartLocation = resultSet.getString(2)
                                                    EndLocation = resultSet.getString(3)
                                                    BusNo = resultSet.getString(4)

                                                    if (charge != 0.0) {
                                                        runOnUiThread {


                                                            val cardView = CardView(this)
                                                            val cardViewLayoutParams =
                                                                LinearLayout.LayoutParams(
                                                                    LinearLayout.LayoutParams.WRAP_CONTENT, // Width - Match the parent
                                                                    LinearLayout.LayoutParams.WRAP_CONTENT  // Height - Wrap content (adjust as needed)
                                                                )
                                                            cardViewLayoutParams.gravity =
                                                                Gravity.CENTER  // Center the CardView
                                                            cardView.layoutParams =
                                                                cardViewLayoutParams

                                                            // Create a TextView to display the charge
                                                            val chargeTextView = TextView(this)
                                                            chargeTextView.text = "Charge: $charge"
                                                            chargeTextView.textSize = 24f
                                                            chargeTextView.layoutParams =
                                                                LinearLayout.LayoutParams(
                                                                    LinearLayout.LayoutParams.WRAP_CONTENT,
                                                                    LinearLayout.LayoutParams.WRAP_CONTENT
                                                                )

                                                            // Add the TextView to the CardView
                                                            cardView.addView(chargeTextView)

                                                            // Assuming you have a LinearLayout as your main layout
                                                            val mainLayout = LinearLayout(this)
                                                            mainLayout.layoutParams =
                                                                LinearLayout.LayoutParams(
                                                                    LinearLayout.LayoutParams.MATCH_PARENT,
                                                                    LinearLayout.LayoutParams.MATCH_PARENT
                                                                )
                                                            mainLayout.orientation =
                                                                LinearLayout.VERTICAL // You can set the orientation as needed

                                                            mainLayout.id =
                                                                View.generateViewId() // Automatically generates a unique ID

                                                            setContentView(mainLayout)

                                                            // Add the CardView to the main layout
                                                            mainLayout.addView(cardView)
                                                        }

                                                    } else {

                                                        runOnUiThread {
                                                            val cardView = CardView(this)
                                                            val cardViewLayoutParams =
                                                                LinearLayout.LayoutParams(
                                                                    LinearLayout.LayoutParams.WRAP_CONTENT, // Width - Match the parent
                                                                    LinearLayout.LayoutParams.WRAP_CONTENT  // Height - Wrap content (adjust as needed)
                                                                )
                                                            cardViewLayoutParams.gravity =
                                                                Gravity.CENTER  // Center the CardView
                                                            cardView.layoutParams =
                                                                cardViewLayoutParams

                                                            // Create a TextView to display the charge
                                                            val chargeTextView = TextView(this)
                                                            chargeTextView.text =
                                                                "Trip started :$customerId"
                                                            chargeTextView.textSize = 24f
                                                            chargeTextView.layoutParams =
                                                                LinearLayout.LayoutParams(
                                                                    LinearLayout.LayoutParams.WRAP_CONTENT,
                                                                    LinearLayout.LayoutParams.WRAP_CONTENT
                                                                )

                                                            // Add the TextView to the CardView
                                                            cardView.addView(chargeTextView)

                                                            // Assuming you have a LinearLayout as your main layout
                                                            val mainLayout = LinearLayout(this)
                                                            mainLayout.layoutParams =
                                                                LinearLayout.LayoutParams(
                                                                    LinearLayout.LayoutParams.MATCH_PARENT,
                                                                    LinearLayout.LayoutParams.MATCH_PARENT
                                                                )
                                                            mainLayout.orientation =
                                                                LinearLayout.VERTICAL // You can set the orientation as needed

                                                            mainLayout.id =
                                                                View.generateViewId() // Automatically generates a unique ID

                                                            setContentView(mainLayout)

                                                            // Add the CardView to the main layout
                                                            mainLayout.addView(cardView)

                                                        }
                                                    }


                                                    // Assuming the charge is in the first column of the result
                                                    var newCharge = -charge
                                                    var newlocation =
                                                        StartLocation + "-" + EndLocation

                                                    val dateFormat = SimpleDateFormat(
                                                        "yyyy-MM-dd",
                                                        Locale.getDefault()
                                                    )
                                                    val timeFormat = SimpleDateFormat(
                                                        "HH:mm:ss",
                                                        Locale.getDefault()
                                                    )

                                                    val currentDate = dateFormat.format(Date())
                                                    val currentTime = timeFormat.format(Date())

                                                    var query: String
                                                    query =
                                                        """INSERT INTO CustomerPayment(cusId,detail,date,time,refNo,price)
                                                             VALUES("$customerId","$newlocation",'$currentDate','$currentTime',"$BusNo",'$newCharge')                                                      
                                                               """
                                                    if (charge != 0.0) {
                                                        var preparedStatement =
                                                            connection.prepareStatement(query)
                                                        preparedStatement.execute()
                                                    }

                                                    query =
                                                        """ SELECT SUM(price) AS balance  FROM `CustomerPayment` WHERE cusId='$customerId'      """

                                                    val preparedStatement =
                                                        connection.prepareStatement(query)
                                                    val resultSet = preparedStatement.executeQuery()
                                                    while (resultSet.next()) {
                                                        val balance: Float =
                                                            resultSet.getFloat("balance")

                                                        if (balance < 0) {
                                                            val cashpay = -balance
                                                            val CashPayment: String = "Cash payment"

                                                            val query =
                                                                """  INSERT INTO CustomerPayment(cusId,detail,date,time,refNo,price)
                                                             VALUES("$customerId","$CashPayment",'$currentDate','$currentTime',"$BusNo",'$cashpay')                                                               
                                                                """
                                                            val preparedStatement =
                                                                connection.prepareStatement(query)
                                                            preparedStatement.execute()

                                                            var paidByWallet=0.0
                                                            if(balance<0)
                                                             paidByWallet = charge - balance

                                                            val query2 =
                                                                """ UPDATE `BusSeatBooking` AS bsb
                                                                                                   JOIN (
                                                                                                   SELECT COUNT(*) AS count
                                                                                                   FROM `BusSeatBooking`
                                                                                                   WHERE cusId = '$customerId' AND bookingNo = '$scheduleId'
                                                                                                   ) AS subquery
                                                                                                   SET bsb.chargeByWallet = '$paidByWallet' / subquery.count
                                                                                                   WHERE bsb.cusId = '$customerId' AND bsb.bookingNo = '$scheduleId'  """

                                                            val preparedStatement2 =
                                                                connection.prepareStatement(query2)
                                                            preparedStatement2.execute()


                                                        }

                                                    }


                                                }


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

                                /***************************************************************************************************************************************************/

                            } else {
                                val insertQuery: String

                                val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                                val timeFormat = SimpleDateFormat("HH:mm:ss", Locale.getDefault())

                                val currentDate = dateFormat.format(Date())
                                val currentTime = timeFormat.format(Date())

                               val query2:String
                                if (vehicleType == "Bus") {

                                     query2 = " SELECT MAX(seatNo) AS maxSeatNo FROM BusSeatBooking WHERE bookingNo = $scheduleId"

                                } else {
                                     query2 = " SELECT MAX(seatNo) FROM TrainSeatBooking WHERE bookingNo = '$scheduleId'"

                                }

                                val preparedStatement = connection.prepareStatement(query2)
                                val resultSet = preparedStatement.executeQuery()
                                while (resultSet.next()) {
                                    val maxSeat = resultSet.getInt("maxSeatNo") + 1
                                    val insertQuery: String
                                    if (vehicleType == "Bus") {

                                        insertQuery = """
                                                     
                                                     INSERT INTO BusSeatBooking (cusId,bookingNo,issuedDate,issuedTime,seatNo) VALUES ("$customerId",'$scheduleId','$currentDate','$currentTime','$maxSeat');
      """
                                    } else {
                                        insertQuery =
                                            """ INSERT INTO TrainSeatBooking (cusId,bookingNo,issuedDate,issuedTime,seatNo) VALUES ("$customerId",'$scheduleId','$currentDate','$currentTime','$maxSeat');
                                        """

                                    }
                                    val insertStatement = connection.prepareStatement(insertQuery)
                                    insertStatement.executeUpdate()
                                }


                            }

                            //  updateDistanceTable(customerId)
                            /***************************************************************************************************************************************************/


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
//                                            preparedStatement.setString(1, scheduleId)
//                                            preparedStatement.setString(2, customerId)

                                        val rowsUpdated = preparedStatement.executeUpdate()

                                        if (rowsUpdated > 0) {
                                            Log.e(
                                                "success",
                                                "customer identified and diatance updated"
                                            )
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
                            /***************************************************************************************************************************************************/


                            }
                         catch (e: SQLException) {
                            e.printStackTrace()
                        }
                    } else {
                        // Handle the case where the database connection is null
                        Log.e("connection", "null")
                    }
                }

//                val intent = Intent(this, InspectorStartedTrip::class.java)
//                startActivity(intent)
            }
        } else {
            // Handle the case where QR code could not be scanned
        }
    }


//    private fun QRCodeResult(customerId: String) {
//
//        val query: String
//
//        query = if (vehicleType == "Bus") {
//            """SELECT * from BusSeatBooking WHERE cusId= ? AND bookingNo= ?"""
//        } else {
//            """SELECT * from TrainSeatBooking WHERE cusId=? AND bookingNo=? """
//        }
//
//        cusConSQL.conclass { connection ->
//            if (connection != null) {
//                try {
//                    val preparedStatement = connection.prepareStatement(query)
//                    preparedStatement.setString(1, customerId)
//                    preparedStatement.setString(2, scheduleId)
//
//
//                    val rowsReturned = preparedStatement.executeQuery()
//
//                    if (rowsReturned != null) {
//                        Log.e("success", "customer identified")
//                        updateDistanceTable(customerId)
//                    } else {
//                        val insertQuery = if (vehicleType == "Bus") {
//                            """SET @maxSeatNo = (SELECT COALESCE(MAX(seatNo) + 1, 1) FROM BusSeatBooking WHERE bookingNo = '$scheduleId');
//INSERT INTO BusSeatBooking (cusId, bookingNo, seatNo) VALUES ('$customerId', '$scheduleId', @maxSeatNo);"""
//                        } else {
//                            """SET @maxSeatNo = (SELECT COALESCE(MAX(seatNo) + 1, 1) FROM TrainSeatBooking WHERE bookingNo = '$scheduleId');
//INSERT INTO TrainSeatBooking (cusId, bookingNo, seatNo) VALUES ('$customerId', '$scheduleId', @maxSeatNo);"""
//                        }
//
//                        try {
//                            val insertStatement = connection.prepareStatement(insertQuery)
//                            insertStatement.executeUpdate()
//                            updateDistanceTable(customerId)
//                        } catch (e: SQLException) {
//                            e.printStackTrace()
//                        } finally {
//                            connection.close()
//                        }
//                    }
//
//                    preparedStatement.close()
//                } catch (e: SQLException) {
//                    e.printStackTrace()
//                } finally {
//                    connection.close()
//                }
//            } else {
//                // Handle the case where the database connection is null
//                Log.e("connection","null")
//            }
//        }
//    }

//    private fun updateDistanceTable(customerId: String) {
//        Log.e("called","updateDistanceTable")
//        cusConSQL.conclass { connection ->
//            val query: String
//
//            query = if (vehicleType == "Bus") {
//                """UPDATE BusSeatBooking b
//JOIN Bus_schedule bs ON b.bookingNo = bs.BusScheduleId
//SET
//    b.StartDistance = CASE
//        WHEN b.StartDistance IS NULL THEN '$destination'
//        ELSE b.StartDistance
//    END,
//    b.EndDistance = CASE
//        WHEN b.StartDistance IS NOT NULL THEN '$destination'
//        ELSE b.EndDistance
//    END,
//    b.charge = CASE
//        WHEN b.StartDistance IS NOT NULL THEN ('$destination' - b.StartDistance) * 100
//        ELSE b.charge
//    END
//WHERE
//    b.BookingNo = '$scheduleId' AND
//    b.CusId = '$customerId' AND
//    bs.TripStarted = TRUE;
//"""
//            } else {
//                """UPDATE TrainSeatBooking b
//JOIN Train_schedule bs ON b.bookingNo = bs.TrainScheduleId
//SET
//    b.StartDistance = CASE
//        WHEN b.StartDistance IS NULL THEN '$destination'
//        ELSE b.StartDistance
//    END,
//    b.EndDistance = CASE
//        WHEN b.StartDistance IS NOT NULL THEN '$destination'
//        ELSE b.EndDistance
//    END,
//    b.charge = CASE
//        WHEN b.StartDistance IS NOT NULL THEN ('$destination' - b.StartDistance) * 100
//        ELSE b.charge
//    END
//WHERE
//    b.BookingNo = '$scheduleId' AND
//    b.CusId = '$customerId' AND
//    bs.TripStarted = TRUE;
//"""
//            }
//
//            if (connection != null) {
//                try {
//                    val preparedStatement = connection.prepareStatement(query)
//                    preparedStatement.setString(1, scheduleId)
//                    preparedStatement.setString(2, customerId)
//
//                    val rowsUpdated = preparedStatement.executeUpdate()
//
//                    if (rowsUpdated > 0) {
//                        Log.e("success", "customer identified")
//                    } else {
//                        // Handle accordingly
//                    }
//
//                    preparedStatement.close()
//                } catch (e: SQLException) {
//                    e.printStackTrace()
//                } finally {
//                    connection.close()
//                }
//            } else {
//                // Handle the case where the database connection is null
//            }
//        }
//    }
}
