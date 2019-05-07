package com.example.wificheck.model.repository

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import com.example.wificheck.model.database.Database
import com.example.wificheck.model.database.DatabaseHelper
import com.example.wificheck.model.entity.Location


class LocationRepositoryImpl(var context: Context) : LocationRepository {


    // samenvoegen van dphelper in constructor
    @SuppressLint("Recycle")
    override fun getLocation(): ArrayList<Location> {

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
    override fun getLocationById(id: Int): Location {
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

    override fun addLocation(location: Location) {
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

    override fun removeLocation(location: Location){
        val dbHelper = DatabaseHelper(context)
        val db = dbHelper.readableDatabase
        db.delete(Database.Location.LOC, "_id = ${location.id}", null)
    }

    override fun updateLocation(location: Location) {
        val dbHelper = DatabaseHelper(context)
        val db = dbHelper.readableDatabase

        val values = ContentValues()
        values.put(Database.Location.LOC_NAME, location.name)
        values.put(Database.Location.LOC_LONG, location.longitude)
        values.put(Database.Location.LOC_RAD, location.radius)
        values.put(Database.Location.LOC_LAT, location.latitude)
        db.update(Database.Location.LOC, values,"_id = ${location.id}", null)
    }
}