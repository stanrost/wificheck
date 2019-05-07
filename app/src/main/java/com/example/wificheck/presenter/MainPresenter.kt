package com.example.wificheck.presenter

import android.support.v4.view.ViewPager

interface MainPresenter {

    fun hideOrShowFloatingActionButton(position:Int)
    fun setGeofenceLocations()
    fun setInsideLocation(description: String)
    fun checkView(viewPager: ViewPager?)
    fun setActive(active: Boolean)
}