package com.example.wificheck.Presenter

import android.content.Context

interface Tab1Presenter{

    fun getLocationNames(context: Context)

    fun goToDetailPage(index:Int)
}