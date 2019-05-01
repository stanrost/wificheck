package com.example.wificheck.Presenter

import android.content.Context
import android.location.Location
import com.example.wificheck.Model.service.LocationServiceImpl
import com.example.wificheck.View.fragment.Tab1View

class Tab1PresenterImpl(var view: Tab1View, context: Context) : Tab1Presenter {

    lateinit var pairs: ArrayList<Pair<String, Int>>
    private val globalContext: Context = context

    override fun getLocationNames() {
        pairs = LocationServiceImpl(globalContext).getNamesAndIds()
        val names = ArrayList<String>()

        for ((name, id) in pairs) {
            names.add(name)
        }
        view.setListView(names)
    }

    override fun goToDetailPage(index: Int) {
        val (name, id) = pairs[index]
        view.goToDetailPage(id, globalContext)
    }

    override fun getDistancesList(lat: Double, long: Double) {

        var distances = ArrayList<Double>()

        for(location in LocationServiceImpl(globalContext).getLocation()){

        }


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
}