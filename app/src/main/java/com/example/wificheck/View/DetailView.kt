package com.example.wificheck.view

import com.example.wificheck.model.entity.Location

interface DetailView{
    fun showInformation(name: String, radius: Double, pair: Pair<Double, Double>)
    fun setLocation(location: Location)
}
