package com.example.wificheck.backgroundService

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.example.wificheck.view.MainActivity


class BroadcastReceiver : BroadcastReceiver() {
    val OPEN = "OPEN"
    val CLOSE = "CLOSE"
    val CHANGE = "CHANGE"

    override fun onReceive(context: Context?, intent: Intent?) {
        val action = intent!!.action

        if(OPEN.equals(action)){
            val openIntent = Intent(context, MainActivity::class.java)
            context!!.startActivity(openIntent)
            val closeIntent = Intent(Intent.ACTION_CLOSE_SYSTEM_DIALOGS)
            context.sendBroadcast(closeIntent)
        }
        else if(CLOSE.equals(action)){
            val serviceIntent = Intent(context, MyService::class.java)
            context!!.stopService(serviceIntent)
        }
        else if(CHANGE.equals(action)){
            val serviceIntent = Intent(context, MyService::class.java)
            context!!.startService(serviceIntent)
        }
    }

}
