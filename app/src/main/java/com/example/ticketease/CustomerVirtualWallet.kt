package com.example.ticketease

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.example.ticketease.fragment.CustomerPendingTransactionsFragment
import com.example.ticketease.fragment.CustomerTopUpFragment
import com.example.ticketease.fragment.CustomerTransactionsFragment

class CustomerVirtualWallet : AppCompatActivity() {

    private lateinit var cusVirtualWalletBack : ImageView
    private lateinit var cusVirtualWalletTransactions : LinearLayout
    private lateinit var cusVirtualWalletTopUp : LinearLayout
    private lateinit var cusVirtualPendingTransactions : LinearLayout
    private lateinit var cusVirtualWalletTransactionsImage : ImageView
    private lateinit var cusVirtualWalletTopUpImage : ImageView
    private lateinit var cusVirtualPendingTransactionsImage : ImageView

    private var currentFragment: Fragment? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_customer_virtual_wallet)

        cusVirtualWalletBack = findViewById(R.id.cusVirtualWalletBack)

        cusVirtualWalletBack.setOnClickListener { // Start the CustomerAccountManagement activity
            finish()
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
}