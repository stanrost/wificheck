package com.example.wificheck.presenter.fragment

import android.content.Context
import com.example.wificheck.model.entity.Location
import com.example.wificheck.model.repository.LocationRepository
import com.example.wificheck.model.repository.LocationRepositoryImpl
import com.example.wificheck.model.repository.SharedPreferenceUpdate
import com.example.wificheck.model.repository.SharedPreferenceUpdateImpl
import com.example.wificheck.view.fragment.Tab1View
import java.util.Collections.sort

class Tab1PresenterImpl(
    private var mView: Tab1View,
    private var mContext: Context,
    private var mLocationRepository: LocationRepository = LocationRepositoryImpl(mContext),
    private var mSharedPreference: SharedPreferenceUpdate = SharedPreferenceUpdateImpl(mContext)
) :
    Tab1Presenter {
    var mLocations = ArrayList<Location>()

    override fun getLocations() {
        mLocations.clear()
        mLocations = mLocationRepository.getLocation()
        mView.setListView(mLocations)
    }

    override fun getLocationsByName() {
        mLocations.clear()
        mLocations = mLocationRepository.getLocation()

        sort(mLocations, Comparator { o1: Location, o2: Location ->
            o1.name.toUpperCase().compareTo(o2.name.toUpperCase())
        })
        mView.setListView(mLocations)
    }

    override fun goToDetailPage(id: Int) {
        mView.goToDetailPage(id, mContext)
    }

    override fun getDistancesList(lat: Double, long: Double) {
        val distances = ArrayList<Pair<Location, Double>>()

        for (location in mLocationRepository.getLocation()) {
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

    override fun getList(lat: Double, long: Double) {
        when {
            SharedPreferenceUpdateImpl(mContext).getListOrder() == 0 -> getDistancesList(lat, long)
            SharedPreferenceUpdateImpl(mContext).getListOrder() == 1 -> getLocations()
            SharedPreferenceUpdateImpl(mContext).getListOrder() == 2 -> getLocationsByName()
            else -> getLocationsByName()
        }
    }

    override fun setSort(sortId: Int) {
        SharedPreferenceUpdateImpl(mContext).setSort(sortId)
    }

    override fun changeList(text: String) {
        val locations = mLocations.filter {
            it.name.toUpperCase().contains(text.toUpperCase())
        } as ArrayList

        mView.setListView(locations)
    }

    override fun removeLocation(location: Location, lat: Double, long: Double) {
        mLocationRepository.removeLocation(location)
        mView.removeGeofence(location.name)
        getList(lat, long)
    }

    override fun setInsideLocation(description: String) {
        mSharedPreference.setInsideLocation(description)
    }

    override fun getInsideLocation(): String {
        return mSharedPreference.getInsideLocation()
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

    fun goToDetailFragment(id: Int) {
        mView.goToDetailFragment(id, mContext)
    }
}