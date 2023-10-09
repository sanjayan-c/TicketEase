package com.example.ticketease


import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.ticketease.adapter.InspectorTimetableAdapter
import com.example.ticketease.data.InspectorTimeTableItems
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


class InspectorTimeTable : AppCompatActivity(), InspectorTimetableAdapter.OnStartTripClickListener {

    private lateinit var adapter: InspectorTimetableAdapter
    private lateinit var recyclerView: RecyclerView
    private var selectedDayView: TextView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.inspector_timetable)

        // Find the ImageView for going back
        val cusTransportationTimeTableBack =
            findViewById<ImageView>(R.id.cusTransportationTimeTableBack)

        // Set up the click listener for going back
        cusTransportationTimeTableBack.setOnClickListener {
            val intent = Intent(this@InspectorTimeTable, InspectorHome::class.java)
            startActivity(intent)
        }

        // Get the list of days of the week
//        val daysOfWeek = listOf("Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun")
//
//        // Set up click listeners for each day
//        for (day in daysOfWeek) {
//            val dayView = findViewById<TextView>(resources.getIdentifier("day$day", "id", packageName))
//            dayView.setOnClickListener {
//                handleDayClick(dayView, day)
//            }
//        }

        // Initialize RecyclerView and Adapter
        recyclerView = findViewById(R.id.recyclerInspectorTimetable)
        adapter = InspectorTimetableAdapter(fetchDataForDay("Tue"), this)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)

        // Set a default selected day (e.g., Tuesday)
        val defaultSelectedDay = "Tue"
        selectedDayView = findViewById<TextView>(resources.getIdentifier("day$defaultSelectedDay", "id", packageName))
        selectedDayView?.let {
            handleDayClick(it, defaultSelectedDay)
        }
    }

    private fun handleDayClick(dayView: TextView, day: String) {
        selectedDayView?.isSelected = false // Deselect the previously selected day
        selectedDayView = dayView
        selectedDayView?.isSelected = true  // Select the clicked day

        updateRecyclerViewForSelectedDay(day)
    }



    private fun updateRecyclerViewForSelectedDay(selectedDay: String) {
        val dataForSelectedDay = fetchDataForDay(selectedDay)
        adapter.updateData(dataForSelectedDay)
    }

    private fun fetchDataForDay(day: String): List<InspectorTimeTableItems> {
        // Replace this with your logic to fetch data for the selected day
        if (day == "Mon") {
            return listOf(
                InspectorTimeTableItems("Colombo", "Galle", "10.15 A.M-3.30 P.M", "2023-10-08"),
                InspectorTimeTableItems("Colombo", "Galle", "10.15 A.M-3.30 P.M", "2023-10-08"),
                InspectorTimeTableItems("Colombo", "canada", "10.15 A.M-3.30 P.M", "2023-10-08"),
                InspectorTimeTableItems("Colombo", "india", "10.15 A.M-3.30 P.M", "2023-10-08"),
                InspectorTimeTableItems("Colombo", "Galle", "10.15 A.M-3.30 P.M", "2023-10-08")
            )
        } else if (day == "Tue") {
            return listOf(
                InspectorTimeTableItems("Tue", "Galle", "10.15 A.M-3.30 P.M", "2023-10-08"),
                InspectorTimeTableItems("Colombo", "Galle", "10.15 A.M-3.30 P.M", "2023-10-08"),
                InspectorTimeTableItems("Colombo", "canada", "10.15 A.M-3.30 P.M", "2023-10-08"),
                InspectorTimeTableItems("Colombo", "india", "10.15 A.M-3.30 P.M", "2023-10-08"),
                InspectorTimeTableItems("Colombo", "Galle", "10.15 A.M-3.30 P.M", "2023-10-08")
            )
        } else if (day == "Wed") {
            return listOf(
                InspectorTimeTableItems("Thur", "Galle", "10.15 A.M-3.30 P.M", "2023-10-08"),
                InspectorTimeTableItems("Colombo", "Galle", "10.15 A.M-3.30 P.M", "2023-10-08"),
                InspectorTimeTableItems("Colombo", "canada", "10.15 A.M-3.30 P.M", "2023-10-08"),
                InspectorTimeTableItems("Colombo", "india", "10.15 A.M-3.30 P.M", "2023-10-08"),
                InspectorTimeTableItems("Colombo", "Galle", "10.15 A.M-3.30 P.M", "2023-10-08")
            )
        }
        return emptyList()
    }

    override fun onStartTripClick(position: Int) {
        val intent = Intent(this@InspectorTimeTable, InspectorStartedTrip::class.java)
        startActivity(intent)
    }
}
