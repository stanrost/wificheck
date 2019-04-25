package com.example.wificheck.Model

import android.content.Context
import com.example.wificheck.Model.Entity.Location

interface AddModel{
    fun addLocation (context:Context, location: Location)
}