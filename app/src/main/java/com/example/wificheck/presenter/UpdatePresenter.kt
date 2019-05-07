package com.example.wificheck.presenter

import com.example.wificheck.model.entity.Location

interface UpdatePresenter {

    fun  updateLocation(id: Int, name: String?, long: Double?, lat: Double?, radius: Double?)
    fun addCircle(radius: Int)
}