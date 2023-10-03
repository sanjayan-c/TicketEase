package com.example.ticketease.data

data class CustomerTransportationItem(
    val startLocations: String,
    val endLocations: String,
    val vehicleNo: String,
    val time: String,
    val routeNo: String
)
data class CustomerMyBookingsItem(
    val startLocations: String,
    val endLocations: String,
    val vehicleNo: String,
    val routeNo: String,
    val time: String,
    val count: String,
    val seatNo: String,
    val date: String,
    val issuedDate: String,
    val distance: String,
    val cost: String
)

data class CustomerTransactions(
    val detail: String,
    val date: String,
    val time: String,
    val no: String,
    val price: String,
    val isTopUp: Boolean
)