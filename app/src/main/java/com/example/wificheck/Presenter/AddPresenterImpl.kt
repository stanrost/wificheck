package com.example.wificheck.Presenter

import android.content.Context
import com.example.wificheck.Model.Entity.Location
import com.example.wificheck.Model.service.LocationServiceImpl
import com.example.wificheck.View.AddActivity

class AddPresenterImpl(val view: AddActivity, val context: Context) : AddPresenter {

    override fun addLocation(location: Location) {
        LocationServiceImpl(context).addLocation(location)
    }

    override fun addCircle(radius: Int) {
        view.addCircle(radius * 1.0)
    }

}