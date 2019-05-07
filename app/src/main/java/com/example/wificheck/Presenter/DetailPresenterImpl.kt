package com.example.wificheck.presenter

import android.content.Context
import com.example.wificheck.model.entity.Location
import com.example.wificheck.model.repository.LocationRepository
import com.example.wificheck.model.repository.LocationRepositoryImpl
import com.example.wificheck.view.DetailActivity

class DetailPresenterImpl(
    private val view: DetailActivity,
    private val locationRepository: LocationRepository = LocationRepositoryImpl(view as Context)
) : DetailPresenter {

    override fun getLocationById(id: Int) {
        val location: Location = locationRepository.getLocationById(id)
        val name = location.name
        val radius = location.radius
        val pair: Pair<Double, Double> = Pair(location.latitude, location.longitude)
        view.showInformation(name, radius, pair)
        view.setLocation(location)
    }
}