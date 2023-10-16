package com.example.ticketease

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Base64
import android.util.Log
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextClock
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.example.ticketease.data.ImageDataSingleton
import com.example.ticketease.fragment.CustomerPendingTransactionsFragment
import com.example.ticketease.fragment.CustomerTopUpFragment
import com.example.ticketease.fragment.CustomerTransactionsFragment
import com.google.firebase.auth.FirebaseAuth
import java.sql.SQLException
import java.text.SimpleDateFormat

class CustomerVirtualWallet : AppCompatActivity() {

    private lateinit var cusVirtualWalletBack : ImageView
    private lateinit var cusVirtualWalletTransactions : LinearLayout
    private lateinit var cusVirtualWalletTopUp : LinearLayout
    private lateinit var cusVirtualPendingTransactions : LinearLayout
    private lateinit var cusWalletProgressBarLayout : FrameLayout
    private lateinit var cusVirtualWalletTransactionsImage : ImageView
    private lateinit var cusVirtualWalletTopUpImage : ImageView
    private lateinit var cusVirtualPendingTransactionsImage : ImageView
    private lateinit var cusVirtualWalletBalanceText : TextView
    private lateinit var cusVirtualWalletCardNo : TextView
    private lateinit var cusVirtualWalletTimeLastUpdate : TextView
    private lateinit var userAuth: FirebaseAuth

    private var currentFragment: Fragment? = null

