package com.example.wificheck.view

import com.example.wificheck.Model.Entity.Location

interface MainView {

    fun openAddActivity()
    fun startGeofence(locations: ArrayList<Location>)

}