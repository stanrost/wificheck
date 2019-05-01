package com.example.wificheck.backgroundService


import android.app.*
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Binder
import android.os.IBinder
import android.support.v4.content.LocalBroadcastManager
import android.widget.RemoteViews
import com.example.wificheck.R

class MyService : Service() {

    val OPEN = "OPEN"
    val CLOSE = "CLOSE"
    val DESCRIPTION = "DESCRIPTION"
    val SERVICE = "SERVICE"

    private val mServiceBinder = ServiceBinder()
    var mContext : Context? = null
    lateinit var mNotificationView : RemoteViews
    lateinit var mBuilder : Notification.Builder

    private val mNotification: Notification
        get() {
            val channel = NotificationChannel("01", "channel", NotificationManager.IMPORTANCE_DEFAULT)

            val notificationManager = getSystemService(NotificationManager::class.java)
            notificationManager.createNotificationChannel(channel)

            mBuilder = Notification.Builder(applicationContext, "01").setAutoCancel(true)
                .setSmallIcon(R.mipmap.ic_launcher)
                //.setContentTitle("The geofence app is running")
                //.setContentText("Checking your location...")
                .setCustomContentView(mNotificationView)
            return mBuilder.build()
        }

    override fun onBind(intent: Intent): IBinder {
        return mServiceBinder
    }

    override fun onCreate() {
        super.onCreate()
        mContext = this

        val iOpen = Intent(mContext, BroadcastReceiver::class.java)
        iOpen.setAction(OPEN)
        val pOpen = PendingIntent.getBroadcast(this, 0, iOpen, 0)

        val iClose = Intent(mContext, BroadcastReceiver::class.java)
        iClose.setAction(CLOSE)
        val pClose = PendingIntent.getBroadcast(this, 0,iClose,0)

        var text = ""
        val receiver = object : android.content.BroadcastReceiver() {
            override fun onReceive(context: Context?, intent: Intent?) {
                val description = intent!!.getSerializableExtra(DESCRIPTION ) as String
                text = description
            }
        }
        LocalBroadcastManager.getInstance(this).registerReceiver(receiver, IntentFilter(SERVICE));

        mNotificationView = RemoteViews(packageName, R.layout.notification_running)
        mNotificationView.setTextViewText(R.id.tv_check, text)
        mNotificationView.setOnClickPendingIntent(R.id.open, pOpen)
        mNotificationView.setOnClickPendingIntent(R.id.close, pClose)
        startForeground(12345678, mNotification)
    }

    inner class ServiceBinder : Binder() {
        val service: MyService
            get() = this@MyService
    }

}
