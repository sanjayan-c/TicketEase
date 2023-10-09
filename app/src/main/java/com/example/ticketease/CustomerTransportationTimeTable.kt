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
    private var cusStartLocations: MutableList<String> = mutableListOf()
    private var cusEndLocations: MutableList<String> = mutableListOf()
    private lateinit var cusStartLocationsAdapter: ArrayAdapter<String>
    private lateinit var cusEndLocationsAdapter: ArrayAdapter<String>
    private var selectedStartLocation: String = ""
    private var selectedEndLocation: String = ""
    private var selectedDate: String = ""
    private var formattedDate: String = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_customer_transportation_time_table)

        val cusTransportationTimeTableBack = findViewById<ImageView>(R.id.cusTransportationTimeTableBack)

        cusTransportationTimeTableBack.setOnClickListener {
            // Start the CustomerAccountManagement activity
            finish()
        }

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
                val selectedTravelMethod = travelMethods[position]
                // Log the selected travel method
                Log.d("SelectedTravelMethod", selectedTravelMethod)
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

        cusStartLocations = mutableListOf("Colombo", "Kandy", "Jaffna", "Nuwara-Eliya")
        cusEndLocations = mutableListOf("Colombo", "Kandy", "Jaffna", "Nuwara-Eliya")

        // Start place

        cusTransStartSpinner = findViewById(R.id.cusTransStartSpinner)
        cusTransStartSelect = findViewById(R.id.cusTransStartSelect)
        // Create an ArrayAdapter to populate the Spinner with travel methods
        cusStartLocationsAdapter = ArrayAdapter(this, R.layout.customer_start_location_dropdown, cusStartLocations)
        cusStartLocationsAdapter.setDropDownViewResource(R.layout.customer_start_location_dropdown)
        // Set the adapter for the Spinner
        cusTransStartSpinner.adapter = cusStartLocationsAdapter
        // Handle the click event on the ImageView to show the dropdown
        cusTransStartSelect.setOnClickListener {
            cusTransStartSpinner.performClick() // Show the dropdown
        }

        //End place

        cusTransEndSpinner = findViewById(R.id.cusTransEndSpinner)
        cusTransEndSelect = findViewById(R.id.cusTransEndSelect)
        // Create an ArrayAdapter to populate the Spinner with travel methods
        cusEndLocationsAdapter = ArrayAdapter(this, R.layout.customer_end_location_dropdown, cusEndLocations)
        cusEndLocationsAdapter.setDropDownViewResource(R.layout.customer_end_location_dropdown)
        // Set the adapter for the Spinner
        cusTransEndSpinner.adapter = cusEndLocationsAdapter
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
                val formattedDate = formattedDate

                // Create a list to store the retrieved data
                var query: String = ""
                if (selectedTransportMethod.equals("Bus")) {
                    // Create a SQL query to fetch data based on the selected criteria
                    query = "SELECT * FROM BusTransport " +
                            "WHERE startLocation = ? " +
                            "AND endLocation = ? " +
                            "AND date = ?"
                } else {
                    // Create a SQL query to fetch data based on the selected criteria
                    query = "SELECT * FROM TrainTransport " +
                            "WHERE startLocation = ? " +
                            "AND endLocation = ? " +
                            "AND date = ?"
                }

                // Create the database connection using the cusConSQL.conclass function
                val cusConSQL = CusConSQL()
                cusConSQL.conclass { connection ->
                    if (connection != null) {
                        try {
                            val preparedStatement = connection.prepareStatement(query)
                            preparedStatement.setString(1, selectedStartLocation)
                            preparedStatement.setString(2, selectedEndLocation)
                            preparedStatement.setString(3, formattedDate)

                            val resultSet = preparedStatement.executeQuery()

                            while (resultSet.next()) {
                                // Retrieve data from the result set and create CustomerTransportationItem objects
                                val vehicleNo = resultSet.getString("vehicleNo")
                                val time = resultSet.getString("time")
                                val routeNo = resultSet.getString("routeNo")

                                // Create a CustomerTransportationItem and add it to the list
                                val transportationItem = CustomerTransportationItem(
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

        val datePickerDialog = DatePickerDialog(
            this,
            { _, selectedYear, selectedMonth, selectedDay ->
                // Handle date selection here
                formattedDate = "$selectedYear-${String.format("%02d", selectedMonth + 1)}-${String.format("%02d", selectedDay)}"
                //selectedDate = "$selectedDay/${selectedMonth + 1}/$selectedYear"
                val formattedDay = String.format("%02d", selectedDay)
                val formattedMonth = String.format("%02d", selectedMonth + 1) // Adding 1 because months are 0-indexed
                selectedDate = "$formattedDay/$formattedMonth/$selectedYear"
                cusTransDateText.text = selectedDate
            },
            year, month, dayOfMonth
        )
        // Set a minimum date constraint (e.g., today's date)
        datePickerDialog.datePicker.minDate = System.currentTimeMillis() - 1000
        // Show the DatePickerDialog
        datePickerDialog.show()
    }

}

