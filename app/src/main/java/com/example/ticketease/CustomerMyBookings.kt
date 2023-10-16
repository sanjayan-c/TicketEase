package com.example.ticketease

import android.content.Intent
import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Base64
import android.util.Log
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.ticketease.adapter.CustomerMyBookingsAdapter
import com.example.ticketease.data.CustomerMyBookingsItem
import com.example.ticketease.data.CustomerTransactions
import com.example.ticketease.data.ImageDataSingleton
import com.google.firebase.auth.FirebaseAuth
import java.math.BigDecimal
import java.sql.SQLException
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class CustomerMyBookings : AppCompatActivity() {

    private var cusMyBookingsBack : ImageView? = null
    private var cusMyBookingsCustomerName : TextView? = null
    private var cusMyBookingsCustomerNIC : TextView? = null
    private var CustomerMyBookingsNoText : TextView? = null
    private var CustomerMyBookingsProgressBar : ProgressBar? = null
    private var CustomerMyBookingsProgressBarLayout : FrameLayout? = null
    private lateinit var userAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_customer_my_bookings)

        cusMyBookingsBack = findViewById(R.id.cusMyBookingsBack)
        cusMyBookingsCustomerName = findViewById(R.id.cusMyBookingsCustomerName)
        cusMyBookingsCustomerNIC = findViewById(R.id.cusMyBookingsCustomerNIC)
        CustomerMyBookingsNoText = findViewById(R.id.CustomerMyBookingsNoText)
        CustomerMyBookingsProgressBar = findViewById(R.id.CustomerMyBookingsProgressBar)
        CustomerMyBookingsProgressBarLayout = findViewById(R.id.CustomerMyBookingsProgressBarLayout)
        userAuth= FirebaseAuth.getInstance()
        // Disable user interaction with the entire layout
        CustomerMyBookingsProgressBarLayout?.isClickable = true
        CustomerMyBookingsProgressBarLayout?.isFocusable = true
        val myBusBookingsList = mutableListOf<CustomerMyBookingsItem>()
        val myTrainBookingsList = mutableListOf<CustomerMyBookingsItem>()
        val combinedList = mutableListOf<CustomerMyBookingsItem>()
        val cusConSQL = CusConSQL()
        cusConSQL.conclass { connection ->
            if (connection != null) {

                // Your SQL query to fetch customer details
                val user = userAuth.currentUser?.uid ?: ""
                val query = "SELECT " +
                        "bb.bookingNo, " +
                        "COUNT(bb.seatNo) AS totalSeat, " +
                        "bb.issuedDate, " +
                        "DATE_FORMAT(bb.issuedTime, '%H:%i') AS issuedTime, " +
                        "bs.busNo, " +
                        "bs.Date, " +
                        "bs.FromTime, " +
                        "bs.RouteNo, " +
                        "bs.StartLocation, " +
                        "bs.EndLocation, " +
                        "bb.startDistance, " +
                        "bb.endDistance, " +
                        "SUM(bb.charge) AS totalCharge " +
                        "FROM " +
                        "BusSeatBooking bb, Bus_schedule bs " +
                        "WHERE " +
                        "bb.cusId = '$user' AND bb.bookingNo = bs.BusScheduleId " +
                        "GROUP BY " +
                        "bb.bookingNo, bb.issuedDate, DATE_FORMAT(bb.issuedTime, '%H:%i'), bs.busNo, bs.Date, bs.FromTime, bs.RouteNo, bs.StartLocation, bs.EndLocation, bb.startDistance, bb.endDistance, bb.charge;"

                try {
                    // Create a statement
                    val statement = connection.createStatement()

                    // Execute the query
                    val resultSet = statement.executeQuery(query)

                    // Iterate through the result set and log the details
                    while (resultSet.next()) {
                        // Retrieve data from the result set and create CustomerTransportationItem objects
                        val bookingNo = resultSet.getInt("bookingNo")
                        val startLocation = resultSet.getString("StartLocation")
                        val endLocation = resultSet.getString("EndLocation")
                        val vehicleNo = resultSet.getString("busNo")
                        val routeNo = resultSet.getString("RouteNo")
                        val time = resultSet.getString("FromTime")
                        var count = resultSet.getString("totalSeat")
                        val date = resultSet.getString("Date")
                        var issuedDate = resultSet.getString("issuedDate")
                        val issuedTime = resultSet.getString("issuedTime")
                        val startDistance = resultSet.getBigDecimal("startDistance") ?: BigDecimal.ZERO
                        val endDistance = resultSet.getBigDecimal("endDistance") ?: BigDecimal.ZERO
                        var cost = resultSet.getBigDecimal("totalCharge") ?: BigDecimal.ZERO

                        var distance = endDistance - startDistance
                        Log.d("Query 1","Query 1 is successful")

                        val query2 = "SELECT seatNo " +
                                "FROM BusSeatBooking " +
                                "WHERE cusId = '$user' " +
                                "AND bookingNo = '$bookingNo' " +
                                "AND issuedDate = '$issuedDate' " +
                                "AND TIME_FORMAT(issuedTime, '%H:%i') = '$issuedTime'"

                        var seatNumbers = mutableListOf<String>()
                        var seatNo = ""

                        try {
                            // Create a statement
                            val statement2 = connection.createStatement()

                            // Execute the query
                            val resultSet2 = statement2.executeQuery(query2)

                            // Iterate through the result set and log the details
                            while (resultSet2.next()) {
                                val seat = resultSet2.getInt("seatNo")
                                seatNumbers.add(seat.toString())
                            }
                            Log.d("Query 2","Query 2 is successful")
                            seatNo = seatNumbers.joinToString(", ")
                            // Close the statement and result set
                            statement2.close()
                            resultSet2.close()

                        } catch (e: SQLException) {
                            Log.e("SQL Error", "SQL Exception: " + e.message)
                            e.printStackTrace()
                        }
                        // Log the retrieved data
                        Log.d("CustomerDetails", "bookingNo: $bookingNo")
                        Log.d("CustomerDetails", "startLocation: $startLocation")
                        Log.d("CustomerDetails", "endLocation: $endLocation")
                        Log.d("CustomerDetails", "vehicleNo: $vehicleNo")
                        Log.d("CustomerDetails", "routeNo: $routeNo")
                        Log.d("CustomerDetails", "time: $time")
                        Log.d("CustomerDetails", "count: $count")
                        Log.d("CustomerDetails", "seatNo: $seatNo")
                        Log.d("CustomerDetails", "date: $date")
                        Log.d("CustomerDetails", "issuedDate: $issuedDate")
                        Log.d("CustomerDetails", "issuedTime: $issuedTime")
                        Log.d("CustomerDetails", "startDistance: $startDistance")
                        Log.d("CustomerDetails", "endDistance: $endDistance")
                        Log.d("CustomerDetails", "distance: $distance")
                        Log.d("CustomerDetails", "cost: $cost")
                        // Create a CustomerTransportationItem and add it to the list
                        val myBusBookings = CustomerMyBookingsItem(
                            bookingNo,
                            startLocation,
                            endLocation,
                            vehicleNo,
                            routeNo,
                            time,
                            count,
                            seatNo,
                            date,
                            issuedDate,
                            issuedTime,
                            distance,
                            cost
                        )
                        myBusBookingsList.add(myBusBookings)
                    }

                    // Close the statement and result set
                    statement.close()
                    resultSet.close()

                } catch (e: SQLException) {
                    Log.e("SQL Error", "SQL Exception: " + e.message)
                    e.printStackTrace()
                    // Close the connection in the finally block to ensure it's always closed
                    connection.close()
                }


                val query3 = "SELECT " +
                        "bb.bookingNo, " +
                        "COUNT(bb.seatNo) AS totalSeat, " +
                        "bb.issuedDate, " +
                        "DATE_FORMAT(bb.issuedTime, '%H:%i') AS issuedTime, " +
                        "bs.trainNo, " +
                        "bs.Date, " +
                        "bs.FromTime, " +
                        "bs.RouteLine, " +
                        "bs.StartLocation, " +
                        "bs.EndLocation, " +
                        "bb.startDistance, " +
                        "bb.endDistance, " +
                        "SUM(bb.charge) AS totalCharge " +
                        "FROM " +
                        "TrainSeatBooking bb, Train_schedule bs " +
                        "WHERE " +
                        "bb.cusId = '$user' AND bb.bookingNo = bs.TrainScheduleId " +
                        "GROUP BY " +
                        "bb.bookingNo, bb.issuedDate, DATE_FORMAT(bb.issuedTime, '%H:%i'), bs.trainNo, bs.Date, bs.FromTime, bs.RouteLine, bs.StartLocation, bs.EndLocation, bb.startDistance, bb.endDistance, bb.charge;"

                try {
                    // Create a statement
                    val statement = connection.createStatement()

                    // Execute the query
                    val resultSet = statement.executeQuery(query3)

                    // Iterate through the result set and log the details
                    while (resultSet.next()) {
                        // Retrieve data from the result set and create CustomerTransportationItem objects
                        val bookingNo = resultSet.getInt("bookingNo")
                        val startLocation = resultSet.getString("StartLocation")
                        val endLocation = resultSet.getString("EndLocation")
                        val vehicleNo = resultSet.getString("trainNo")
                        val routeNo = resultSet.getString("RouteLine")
                        val time = resultSet.getString("FromTime")
                        var count = resultSet.getString("totalSeat")
                        val date = resultSet.getString("Date")
                        var issuedDate = resultSet.getString("issuedDate")
                        val issuedTime = resultSet.getString("issuedTime")
                        val startDistance = resultSet.getBigDecimal("startDistance") ?: BigDecimal.ZERO
                        val endDistance = resultSet.getBigDecimal("endDistance") ?: BigDecimal.ZERO
                        var cost = resultSet.getBigDecimal("totalCharge") ?: BigDecimal.ZERO

                        var distance = endDistance - startDistance
                        Log.d("Query 3","Query 3 is successful")

                        val query2 = "SELECT seatNo " +
                                "FROM TrainSeatBooking " +
                                "WHERE cusId = '$user' " +
                                "AND bookingNo = '$bookingNo' " +
                                "AND issuedDate = '$issuedDate' " +
                                "AND TIME_FORMAT(issuedTime, '%H:%i') = '$issuedTime'"

                        var seatNumbers = mutableListOf<String>()
                        var seatNo = ""

                        try {
                            // Create a statement
                            val statement2 = connection.createStatement()

                            // Execute the query
                            val resultSet2 = statement2.executeQuery(query2)

                            // Iterate through the result set and log the details
                            while (resultSet2.next()) {
                                val seat = resultSet2.getInt("seatNo")
                                seatNumbers.add(seat.toString())
                            }
                            Log.d("Query 2","Query 2 is successful")
                            seatNo = seatNumbers.joinToString(", ")
                            // Close the statement and result set
                            statement2.close()
                            resultSet2.close()

                        } catch (e: SQLException) {
                            Log.e("SQL Error", "SQL Exception: " + e.message)
                            e.printStackTrace()
                        }
                        // Log the retrieved data
                        Log.d("CustomerDetails", "bookingNo: $bookingNo")
                        Log.d("CustomerDetails", "startLocation: $startLocation")
                        Log.d("CustomerDetails", "endLocation: $endLocation")
                        Log.d("CustomerDetails", "vehicleNo: $vehicleNo")
                        Log.d("CustomerDetails", "routeNo: $routeNo")
                        Log.d("CustomerDetails", "time: $time")
                        Log.d("CustomerDetails", "count: $count")
                        Log.d("CustomerDetails", "seatNo: $seatNo")
                        Log.d("CustomerDetails", "date: $date")
                        Log.d("CustomerDetails", "issuedDate: $issuedDate")
                        Log.d("CustomerDetails", "issuedTime: $issuedTime")
                        Log.d("CustomerDetails", "startDistance: $startDistance")
                        Log.d("CustomerDetails", "endDistance: $endDistance")
                        Log.d("CustomerDetails", "distance: $distance")
                        Log.d("CustomerDetails", "cost: $cost")
                        // Create a CustomerTransportationItem and add it to the list
                        val myTrainBookings = CustomerMyBookingsItem(
                            bookingNo,
                            startLocation,
                            endLocation,
                            vehicleNo,
                            routeNo,
                            time,
                            count,
                            seatNo,
                            date,
                            issuedDate,
                            issuedTime,
                            distance,
                            cost
                        )
                        myTrainBookingsList.add(myTrainBookings)
                    }

                    // Close the statement and result set
                    statement.close()
                    resultSet.close()


                    combinedList.addAll(myBusBookingsList)
                    combinedList.addAll(myTrainBookingsList)

                    // Check if retrievedData is empty
                    if (combinedList.isEmpty()) {
                        runOnUiThread {
                            // Show the "Nothing to show" TextView
                            CustomerMyBookingsProgressBar?.visibility = View.GONE
                            CustomerMyBookingsNoText!!.visibility = View.VISIBLE
                        }
                    } else {
                        runOnUiThread {

                            // Hide the loading screen
                            CustomerMyBookingsProgressBarLayout?.visibility = View.GONE
                            CustomerMyBookingsProgressBar?.visibility = View.GONE
                            CustomerMyBookingsNoText!!.visibility = View.GONE
                            // Re-enable user interaction with the entire layout
                            CustomerMyBookingsProgressBarLayout?.isClickable = false
                            CustomerMyBookingsProgressBarLayout?.isFocusable = false
                        }
                    }
                } catch (e: SQLException) {
                    Log.e("SQL Error", "SQL Exception: " + e.message)
                    e.printStackTrace()
                } finally {
                    // Close the connection in the finally block to ensure it's always closed
                    connection.close()
                }
            } else {
                // Handle connection error
                Log.e("TAG", "Connection Error")
            }


            val currentDate = SimpleDateFormat("yyyy-MM-dd", Locale.US).format(Date())
            // Sort the sampleCusMyBookingsData list using a custom comparator
            val sortedData =
                combinedList.sortedWith(compareByDescending<CustomerMyBookingsItem> {
                    it.date == currentDate // First, check if the date matches the current date
                }.thenByDescending {
                    it.date // Then, sort by date in descending order
                }.thenByDescending {
                    it.time // Then, sort by time in descending order
                }.thenByDescending {
                    it.issuedDate // Then, sort by time in descending order
                }.thenByDescending {
                    it.issuedTime // Then, sort by time in descending order
                })

            runOnUiThread {
                val recyclerView = findViewById<RecyclerView>(R.id.recyclerListCustomerMyBookings)
                val adapter = CustomerMyBookingsAdapter(sortedData)
                recyclerView.adapter = adapter
                recyclerView.layoutManager = LinearLayoutManager(this)
            }
        }


        cusMyBookingsBack!!.setOnClickListener { // Start the CustomerAccountManagement activity
            finish()
        }

        cusMyBookingsCustomerName!!.text =
        ImageDataSingleton.firstName + " " + ImageDataSingleton.lasttName
        cusMyBookingsCustomerNIC!!.text = ImageDataSingleton.nic

    }
}