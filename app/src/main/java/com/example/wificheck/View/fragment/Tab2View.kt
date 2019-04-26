package com.example.wificheck.View.fragment

import com.example.wificheck.Model.Entity.Location

interface Tab2View {
    fun setMarkers(locations: ArrayList<Location>)
    fun setMarker(location: Location)
    fun noMarker()
}