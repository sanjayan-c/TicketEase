package com.example.ticketease.data

data class InspectorTimeTableItems(
    val busScheduleId: String?,
    val date: String?,
    val startLocation: String?,
    val endLocation: String?,
    val fromTime: String?,
    val toTime: String?

)

data class InspectorJourneyItems(
    val startLocations: String,
    val endLocations: String,
    val time: String,
    val date: String,
    val passenger:String,
    val income:String

    )

object ImageDataSingleton {
    var firstName: String? = null
    var lasttName: String? = null
    var imageData: String? = null // This variable will hold the image data as a Base64-encoded string
}