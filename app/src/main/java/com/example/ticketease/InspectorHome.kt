package com.example.ticketease

import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Base64
import android.util.Log
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.ticketease.data.ImageDataSingleton
import com.google.firebase.auth.FirebaseAuth
import java.sql.SQLException
// ... (imports and other code)

class InspectorHome : AppCompatActivity() {

    private lateinit var InsAccountManagement: LinearLayout
    private lateinit var InsTimeTable: LinearLayout
    private lateinit var JourneyHistory: LinearLayout
    private lateinit var Logout: LinearLayout

    private val cusConSQL = CusConSQL()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.inspector_homepage)

        InsAccountManagement = findViewById(R.id.Ins_Acc_Management)
        InsTimeTable = findViewById(R.id.Ins_Timetable)
        JourneyHistory = findViewById(R.id.Ins_JourneyHistory)
        Logout = findViewById(R.id.logout)
        val Inspectorname = findViewById<TextView>(R.id.Inspectorname)
        val inspectorIdView = findViewById<TextView>(R.id.inspectorId)
        val  vehicletypeView = findViewById<TextView>(R.id.vehicle)
        val vehicleNoView = findViewById<TextView>(R.id.vehicleno)
        val InsImageView = findViewById<ImageView>(R.id.cusProfileImage)

        var inspectorID: String? = null
        var inspectorFirstName: String? = null
        var inspectorLastName: String? = null
        var busNo: String? = null
        var vehicleType: String? = null
        var InsImage: String? = null
        var TrainNo:String?=null

        cusConSQL.conclass { connection ->
            if (connection != null) {
                val user = 1 // Replace with your actual logic
                val query =
                    """
 SELECT 
    i.insId,
    i.InsFirstname,
    i.InsLastname,
    i.InsImage,
    i.UID,
    b.busNo,
    t.trainNo, -- Add train number column
    CASE 
        WHEN i.insId BETWEEN 1 AND 1000 THEN 'Bus'
        ELSE 'Train'
    END AS VehicleType
FROM inspector i
LEFT JOIN Bus b ON i.insId = b.insId
LEFT JOIN Train t ON i.insId = t.insId -- Add join for Train table
WHERE i.insId = '$user';

    """

                try {
                    val statement = connection.prepareStatement(query)
                    val resultSet = statement.executeQuery(query)

                    while (resultSet.next()) {
                        inspectorID = resultSet.getString("insId")
                        inspectorFirstName = resultSet.getString("InsFirstname") ?: ""
                        inspectorLastName = resultSet.getString("InsLastname") ?: ""
                        busNo = resultSet.getString("busNo")
                        vehicleType = resultSet.getString("VehicleType")
                        InsImage=resultSet.getString("InsImage")
                        TrainNo=resultSet.getString("trainNo")

                        Log.d("InspectorDetails", "inspectorID: $inspectorID")
                        Log.d("InspectorDetails", "inspectorFirstName: $inspectorFirstName")
                        Log.d("InspectorDetails", "inspectorLastName: $inspectorLastName")
                        Log.d("InspectorDetails", "busNo: $busNo")
                        Log.d("InspectorDetails", "VehicleType: $vehicleType")
                        Log.d("CustomerDetails", "cusImage: $InsImage")
                    }

                    statement.close()
                    resultSet.close()
                    ImageDataSingleton.imageData = InsImage
                    ImageDataSingleton.firstName = inspectorFirstName
                    ImageDataSingleton.lasttName = inspectorLastName
                    runOnUiThread {
                        inspectorIdView?.text = inspectorID
                        Inspectorname?.text = "$inspectorFirstName $inspectorLastName"
                        if(vehicleType=="Bus") {
                            vehicleNoView?.text = busNo
                        }else{
                            vehicleNoView?.text = TrainNo
                        }
                        vehicletypeView.text = vehicleType


                        if (InsImage != null) {
                            // Decode the Base64 string to a Bitmap
                            val decodedBytes = Base64.decode(InsImage, Base64.DEFAULT)
                            val decodedBitmap = BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.size)

                            // Set the decoded Bitmap as the image for the ImageView
                            InsImageView?.setImageBitmap(decodedBitmap)
                        } else {
                            // If cusImage is null, you can set a default image or do nothing
                            InsImageView?.setImageResource(R.drawable.cus_image_not_found)
                        }


                        // You can use inspectorImage as needed for displaying images
                    }
                } catch (e: SQLException) {
                    Log.e("SQL Error", "SQL Exception: ${e.message}")
                    e.printStackTrace()
                } catch (e: Exception) {
                    Log.e("General Error", "Error: ${e.message}")
                    e.printStackTrace()
                }
            }
        }

        InsAccountManagement.setOnClickListener {
            val intent = Intent(this@InspectorHome, InspectorAccountManagement::class.java)
            intent.putExtra("inspectorID", inspectorID)
            intent.putExtra("inspectorFirstName", inspectorFirstName)
            intent.putExtra("inspectorLastName", inspectorLastName)
            if(vehicleType=="Bus") {
                intent.putExtra("inspectorVehicleNo", busNo)
            }else{
                intent.putExtra("inspectorVehicleNo", TrainNo)
            }
            intent.putExtra("inspectorVehicleType", vehicleType)

            startActivity(intent)
        }

        InsTimeTable.setOnClickListener {
            val intent = Intent(this@InspectorHome, InspectorTimeTable::class.java)
            intent.putExtra("inspectorVehicleNo", "NC-1341")
            intent.putExtra("inspectorVehicleType",vehicleType)
            startActivity(intent)
        }

        JourneyHistory.setOnClickListener {
            val intent = Intent(this@InspectorHome, InspectorJourneyHistory::class.java)
            intent.putExtra("inspectorVehicleNo", "NC-1341")
            intent.putExtra("inspectorVehicleType",vehicleType)
            startActivity(intent)
        }
    }
}
