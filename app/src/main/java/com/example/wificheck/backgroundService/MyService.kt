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

    private val binder = ServiceBinder()
    var context : Context? = null
    lateinit var notificationView : RemoteViews
    lateinit var receiver : BroadcastReceiver
    lateinit var builder : Notification.Builder

    private val notification: Notification
        get() {

            val channel = NotificationChannel("channel_01", "My Channel", NotificationManager.IMPORTANCE_DEFAULT)

            val notificationManager = getSystemService(NotificationManager::class.java)
            notificationManager.createNotificationChannel(channel)

            builder = Notification.Builder(applicationContext, "channel_01").setAutoCancel(true)
                .setSmallIcon(R.mipmap.ic_launcher)
                //.setContentTitle("The geofence app is running")
                //.setContentText("Checking your location...")
                .setCustomContentView(notificationView)
            return builder.build()
        }

    override fun onBind(intent: Intent): IBinder {
        return binder
    }

    override fun onCreate() {
        super.onCreate()
        context = this

        val iOpen = Intent(context, BroadcastReceiver::class.java)
        iOpen.setAction("Open")
        val pOpen = PendingIntent.getBroadcast(this, 0, iOpen, 0)

        val iClose = Intent(context, BroadcastReceiver::class.java)
        iClose.setAction("Close")
        val pClose = PendingIntent.getBroadcast(this, 0,iClose,0)

        var text = ""
        var receiver = object : android.content.BroadcastReceiver() {
            override fun onReceive(context: Context?, intent: Intent?) {
                var description = intent!!.getSerializableExtra("DESCRIPTION" ) as String
                text = description
            }
        }
        LocalBroadcastManager.getInstance(this).registerReceiver(receiver, IntentFilter("SERVICE"));

        notificationView = RemoteViews(packageName, R.layout.notification_running)


        notificationView.setTextViewText(R.id.tv_check, text)

        notificationView.setOnClickPendingIntent(R.id.open, pOpen)
        notificationView.setOnClickPendingIntent(R.id.close, pClose)

        startForeground(12345678, notification)


    }


    inner class ServiceBinder : Binder() {
        val service: MyService
            get() = this@MyService
    }

}
