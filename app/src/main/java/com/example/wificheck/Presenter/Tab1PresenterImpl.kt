package com.example.wificheck.Presenter

import android.content.Context
import com.example.wificheck.Model.Entity.Location
import com.example.wificheck.Model.service.LocationServiceImpl
import com.example.wificheck.Model.service.SharedPreferenceServiceImpl
import com.example.wificheck.view.fragment.Tab1View
import java.util.Collections.sort
import kotlin.Comparator
import kotlin.collections.ArrayList

class Tab1PresenterImpl(var mView: Tab1View, var mContext: Context) : Tab1Presenter {

    var mLocations = ArrayList<Location>()

    override fun getLocations() {
        mLocations.clear()
        mLocations = LocationServiceImpl(mContext).getLocation()
        mView.setListView(mLocations)
    }

    override fun getLocationsByName() {
        mLocations.clear()
        mLocations = LocationServiceImpl(mContext).getLocation()

        sort(mLocations, Comparator { o1: Location, o2: Location ->
            o1.name.compareTo(o2.name)
        })
        mView.setListView(mLocations)
    }

    override fun goToDetailPage(id: Int) {
        mView.goToDetailPage(id, mContext)
    }

    override fun getDistancesList(lat: Double, long: Double) {
        val distances = ArrayList<Pair<Location, Double>>()

        for (location in LocationServiceImpl(mContext).getLocation()) {
            val distance = distance(lat, long, location.latitude, location.longitude)

            distances.add(Pair(location, distance))
        }

        distances.sortBy { it.second }
        mLocations.clear()

        for (p1 in distances) {
            val (l1, d1) = p1
            mLocations.add(l1)
        }

        mView.setListView(mLocations)
    }

    private fun distance(lat1: Double, lng1: Double, lat2: Double, lng2: Double): Double {

        val earthRadius = 6371

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
        if (SharedPreferenceServiceImpl().getListOrder(mContext) == 0) {
            getDistancesList(lat, long)
        } else if (SharedPreferenceServiceImpl().getListOrder(mContext) == 1) {
            getLocations()
        } else if (SharedPreferenceServiceImpl().getListOrder(mContext) == 2) {
            getLocationsByName()
        }
        else{
            getLocationsByName()
        }
    }

    override fun setSort(sortId: Int) {
        SharedPreferenceServiceImpl().setSort(sortId, mContext)
    }

    override fun changeList(text: String){

        var locations = mLocations.filter{
            it.name.toUpperCase().contains(text.toUpperCase())
        } as ArrayList

        mView.setListView(locations)
    }

    override fun removeLocation(location: Location, lat: Double, long: Double) {
        LocationServiceImpl(mContext).removeLocation(location)
        getList(lat, long)
    }

}