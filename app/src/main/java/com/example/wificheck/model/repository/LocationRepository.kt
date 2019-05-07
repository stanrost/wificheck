package com.example.wificheck.model.repository

import com.example.wificheck.model.entity.Location


interface LocationRepository {

    fun getLocation():ArrayList<Location>
    fun getLocationById(id: Int) :Location
    fun addLocation (location: Location)
    fun removeLocation(location: Location)
    fun updateLocation(location: Location)
}