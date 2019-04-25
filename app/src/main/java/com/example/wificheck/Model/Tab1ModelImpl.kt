package com.example.wificheck.Model

import android.content.Context
import com.example.wificheck.Model.Database.DatabaseHelper
import com.example.wificheck.Model.Entity.Location
import com.example.wificheck.View.fragment.Tab1View

class Tab1ModelImpl(view: Tab1View) : Tab1Model{

    var view: Tab1View = view

    override fun getLocaiton(context: Context):ArrayList<Location> {

        val selectQuery = "SELECT * FROM LOCATION"
        var locations : ArrayList<Location> = ArrayList<Location>()
        val dbHelper = DatabaseHelper(context)
        val db = dbHelper.readableDatabase

        var cursor = db.rawQuery(selectQuery, null)

        cursor.moveToFirst()
        while (!cursor.isAfterLast) {

            val id = cursor.getInt(0)
            val locName = cursor.getString(1)
            val locLong = cursor.getDouble(2)
            val locLat = cursor.getDouble(3)
            val locRad = cursor.getDouble(3)
            val location = Location(id, locName, locLong, locLat, locRad)

            locations.add(location)
            cursor.moveToNext()
        }
        return locations
    }

}