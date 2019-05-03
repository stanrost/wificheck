package com.example.wificheck.Model.repository

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import com.example.wificheck.Model.Database.Database
import com.example.wificheck.Model.Database.DatabaseHelper
import com.example.wificheck.Model.Entity.Location


class LocationRepositoryImpl : LocationRepository {


    @SuppressLint("Recycle")
    override fun getLocation(context: Context): ArrayList<Location> {

        val selectQuery = "SELECT * FROM LOCATION"
        val locations: ArrayList<Location> = ArrayList<Location>()
        val dbHelper = DatabaseHelper(context)
        val db = dbHelper.readableDatabase

        val cursor = db.rawQuery(selectQuery, null)

        cursor.moveToFirst()
        while (!cursor.isAfterLast) {

            val id = cursor.getInt(0)
            val locName = cursor.getString(1)
            val locLong = cursor.getDouble(2)
            val locRad = cursor.getDouble(3)
            val locLat = cursor.getDouble(4)
            val location = Location(id, locName, locLong, locLat, locRad)

            locations.add(location)
            cursor.moveToNext()
        }
        return locations
    }

    @SuppressLint("Recycle")
    override fun getLocationById(id: Int, context: Context): Location {
        val selectQuery = "SELECT * FROM LOCATION WHERE _ID = $id"
        val locations: ArrayList<Location> = ArrayList<Location>()
        val dbHelper = DatabaseHelper(context)
        val db = dbHelper.readableDatabase

        val cursor = db.rawQuery(selectQuery, null)

        cursor.moveToFirst()
        while (!cursor.isAfterLast) {

            val id = cursor.getInt(0)
            val locName = cursor.getString(1)
            val locLong = cursor.getDouble(2)
            val locRad = cursor.getDouble(3)
            val locLat = cursor.getDouble(4)
            val location = Location(id, locName, locLong, locLat, locRad)

            locations.add(location)
            cursor.moveToNext()
        }
        return locations[0]
    }

    override fun addLocation(context: Context, location: Location) {
        val values = ContentValues()

        val dbHelper = DatabaseHelper(context)
        val db = dbHelper.readableDatabase
        values.put(Database.Location.LOC_NAME, location.name)
        values.put(Database.Location.LOC_LONG, location.longitude)
        values.put(Database.Location.LOC_RAD, location.radius)
        values.put(Database.Location.LOC_LAT, location.latitude)

        db.insert(Database.Location.LOC, null, values)
        db.close()
    }

    override fun removeLocation(context: Context, location: Location){
        val dbHelper = DatabaseHelper(context)
        val db = dbHelper.readableDatabase
        db.delete(Database.Location.LOC, "_id = ${location.id}", null)
    }
}