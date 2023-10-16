package com.example.ticketease

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity


class InspectorStartedTrip : AppCompatActivity() {

    private lateinit var ins_qr: LinearLayout
    private lateinit var TripStartTime: TextView
    private lateinit var scheduleId:String
    private lateinit var vehicleType:String
    private val cusConSQL = CusConSQL()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.insepector_started_trip)
        val cusAccManagementBack = findViewById<ImageView>(R.id.cusTransportationTimeTableBack)
//         EndTrip = findViewById(R.id.endbutton)
        scheduleId = intent.getStringExtra("scheduleId") ?: ""
        vehicleType =intent.getStringExtra("vehicleType") ?: ""


        cusAccManagementBack.setOnClickListener { // Start the CustomerAccountManagement activity
            val intent = Intent(this@InspectorStartedTrip, InspectorTimeTable::class.java)
            startActivity(intent)
        }


        startService(Intent(this, TripService::class.java))



            //  TripStartTime = findViewById(R.id.TripStartTime)
        ins_qr = findViewById(R.id.ins_qr)

        // Initialize SharedPreferences



        ins_qr.setOnClickListener {
            // Start the CustomerAccountManagement activity
            val intent = Intent(this@InspectorStartedTrip, QRScanner::class.java)
            intent.putExtra("scheduleId",scheduleId)
            intent.putExtra("vehicleType",vehicleType)
            startActivity(intent)
        }

        val endTripButton = findViewById<Button>(R.id.endbutton)
        endTripButton.setOnClickListener {
            stopTripService()
            Log.e("service Stoped","done")
        }


    }


    private fun stopTripService() {
        val stopServiceIntent = Intent(TripService.STOP_SERVICE_ACTION)
        sendBroadcast(stopServiceIntent)
    }

}
