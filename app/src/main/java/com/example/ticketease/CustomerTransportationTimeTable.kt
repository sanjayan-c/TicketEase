package com.example.ticketease

import android.app.DatePickerDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.ticketease.adapter.CustomerTransportationAdapter
import com.example.ticketease.data.CustomerTransportationItem
import java.sql.SQLException
import java.util.Calendar

class CustomerTransportationTimeTable : AppCompatActivity() {

    private lateinit var cusTransDateText: TextView
    private lateinit var cusTransStartSpinner: Spinner
    private lateinit var cusTransEndSpinner: Spinner
    private lateinit var cusTransMethodSpinner: Spinner
    private lateinit var cusTransMethodSelect: ImageView
    private lateinit var cusTransDateSelect: ImageView
    private lateinit var cusTransStartSelect: ImageView
    private lateinit var cusTransEndSelect: ImageView
    private var cusLocations: MutableList<String> = mutableListOf()
    private var cusEndLocations: MutableList<String> = mutableListOf()
    private lateinit var cusStartLocationsAdapter: ArrayAdapter<String>
    private lateinit var cusEndLocationsAdapter: ArrayAdapter<String>
    private var selectedStartLocation: String = ""
    private var selectedEndLocation: String = ""
    private var selectedDate: String = ""
    private var formattedDate: String = ""
    private var formattedWeekDay: String = ""
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_customer_transportation_time_table)

        val cusTransportationTimeTableBack = findViewById<ImageView>(R.id.cusTransportationTimeTableBack)

        cusTransportationTimeTableBack.setOnClickListener {
            // Start the CustomerAccountManagement activity
            finish()
        }
        var selectedTravelMethod: String ="";
        // Travel method

        cusTransMethodSpinner = findViewById(R.id.cusTransMethodSpinner)
        cusTransMethodSelect = findViewById(R.id.cusTransMethodSelect)
        val travelMethods = listOf("Bus", "Train")
        // Create an ArrayAdapter to populate the Spinner with travel methods
        val custravelMethodsAdapter = ArrayAdapter(this, R.layout.customer_timetable_dropdown, travelMethods)
        custravelMethodsAdapter.setDropDownViewResource(R.layout.customer_timetable_dropdown)
        // Set the adapter for the Spinner
        cusTransMethodSpinner.adapter = custravelMethodsAdapter
        // Handle the click event on the ImageView to show the dropdown
        cusTransMethodSelect.setOnClickListener {
            cusTransMethodSpinner.performClick() // Show the dropdown
        }
        // Set an OnItemSelectedListener for the Spinner
        cusTransMethodSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                // Get the selected travel method
                selectedTravelMethod = travelMethods[position]
                // Log the selected travel method
                Log.d("SelectedTravelMethod", selectedTravelMethod)
                method(selectedTravelMethod);
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {
                // Do nothing
            }
        }

        // Travel Date

        cusTransDateSelect = findViewById(R.id.cusTransDateSelect)
        cusTransDateText = findViewById(R.id.cusTransDateText)

        // Set an OnClickListener on the ImageView to show the DatePickerDialog
        cusTransDateSelect.setOnClickListener {
            showDatePicker()
        }


        cusTransStartSpinner = findViewById(R.id.cusTransStartSpinner)
        cusTransStartSelect = findViewById(R.id.cusTransStartSelect)
        cusTransEndSpinner = findViewById(R.id.cusTransEndSpinner)
        cusTransEndSelect = findViewById(R.id.cusTransEndSelect)

        // Start place



        // Handle the click event on the ImageView to show the dropdown
        cusTransStartSelect.setOnClickListener {
            cusTransStartSpinner.performClick() // Show the dropdown
        }

        //End place



        // Handle the click event on the ImageView to show the dropdown
        cusTransEndSelect.setOnClickListener {
            cusTransEndSpinner.performClick() // Show the dropdown
        }


        // Transport Time Table
