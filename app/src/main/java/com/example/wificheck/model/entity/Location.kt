package com.example.wificheck.model.entity

import java.io.Serializable

class Location : Serializable{

    var id: Int = 0
        get() = field
        set(value) {
            field = value
        }

    var name: String = ""
        get() = field
        set(value) {
            field = value
        }

    var longitude: Double = 0.0
        get() = field
        set(value) {
            field = value
        }

    var latitude: Double = 0.0
        get() = field
        set(value) {
            field = value
        }

    var radius: Double = 0.0
        get() = field
        set(value) {
            field = value
        }

    constructor(name:String, long:Double, lat:Double, radius: Double) {
        this.name = name
        longitude = long
        latitude = lat
        this.radius = radius
    }

    constructor(id:Int, name:String, long:Double, lat:Double, radius: Double) {
        this.id = id
        this.name = name
        longitude = long
        latitude = lat
        this.radius = radius
    }
}
