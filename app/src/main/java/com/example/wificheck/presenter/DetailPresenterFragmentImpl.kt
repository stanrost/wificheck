package com.example.wificheck.presenter

import android.content.Context
import com.example.wificheck.model.entity.Location
import com.example.wificheck.model.repository.LocationRepositoryImpl
import com.example.wificheck.view.DetailActivity
import com.example.wificheck.view.fragment.DetailFragment
import com.example.wificheck.view.fragment.DetailFragmentView

class DetailPresenterFragmentImpl(val view: DetailFragmentView, val context: Context, val locationRepository : LocationRepositoryImpl = LocationRepositoryImpl(context)) : DetailFragmentPresenter {

    override fun getLocationById(id: Int) {

        val location: Location = locationRepository.getLocationById(id)
        val name = location.name
        val radius = location.radius
        val pair: Pair<Double, Double> = Pair(location.latitude, location.longitude)
        view.showInformation(name, radius, pair)
    }
}