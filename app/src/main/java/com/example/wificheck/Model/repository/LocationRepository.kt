package com.example.wificheck.Model.repository

import android.content.Context
import com.example.wificheck.Model.Entity.Location


interface LocationRepository {

    fun getLocation(context: Context):ArrayList<Location>
    fun getLocationById(id: Int, context: Context) :Location
    fun addLocation(context: Context, location: Location)
    fun removeLocation(context: Context, location: Location)
}