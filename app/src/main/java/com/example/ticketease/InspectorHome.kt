package com.example.ticketease

import android.content.Intent
import android.os.Bundle
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity

class InspectorHome: AppCompatActivity()  {

    private lateinit var InsAccountManagement : LinearLayout
    private lateinit var InsTimeTable : LinearLayout
    private lateinit var JourneyHistory : LinearLayout
    private lateinit var Logout : LinearLayout



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.inspector_homepage)

        InsAccountManagement = findViewById(R.id.Ins_Acc_Management)
        InsTimeTable = findViewById(R.id.Ins_Timetable)
        JourneyHistory = findViewById(R.id.Ins_JourneyHistory)
        Logout = findViewById(R.id.logout)

        InsAccountManagement.setOnClickListener { // Start the CustomerAccountManagement activity
            val intent = Intent(this@InspectorHome, InspectorAccountManagement::class.java)
            startActivity(intent)
        }

        InsTimeTable.setOnClickListener { // Start the CustomerAccountManagement activity
            val intent = Intent(this@InspectorHome, InspectorTimeTable::class.java)
            startActivity(intent)
        }

        JourneyHistory.setOnClickListener { // Start the CustomerAccountManagement activity
            val intent = Intent(this@InspectorHome, InspectorJourneyHistory::class.java)
            startActivity(intent)
        }

//        Logout.setOnClickListener { // Start the CustomerAccountManagement activity
//            val intent = Intent(this@CustomerHome, CustomerMyBookings::class.java)
//            startActivity(intent)
//        }



    }
}