package com.example.wificheck.presenter.fragment

import android.content.Context
import com.example.wificheck.model.repository.LocationRepositoryImpl
import com.example.wificheck.presenter.fragment.Tab2Presenter
import com.example.wificheck.view.fragment.Tab2View

class Tab2PresenterImpl(val view: Tab2View,  val context: Context, val locationRepository : LocationRepositoryImpl = LocationRepositoryImpl(context)) :
    Tab2Presenter {

    override fun getLocationsLatLng() {

        val locations = locationRepository.getLocation()
        if (locations.size > 1) {
            view.setMarkers(locations)
        } else if (locations.size > 0) {
            view.setMarker(locations[0])
        } else {
            view.noMarker()
        }
    }
}