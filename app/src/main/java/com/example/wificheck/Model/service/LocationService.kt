package com.example.wificheck.Model.service

import com.example.wificheck.Model.Entity.Location


interface LocationService {

    fun getNamesAndIds ():ArrayList<Pair<String, Int>>

    fun getLocationById(id: Int) :Location

    fun getLocation():ArrayList<Location>

    fun addLocation(location: Location)
}