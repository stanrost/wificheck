package com.example.wificheck.Presenter

import android.content.Context
import com.example.wificheck.Model.Entity.Location

interface AddPresenter {

    fun addLocation(location:Location)
    fun addCircle(radius: Int)
}