package com.example.wificheck.backgroundService

import android.Manifest
import android.content.ContentValues.TAG
import android.graphics.Color
import android.support.v4.app.NotificationCompat
import android.support.v4.app.TaskStackBuilder
import android.util.Log
import com.google.android.gms.location.Geofence
import com.google.android.gms.location.GeofencingEvent
import android.content.pm.PackageManager
import android.Manifest.permission
import android.Manifest.permission.ACCESS_COARSE_LOCATION
import android.annotation.SuppressLint
import android.app.*
import android.content.*
import android.location.Criteria
import android.location.LocationManager
import android.net.wifi.WifiManager
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v4.content.ContextCompat
import android.support.v4.content.LocalBroadcastManager
import android.widget.RemoteViews
import android.widget.Toast
import com.example.wificheck.BuildConfig
import com.example.wificheck.R
import com.example.wificheck.View.MainActivity
import com.example.wificheck.View.fragment.Tab1Fragment


class GeofenceTransitionsIntentService : IntentService("GeofenceTransitionsIntentService") {

    internal lateinit var notificationManager: NotificationManager

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        super.onStartCommand(intent, startId, startId)

        return START_STICKY
    }

    override fun onHandleIntent(intent: Intent?) {
        val geofencingEvent = GeofencingEvent.fromIntent(intent)
        if (geofencingEvent.hasError()) {
            return
        }
        // Get the transition type.
        val geofenceTransition = geofencingEvent.geofenceTransition
        val geofenceId = getFirstReminder(geofencingEvent.triggeringGeofences)

        // Test that the reported transition was of interest.
        if (geofenceTransition == Geofence.GEOFENCE_TRANSITION_ENTER) {
            openNotification("You are inside wifilocation: $geofenceId")
            changeWifi(true)
        } else if (geofenceTransition == Geofence.GEOFENCE_TRANSITION_EXIT) {
            openNotification("You ar not inside a wifilocation")
            changeWifi(false)
        } else {
            // Log the error.
        }
    }


    private fun getFirstReminder(triggeringGeofences: List<Geofence>): String? {
        val firstGeofence = triggeringGeofences[0]
        return firstGeofence.requestId
    }


    fun checkIfRunning(): Boolean {
        val sp = getSharedPreferences("SharedPreferences", Context.MODE_PRIVATE)
        var active = sp.getBoolean("active", false)

        return active
    }


    fun openNotification(description: String) {

        val wifiManager = this.getSystemService(Context.WIFI_SERVICE) as WifiManager
        wifiManager.isWifiEnabled = true

        if (!checkIfRunning()) {
            val NOTIFICATION_ID = 234
            notificationManager =
                applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

            val CHANNEL_ID = "my_channel_$description"
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                val name = "my_channel"
                val Description = "This is my channel"
                val importance = NotificationManager.IMPORTANCE_HIGH
                val mChannel = NotificationChannel(CHANNEL_ID, name, importance)
                mChannel.description = Description
                mChannel.enableLights(true)
                mChannel.lightColor = Color.RED
                mChannel.enableVibration(true)
                mChannel.vibrationPattern = longArrayOf(100, 200, 300, 400, 500, 400, 300, 200, 400)
                mChannel.setShowBadge(false)
                notificationManager.createNotificationChannel(mChannel)
            }

            val builder = NotificationCompat.Builder(applicationContext, CHANNEL_ID)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(description)

            val resultIntent = Intent(applicationContext, MainActivity::class.java)
            val stackBuilder = TaskStackBuilder.create(applicationContext)
            stackBuilder.addParentStack(MainActivity::class.java)
            stackBuilder.addNextIntent(resultIntent)
            val resultPendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT)

            builder.setContentIntent(resultPendingIntent)
            notificationManager.notify(NOTIFICATION_ID, builder.build())

        } else {
            var intent = Intent("EVENT_SNACKBAR")
            intent.putExtra("DESCRIPTION", description)
            LocalBroadcastManager.getInstance(this).sendBroadcast(intent)
        }
    }



    fun changeWifi(status : Boolean) {
        val wifiManager = this.getSystemService(Context.WIFI_SERVICE) as WifiManager
        wifiManager.isWifiEnabled = status
    }
}