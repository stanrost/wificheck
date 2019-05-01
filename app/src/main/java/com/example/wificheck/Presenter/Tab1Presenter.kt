package com.example.wificheck.Presenter

import android.content.Context
import android.location.Location

interface Tab1Presenter{

    fun getLocationNames()

    fun goToDetailPage(index:Int)
    fun getDistancesList(lat: Double, long: Double)
}