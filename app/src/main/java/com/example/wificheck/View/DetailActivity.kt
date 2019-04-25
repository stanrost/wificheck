package com.example.wificheck.View

import android.graphics.Color
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.example.wificheck.Model.Entity.Location
import com.example.wificheck.R
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.CircleOptions
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import kotlinx.android.synthetic.main.activity_detail.*

class DetailActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var mapsView :MapView
    private lateinit var location: Location
    private var locationMarker: Marker? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        location  = intent.getSerializableExtra("location") as Location


        var etName = et_name
        etName.setText(location.name)

        mapsView = mv_location
        mapsView.onCreate(savedInstanceState)
        mapsView.getMapAsync(this)
    }

    fun addCircle(){
        val circleOptions = CircleOptions()
            .center(locationMarker!!.getPosition())
            .strokeColor(Color.argb(50, 70, 70, 70))
            .fillColor(Color.argb(100, 150, 150, 150))
            .radius(location.radius)
        mMap!!.addCircle(circleOptions)
    }

    override fun onMapReady(googleMap: GoogleMap?) {
        mMap = googleMap!!

        val vianen = LatLng(location.latitude, location.longitude)
        locationMarker = mMap.addMarker(MarkerOptions().position(vianen))
        var zoom = 15f
        var cameraUpdate = CameraUpdateFactory.newLatLngZoom(vianen, zoom)
        mMap!!.animateCamera(cameraUpdate)

        addCircle()
        mapsView.onResume()


    }
}
