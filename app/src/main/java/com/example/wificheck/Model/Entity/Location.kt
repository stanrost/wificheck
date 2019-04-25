package com.example.wificheck.Model.Entity

class Location{

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

    var radius: Int = 0
        get() = field
        set(value) {
            field = value
        }

    constructor(name:String, long:Double, lat:Double, radius: Int) {
        this.name = name
        longitude = long
        latitude = lat
        this.radius = radius
    }

    constructor(id:Int, name:String, long:Double, lat:Double, radius: Int) {
        this.id = id
        this.name = name
        longitude = long
        latitude = lat
        this.radius = radius
    }
}
