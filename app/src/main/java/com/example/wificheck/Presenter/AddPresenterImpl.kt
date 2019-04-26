package com.example.wificheck.Presenter

import android.content.Context
import com.example.wificheck.Model.Entity.Location
import com.example.wificheck.Model.service.LocationServiceImpl

class AddPresenterImpl : AddPresenter{
    override fun addLocation(context: Context, location: Location) {
        LocationServiceImpl(context).addLocation(location)
    }


}