//        val sampleData = listOf(
//            CustomerTransportationItem("Colombo",  "Galle","NA - 9856", "10.15 A.M","100"),
//            CustomerTransportationItem("Kandy",  "Jaffna","ND - 9556", "1.15 P.M","17"),
//            CustomerTransportationItem("Jaffna",  "Galle","NA - 3216", "4.15 P.M","430"),
//            CustomerTransportationItem("Trincomalee",  "Galle","NA - 6556", "7.15 P.M","65"),
//            CustomerTransportationItem("Jaffna",  "Colombo","NA - 3216", "4.15 P.M","87"),
//            CustomerTransportationItem("Kandy",  "Galle","NA - 6556", "7.15 P.M","25")
//        )
//        var cusTransSearch=findViewById<TextView>(R.id.cusTransSearch)
//        cusTransSearch.setOnClickListener {
//            val recyclerView = findViewById<RecyclerView>(R.id.recyclerListCustomerTransport)
//            Log.d("SelectedDate", selectedDate)
//            val adapter = CustomerTransportationAdapter(sampleData, selectedDate)
//            recyclerView.adapter = adapter
//            recyclerView.layoutManager = LinearLayoutManager(this)
//        }

        val cusTransSearch = findViewById<TextView>(R.id.cusTransSearch)
        // Create a list to store the retrieved data
        val retrievedData = mutableListOf<CustomerTransportationItem>()

        cusTransSearch.setOnClickListener {
            val selectedTransportMethod = cusTransMethodSpinner.selectedItem.toString()
            val selectedStartLocation = cusTransStartSpinner.selectedItem.toString()
            val selectedEndLocation = cusTransEndSpinner.selectedItem.toString()
            // Check if a date is selected
            if (selectedDate.isNullOrEmpty()) {
                // Show a message to the user indicating that a date needs to be selected
                Toast.makeText(this, "Please select a date", Toast.LENGTH_SHORT).show()
            } else if(selectedStartLocation == selectedEndLocation){
                // Show a message to the user indicating that start and end locations cannot be the same
                Toast.makeText(this, "Start and end locations can't be same", Toast.LENGTH_SHORT).show()
            }else {
                var loadingCusTTProgressBarLayout =
                    findViewById<FrameLayout>(R.id.loadingCusTTProgressBarLayout)
                var loadingCusTTProgressBar =
                    findViewById<ProgressBar>(R.id.loadingCusTTProgressBar)
                runOnUiThread {
                    // Clear the existing data
                    retrievedData.clear()
                    val recyclerView = findViewById<RecyclerView>(R.id.recyclerListCustomerTransport)
                    recyclerView.adapter = null

                    val noDataTextView = findViewById<TextView>(R.id.loadingCusTTNoText)
                    noDataTextView.visibility = View.GONE
                    loadingCusTTProgressBarLayout?.visibility = View.VISIBLE
                    loadingCusTTProgressBar?.visibility = View.VISIBLE
                    // Disable user interaction with the entire layout
                    loadingCusTTProgressBarLayout?.isClickable = true
                    loadingCusTTProgressBarLayout?.isFocusable = true
                }

                // Get the selected transportation method, start place, end place, and date
                Log.d("selectedTransportMethod", selectedTransportMethod)
                Log.d("selectedStartLocation", selectedStartLocation)
                Log.d("selectedEndLocation", selectedEndLocation)
                // Assuming you have a function to format the date as needed

                // Create the database connection using the cusConSQL.conclass function
                val cusConSQL = CusConSQL()
                cusConSQL.conclass { connection ->
                    if (connection != null) {
                        try {
                            // Create a list to store the retrieved data
                            var query: String = ""
                            if (selectedTransportMethod.equals("Bus")) {
                                // Create a SQL query to fetch data based on the selected criteria
                                query = "SELECT bs.* FROM Bus_schedule bs " +
                                        "WHERE bs.StartLocation = ? " +
                                        "AND bs.EndLocation = ?" +
                                        "AND bs.Date = ? " +
                                        "AND  IFNULL(" +
                                        " (SELECT MAX(bb.seatNo) " +
                                        "   FROM BusSeatBooking bb" +
                                        "   WHERE bb.bookingNo = bs.BusScheduleId" +
                                        "),0)  < (" +
                                        "SELECT b.capacity" +
                                        "    FROM Bus b" +
                                        "    WHERE b.busNo = bs.busNo" +
                                        "  );"

                                val preparedStatement = connection.prepareStatement(query)
                                preparedStatement.setString(1, selectedStartLocation)
                                preparedStatement.setString(2, selectedEndLocation)
                                preparedStatement.setString(3, formattedDate)

                                val resultSet = preparedStatement.executeQuery()

                                while (resultSet.next()) {
                                    // Retrieve data from the result set and create CustomerTransportationItem objects
                                    val scheduleId = resultSet.getInt("BusScheduleId")
                                    val vehicleNo = resultSet.getString("busNo")
                                    val time = resultSet.getString("FromTime")
                                    val routeNo = resultSet.getString("RouteNo")

//                                    val from = resultSet.getString("Date")
//                                    val to = resultSet.getString("ToDay")
//
//                                    val daysOfWeek = arrayOf(
//                                        "Monday",
//                                        "Tuesday",
//                                        "Wednesday",
//                                        "Thursday",
//                                        "Friday",
//                                        "Saturday",
//                                        "Sunday"
//                                    )
//
//                                    // Get the indices of the selected day, fromDay, and toDay
//                                    val selectedDayIndex = daysOfWeek.indexOf(formattedWeekDay)
//                                    val fromDayIndex = daysOfWeek.indexOf(from)
//                                    val toDayIndex = daysOfWeek.indexOf(to)
//
//                                    // Check if the selected day falls within the range of fromDay to toDay
//                                    if (selectedDayIndex in (fromDayIndex..toDayIndex)) {
//                                        // Create a CustomerTransportationItem and add it to the list
//                                        val transportationItem = CustomerTransportationItem(
//                                            selectedStartLocation,
//                                            selectedEndLocation,
//                                            vehicleNo,
//                                            time,
//                                            routeNo
//                                        )
//                                        retrievedData.add(transportationItem)
//                                    }
                                    // Create a CustomerTransportationItem and add it to the list
                                    val transportationItem = CustomerTransportationItem(
                                        selectedTransportMethod,
                                        scheduleId,
                                        selectedStartLocation,
                                        selectedEndLocation,
                                        vehicleNo,
                                        time,
                                        routeNo
                                    )
                                    retrievedData.add(transportationItem)
                                }

                                resultSet.close()
                                preparedStatement.close()
                            } else {
                                // Create a SQL query to fetch data based on the selected criteria
                                query = "SELECT bs.* FROM Train_schedule bs " +
                                        "WHERE bs.StartLocation = ? " +
                                        "AND bs.EndLocation = ?" +
                                        "AND bs.Date = ? " +
                                        "AND  IFNULL(" +
                                        " (SELECT MAX(bb.seatNo) " +
                                        "   FROM TrainSeatBooking bb" +
                                        "   WHERE bb.bookingNo = bs.TrainScheduleId" +
                                        "),0)  < (" +
                                        "SELECT b.capacity" +
                                        "    FROM Train b" +
                                        "    WHERE b.trainNo = bs.trainNo" +
                                        "  );"

                                val preparedStatement = connection.prepareStatement(query)
                                preparedStatement.setString(1, selectedStartLocation)
                                preparedStatement.setString(2, selectedEndLocation)
                                preparedStatement.setString(3, formattedDate)

                                val resultSet = preparedStatement.executeQuery()

                                while (resultSet.next()) {
                                    // Retrieve data from the result set and create CustomerTransportationItem objects
                                    val scheduleId = resultSet.getInt("TrainScheduleId")
                                    val vehicleNo = resultSet.getString("trainNo")
                                    val time = resultSet.getString("FromTime")
                                    val routeNo = resultSet.getString("RouteLine")
//                                    val from = resultSet.getString("FromDay")
//                                    val to = resultSet.getString("ToDay")
//
//                                    val daysOfWeek = arrayOf(
//                                        "Monday",
//                                        "Tuesday",
//                                        "Wednesday",
//                                        "Thursday",
//                                        "Friday",
//                                        "Saturday",
//                                        "Sunday"
//                                    )
//
//                                    // Get the indices of the selected day, fromDay, and toDay
//                                    val selectedDayIndex = daysOfWeek.indexOf(formattedWeekDay)
//                                    val fromDayIndex = daysOfWeek.indexOf(from)
//                                    val toDayIndex = daysOfWeek.indexOf(to)
//
//                                    // Check if the selected day falls within the range of fromDay to toDay
//                                    if (selectedDayIndex in (fromDayIndex..toDayIndex)) {
//                                        // Create a CustomerTransportationItem and add it to the list
//                                        val transportationItem = CustomerTransportationItem(
//                                            selectedStartLocation,
//                                            selectedEndLocation,
//                                            vehicleNo,
//                                            time,
//                                            routeNo
//                                        )
//                                        retrievedData.add(transportationItem)
//                                    }
                                    // Create a CustomerTransportationItem and add it to the list
                                    val transportationItem = CustomerTransportationItem(
                                        selectedTransportMethod,
                                        scheduleId,
                                        selectedStartLocation,
                                        selectedEndLocation,
                                        vehicleNo,
                                        time,
                                        routeNo
                                    )
                                    retrievedData.add(transportationItem)
                                }

                                resultSet.close()
                                preparedStatement.close()
                            }


                            // Check if retrievedData is empty
                            if (retrievedData.isEmpty()) {
                                runOnUiThread {
                                    // Show the "Nothing to show" TextView
                                    loadingCusTTProgressBar?.visibility = View.GONE
                                    val noDataTextView = findViewById<TextView>(R.id.loadingCusTTNoText)
                                    noDataTextView.visibility = View.VISIBLE
                                }
                            }else {
                                runOnUiThread {
                                    // Hide the loading screen
                                    loadingCusTTProgressBarLayout?.visibility = View.GONE
                                    loadingCusTTProgressBar?.visibility = View.GONE
                                    // Re-enable user interaction with the entire layout
                                    loadingCusTTProgressBarLayout?.isClickable = false
                                    loadingCusTTProgressBarLayout?.isFocusable = false
                                }
                            }
                        } catch (e: SQLException) {
                            e.printStackTrace()
                            // Handle any errors
                        }finally {
                            // Close the connection in the finally block to ensure it's always closed
                            connection.close()
                        }
                    } else {
                        // Handle the case where the database connection is null
                    }

                    runOnUiThread {
                        // Set the retrieved data in the RecyclerView
                        val recyclerView =
                            findViewById<RecyclerView>(R.id.recyclerListCustomerTransport)
                        val adapter = CustomerTransportationAdapter(retrievedData, selectedDate)
                        recyclerView.adapter = adapter
                        recyclerView.layoutManager = LinearLayoutManager(this)
                    }
                }
            }
        }


    }

    private fun showDatePicker() {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH)

        // Calculate the maximum date (2 weeks from today)
        calendar.add(Calendar.DAY_OF_YEAR, 14)
        val maxYear = calendar.get(Calendar.YEAR)
        val maxMonth = calendar.get(Calendar.MONTH)
        val maxDayOfMonth = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(
            this,
            { _, selectedYear, selectedMonth, selectedDay ->
                // Convert the selected date to a Calendar instance
                val selectedDateCalendar = Calendar.getInstance()
                selectedDateCalendar.set(selectedYear, selectedMonth, selectedDay)

                // Get the day of the week for the selected date
                val dayOfWeek = selectedDateCalendar.get(Calendar.DAY_OF_WEEK)

                // Convert the day of the week to the desired format
                val daysOfWeek = arrayOf(
                    "Sunday",
                    "Monday",
                    "Tuesday",
                    "Wednesday",
                    "Thursday",
                    "Friday",
                    "Saturday"
                )
                formattedWeekDay = daysOfWeek[dayOfWeek - 1]
                Log.d("formattedWeekDay",formattedWeekDay)

                //formattedDate = "$selectedYear-${String.format("%02d", selectedMonth + 1)}-${String.format("%02d", selectedDay)}"
                //selectedDate = "$selectedDay/${selectedMonth + 1}/$selectedYear"
                val formattedDay = String.format("%02d", selectedDay)
                val formattedMonth = String.format("%02d", selectedMonth + 1) // Adding 1 because months are 0-indexed
                selectedDate = "$formattedDay/$formattedMonth/$selectedYear"
                formattedDate=  "$selectedYear-$formattedMonth-$formattedDay"
                cusTransDateText.text = selectedDate
            },
            year, month, dayOfMonth
        )
        // Set a minimum date constraint (e.g., today's date)
