package com.example.wificheck.view.fragment

import android.content.Context
import com.example.wificheck.model.entity.Location

interface Tab1View {

    fun setListView(locationNames: ArrayList<Location>)
    fun goToDetailPage(id: Int, context: Context)
    fun goToDetailFragment(id: Int, mContext: Context)
    fun removeGeofence(name:String)
}