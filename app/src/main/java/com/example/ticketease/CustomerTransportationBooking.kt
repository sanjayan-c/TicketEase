package com.example.ticketease

import android.content.Intent
import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Base64
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Spinner
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.AppCompatButton
import com.example.ticketease.data.ImageDataSingleton
import com.google.firebase.auth.FirebaseAuth
import java.sql.SQLException
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Date
import java.util.Locale

class CustomerTransportationBooking : AppCompatActivity() {

    private var cusBookingStart: TextView? = null
    private var cusBookingEnd: TextView? = null
    private var cusBookingDateText: TextView? = null
    private var cusBookingCountSpinner: Spinner? = null
    private var cusBookingCountSelect: ImageView? = null
    private var cusBookingBack: ImageView? = null
    private var btnCusBooking: AppCompatButton? = null
    private var btnCusBookingConfirm: AppCompatButton? = null
    private var btnCusBookingCancel: AppCompatButton? = null
    private var cusBookingButtons: LinearLayout? = null
    private var cusBookingButtons2: LinearLayout? = null
    private var cusBookingButtonsTicketDetails: FrameLayout? = null
    private var userAuth: FirebaseAuth? = null
    private var selectedSeatCount: Int? = null
    private var scheduleId: Int? = null
    private var vehicleType: String = ""
    private var startLocations: String = ""
    private var endLocations: String = ""
    private var vehicleNo: String = ""
    private var time: String = ""
    private var selectedDate: String = ""
    private var userSelectedDate: String = ""
    private var routeNo: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_customer_splash_transactions_booking)

        userAuth= FirebaseAuth.getInstance()

        val cusConSQL = CusConSQL()
        cusConSQL.conclass { connection ->
            if (connection != null) {

                // Retrieve data from the intent
                val extras = intent.extras
                if (extras != null) {
                    vehicleType = extras.getString("vehicleType", "")
                    scheduleId = extras.getInt("scheduleId")
                    startLocations = extras.getString("startLocations", "")
                    endLocations = extras.getString("endLocations", "")
                    vehicleNo = extras.getString("vehicleNo", "")
                    time = extras.getString("time", "")
                    userSelectedDate = extras.getString("selectedDate", "")
                    routeNo = extras.getString("routeNo","")
                    // Assuming that selectedDate is in "dd/MM/yyyy" format
                    val parts = userSelectedDate.split("/")
                    if (parts.size == 3) {
                        val day = parts[0]
                        val month = parts[1]
                        val year = parts[2]

                        // Reformat the date to "yyyy-MM-dd" format
                        selectedDate = "$year-$month-$day"
                    }
                    Log.d("Details", "vehicleType: $vehicleType")
                    Log.d("Details", "vehicleNo: $vehicleNo")
                    Log.d("Details", "time: $time")
                    Log.d("Details", "selectedDate: $selectedDate")
                    // You can also use other data like vehicleNo and time if needed
                }

                // Database connection successful, perform operations
                // Your SQL query to fetch customer details
                val user = userAuth!!.currentUser?.uid ?: ""

                var seatCount: Int? = null
                var bookedSeatCount: Int = 0
                var availableSeatCount: Int = 5
                try {
                    var query: String = ""
                    query = if(vehicleType.equals("Bus")) {
                        "SELECT b.capacity, MAX(bb.seatNo) AS maxSeatNo " +
                                "FROM BusSeatBooking bb, Bus_schedule bs, Bus b " +
                                "WHERE bb.bookingNo = bs.BusScheduleId AND bs.busNo = b.busNo AND bb.bookingNo = $scheduleId " +
                                "GROUP BY bb.bookingNo, b.capacity;"
                    }else{
                        "SELECT b.capacity, MAX(bb.seatNo) AS maxSeatNo " +
                                "FROM TrainSeatBooking bb, Train_schedule bs, Train b " +
                                "WHERE bb.bookingNo = bs.TrainScheduleId AND bs.trainNo = b.trainNo AND bb.bookingNo = $scheduleId " +
                                "GROUP BY bb.bookingNo, b.capacity;"
                    }
                    // Create a prepared statement
                    val statement = connection.prepareStatement(query)

                    // Execute the query
                    val resultSet = statement.executeQuery(query)

                    // Iterate through the result set and log the details
                    while (resultSet.next()) {
                        seatCount = resultSet.getInt("capacity")
                        bookedSeatCount = resultSet.getInt("maxSeatNo")
                        availableSeatCount= seatCount!! -bookedSeatCount!!
                    }
                    // Log the customer details
                    Log.d("CustomerDetails", "seatCount: $seatCount")
                    Log.d("CustomerDetails", "bookedSeatCount: $bookedSeatCount")


                    // Close the statement and result set
                    statement.close()
                    resultSet.close()
                    switchToCustomerBookingsLayout()
                    runOnUiThread {

                        // Set the retrieved data to your TextView elements
                        cusBookingStart?.text = "$startLocations"
                        cusBookingEnd?.text = "$endLocations"
                        cusBookingDateText?.text = "$userSelectedDate"

                        cusBookingBack?.setOnClickListener {
                            finish()
                        }

                        val cusBookSeatsCount = if (availableSeatCount!! < 5) {
                            (1..availableSeatCount!!).toList()
                        } else {
                            (1..5).toList()
                        }
                        // Create an ArrayAdapter to populate the Spinner with travel methods
                        val custravelMethodsAdapter = ArrayAdapter(this, R.layout.customer_timetable_dropdown, cusBookSeatsCount)
                        custravelMethodsAdapter.setDropDownViewResource(R.layout.customer_timetable_dropdown)
                        // Set the adapter for the Spinner
                        cusBookingCountSpinner?.adapter = custravelMethodsAdapter
                        // Handle the click event on the ImageView to show the dropdown
                        cusBookingCountSelect?.setOnClickListener {
                            cusBookingCountSpinner?.performClick() // Show the dropdown
                        }
                        // Set an OnItemSelectedListener for the Spinner
                        cusBookingCountSpinner?.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                                // Get the selected travel method
                                selectedSeatCount = cusBookSeatsCount[position]
                                // Log the selected travel method
                                Log.d("selectedSeatCount", selectedSeatCount.toString())
                            }
                            override fun onNothingSelected(parent: AdapterView<*>?) {
                                // Do nothing
                            }
                        }



                        btnCusBooking?.setOnClickListener {
                            cusBookingButtons?.visibility = View.GONE
                            cusBookingCountSelect?.visibility = View.GONE
                            cusBookingButtonsTicketDetails?.visibility = View.VISIBLE
                            cusBookingButtons2?.visibility = View.VISIBLE

                            var cusBookingRoute = findViewById<TextView>(R.id.cusBookingRoute)
                            var cusBookingCustomerName = findViewById<TextView>(R.id.cusBookingCustomerName)
                            var cusBookingCustomerNIC = findViewById<TextView>(R.id.cusBookingCustomerNIC)
                            var cusBookingDepartureDate = findViewById<TextView>(R.id.cusBookingDepartureDate)
                            var cusBookingDepartureTime = findViewById<TextView>(R.id.cusBookingDepartureTime)
                            var cusBookingNoOfPassengers = findViewById<TextView>(R.id.cusBookingNoOfPassengers)
                            var cusBookingVehicleNo = findViewById<TextView>(R.id.cusBookingVehicleNo)
                            var cusBookingIssuedDate = findViewById<TextView>(R.id.cusBookingIssuedDate)
                            var cusBookingRouteNo = findViewById<TextView>(R.id.cusBookingRouteNo)
                            val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()) // Example format: "yyyy-MM-dd"
                            // Get the current date and format it
                            val issueDate = sdf.format(Date())
                            cusBookingCustomerName.text = ImageDataSingleton.firstName+" "+ImageDataSingleton.lasttName
                            cusBookingCustomerNIC.text = ImageDataSingleton.nic
                            cusBookingRoute.text = "$startLocations - $endLocations"
                            cusBookingDepartureDate.text = selectedDate
                            cusBookingDepartureTime.text = time
                            cusBookingVehicleNo.text = vehicleNo
                            cusBookingNoOfPassengers.text = selectedSeatCount.toString()
                            cusBookingIssuedDate.text = issueDate
                            cusBookingRouteNo.text = routeNo
                        }

                        btnCusBookingConfirm?.setOnClickListener {
                            if (selectedSeatCount!! > 0) {
                                val cusConSQL = CusConSQL()
                                cusConSQL.conclass { connection ->
                                    if (connection != null) {
                                        try {
                                            val (currentDate, currentTime) = getCurrentDateTime()
                                            for (i in 1..selectedSeatCount!!) {
                                                val seatNo = bookedSeatCount!! + i

                                                query = if(vehicleType.equals("Bus")){
                                                    "INSERT INTO BusSeatBooking (bookingNo, seatNo, cusId, issuedDate, issuedTime) " +
                                                            "VALUES (?, ?, ?, ?, ?)"
                                                }else{
                                                    "INSERT INTO TrainSeatBooking (bookingNo, seatNo, cusId, issuedDate, issuedTime) " +
                                                            "VALUES (?, ?, ?, ?, ?)"
                                                }
                                                val statement = connection.prepareStatement(query)

                                                statement.setInt(1, scheduleId!!)
                                                statement.setInt(2, seatNo)
                                                statement.setString(3, user)
                                                statement.setString(4, currentDate)
                                                statement.setString(5, currentTime)
                                                statement.executeUpdate()
                                                statement.close()
                                            }
                                            Log.d("BookingConfirmation", "Booking was successful.")
                                            val intent = Intent(this@CustomerTransportationBooking, CustomerHome::class.java)
                                            startActivity(intent)
                                            // Handle success, e.g., show a confirmation message
                                        } catch (e: SQLException) {
                                            Log.e("SQL Error", "SQL Exception: " + e.message)
                                            e.printStackTrace()
                                            // Handle the exception, e.g., show an error message
                                        }finally {
                                            // Close the connection in the finally block to ensure it's always closed
                                            connection.close()
                                        }
                                    } else {
                                        // Handle connection error
                                        Log.e("TAG", "Connection Error")
                                    }
                                }

                            } else {
                                val intent = Intent(this@CustomerTransportationBooking, CustomerHome::class.java)
                                startActivity(intent)
                            }
                        }


                        btnCusBookingCancel?.setOnClickListener {
                            finish()
                        }
                    }
                } catch (e: SQLException) {
                    Log.e("SQL Error", "SQL Exception: " + e.message)
                    e.printStackTrace()
                }finally {
                    // Close the connection in the finally block to ensure it's always closed
                    connection.close()
                }
            } else {
                // Handle connection error
                Log.e("TAG", "Connection Error")
            }
        }


    }
    private fun switchToCustomerBookingsLayout() {
        runOnUiThread {
            // Switch to the main activity_customer_home layout
            setContentView(R.layout.activity_customer_transportation_booking)
            cusBookingBack = findViewById(R.id.cusBookingBack)
            cusBookingStart=findViewById(R.id.cusBookingStart)
            cusBookingEnd=findViewById(R.id.cusBookingEnd)
            cusBookingDateText=findViewById(R.id.cusBookingDateText)
            cusBookingCountSpinner = findViewById(R.id.cusBookingCountSpin)
            cusBookingCountSelect = findViewById(R.id.cusBookingCountSelect)
            // Book button
            btnCusBooking=findViewById(R.id.btnCusBooking)
            cusBookingButtons2 = findViewById(R.id.cusBookingButtons2)
            cusBookingButtons = findViewById(R.id.cusBookingButtons)
            cusBookingButtonsTicketDetails = findViewById(R.id.cusBookingButtonsTicketDetails)
            btnCusBookingConfirm=findViewById(R.id.btnCusBookingConfirm)
            btnCusBookingCancel=findViewById(R.id.btnCusBookingCancel)
        }
    }
    fun getCurrentDateTime(): Pair<String, String> {
        val currentDateTime = LocalDateTime.now()
        val dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        val timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss")
        val currentDate = currentDateTime.format(dateFormatter)
        val currentTime = currentDateTime.format(timeFormatter)
        return Pair(currentDate, currentTime)
    }
}