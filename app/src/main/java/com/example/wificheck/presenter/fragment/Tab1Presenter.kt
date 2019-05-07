package com.example.wificheck.presenter.fragment

import com.example.wificheck.model.entity.Location

interface Tab1Presenter{

    fun getLocations()
    fun getLocationsByName()
    fun goToDetailPage(id:Int)
    fun getDistancesList(lat: Double, long: Double)
    fun getList(lat: Double, long: Double)
    fun setSort(sortId: Int)
    fun changeList(text: String)
    fun removeLocation(location: Location, lat: Double, long: Double)
    fun setInsideLocation(description: String)
    fun getInsideLocation(): String
}