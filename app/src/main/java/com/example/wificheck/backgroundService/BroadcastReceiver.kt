package com.example.wificheck.backgroundService

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.example.wificheck.View.MainActivity
import com.example.wificheck.View.fragment.Tab1Fragment


class BroadcastReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {
        val action = intent!!.action

        if("Open".equals(action)){
            var intent = Intent(context, MainActivity::class.java)
            context!!.startActivity(intent)
            val closeIntent = Intent(Intent.ACTION_CLOSE_SYSTEM_DIALOGS)
            context!!.sendBroadcast(closeIntent)
        }
        else if("Close".equals(action)){
            var serviceIntent = Intent(context, MyService::class.java)
            context!!.stopService(serviceIntent)
        }
        else if("change".equals(action)){
            var serviceIntent = Intent(context, MyService::class.java)
            context!!.startService(serviceIntent)
        }
    }

}
