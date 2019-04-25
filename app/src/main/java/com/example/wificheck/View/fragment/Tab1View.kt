package com.example.wificheck.View.fragment

import android.view.View
import com.example.wificheck.Model.Entity.Location

interface Tab1View {

    fun setListView(locations:ArrayList<Location>, view: View)
}