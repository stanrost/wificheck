package com.example.wificheck.backgroundService


import android.app.*
import android.app.Notification.VISIBILITY_SECRET
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Binder
import android.os.IBinder
import android.support.v4.app.NotificationCompat
import android.support.v4.content.LocalBroadcastManager
import android.widget.RemoteViews
import com.example.wificheck.R

class MyService : Service() {

    val OPEN = "OPEN"
    val CLOSE = "CLOSE"
    val DESCRIPTION = "DESCRIPTION"
    val SERVICE = "SERVICE"
    val DISCRIPTION = "DESCRIPTION"
    val SHOW_DISCRIPTION = "SHOW_DESCRIPTION"

    private val mServiceBinder = ServiceBinder()
    var mContext : Context? = null
    lateinit var mDescription : String
    lateinit var mNotificationView : RemoteViews
    lateinit var mBuilder : Notification.Builder
    lateinit var mBroadcastReceiver: android.content.BroadcastReceiver

    private val mNotification: Notification
        get() {
            val channel = NotificationChannel("01", "channel", NotificationManager.IMPORTANCE_DEFAULT)

            val notificationManager = getSystemService(NotificationManager::class.java)
            notificationManager.createNotificationChannel(channel)

            val iOpen = Intent(mContext, BroadcastReceiver::class.java)
            iOpen.setAction(OPEN)
            val pOpen = PendingIntent.getBroadcast(this, 0, iOpen, 0)

            val iClose = Intent(mContext, BroadcastReceiver::class.java)
            iClose.setAction(CLOSE)
            val pClose = PendingIntent.getBroadcast(this, 0,iClose,0)

            mBuilder = Notification.Builder(applicationContext, "01").setAutoCancel(true)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle("Wifi Checker is checking")
                .setContentText(mDescription)
                .setVisibility(VISIBILITY_SECRET)
                .addAction(R.drawable.ic_option, "close", pClose)
                .addAction(R.drawable.ic_option, "open", pOpen)
            return mBuilder.build()
        }

    override fun onBind(intent: Intent): IBinder {
        return mServiceBinder
    }

    override fun onCreate() {
        super.onCreate()
        mContext = this

        mBroadcastReceiver = object : android.content.BroadcastReceiver() {
            override fun onReceive(context: Context?, intent: Intent?) {
                val description = intent!!.getSerializableExtra(DISCRIPTION) as String
                mDescription = description
                //ugly
                startForeground(12345678, mNotification)
                //mBuilder.build()
            }
        }

        LocalBroadcastManager.getInstance(applicationContext).registerReceiver(mBroadcastReceiver, IntentFilter(SHOW_DISCRIPTION))
        mDescription = ""
        startForeground(12345678, mNotification)
    }

    inner class ServiceBinder : Binder() {
        val service: MyService
            get() = this@MyService
    }

}
