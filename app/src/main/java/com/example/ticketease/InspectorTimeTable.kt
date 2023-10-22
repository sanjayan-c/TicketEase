package com.example.ticketease


import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Base64
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.ticketease.adapter.InspectorTimetableAdapter
import com.example.ticketease.data.ImageDataSingleton
import com.example.ticketease.data.InspectorTimeTableItems
import java.sql.SQLException
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale


class InspectorTimeTable : AppCompatActivity(), InspectorTimetableAdapter.OnStartTripClickListener {

    private lateinit var adapter: InspectorTimetableAdapter
    private lateinit var recyclerView: RecyclerView
    private lateinit var dayMon: TextView
    private lateinit var dayTue: TextView
    private lateinit var dayWed: TextView
    private lateinit var dayThur: TextView
    private lateinit var dayFri: TextView
    private lateinit var daySat: TextView
    private lateinit var daySun: TextView
    private var selectedDayView: TextView? = null
    private val cusConSQL = CusConSQL()
    private lateinit var VehicleNo:String
    private lateinit var VehicleType:String
   // val dataList = mutableListOf<InspectorTimeTableItems>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.inspector_timetable)

        VehicleNo = intent.getStringExtra("inspectorVehicleNo") ?: ""
        VehicleType = intent.getStringExtra("inspectorVehicleType") ?: ""
        dayMon = findViewById(R.id.dayMon)
        dayTue = findViewById(R.id.dayTue)
        dayWed = findViewById(R.id.dayWed)
        dayThur = findViewById(R.id.dayThur)
        dayFri = findViewById(R.id.dayFri)
        daySat = findViewById(R.id.daySat)
        daySun = findViewById(R.id.daySun)

        val (date, dayOfWeek) = getCurrentDateAndDayOfWeek()
        println("Today is $date, and the current day is $dayOfWeek.")

        val currentDayView = when (dayOfWeek) {
            "Monday" -> dayMon
            "Tuesday" -> dayTue
            "Wednesday" -> dayWed
            "Thursday" -> dayThur
            "Friday" -> dayFri
            "Saturday" -> daySat
            "Sunday" -> daySun
            else -> null
        }

        currentDayView?.post {
            currentDayView?.performClick()
        }

        // Define a click listener for each TextView
        dayMon.setOnClickListener {
            val nextDate = handleDayClick(dayMon)
            // Change the text color of the clicked TextView
            setDayTextColor(dayMon, R.color.bluegray_100_7f)
            retriveData(nextDate)
        }
        // Define a click listener for each TextView
        dayTue.setOnClickListener {
            val nextDate = handleDayClick(dayTue)
            // Change the text color of the clicked TextView
            setDayTextColor(dayTue, R.color.bluegray_100_7f)
            retriveData(nextDate)
        }
        // Define a click listener for each TextView
        dayWed.setOnClickListener {
            val nextDate = handleDayClick(dayWed)
            // Change the text color of the clicked TextView
            setDayTextColor(dayWed, R.color.bluegray_100_7f)
            retriveData(nextDate)
        }
        // Define a click listener for each TextView
        dayThur.setOnClickListener {
            val nextDate = handleDayClick(dayThur)
            // Change the text color of the clicked TextView
            setDayTextColor(dayThur, R.color.bluegray_100_7f)
            retriveData(nextDate)
        }
        // Define a click listener for each TextView
        dayFri.setOnClickListener {
            val nextDate = handleDayClick(dayFri)
            // Change the text color of the clicked TextView
            setDayTextColor(dayFri, R.color.bluegray_100_7f)
            retriveData(nextDate)
        }
        // Define a click listener for each TextView
        daySat.setOnClickListener {
            val nextDate = handleDayClick(daySat)
            // Change the text color of the clicked TextView
            setDayTextColor(daySat, R.color.bluegray_100_7f)
            retriveData(nextDate)
        }
        // Define a click listener for each TextView
        daySun.setOnClickListener {
            val nextDate = handleDayClick(daySun)
            // Change the text color of the clicked TextView
            setDayTextColor(daySun, R.color.bluegray_100_7f)
            retriveData(nextDate)
        }


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

//        // Transport Time Table
//        val sampleCusMyBookingsData = listOf(
//            InspectorTimeTableItems("Colombo", "Galle", "NA - 9856", "Route 1", "10.15 A.M", "2"),
//            InspectorTimeTableItems("Kandy", "Jaffna", "ND - 9556", "Route 2", "1.15 P.M", "1"),
//            InspectorTimeTableItems("Jaffna", "Galle", "NA - 3216", "Route 3", "4.15 P.M", "4"),
//            InspectorTimeTableItems("Trincomalee", "Galle", "NA - 6556", "Route 4", "7.15 P.M", "3"),
//            InspectorTimeTableItems("Jaffna", "Colombo", "NA - 3216", "Route 5", "4.15 P.M", "2")
//        )
////        // Sort the sampleCusMyBookingsData list by the "date" property in descending order (latest first)
////        val sortedData = sampleCusMyBookingsData.sortedByDescending { it.date }
//        // Get the current date in the format "yyyy-MM-dd"
//        val currentDate = SimpleDateFormat("yyyy-MM-dd", Locale.US).format(Date())
//
//        // Sort the sampleCusMyBookingsData list using a custom comparator
//        val sortedData = sampleCusMyBookingsData.sortedWith(compareByDescending<InspectorTimeTableItems> {
//            it.date == currentDate // First, check if the date matches the current date
//        }.thenByDescending {
//            it.date // Then, sort by date in descending order
//        })
//
//        val recyclerView = findViewById<RecyclerView>(R.id.recyclerInspectorTimetable)
//        val adapter = InspectorTimetableAdapter(sortedData,this)
//        recyclerView.adapter = adapter
//        recyclerView.layoutManager = LinearLayoutManager(this)
    }


