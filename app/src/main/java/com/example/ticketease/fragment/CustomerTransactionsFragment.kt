package com.example.ticketease.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ProgressBar
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.ticketease.CusConSQL
import com.example.ticketease.R
import com.example.ticketease.adapter.CustomerMyBookingsAdapter
import com.example.ticketease.adapter.CustomerTransactionsAdapter
import com.example.ticketease.adapter.CustomerTransportationAdapter
import com.example.ticketease.data.CustomerMyBookingsItem
import com.example.ticketease.data.CustomerTransactions
import com.example.ticketease.data.CustomerTransportationItem
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.sql.SQLException
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class CustomerTransactionsFragment : Fragment() {

    // Declare the userAuth variable
    private lateinit var userAuth: FirebaseAuth
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_customer_transactions, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        userAuth= FirebaseAuth.getInstance()
        val retrievedData = mutableListOf<CustomerTransactions>()

        var cusFragmentTransactionsProgressBarLayout = view.findViewById<FrameLayout>(R.id.cusFragmentTransactionsProgressBarLayout)
        var cusFragmentTransactionsProgressBar = view.findViewById<ProgressBar>(R.id.cusFragmentTransactionsProgressBar)
//        val noDataTextView = view.findViewById<TextView>(R.id.cusFragmentTransactionsNoText)
        requireActivity().runOnUiThread {
//            noDataTextView.visibility = View.GONE
            cusFragmentTransactionsProgressBarLayout?.visibility = View.VISIBLE
            cusFragmentTransactionsProgressBar?.visibility = View.VISIBLE
            // Disable user interaction with the entire layout
            cusFragmentTransactionsProgressBarLayout?.isClickable = true
            cusFragmentTransactionsProgressBarLayout?.isFocusable = true
        }
        // Start a Coroutine to load the data in the background
        CoroutineScope(Dispatchers.IO).launch {
            if (!isAdded) {
                return@launch
            }

            // Create the database connection using the cusConSQL.conclass function
            val cusConSQL = CusConSQL()
            cusConSQL.conclass { connection ->
                if (!isAdded) {
                    return@conclass
                }
                if (connection != null) {
                    try {
                        val user = userAuth.currentUser?.uid ?: ""
                        var query =
                            "SELECT paymentId,detail,date,time,refNo,price FROM CustomerPayment " +
                                    "WHERE cusId = ? "

                        val preparedStatement = connection.prepareStatement(query)
                        preparedStatement.setString(1, user)

                        val resultSet = preparedStatement.executeQuery()

                        while (resultSet.next()) {
                            // Retrieve data from the result set and create CustomerTransportationItem objects
                            val paymentId = resultSet.getString("paymentId")
                            val detail = resultSet.getString("detail")
                            val date = resultSet.getString("date")
                            val time = resultSet.getString("time")
                            var refNo = resultSet.getString("refNo")
                            val price = resultSet.getBigDecimal("price")
                            // Check if refNo is null, and if it is, assign paymentId to it
                            if (refNo == null) {
                                refNo = paymentId
                            }

                            // Create a CustomerTransportationItem and add it to the list
                            val customerTransactions = CustomerTransactions(
                                detail,
                                date,
                                time,
                                refNo,
                                price
                            )
                            retrievedData.add(customerTransactions)
                        }

                        resultSet.close()
                        preparedStatement.close()
                        // Check if retrievedData is empty
                        if (retrievedData.isEmpty()) {
                            requireActivity().runOnUiThread {
                                if (!isAdded) {
                                    return@runOnUiThread
                                }
                                // Show the "Nothing to show" TextView
                                cusFragmentTransactionsProgressBar?.visibility = View.GONE
                                val cusFragmentTransactionsNoText =
                                    view.findViewById<TextView>(R.id.cusFragmentTransactionsNoText)
                                cusFragmentTransactionsNoText.visibility = View.VISIBLE
                            }
                        } else {
                            requireActivity().runOnUiThread {
                                if (!isAdded) {
                                    return@runOnUiThread
                                }
                                // Hide the loading screen
                                cusFragmentTransactionsProgressBarLayout?.visibility = View.GONE
                                cusFragmentTransactionsProgressBar?.visibility = View.GONE
                                // Re-enable user interaction with the entire layout
                                cusFragmentTransactionsProgressBarLayout?.isClickable = false
                                cusFragmentTransactionsProgressBarLayout?.isFocusable = false
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

                // Sort the combined list by date in descending order (latest first)
                val sortedList =
                    retrievedData.sortedWith(compareByDescending<CustomerTransactions> { it.date }.thenByDescending { it.time })
                requireActivity().runOnUiThread {
                    if (!isAdded) {
                        return@runOnUiThread
                    }
                    // Set the retrieved data in the RecyclerView
                    val recyclerView =
                        view.findViewById<RecyclerView>(R.id.cusFragmentTransactionsRecyclerView)
                    val adapter = CustomerTransactionsAdapter(sortedList)
                    recyclerView.adapter = adapter
                    recyclerView.layoutManager = LinearLayoutManager(requireContext())
                }
            }
        }
//
//
//
//
//        // You can access and set UI elements here
//        // Example:
//        // transactionsTextView.text = "Transactions Fragment"
//        val transactionsTopUp = listOf(
//            CustomerTransactions("Top-Up Wallet", "2023/06/18","08:30", "Ref No : 612347678326", "50000.00",true),
//            CustomerTransactions("Top-Up Wallet", "2023/06/02","12:30", "Ref No : 235345348326", "250.00",true),
//            // Add more items as needed
//        )
//
//        val transactionsPayments = listOf(
//            CustomerTransactions("Malabe - Colombo", "2023/06/15","14:35", "Vehicle No: NB-9856", "75.00",false),
//            CustomerTransactions("Colombo - Malabe", "2023/06/15","09:55", "Vehicle No: NC-1543 ", "75.00",false),
//            // Add more items as needed
//        )
//
//        val combinedList = mutableListOf<CustomerTransactions>()
//        combinedList.addAll(transactionsTopUp)
//        combinedList.addAll(transactionsPayments)
//
//        // Sort the combined list by date in descending order (latest first)
//        val sortedList = combinedList.sortedByDescending { it.date }
//
//        val recyclerView = view.findViewById<RecyclerView>(R.id.cusFragmentTransactionsRecyclerView)
//        val adapter = CustomerTransactionsAdapter(sortedList)
//        recyclerView.adapter = adapter
//        recyclerView.layoutManager = LinearLayoutManager(requireContext())
    }

}