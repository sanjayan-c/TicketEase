package com.example.ticketease

import android.app.Service
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.IBinder
import java.util.*

class TripService : Service() {

    private val timer = Timer()


    override fun onCreate() {
        super.onCreate()
        // Other initialization code...

        val filter = IntentFilter(STOP_SERVICE_ACTION)
        registerReceiver(stopServiceReceiver, filter)
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

        return START_STICKY
    }

    override fun onDestroy() {
        super.onDestroy()
        // Other cleanup code...

        unregisterReceiver(stopServiceReceiver)
    }

    companion object {
        const val STOP_SERVICE_ACTION = "com.example.ticketease.STOP_TRIP_SERVICE"
    }

    private val stopServiceReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            if (intent?.action == STOP_SERVICE_ACTION) {
                stopSelf()
            }
        }
    }


}
