package com.example.ticketease.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.ticketease.R
import com.example.ticketease.adapter.CustomerMyBookingsAdapter
import com.example.ticketease.adapter.CustomerTransactionsAdapter
import com.example.ticketease.data.CustomerMyBookingsItem
import com.example.ticketease.data.CustomerTransactions
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class CustomerTransactionsFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_customer_transactions, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // You can access and set UI elements here
        // Example:
        // transactionsTextView.text = "Transactions Fragment"
        val transactionsTopUp = listOf(
            CustomerTransactions("Top-Up Wallet", "2023/06/18","08:30", "Ref No : 612347678326", "50000.00",true),
            CustomerTransactions("Top-Up Wallet", "2023/06/02","12:30", "Ref No : 235345348326", "250.00",true),
            // Add more items as needed
        )

        val transactionsPayments = listOf(
            CustomerTransactions("Malabe - Colombo", "2023/06/15","14:35", "Vehicle No: NB-9856", "75.00",false),
            CustomerTransactions("Colombo - Malabe", "2023/06/15","09:55", "Vehicle No: NC-1543 ", "75.00",false),
            // Add more items as needed
        )

        val combinedList = mutableListOf<CustomerTransactions>()
        combinedList.addAll(transactionsTopUp)
        combinedList.addAll(transactionsPayments)

        // Sort the combined list by date in descending order (latest first)
        val sortedList = combinedList.sortedByDescending { it.date }

        val recyclerView = view.findViewById<RecyclerView>(R.id.cusFragmentTransactionsRecyclerView)
        val adapter = CustomerTransactionsAdapter(sortedList)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
    }

}