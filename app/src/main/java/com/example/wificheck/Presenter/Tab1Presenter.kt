package com.example.wificheck.Presenter

import android.content.Context
import com.example.wificheck.Model.Entity.Location

interface Tab1Presenter{

    fun getLocations()
    fun getLocationsByName()
    fun goToDetailPage(id:Int)
    fun getDistancesList(lat: Double, long: Double)
    fun getList(lat: Double, long: Double)
    fun setSort(sortId: Int)
}