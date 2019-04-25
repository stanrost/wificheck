package com.example.wificheck.Model.Database

import android.provider.BaseColumns
import android.provider.BaseColumns._ID


class Database() {

    class Location : BaseColumns {
        companion object {
            val LOCATION = "Location"
            val LOC_NAME = "name"
            val LOC_LONG = "longitude"
            val LOC_LAT = "latitude"
            val LOC_RAD = "radius"

            val CREATE_TABLE = "CREATE TABLE IF NOT EXISTS " +
                    LOCATION + " (" +
                    _ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    LOC_NAME + " TEXT, " +
                    LOC_LONG + " DOUBLE, " +
                    LOC_LAT + " DOUBLE, " +
                    LOC_RAD + "DOUBLE" + ")"
        }
    }
}
