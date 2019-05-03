package com.example.wificheck.view.fragment

import com.example.wificheck.Model.Entity.Location

interface Tab2View {
    fun setMarkers(locations: ArrayList<Location>)
    fun setMarker(location: Location)
    fun noMarker()
}