package com.example.wificheck.backgroundService

import android.app.*
import android.app.Notification.VISIBILITY_SECRET
import android.content.Context
import android.content.Intent
import android.os.Binder
import android.os.Build
import android.os.IBinder
import android.support.v4.app.NotificationCompat
import com.example.wificheck.R

class MyService : Service() {

    companion object {
        private const val OPEN = "OPEN"
        private const val CLOSE = "CLOSE"
        private const val ID = 1
        private const val CHANNEL_ID = "01"
    }

    private val mServiceBinder = ServiceBinder()
    var mContext: Context? = null
    lateinit var mBuilder: NotificationCompat.Builder
    lateinit var notificationManager: NotificationManager

    private val mNotification: Notification
        get() {
            notificationManager =
                applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                val channel = NotificationChannel(CHANNEL_ID, "channel", NotificationManager.IMPORTANCE_DEFAULT)

                notificationManager = getSystemService(NotificationManager::class.java)
                notificationManager.createNotificationChannel(channel)
            }
            val iOpen = Intent(mContext, BroadcastReceiver::class.java)
            iOpen.setAction(OPEN)
            val pOpen = PendingIntent.getBroadcast(this, 0, iOpen, 0)

            val iClose = Intent(mContext, BroadcastReceiver::class.java)
            iClose.setAction(CLOSE)
            val pClose = PendingIntent.getBroadcast(this, 0, iClose, 0)

            mBuilder = NotificationCompat.Builder(applicationContext, CHANNEL_ID).setAutoCancel(true)
                .setSmallIcon(R.drawable.ic_wifi)
                .setContentTitle(getString(R.string.wifi_checker_checking))
                .setVisibility(VISIBILITY_SECRET)
                .addAction(R.drawable.ic_option, getString(R.string.close), pClose)
                .addAction(R.drawable.ic_option, getString(R.string.open), pOpen)
            return mBuilder.build()
        }

    override fun onBind(intent: Intent): IBinder {
        return mServiceBinder
    }

    override fun onCreate() {
        super.onCreate()
        mContext = this
        startForeground(ID, mNotification)
    }

    inner class ServiceBinder : Binder() {
        val service: MyService
            get() = this@MyService
    }

}
