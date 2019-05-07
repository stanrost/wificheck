package com.example.wificheck.presenter.fragment

import android.content.Context
import com.example.wificheck.model.repository.LocationRepository
import com.example.wificheck.model.repository.LocationRepositoryImpl
import com.example.wificheck.view.fragment.Tab2View

class Tab2PresenterImpl(
    private val view: Tab2View,
    private val context: Context,
    private val locationRepository: LocationRepository = LocationRepositoryImpl(context)
) :
    Tab2Presenter {
    override fun getLocationsLatLng() {
        val locations = locationRepository.getLocation()
        when {
            locations.size > 1 -> view.setMarkers(locations)
            locations.size > 0 -> view.setMarker(locations[0])
            else -> view.noMarker()
        }
    }
}