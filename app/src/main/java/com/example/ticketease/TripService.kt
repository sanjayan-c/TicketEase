package com.example.ticketease

import android.app.Service
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.IBinder
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import java.util.*
import android.Manifest
import android.content.pm.PackageManager
import android.location.Location
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.maps.model.LatLng
import kotlin.math.asin
import kotlin.math.cos
import kotlin.math.pow
import kotlin.math.sin
import kotlin.math.sqrt


class SharedViewModel : ViewModel() {
    private val _totalDistance = MutableLiveData<Double>()
    val totalDistance: LiveData<Double> get() = _totalDistance

    fun updateTotalDistance(distance: Double) {
        _totalDistance.value = distance
    }
}



class TripService : Service() {


    private lateinit var sharedViewModel: SharedViewModel

    private val timer = Timer()
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private var locationCallback: LocationCallback? = null
    private var totalDistance: Double = 0.0
    private var startLatLng: LatLng? = null
    override fun onCreate() {
        super.onCreate()
        // Other initialization code...
        sharedViewModel = ViewModelProvider.AndroidViewModelFactory(application)
            .create(SharedViewModel::class.java)


        val filter = IntentFilter(STOP_SERVICE_ACTION)
        registerReceiver(stopServiceReceiver, filter)
        Log.d("LocationUpdate", "Entered the class")
        // Initialize the Fused Location Provider
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
    }




    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        // Start your background task here, for example, using a Timer
        timer.scheduleAtFixedRate(object : TimerTask() {
            override fun run() {
                // Code to be executed periodically in the background
                // Update any UI elements or perform necessary tasks
                // For example, you can update a notification with the elapsed time
            }
        }, 0, 1000) // Run every 1000 milliseconds (1 second)
        Log.d("LocationUpdate", "Entered on start")
        // Request location updates
        requestLocationUpdates()
        return START_STICKY
    }

    override fun onDestroy() {
        super.onDestroy()
        // Other cleanup code...
        // Stop location updates when the service is destroyed
        stopLocationUpdates()
        unregisterReceiver(stopServiceReceiver)
    }

    companion object {
        const val STOP_SERVICE_ACTION = "com.example.ticketease.STOP_TRIP_SERVICE"
    }

    private val stopServiceReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            if (intent?.action == STOP_SERVICE_ACTION) {
                Log.d("LocationUpdate", "Received stop service request")
                stopSelf()
            }
        }
    }

    private fun requestLocationUpdates() {
        Log.d("LocationUpdate", "Request for location")
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            val locationRequest = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setInterval(1000)  // Update interval in milliseconds
                .setFastestInterval(500)  // Fastest update interval
                .setMaxWaitTime(1000)  // Maximum wait time between updates

            locationCallback = object : LocationCallback() {
                override fun onLocationResult(locationResult: LocationResult) {
                    for (location in locationResult.locations) {
                        // Handle location updates here
                        val latitude = location.latitude
                        val longitude = location.longitude
                        Log.d("LocationUpdate", "Latitude: $latitude, Longitude: $longitude")
                        if (startLatLng == null) {
                            startLatLng = LatLng(latitude, longitude)
                        } else {
                            val endLatLng = LatLng(latitude, longitude)
                            totalDistance += calculateDistance(startLatLng!!, endLatLng)
                            startLatLng = endLatLng

                            sharedViewModel.updateTotalDistance(totalDistance)
                        }



                        Log.d("LocationUpdate", "Total Distance : $totalDistance")
                    }
                }
            }

            fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback!!, null)
        }
    }
    private fun stopLocationUpdates() {
        if (locationCallback != null) {
            fusedLocationClient.removeLocationUpdates(locationCallback!!)
            locationCallback = null
        }
    }

    private fun calculateDistance(start: LatLng, end: LatLng): Double {
        // Haversine formula to calculate the distance between two LatLng points
        val radius = 6371 // Earth's radius in kilometers
        val lat1 = Math.toRadians(start.latitude)
        val lat2 = Math.toRadians(end.latitude)
        val lon1 = Math.toRadians(start.longitude)
        val lon2 = Math.toRadians(end.longitude)

        val dLat = lat2 - lat1
        val dLon = lon2 - lon1
        // The square of half the chord length between the two points on the Earth's surface
        val a = sin(dLat / 2).pow(2) + cos(lat1) * cos(lat2) * sin(dLon / 2).pow(2)
        // The central angle between the two points on the Earth's surface.
        val c = 2 * asin(sqrt(a))

        return radius * c * 1000 // Convert to meters
    }
}
