package com.example.wificheck.Presenter

import android.content.Context
import com.example.wificheck.Model.service.LocationServiceImpl
import com.example.wificheck.View.fragment.Tab2View

class Tab2PresenterImpl(view:Tab2View, context: Context) : Tab2Presenter{

    var view: Tab2View = view
    var context:Context = context

    override fun getLocationsLatLng() {

        view.setMarkers(LocationServiceImpl(context).getLocation())
    }

}