//    private fun handleDayClick(dayView: TextView, day: String) {
//        selectedDayView?.isSelected = false // Deselect the previously selected day
//        selectedDayView = dayView
//        selectedDayView?.isSelected = true  // Select the clicked day
//
//        updateRecyclerViewForSelectedDay(day)
//    }


//
//    private fun updateRecyclerViewForSelectedDay(selectedDay: String) {
//        val dataForSelectedDay = fetchDataForDay(selectedDay)
//        adapter.updateData(dataForSelectedDay)
//    }

//    private fun fetchDataForDay(BusNo: String): List<InspectorTimeTableItems> {
//        val newDataList  = mutableListOf<InspectorTimeTableItems>()
//
//        cusConSQL.conclass { connection ->
//            if (connection != null) {
//                // Replace with your actual logic
//                var busScheduleId: String? = null
//                var date: String? = null
//                var startLocation: String? = null
//                var endLocation: String? = null
//                var fromTime: String? = null
//                var toTime: String? = null
//
//                val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
//                val currentDate = dateFormat.format(Date())
//
//                val query = """
//                SELECT * FROM Bus_schedule
//                WHERE Date = '$currentDate' AND BusNo = '$BusNo'
//            """
//
//                try {
//                    val statement = connection.createStatement()
//                    val resultSet = statement.executeQuery(query)
//
//                    while (resultSet.next()) {
//                        busScheduleId = resultSet.getString("BusScheduleId")
//                        date = resultSet.getString("Date")
//                        startLocation = resultSet.getString("StartLocation")
//                        endLocation = resultSet.getString("EndLocation")
//                        fromTime = resultSet.getString("FromTime")
//                        toTime = resultSet.getString("ToTime")
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
//                        dataList.add(inspectorTimeTableItem)
//                    }
//
//                    statement.close()
//                    resultSet.close()
//
//                    runOnUiThread {
//                        adapter.updateData(newDataList)
//                    }
//                    // ... rest of your code ...
//                } catch (e: SQLException) {
//                    Log.e("SQL Error", "SQL Exception: ${e.message}")
//                    e.printStackTrace()
//                } catch (e: Exception) {
//                    Log.e("General Error", "Error: ${e.message}")
//                    e.printStackTrace()
//                }
//            }
//        }
//
//        return dataList
//    }

    override fun onStartTripClick(position: Int,scheduleId:String?){
        val intent = Intent(this@InspectorTimeTable, InspectorStartedTrip::class.java)
        intent.putExtra("scheduleId", scheduleId)
        intent.putExtra("VehicleType", VehicleType)
        cusConSQL.conclass { connection ->
        val query :String
        if(VehicleType=="Bus") {
             query =
                "UPDATE Bus_schedule SET TripStarted = TRUE WHERE BusScheduleId = ? AND TripStarted IS NULL"
        }else{
             query =
                "UPDATE Train_schedule SET TripStarted = TRUE WHERE TrainScheduleId = ? AND TripStarted IS NULL"
        }

            if (connection != null) {
                try {
                    val preparedStatement = connection.prepareStatement(query)
                    preparedStatement.setString(1, scheduleId)

                    val rowsUpdated = preparedStatement.executeUpdate()

                    if (rowsUpdated > 0) {
                        // Trip started successfully
                        Log.e("Trip","Trip started")

                        Toast.makeText(this, "Trip started", Toast.LENGTH_SHORT).show()
                        // You may want to perform additional actions or update UI
                    } else {
                        // Trip may have already started or BusScheduleId not found
                        // Handle accordingly
                    }

                    preparedStatement.close()
                } catch (e: SQLException) {
                    e.printStackTrace()
                    // Handle database errors
                } finally {
                    connection.close()
                }
            } else {
                // Handle the case where the database connection is null
            }
        }


        startActivity(intent)
    }
    fun getCurrentDateAndDayOfWeek(): Pair<String, String> {
        val calendar = Calendar.getInstance()
        val currentDate = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(calendar.time)
        val dayOfWeek = when (calendar.get(Calendar.DAY_OF_WEEK)) {
            Calendar.MONDAY -> "Monday"
            Calendar.TUESDAY -> "Tuesday"
            Calendar.WEDNESDAY -> "Wednesday"
            Calendar.THURSDAY -> "Thursday"
            Calendar.FRIDAY -> "Friday"
            Calendar.SATURDAY -> "Saturday"
            Calendar.SUNDAY -> "Sunday"
            else -> "Unknown"
        }
        return Pair(currentDate, dayOfWeek)
    }
    private fun setDayTextColor(textView: TextView, colorId: Int) {
        dayMon.setTextColor(resources.getColor(R.color.white, null))
        dayTue.setTextColor(resources.getColor(R.color.white, null))
        dayWed.setTextColor(resources.getColor(R.color.white, null))
        dayThur.setTextColor(resources.getColor(R.color.white, null))
        dayFri.setTextColor(resources.getColor(R.color.white, null))
        daySat.setTextColor(resources.getColor(R.color.white, null))
        daySun.setTextColor(resources.getColor(R.color.white, null))
        // Change the text color of the TextView to the specified color
        textView.setTextColor(resources.getColor(R.color.bluegray_100_7f, null))
    }
    private fun handleDayClick(textView: TextView): String  {
        // Get the current date and day of the week
        val currentDateAndDayOfWeek = getCurrentDateAndDayOfWeek()
        val (currentDate, currentDayOfWeek) = currentDateAndDayOfWeek

        // Get the clicked day of the week from the TextView's tag
        val clickedDayOfWeek = textView.tag as String

        // Calculate the date for the clicked day
        val nextDate = calculateNextDate(currentDate, currentDayOfWeek, clickedDayOfWeek)

        // Display the next date
        println("You clicked on $clickedDayOfWeek. The next date is $nextDate.")

        // Return the next date as a string
        return nextDate
    }
    private fun calculateNextDate(currentDate: String, currentDayOfWeek: String, clickedDayOfWeek: String): String {
        val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val calendar = Calendar.getInstance()
        calendar.time = sdf.parse(currentDate)
        calendar.add(Calendar.DAY_OF_WEEK, daysUntilNextDay(currentDayOfWeek, clickedDayOfWeek))
        return sdf.format(calendar.time)
    }
    private fun daysUntilNextDay(currentDay: String, nextDay: String): Int {
        val daysOfWeek = listOf("Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday")
        val currentDayIndex = daysOfWeek.indexOf(currentDay)
        val nextDayIndex = daysOfWeek.indexOf(nextDay)
        return (nextDayIndex - currentDayIndex + 7) % 7
    }
    private fun retriveData(date: String) {
        cusConSQL.conclass { connection ->
            if (connection != null) {

                try {
                    // Your database query logic here
                    // ...
                    var schedule:String
                    var vehicle:String
                    if(VehicleType=="Bus"){
                         schedule="Bus_schedule"
                        vehicle="busNo"
                    }else{
                        schedule="Train_schedule"
                        vehicle="trainNo"
                    }


                    // Example query using your commented code
                    val query = """
                    SELECT * FROM $schedule
                    WHERE Date = ? AND $vehicle = ?
                """
                    val preparedStatement = connection.prepareStatement(query)
                    preparedStatement.setString(1, date)
                    preparedStatement.setString(2, VehicleNo)

                    val resultSet = preparedStatement.executeQuery()

                    // Create a list to store the retrieved data
                    val retrievedData = mutableListOf<InspectorTimeTableItems>()

                    while (resultSet.next()) {
                        // Retrieve data from the result set and create InspectorTimeTableItems objects
                        val busScheduleId:String
                        if(schedule=="Bus_schedule"){
                             busScheduleId = resultSet.getString("BusScheduleId")
                        }else{
                             busScheduleId = resultSet.getString("TrainScheduleId")
                        }

                        val startLocation = resultSet.getString("StartLocation")
                        val endLocation = resultSet.getString("EndLocation")
                        val fromTime = resultSet.getString("FromTime")
                        val toTime = resultSet.getString("ToTime")

                        // Create an InspectorTimeTableItems object and add it to the list
                        val inspectorTimeTableItem = InspectorTimeTableItems(
                            busScheduleId,
                            date,
                            startLocation,
                            endLocation,
                            fromTime,
                            toTime
                        )
                        retrievedData.add(inspectorTimeTableItem)
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

                            // Sort the retrievedData list using a custom comparator
                            val sortedData = retrievedData.sortedWith(compareByDescending<InspectorTimeTableItems> {
                                it.date == date // First, check if the date matches the current date
                            }.thenByDescending {
                                it.date // Then, sort by date in descending order
                            })

                            val recyclerView = findViewById<RecyclerView>(R.id.recyclerInspectorTimetable)
                            val adapter = InspectorTimetableAdapter(sortedData, this)
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
    }

}