//        datePickerDialog.datePicker.minDate = System.currentTimeMillis() - 1000
        // Set a maximum date constraint (2 weeks from today)
//        val maxDateCalendar = Calendar.getInstance()
//        maxDateCalendar.set(maxYear, maxMonth, maxDayOfMonth)
//        datePickerDialog.datePicker.maxDate = maxDateCalendar.timeInMillis
        // Show the DatePickerDialog
        datePickerDialog.show()
    }

    fun method(selectedTravelMethod:String) {

        if (selectedTravelMethod.equals("Bus")) {
            cusLocations = mutableListOf(
                "Colombo",
                "Panadura",
                "Dellawa",
                "Galle",
                "Ella",
                "Kandy",
                "Jaffna",
                "Nuwara Eliya"
            )
            cusLocations = cusLocations.sorted().toMutableList()

            // Create an ArrayAdapter to populate the Spinner with travel methods
            cusStartLocationsAdapter =
                ArrayAdapter(this, R.layout.customer_start_location_dropdown, cusLocations)
            cusStartLocationsAdapter.setDropDownViewResource(R.layout.customer_start_location_dropdown)
            // Set the adapter for the Spinner
            cusTransStartSpinner.adapter = cusStartLocationsAdapter

            cusEndLocationsAdapter =
                ArrayAdapter(this, R.layout.customer_end_location_dropdown, cusLocations)
            cusEndLocationsAdapter.setDropDownViewResource(R.layout.customer_end_location_dropdown)
            // Set the adapter for the Spinner
            cusTransEndSpinner.adapter = cusEndLocationsAdapter
        } else {
            cusLocations = mutableListOf("Colombo Fort", "Badulla", "Kokuvil", "Kankesanthurai")
            cusLocations = cusLocations.sorted().toMutableList()

            // Create an ArrayAdapter to populate the Spinner with travel methods
            cusStartLocationsAdapter =
                ArrayAdapter(this, R.layout.customer_start_location_dropdown, cusLocations)
            cusStartLocationsAdapter.setDropDownViewResource(R.layout.customer_start_location_dropdown)
            // Set the adapter for the Spinner
            cusTransStartSpinner.adapter = cusStartLocationsAdapter

            cusEndLocationsAdapter =
                ArrayAdapter(this, R.layout.customer_end_location_dropdown, cusLocations)
            cusEndLocationsAdapter.setDropDownViewResource(R.layout.customer_end_location_dropdown)
            // Set the adapter for the Spinner
            cusTransEndSpinner.adapter = cusEndLocationsAdapter
        }
    }

}

