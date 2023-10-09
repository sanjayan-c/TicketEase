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

data class Traveller(
    val nic: String,
    val email: String,
    val uid : String,
    val type : String
)

object ImageDataSingleton {
    var nic: String? = null
    var firstName: String? = null
    var lasttName: String? = null
    var imageData: String? = null // This variable will hold the image data as a Base64-encoded string
}