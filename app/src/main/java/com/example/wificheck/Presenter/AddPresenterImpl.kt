package com.example.wificheck.Presenter

import android.content.Context
import com.example.wificheck.Model.AddModelImpl
import com.example.wificheck.Model.Entity.Location

class AddPresenterImpl : AddPresenter{
    override fun addLocation(context: Context, location: Location) {
        AddModelImpl().addLocation(context, location)
    }


}