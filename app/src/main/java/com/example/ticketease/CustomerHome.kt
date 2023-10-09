package com.example.ticketease

import android.content.Intent
import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Base64
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextClock
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import com.example.ticketease.data.ImageDataSingleton
import com.google.firebase.auth.FirebaseAuth
import java.sql.Connection
import java.sql.SQLException

class CustomerHome : AppCompatActivity() {

    private var cusAccountManagement : LinearLayout? = null
    private var cusProfileImage : ImageView? = null
    private var cusQR : LinearLayout? = null
    private var cusBookNow : LinearLayout? = null
    private var cusBookings : LinearLayout? = null
    private var cusWallet : LinearLayout? = null
    private var cusLogout : LinearLayout? = null
    private lateinit var userAuth: FirebaseAuth
    private var textViewBelowImage: TextView? = null

    lateinit var connect: Connection
    var ConnectionResult: String=""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_customer_splash_home)

        userAuth= FirebaseAuth.getInstance()

        if (userAuth.currentUser != null) {2
            val cusConSQL = CusConSQL()
            cusConSQL.conclass { connection ->
                if (connection != null) {
                    // Database connection successful, perform operations
                    // Your SQL query to fetch customer details
                    val user = userAuth.currentUser?.uid ?: ""
                    val query = "SELECT * FROM customer WHERE cusId = '$user'"

                    try {

                        // Create a statement
                        val statement = connection.createStatement()

                        // Execute the query
                        val resultSet = statement.executeQuery(query)

                        var cusFirstName: String? = null
                        var cusLastName: String? = null
                        var cusId: String? = null
                        var cusNic: String? = null
                        var cusGmail: String? = null
                        var cusImage: String? = null

                        // Iterate through the result set and log the details
                        while (resultSet.next()) {
                            cusId = resultSet.getString("cusId")
                            cusFirstName = resultSet.getString("cusFirstName") ?: ""
                            cusLastName = resultSet.getString("cusLastName") ?: ""
                            cusNic = resultSet.getString("cusNic")
                            cusGmail = resultSet.getString("cusGmail")
                            cusImage = resultSet.getString("cusImage")

                            // Log the customer details
                            Log.d("CustomerDetails", "cusId: $cusId")
                            Log.d("CustomerDetails", "cusFirstName: $cusFirstName")
                            Log.d("CustomerDetails", "cusLastName: $cusLastName")
                            Log.d("CustomerDetails", "cusNic: $cusNic")
                            Log.d("CustomerDetails", "cusGmail: $cusGmail")
                            Log.d("CustomerDetails", "cusImage: $cusImage")
                        }

                        // Close the statement and result set
                        statement.close()
                        resultSet.close()
                        switchToCustomerHomeLayout()
                        Log.d("cusFirstName", cusFirstName!!)
                        Log.d("cusLastName", cusLastName!!)
                        ImageDataSingleton.nic = cusNic
                        ImageDataSingleton.firstName = cusFirstName
                        ImageDataSingleton.lasttName = cusLastName
                        ImageDataSingleton.imageData = cusImage
                        runOnUiThread {
                            if (cusFirstName != null) {
                                textViewBelowImage?.text = "$cusFirstName $cusLastName"
                            }

                            if (cusImage != null) {
                                // Decode the Base64 string to a Bitmap
                                val decodedBytes = Base64.decode(cusImage, Base64.DEFAULT)
                                val decodedBitmap = BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.size)

                                // Set the decoded Bitmap as the image for the ImageView
                                cusProfileImage?.setImageBitmap(decodedBitmap)
                            } else {
                                // If cusImage is null, you can set a default image or do nothing
                                cusProfileImage?.setImageResource(R.drawable.cus_image_not_found)
                            }

                            cusAccountManagement?.setOnClickListener { // Start the CustomerAccountManagement activity
                                val intent =
                                    Intent(this@CustomerHome, CustomerAccountManagement::class.java)
                                intent.putExtra("firstName", cusFirstName)
                                intent.putExtra("lastName", cusLastName)
                                intent.putExtra("cusId", cusId)
                                intent.putExtra("cusNic", cusNic)
                                intent.putExtra("cusGmail", cusGmail)
//                                intent.putExtra("cusImage", cusImage)

                                startActivity(intent)
                            }

                            cusQR?.setOnClickListener { // Start the CustomerAccountManagement activity
                                val intent = Intent(this@CustomerHome, CustomerQRCode::class.java)
                                startActivity(intent)
                            }

                            cusBookNow?.setOnClickListener { // Start the CustomerAccountManagement activity
                                val intent = Intent(
                                    this@CustomerHome,
                                    CustomerTransportationTimeTable::class.java
                                )
                                startActivity(intent)
                            }

                            cusBookings?.setOnClickListener { // Start the CustomerAccountManagement activity
                                val intent =
                                    Intent(this@CustomerHome, CustomerMyBookings::class.java)
                                startActivity(intent)
                            }

                            cusWallet?.setOnClickListener { // Start the CustomerAccountManagement activity
                                val intent =
                                    Intent(this@CustomerHome, CustomerVirtualWallet::class.java)
                                startActivity(intent)
                            }

                            cusLogout?.setOnClickListener {
                                // Create an AlertDialog
                                val alertDialogBuilder = AlertDialog.Builder(this)

                                // Set the dialog title and message for logout confirmation
                                alertDialogBuilder
                                    .setTitle("Log Out")
                                    .setMessage("Are you sure you want to log out?")

                                // Add a "Cancel" button
                                alertDialogBuilder.setNegativeButton("Cancel") { dialog, _ ->
                                    // Dismiss the dialog if "Cancel" is clicked
                                    dialog.dismiss()
                                }

                                // Add a "Log Out" button
                                alertDialogBuilder.setPositiveButton("Log Out") { dialog, _ ->
                                    // Perform the logout action
                                    userAuth.signOut()

                                    // Start the CustomerLogIn activity
                                    val intent = Intent(this@CustomerHome, CustomerLogIn::class.java)
                                    finish()
                                    startActivity(intent)

                                    // Dismiss the dialog
                                    dialog.dismiss()
                                }

                                // Create and show the AlertDialog
                                val alertDialog = alertDialogBuilder.create()
                                alertDialog.show()
                            }

                        }
                    } catch (e: SQLException) {
                        Log.e("SQL Error", "SQL Exception: " + e.message)
                        e.printStackTrace()
                    }
                } else {
                    // Handle connection error
                    Log.e("TAG", "Connection Error")
                }
            }
        }else{
            switchToCustomerHomeLayout()
            cusBookNow?.setOnClickListener { // Start the CustomerAccountManagement activity
                val intent = Intent(
                    this@CustomerHome,
                    CustomerTransportationTimeTable::class.java
                )
                startActivity(intent)
            }
            cusLogout?.setOnClickListener { // Start the CustomerAccountManagement activity
                // Perform the logout action
                userAuth.signOut()
                val intent = Intent(this@CustomerHome, CustomerLogIn::class.java)
                finish()
                startActivity(intent)
            }
        }

        // Get the UID of the currently logged-in user
        val currentUserUid = userAuth.currentUser?.uid ?: ""
        Log.d("CurrentUserUid", currentUserUid)
    }

    private fun switchToCustomerHomeLayout() {
        runOnUiThread {
            // Switch to the main activity_customer_home layout
            setContentView(R.layout.activity_customer_home)
            cusAccountManagement = findViewById(R.id.cus_account_management)
            cusQR = findViewById(R.id.cus_qr)
            cusBookNow = findViewById(R.id.cus_calender)
            cusBookings = findViewById(R.id.cus_bookings)
            cusWallet = findViewById(R.id.cus_wallet)
            cusLogout = findViewById(R.id.cus_logout)
            cusProfileImage = findViewById(R.id.cusProfileImage)
            textViewBelowImage = findViewById(R.id.textViewBelowImage)
        }
    }
}