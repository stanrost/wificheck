package com.example.wificheck.Model

import android.content.Context
import com.example.wificheck.Model.Entity.Location

interface Tab1Model{
    fun getLocaiton(context: Context):ArrayList<Location>
}