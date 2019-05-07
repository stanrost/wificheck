package com.example.wificheck.view

import com.example.wificheck.model.entity.Location

interface MainView {

    fun showFloatingActionButton()
    fun hideFloatingActionButton()
    fun openAddActivity()
    fun startGeofence(locations: ArrayList<Location>)

}