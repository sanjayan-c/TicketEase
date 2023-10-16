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
import java.sql.SQLException
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class InspectorJourneyHistory : AppCompatActivity()  {

    private lateinit var InspectorHome : ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.inspector_journey)
//        cusConSQL.conclass { connection ->
//            if (connection != null) {
//
//                try {
//                    // Your database query logic here
//                    // ...
//                    var schedule:String
//                    var vehicleNo:String
//                    val query:String
//                    if(VehicleType=="Bus"){
//                        schedule="Bus_schedule"
//
//
//                         query = """
//                    SELECT * FROM $schedule
//                    WHERE Booking= $vehicleNo
//                """
//
//                    }else{
//                        schedule="Train_schedule"
//
//                        query = """
//                        SELECT * FROM $schedule
//                        WHERE BusNo= $vehicleNo
//                        """
//                    }
//
//
//                     Example query using your commented code
//
//                    val preparedStatement = connection.prepareStatement(query)
//                    preparedStatement.setString(1, date)
//                    preparedStatement.setString(2, VehicleNo)
//
//                   val resultSet = preparedStatement.executeQuery()
//
//                    // Create a list to store the retrieved data
//                    val retrievedData = mutableListOf<InspectorTimeTableItems>()
//
//                    while (resultSet.next()) {
//                        // Retrieve data from the result set and create InspectorTimeTableItems objects
//                        val busScheduleId:String
//                        if(schedule=="Bus_schedule"){
//                            busScheduleId = resultSet.getString("BusScheduleId")
//                        }else{
//                            busScheduleId = resultSet.getString("TrainScheduleId")
//                        }
//
//                        val startLocation = resultSet.getString("StartLocation")
//                        val endLocation = resultSet.getString("EndLocation")
//                        val fromTime = resultSet.getString("FromTime")
//                        val toTime = resultSet.getString("ToTime")
//
//                        // Create an InspectorTimeTableItems object and add it to the list
//                        val inspectorTimeTableItem = InspectorTimeTableItems(
//                            busScheduleId,
//                            date,
//                            startLocation,
//                            endLocation,
//                            fromTime,
//                            toTime
//                        )
//                        retrievedData.add(inspectorTimeTableItem)
//                    }
//
//                    resultSet.close()
//                    preparedStatement.close()
//
//                    // Check if retrievedData is empty
//                    if (retrievedData.isEmpty()) {
//                        runOnUiThread {
//                            // Show the "Nothing to show" TextView
//                        }
//                    } else {
//                        runOnUiThread {
//                            // Hide the loading screen
//
//                            // Sort the retrievedData list using a custom comparator
//                            val sortedData = retrievedData.sortedWith(compareByDescending<InspectorTimeTableItems> {
//                                it.date == date // First, check if the date matches the current date
//                            }.thenByDescending {
//                                it.date // Then, sort by date in descending order
//                            })
//
//                            val recyclerView = findViewById<RecyclerView>(R.id.recyclerInspectorTimetable)
//                            val adapter = InspectorTimetableAdapter(sortedData, this)
//                            recyclerView.adapter = adapter
//                            recyclerView.layoutManager = LinearLayoutManager(this)
//                        }
//                    }
//                } catch (e: SQLException) {
//                    e.printStackTrace()
//                    // Handle any errors
//                } finally {
//                    // Close the connection in the finally block to ensure it's always closed
//                    connection.close()
//                }
//            } else {
//                // Handle the case where the database connection is null
//            }
//        }
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

