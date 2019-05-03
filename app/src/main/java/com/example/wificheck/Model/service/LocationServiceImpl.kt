package com.example.wificheck.Model.service

import android.content.Context
import com.example.wificheck.Model.Entity.Location
import com.example.wificheck.Model.repository.LocationRepositoryImpl

class LocationServiceImpl(var context: Context) : LocationService {

    override fun getNamesAndIds(): ArrayList<Pair<String, Int>> {
        val pairs: ArrayList<Pair<String, Int>> = ArrayList<Pair<String, Int>>()

        for (location in LocationRepositoryImpl().getLocation(context)) {
            pairs.add(Pair(location.name, location.id))
        }
        return pairs
    }

    override fun getLocationById(id: Int): Location {
        return LocationRepositoryImpl().getLocationById(id, context)
    }

    override fun getLocation(): ArrayList<Location> {
        return LocationRepositoryImpl().getLocation(context)
    }

    override fun addLocation(location: Location) {
        LocationRepositoryImpl().addLocation(context, location)
    }

    override fun removeLocation(location: Location) {
        LocationRepositoryImpl().removeLocation(context, location)
    }
}