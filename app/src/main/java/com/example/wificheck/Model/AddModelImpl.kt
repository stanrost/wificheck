package com.example.wificheck.Model

import android.content.ContentValues
import android.content.Context
import com.example.wificheck.Model.Database.Database.Location.Companion.LOCATION
import com.example.wificheck.Model.Database.Database.Location.Companion.LOC_LAT
import com.example.wificheck.Model.Database.Database.Location.Companion.LOC_LONG
import com.example.wificheck.Model.Database.Database.Location.Companion.LOC_NAME
import com.example.wificheck.Model.Database.DatabaseHelper
import com.example.wificheck.Model.Entity.Location

class AddModelImpl : AddModel{
    override fun addLocation(context: Context, location: Location) {
        val values = ContentValues()

        val dbHelper = DatabaseHelper(context)
        val db = dbHelper.readableDatabase
        values.put(LOC_NAME, location.name)
        values.put(LOC_LONG, location.longitude)
        values.put(LOC_LAT, location.latitude)

        db.insert(LOCATION, null, values)
        db.close()
    }

}