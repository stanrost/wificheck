package com.example.wificheck.Presenter

import android.content.Context
import com.example.wificheck.Model.service.LocationServiceImpl
import com.example.wificheck.View.MainActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainPresenterImpl(val view: MainActivity, val context : Context) : MainPresenter{

    override fun hideOrShowFloatingActionButton(position: Int) {
        if (position == 0) {
            view.showFloatingActionButton()
        } else if (position == 1) {
            view.showFloatingActionButton()
        } else {
            view.hideFloatingActionButton()
        }
    }

    override fun setGeofenceLocations() {
        view.startGeofence(LocationServiceImpl(context).getLocation())
    }

}