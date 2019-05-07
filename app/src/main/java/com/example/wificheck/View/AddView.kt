package com.example.wificheck.view

interface AddView{
    fun showError(error: String)
    fun addCircle(radius: Double)
    fun addGeofence()
    fun closeActivity()
}