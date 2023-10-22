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
    private lateinit var VehicleNo:String
    private lateinit var VehicleType:String
    private val cusConSQL = CusConSQL()
    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.inspector_journey)



        VehicleNo = intent.getStringExtra("inspectorVehicleNo") ?: ""
        VehicleType = intent.getStringExtra("inspectorVehicleType") ?: ""

        cusConSQL.conclass { connection ->
            if (connection != null) {

                try {

                    var schedule:String
                    val query:String
                    if(VehicleType=="Bus"){

                         query = """
                             SELECT
    S.StartLocation,
    S.EndLocation,
    S.FromTime AS StartTime,
    S.ToTime AS EndTime,
    S.Date AS TravelDate,
    COUNT(B.cusId) AS NumberOfPassengers,
    SUM(B.charge) AS Revenue
FROM Bus_schedule S
INNER JOIN BusSeatBooking B ON S.BusScheduleId = B.bookingNo
WHERE S.busNo="$VehicleNo"
             
                """

                    }else{

                        query = """
                        SELECT
    S.StartLocation,
    S.EndLocation,
    S.FromTime AS StartTime,
    S.ToTime AS EndTime,
    S.Date AS TravelDate,
    COUNT(B.cusId) AS NumberOfPassengers,
    SUM(B.charge) AS Revenue
FROM Train_schedule S
INNER JOIN TrainSeatBooking B ON S.TrainScheduleId = B.bookingNo
WHERE S.trainNo="$VehicleNo"
                        """
                    }


                    val preparedStatement = connection.prepareStatement(query)
//                    preparedStatement.setString(1, date)
//                    preparedStatement.setString(2, VehicleNo)

                     val resultSet = preparedStatement.executeQuery()

                    // Create a list to store the retrieved data
                      val retrievedData = mutableListOf<InspectorJourneyItems>()

                    while (resultSet.next()) {
                        // Retrieve data from the result set and create InspectorTimeTableItems objects


                        val startLocation = resultSet.getString("StartLocation")
                        val endLocation = resultSet.getString("EndLocation")
                        val fromTime = resultSet.getString("StartTime")
                        val toTime = resultSet.getString("EndTime")
                        val Date = resultSet.getString("TravelDate")
                        val NumberOfPassengers = resultSet.getString("NumberOfPassengers")
                        val Revenue = resultSet.getString("Revenue")

                        // Create an InspectorTimeTableItems object and add it to the list
                        val InspectorJourneyItems = InspectorJourneyItems(
                            startLocation,
                            endLocation,
                            fromTime+"-"+toTime,
                            Date,
                            NumberOfPassengers,
                            Revenue
                        )
                        retrievedData.add(InspectorJourneyItems)
                    }

                    resultSet.close()
                    preparedStatement.close()

                    // Check if retrievedData is empty
                    if (retrievedData.isEmpty()) {
                        runOnUiThread {
                            // Show the "Nothing to show" TextView
                        }
                    } else {
                        runOnUiThread {
                            // Hide the loading screen
                          //val sortedData = sampleCusMyBookingsData.sortedByDescending { it.date }
//        // Get the current date in the format "yyyy-MM-dd"
                          val currentDate = SimpleDateFormat("yyyy-MM-dd", Locale.US).format(Date())

                            // Sort the retrievedData list using a custom comparator
                            val sortedData = retrievedData.sortedWith(compareByDescending<InspectorJourneyItems> {
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
                } catch (e: SQLException) {
                    e.printStackTrace()
                    // Handle any errors
                } finally {
                    // Close the connection in the finally block to ensure it's always closed
                    connection.close()
                }
            } else {
                // Handle the case where the database connection is null
            }
        }
        InspectorHome = findViewById(R.id.cusTransportationTimeTableBack)

        InspectorHome.setOnClickListener { // Start the CustomerAccountManagement activity
            val intent = Intent(this@InspectorJourneyHistory, InspectorHome::class.java)
            startActivity(intent)
        }

//
//
//        // Transport Time Table
//        val sampleCusMyBookingsData = listOf(
//            InspectorJourneyItems("Colombo", "Galle", "10.15 A.M - 11.30 P.M", "11/11/2023", "50", "47000"),
//            InspectorJourneyItems("Colombo", "Galle", "10.15 A.M - 11.30 P.M", "11/11/2023", "50", "47000"),
//            InspectorJourneyItems("Colombo", "Galle", "10.15 A.M - 11.30 P.M", "11/11/2023", "50", "47000"),
//            InspectorJourneyItems("Colombo", "Galle", "10.15 A.M - 11.30 P.M", "11/11/2023", "50", "47000"),
//            InspectorJourneyItems("Colombo", "Galle", "10.15 A.M - 11.30 P.M", "11/11/2023", "50", "47000"),
//        )
////        // Sort the sampleCusMyBookingsData list by the "date" property in descending order (latest first)
////        val sortedData = sampleCusMyBookingsData.sortedByDescending { it.date }
//        // Get the current date in the format "yyyy-MM-dd"
//        val currentDate = SimpleDateFormat("yyyy-MM-dd", Locale.US).format(Date())
//
//        // Sort the sampleCusMyBookingsData list using a custom comparator
//        val sortedData = sampleCusMyBookingsData.sortedWith(compareByDescending<InspectorJourneyItems> {
//            it.date == currentDate // First, check if the date matches the current date
//        }.thenByDescending {
//            it.date // Then, sort by date in descending order
//        })
//
//        val recyclerView = findViewById<RecyclerView>(R.id.Ins_JourneyHistory)
//        val adapter = InspectorJourneyAdapter(sortedData)
//        recyclerView.adapter = adapter
//        recyclerView.layoutManager = LinearLayoutManager(this)
    }
}