    @SuppressLint("SetTextI18n", "SimpleDateFormat")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_customer_virtual_wallet)

        cusVirtualWalletBack = findViewById(R.id.cusVirtualWalletBack)

        cusWalletProgressBarLayout = findViewById(R.id.cusWalletProgressBarLayout)

        cusWalletProgressBarLayout?.isClickable = true
        cusWalletProgressBarLayout?.isFocusable = true

        cusVirtualWalletBack.setOnClickListener { // Start the CustomerAccountManagement activity
            val intent = Intent(this, CustomerHome::class.java)
            startActivity(intent)
        }

        cusVirtualWalletBalanceText = findViewById(R.id.cusVirtualWalletBalanceText)
        cusVirtualWalletCardNo = findViewById(R.id.cusVirtualWalletCardNo)
        cusVirtualWalletTimeLastUpdate = findViewById(R.id.cusVirtualWalletTimeLastUpdate)
        userAuth= FirebaseAuth.getInstance()

        val cusConSQL = CusConSQL()
        cusConSQL.conclass { connection ->
            if (connection != null) {
                // Database connection successful, perform operations#

                val user = userAuth.currentUser?.uid ?: ""

                val query = "SELECT c.cusId, SUM(cp.price) AS totalPayment, MAX(cp.date) AS lastPaymentDate, MAX(cp.time) AS lastPaymentTime " +
                        "FROM customer c " +
                        "LEFT JOIN CustomerPayment cp ON c.cusId = cp.cusId " +
                        "WHERE c.cusId = '$user' " +
                        "GROUP BY c.cusId";

                try {
                    // Create a statement
                    val statement = connection.createStatement()

                    // Execute the query
                    val resultSet = statement.executeQuery(query)

                    var cusId: String? = null
                    var cusBalance: String? = null
                    var cusLastUpdatedDate: String? = null
                    var cusLastUpdatedTime: String? = null

                    // Iterate through the result set and log the details
                    while (resultSet.next()) {
                        cusId = resultSet.getString("cusId")
                        cusBalance = resultSet.getString("totalPayment")
                        cusLastUpdatedDate = resultSet.getString("lastPaymentDate")
                        cusLastUpdatedTime = resultSet.getString("lastPaymentTime")

                        // Log the customer details
                        Log.d("CustomerDetails", "cusId: $cusId")
                        Log.d("CustomerDetails", "cusBalance: $cusBalance")
                        Log.d("CustomerDetails", "cusLastUpdatedDate: $cusLastUpdatedDate")
                        Log.d("CustomerDetails", "cusLastUpdatedTime: $cusLastUpdatedTime")
                    }

                    // Close the statement and result set
                    statement.close()
                    resultSet.close()

                    runOnUiThread {

                        val inputFormat = SimpleDateFormat("yyyy-MM-dd")
                        val date = cusLastUpdatedDate?.let { inputFormat.parse(it) }
                        val outputFormat = SimpleDateFormat("dd/MM/yyyy")
                        val formattedDate = date?.let { outputFormat.format(it) }
                        val maskedCusId = maskCusId(cusId!!)

                        cusWalletProgressBarLayout.visibility = View.GONE

                        cusWalletProgressBarLayout.isClickable = false
                        cusWalletProgressBarLayout.isFocusable = false

                        if(cusBalance!=null) {
                            cusVirtualWalletBalanceText.text = cusBalance
                        }else{
                            cusVirtualWalletBalanceText.text = "0.00"
                        }

                        if (cusLastUpdatedDate == null && cusLastUpdatedTime == null) {
                            cusVirtualWalletTimeLastUpdate.text = "No transactions yet"
                        } else {
                            cusVirtualWalletTimeLastUpdate.text = "Last Updated : $formattedDate   $cusLastUpdatedTime"
                        }

                        cusVirtualWalletCardNo.text = maskedCusId

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


        cusVirtualWalletTransactions = findViewById(R.id.cusVirtualWalletTransactions)
        cusVirtualWalletTopUp = findViewById(R.id.cusVirtualWalletTopUp)
        cusVirtualPendingTransactions = findViewById(R.id.cusVirtualPendingTransactions)
        cusVirtualWalletTransactionsImage = findViewById(R.id.cusVirtualWalletTransactionsImage)
        cusVirtualWalletTopUpImage = findViewById(R.id.cusVirtualWalletTopUpImage)
        cusVirtualPendingTransactionsImage = findViewById(R.id.cusVirtualPendingTransactionsImage)
        // Initialize your fragments (replace with your actual fragments)
        val transactionsFragment = CustomerTransactionsFragment()
        val topUpFragment = CustomerTopUpFragment()
        val pendingsFragment = CustomerPendingTransactionsFragment()
        // Set the initial fragment (e.g., Transactions)
        setFragment(transactionsFragment)

        // Set click listeners for your LinearLayouts (Transactions and Top Up)
        cusVirtualWalletTransactions.setOnClickListener {
            setFragment(transactionsFragment)
            cusVirtualWalletTransactionsImage.setImageResource(R.drawable.cus_my_wallet_transactions_selected)
            cusVirtualWalletTopUpImage.setImageResource(R.drawable.cus_my_wallet_topup)
            cusVirtualPendingTransactionsImage.setImageResource(R.drawable.cus_my_wallet_pendings)
        }

        cusVirtualWalletTopUp.setOnClickListener {
            setFragment(topUpFragment)
            cusVirtualWalletTransactionsImage.setImageResource(R.drawable.cus_my_wallet_transactions)
            cusVirtualWalletTopUpImage.setImageResource(R.drawable.cus_my_wallet_topup_selected)
            cusVirtualPendingTransactionsImage.setImageResource(R.drawable.cus_my_wallet_pendings)
        }

        cusVirtualPendingTransactions.setOnClickListener {
            setFragment(pendingsFragment)
            cusVirtualWalletTransactionsImage.setImageResource(R.drawable.cus_my_wallet_transactions)
            cusVirtualWalletTopUpImage.setImageResource(R.drawable.cus_my_wallet_topup)
            cusVirtualPendingTransactionsImage.setImageResource(R.drawable.cus_my_wallet_pendings_selected)
        }
    }

    private fun setFragment(fragment: Fragment) {
        val fragmentManager: FragmentManager = supportFragmentManager
        val transaction: FragmentTransaction = fragmentManager.beginTransaction()

        // Replace the fragment container with the new fragment
        transaction.replace(R.id.cusTransactionMethodFragmentContainer, fragment)

        // Commit the transaction
        transaction.commit()

        currentFragment = fragment
    }
    fun maskCusId(cusId: String): String {
        if (cusId.length >= 8) {
            val firstFour = cusId.take(4)
            val lastFour = cusId.takeLast(4)
            val maskedMiddle = "X".repeat(cusId.length - 8)
            return "$firstFour$maskedMiddle$lastFour"
        }
        return cusId
    }
}