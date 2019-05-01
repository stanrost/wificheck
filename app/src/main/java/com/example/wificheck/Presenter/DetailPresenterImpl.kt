package com.example.wificheck.Presenter

import com.example.wificheck.Model.Entity.Location
import com.example.wificheck.Model.service.LocationServiceImpl
import com.example.wificheck.View.DetailActivity

class DetailPresenterImpl(val view: DetailActivity) : DetailPresenter {

    override fun getLocationById(id: Int) {

        val location: Location = LocationServiceImpl(view.applicationContext).getLocationById(id)
        val name = location.name
        val radius = location.radius
        val pair: Pair<Double, Double> = Pair(location.latitude, location.longitude)
        view.showInformation(name, radius, pair)
    }
}