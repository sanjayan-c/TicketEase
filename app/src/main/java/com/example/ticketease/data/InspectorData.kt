package com.example.ticketease.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

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


class SharedViewModel : ViewModel() {
    private var _totalDistance = MutableLiveData<Double>()
    val totalDistance: LiveData<Double> get() = _totalDistance

    // Rename these functions
    fun getTotalDistanceLiveData(): LiveData<Double> {
        return totalDistance
    }

    fun setTotalDistance(distance: Double) {
        _totalDistance.value = distance
    }
}


object DistanceDataSingleton {
    var Distance: Double? = null
    // This variable will hold the image data as a Base64-encoded string
}