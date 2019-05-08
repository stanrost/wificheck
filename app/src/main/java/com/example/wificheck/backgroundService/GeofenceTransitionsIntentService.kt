package com.example.wificheck.backgroundService

import android.graphics.Color
import android.support.v4.app.NotificationCompat
import android.support.v4.app.TaskStackBuilder
import com.google.android.gms.location.Geofence
import com.google.android.gms.location.GeofencingEvent
import android.app.*
import android.content.*
import android.net.wifi.WifiManager
import android.support.v4.content.LocalBroadcastManager
import com.example.wificheck.model.repository.SharedPreferenceUpdateImpl
import com.example.wificheck.R
import com.example.wificheck.presenter.service.GoefenceTransitionPresenter
import com.example.wificheck.presenter.service.GoefenceTransitionPresenterImpl
import com.example.wificheck.view.MainActivity


class GeofenceTransitionsIntentService : IntentService("GeofenceTransitionsIntentService") {

    companion object{
        private const val DISCRIPTION = "DESCRIPTION"
        private const val SHOW_DISCRIPTION = "SHOW_DESCRIPTION"
        private const val NOTIFICATION_ID = 234
    }

    private lateinit var notificationManager: NotificationManager

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        super.onStartCommand(intent, startId, startId)
        return START_STICKY
    }

    override fun onHandleIntent(intent: Intent?) {
        val geofencingEvent = GeofencingEvent.fromIntent(intent)
        if (geofencingEvent.hasError()) {
            return
        }
        val geofenceTransition = geofencingEvent.geofenceTransition
        val geofenceId = getFirstReminder(geofencingEvent.triggeringGeofences)

        if (geofenceTransition == Geofence.GEOFENCE_TRANSITION_ENTER && getEnteringCheck()) {
            checkAutomaticOrNotificationSettings(getString(R.string.inside_location) +" $geofenceId", true)
        } else if (geofenceTransition == Geofence.GEOFENCE_TRANSITION_EXIT && getLeavingCheck()) {
            checkAutomaticOrNotificationSettings(getString(R.string.outside_location) + " $geofenceId",false)
        } else {

        }
    }

    private fun checkAutomaticOrNotificationSettings(description : String, wifiChange : Boolean){
        val checkId = SharedPreferenceUpdateImpl(applicationContext).getChecked()

        when (checkId) {
            0 -> {
                openNotification(description)
            }
            1 -> {
                changeWifi(wifiChange)
            }
            2 -> {
                openNotification(description)
                changeWifi(wifiChange)
            }
        }
    }

    private fun getFirstReminder(triggeringGeofences: List<Geofence>): String? {
        val firstGeofence = triggeringGeofences[0]
        return firstGeofence.requestId
    }

    private fun checkIfRunning(): Boolean {
        val geofencePresenter : GoefenceTransitionPresenter = GoefenceTransitionPresenterImpl(applicationContext)
        return geofencePresenter.getActive()
    }

    private fun getEnteringCheck() : Boolean{
        val geofencePresenter : GoefenceTransitionPresenter = GoefenceTransitionPresenterImpl(applicationContext)
        return geofencePresenter.getEnteringCheck()
    }
    private fun getLeavingCheck() : Boolean{
        val geofencePresenter : GoefenceTransitionPresenter = GoefenceTransitionPresenterImpl(applicationContext)
        return geofencePresenter.getLeavingCheck()
    }

    private fun getVibrateCheck() : Boolean{
        val geofencePresenter : GoefenceTransitionPresenter = GoefenceTransitionPresenterImpl(applicationContext)
        return geofencePresenter.getVibrateCheck()
    }

    private fun getLightCheck() : Boolean{
        val geofencePresenter : GoefenceTransitionPresenter = GoefenceTransitionPresenterImpl(applicationContext)
        return geofencePresenter.getLightCheck()
    }

    private fun openNotification(description: String) {

        val wifiManager = this.getSystemService(Context.WIFI_SERVICE) as WifiManager
        wifiManager.isWifiEnabled = true

        if (!checkIfRunning()) {
            notificationManager =
                applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

            val CHANNEL_ID = "my_channel_$description"
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                val name = "my_channel"
                val Description = "This is my channel"
                val importance = NotificationManager.IMPORTANCE_HIGH
                val mChannel = NotificationChannel(CHANNEL_ID, name, importance)
                mChannel.description = Description
                mChannel.enableLights(getLightCheck())
                mChannel.lightColor = Color.RED
                mChannel.enableVibration(getVibrateCheck())
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
            builder.setAutoCancel(true)
            notificationManager.notify(NOTIFICATION_ID, builder.build())

            val intent = Intent(SHOW_DISCRIPTION)
            intent.putExtra(DISCRIPTION, description)
            LocalBroadcastManager.getInstance(this).sendBroadcast(intent)
        } else {
            val intent = Intent(SHOW_DISCRIPTION)
            intent.putExtra(DISCRIPTION, description)
            LocalBroadcastManager.getInstance(this).sendBroadcast(intent)
        }
    }

    private fun changeWifi(status : Boolean) {
        val wifiManager = this.getSystemService(Context.WIFI_SERVICE) as WifiManager
        wifiManager.isWifiEnabled = status
    }
}