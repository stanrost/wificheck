package com.example.wificheck.Presenter

import android.content.Context
import com.example.wificheck.Model.service.LocationServiceImpl
import com.example.wificheck.view.fragment.Tab2View

class Tab2PresenterImpl(var view: Tab2View, var context: Context) : Tab2Presenter {

    override fun getLocationsLatLng() {

        val locations = LocationServiceImpl(context).getLocation()
        if (locations.size > 1) {
            view.setMarkers(LocationServiceImpl(context).getLocation())
        } else if (locations.size > 0) {
            view.setMarker(locations[0])

        } else {
            view.noMarker()
        }
    }

}