package com.example.wificheck.Presenter

import android.content.Context
import android.view.View
import com.example.wificheck.Model.Entity.Location

interface Tab1Presenter{

    fun getLocations(context: Context, view:View)
}