package com.example.wificheck.presenter

import com.example.wificheck.model.entity.Location

interface AddPresenter {

    fun addLocation(name:String?, long: Double?, lat: Double?, radius: Double?)
    fun addCircle(radius: Int)
}