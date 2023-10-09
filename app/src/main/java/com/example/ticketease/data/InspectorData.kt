package com.example.ticketease.data

data class InspectorTimeTableItems(
    val startLocations: String,
    val endLocations: String,
    val time: String,
    val date: String,

)

data class InspectorJourneyItems(
    val startLocations: String,
    val endLocations: String,
    val time: String,
    val date: String,
    val passenger:String,
    val income:String

    )