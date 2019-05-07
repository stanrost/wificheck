package com.example.wificheck.presenter

interface MainPresenter {

    fun hideOrShowFloatingActionButton(position:Int)
    fun setGeofenceLocations()
    fun setInsideLocation(description: String)
}