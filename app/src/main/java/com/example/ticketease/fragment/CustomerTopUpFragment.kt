package com.example.ticketease.fragment

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.FrameLayout
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.AppCompatButton
import com.example.ticketease.CusConSQL
import com.example.ticketease.CustomerHome
import com.example.ticketease.CustomerVirtualWallet
import com.example.ticketease.R
import com.example.ticketease.data.ImageDataSingleton
import com.google.firebase.auth.FirebaseAuth
import java.math.BigDecimal
import java.sql.SQLException
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class CustomerTopUpFragment : Fragment() {

    // Declare the userAuth variable
    private lateinit var userAuth: FirebaseAuth
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_customer_top_up, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        userAuth = FirebaseAuth.getInstance()
        var editFragmentTopUpInput = view.findViewById<EditText>(R.id.editFragmentTopUpInput)
        var cusFragmentTopUp250 = view.findViewById<TextView>(R.id.cusFragmentTopUp250)
        var cusFragmentTopUp500 = view.findViewById<TextView>(R.id.cusFragmentTopUp500)
        var cusFragmentTopUp1000 = view.findViewById<TextView>(R.id.cusFragmentTopUp1000)

        cusFragmentTopUp250.setOnClickListener {
            editFragmentTopUpInput.setText(getString(R.string.amount_250))
        }
        cusFragmentTopUp500.setOnClickListener {
            editFragmentTopUpInput.setText(getString(R.string.amount_500))
        }
        cusFragmentTopUp1000.setOnClickListener {
            editFragmentTopUpInput.setText(getString(R.string.amount_1000))
        }

        var cusFragmentTopUpProgressBarLayout =
            view.findViewById<FrameLayout>(R.id.cusFragmentTopUpProgressBarLayout)
        var cusFragmentTopUpProgressBar =
            view.findViewById<ProgressBar>(R.id.cusFragmentTopUpProgressBar)

        val btnCusRecharge = view.findViewById<AppCompatButton>(R.id.btnCusRecharge)
        btnCusRecharge.setOnClickListener {
            Log.d("Clicked","Recharge")
            val priceText = editFragmentTopUpInput.text.toString()
            val price = if (priceText.isNotEmpty()) {
                BigDecimal(priceText)
            } else {
                BigDecimal.ZERO
            }
            Log.d("price", price.toString())
            if (price == BigDecimal.ZERO) {
                // Show a toast message if price is zero or empty
                Toast.makeText(requireContext(), "Please enter a valid price.", Toast.LENGTH_SHORT)
                    .show()
                Log.d("Clicked","If")
            } else {
                Log.d("Clicked","Else")
                // Create an AlertDialog
                val alertDialogBuilder = AlertDialog.Builder(requireContext())

                // Set the dialog message and title
                alertDialogBuilder
                    .setTitle("Confirm Payment")
                    .setMessage("Proceed with your payment?")

                // Add a "Cancel" button
                alertDialogBuilder.setNegativeButton("Cancel") { dialog, _ ->
                    // Dismiss the dialog if "Cancel" is clicked
                    dialog.dismiss()
                }

                // Add a "Confirm" button
                alertDialogBuilder.setPositiveButton("Confirm") { dialog, _ ->
                    requireActivity().runOnUiThread {
                        // Show the loading screen
                        cusFragmentTopUpProgressBarLayout.visibility = View.VISIBLE
                        cusFragmentTopUpProgressBar.visibility = View.VISIBLE
                        // Disable user interaction with the entire layout
                        cusFragmentTopUpProgressBarLayout.isClickable = true
                        cusFragmentTopUpProgressBarLayout.isFocusable = true
                    }
                    val user = userAuth.currentUser?.uid ?: ""
                    val (currentDate, currentTime) = getCurrentDateTime()
                    val cusConSQL = CusConSQL()
                    cusConSQL.conclass { connection ->
                        if (connection != null) {
                            try {
                                // Update query with placeholders for binding
                                val query =
                                    "INSERT INTO CustomerPayment(cusId,detail,date,time,price,updatedBy) VALUES (?, ?, ?, ?, ?, ?)"

                                val preparedStatement = connection.prepareStatement(query)
                                // Bind the values to the placeholders
                                preparedStatement.setString(1, user)
                                preparedStatement.setString(2, "Top-up wallet")
                                preparedStatement.setString(3, currentDate)
                                preparedStatement.setString(4, currentTime)
                                preparedStatement.setBigDecimal(5, price)
                                preparedStatement.setString(6, user)

                                // Execute the insert query
                                val rowsAffected = preparedStatement.executeUpdate()

                                if (rowsAffected > 0) {
                                    // Insert successful
                                    println("Data inserted successfully.")
                                } else {
                                    // Insert failed
                                    println("Failed to insert data.")
                                }

                                requireActivity().runOnUiThread {
                                    // Hide the loading screen
                                    cusFragmentTopUpProgressBarLayout?.visibility = View.GONE
                                    cusFragmentTopUpProgressBar?.visibility = View.GONE
                                    // Re-enable user interaction with the entire layout
                                    cusFragmentTopUpProgressBarLayout?.isClickable = false
                                    cusFragmentTopUpProgressBarLayout?.isFocusable = false

                                    val intent =
                                        Intent(requireContext(), CustomerVirtualWallet::class.java)
                                    startActivity(intent)
                                }
                                // Perform any UI updates or navigation as needed
                                // For example, show a success message or navigate to another screen
                            } catch (e: SQLException) {
                                Log.e("Update Error", "SQL Exception: ${e.message}")
                                e.printStackTrace()
                                // Handle any errors that occur during the update
                            } finally {
                                // Close the connection in the finally block to ensure it's always closed
                                connection.close()
                            }
                        } else {
                            Log.e("Update Error", "Database connection is null")
                            // Handle the case where the database connection is null
                        }
                    }

                    // Dismiss the dialog
                    dialog.dismiss()

                }
                // Create and show the AlertDialog
                val alertDialog = alertDialogBuilder.create()
                alertDialog.show()

            }

            // You can access and set UI elements here
            // Example:
            // topUpTextView.text = "Top Up Fragment"
        }
    }

    private fun getCurrentDateTime(): Pair<String, String> {
        val currentDateTime = LocalDateTime.now()
        val dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        val timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss")
        val currentDate = currentDateTime.format(dateFormatter)
        val currentTime = currentDateTime.format(timeFormatter)
        return Pair(currentDate, currentTime)
    }
}