package com.example.wificheck.view.fragment

import android.content.Context
import com.example.wificheck.Model.Entity.Location

interface Tab1View {

    fun setListView(locationNames: ArrayList<Location>)
    fun goToDetailPage(id: Int, context: Context)
}