package com.example.wificheck.View.fragment

import android.content.Context

interface Tab1View {

    fun setListView(locationNames:ArrayList<String>)

    fun goToDetailPage(id:Int, context:Context)
}