package com.example.wificheck.Presenter

import android.content.Context
import com.example.wificheck.Model.Entity.Location
import com.example.wificheck.Model.service.LocationServiceImpl
import com.example.wificheck.Model.service.SharedPreferenceService
import com.example.wificheck.Model.service.SharedPreferenceServiceImpl
import com.example.wificheck.View.fragment.Tab1View
import java.util.Collections.sort
import kotlin.Comparator
import kotlin.collections.ArrayList

class Tab1PresenterImpl(var view: Tab1View, context: Context) : Tab1Presenter {


    private val globalContext: Context = context

    override fun getLocations() {
        var locations = LocationServiceImpl(globalContext).getLocation()
        view.setListView(locations)
    }

    override fun getLocationsByName() {
        var locations = LocationServiceImpl(globalContext).getLocation()

        sort(locations, Comparator { o1: Location, o2: Location ->
            o1.name.compareTo(o2.name)
        })

        view.setListView(locations)

    }

    override fun goToDetailPage(id: Int) {
        view.goToDetailPage(id, globalContext)
    }

    override fun getDistancesList(lat: Double, long: Double) {

        var distances = ArrayList<Pair<Location, Double>>()

        for (location in LocationServiceImpl(globalContext).getLocation()) {
            var distance = distance(lat, long, location.latitude, location.longitude)

            distances.add(Pair(location, distance))
        }

        distances.sortBy { it.second }

        var locationList = ArrayList<Location>()

        for (p1 in distances) {
            var (l1, d1) = p1
            locationList.add(l1)
        }

        view.setListView(locationList)
    }

    private fun distance(lat1: Double, lng1: Double, lat2: Double, lng2: Double): Double {

        val earthRadius = 3958.75 // in miles, change to 6371 for kilometers

        val dLat = Math.toRadians(lat2 - lat1)
        val dLng = Math.toRadians(lng2 - lng1)

        val sindLat = Math.sin(dLat / 2)
        val sindLng = Math.sin(dLng / 2)

        val a = Math.pow(sindLat, 2.0) + (Math.pow(sindLng, 2.0)
                * Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)))

        val c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a))

        return earthRadius * c
    }


    override fun getList(lat: Double, long: Double) {
        if (SharedPreferenceServiceImpl().getListOrder(globalContext) == 0) {
            getDistancesList(lat, long)
        } else if (SharedPreferenceServiceImpl().getListOrder(globalContext) == 1) {
            getLocations()
        } else if (SharedPreferenceServiceImpl().getListOrder(globalContext) == 2) {
            getLocationsByName()
        }
        else{
            getLocationsByName()
        }
    }

    override fun setSort(sortId: Int) {
        SharedPreferenceServiceImpl().setSort(sortId, globalContext)
    }


}