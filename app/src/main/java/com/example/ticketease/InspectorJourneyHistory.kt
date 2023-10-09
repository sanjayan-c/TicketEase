package com.example.ticketease

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.ticketease.adapter.InspectorJourneyAdapter
import com.example.ticketease.adapter.InspectorTimetableAdapter
import com.example.ticketease.data.InspectorJourneyItems
import com.example.ticketease.data.InspectorTimeTableItems
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class InspectorJourneyHistory : AppCompatActivity()  {

    private lateinit var InspectorHome : ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.inspector_journey)

        InspectorHome = findViewById(R.id.cusTransportationTimeTableBack)

        InspectorHome.setOnClickListener { // Start the CustomerAccountManagement activity
            val intent = Intent(this@InspectorJourneyHistory, InspectorHome::class.java)
            startActivity(intent)
        }

        // Transport Time Table
        val sampleCusMyBookingsData = listOf(
            InspectorJourneyItems("Colombo", "Galle", "10.15 A.M - 11.30 P.M", "11/11/2023", "50", "47000"),
            InspectorJourneyItems("Colombo", "Galle", "10.15 A.M - 11.30 P.M", "11/11/2023", "50", "47000"),
            InspectorJourneyItems("Colombo", "Galle", "10.15 A.M - 11.30 P.M", "11/11/2023", "50", "47000"),
            InspectorJourneyItems("Colombo", "Galle", "10.15 A.M - 11.30 P.M", "11/11/2023", "50", "47000"),
            InspectorJourneyItems("Colombo", "Galle", "10.15 A.M - 11.30 P.M", "11/11/2023", "50", "47000"),
        )
//        // Sort the sampleCusMyBookingsData list by the "date" property in descending order (latest first)
//        val sortedData = sampleCusMyBookingsData.sortedByDescending { it.date }
        // Get the current date in the format "yyyy-MM-dd"
        val currentDate = SimpleDateFormat("yyyy-MM-dd", Locale.US).format(Date())

        // Sort the sampleCusMyBookingsData list using a custom comparator
        val sortedData = sampleCusMyBookingsData.sortedWith(compareByDescending<InspectorJourneyItems> {
            it.date == currentDate // First, check if the date matches the current date
        }.thenByDescending {
            it.date // Then, sort by date in descending order
        })

        val recyclerView = findViewById<RecyclerView>(R.id.Ins_JourneyHistory)
        val adapter = InspectorJourneyAdapter(sortedData)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)
    }
}

