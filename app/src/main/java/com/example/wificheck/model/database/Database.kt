package com.example.wificheck.model.database

import android.provider.BaseColumns
import android.provider.BaseColumns._ID


class Database() {

    class Location : BaseColumns {
        companion object {
            val LOC = "Location"
            val LOC_NAME = "name"
            val LOC_RAD = "radius"
            val LOC_LONG = "longitude"
            val LOC_LAT = "latitude"

            val CREATE_TABLE = "CREATE TABLE IF NOT EXISTS " +
                    LOC + " (" +
                    _ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    LOC_NAME + " TEXT, " +
                    LOC_LONG + " DOUBLE, " +
                    LOC_RAD + " DOUBLE, " +
                    LOC_LAT + " DOUBLE" + ")"
        }
    }
}
