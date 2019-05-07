package com.example.wificheck.view

interface UpdateView{
    fun addGeofence()
    fun closeActivity()
    fun showError(errorString:String)
    fun addCircle(radius:Double)
}