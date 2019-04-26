package com.example.wificheck.View

import android.graphics.Color
import android.os.Bundle
import android.support.v7.app.AppCompatActivity;
import android.widget.SeekBar
import com.example.wificheck.Model.Entity.Location
import com.example.wificheck.Presenter.AddPresenterImpl
import com.example.wificheck.R
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.*

import kotlinx.android.synthetic.main.activity_add.*

class AddActivity : AppCompatActivity(), AddView, OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var mapsView :MapView
    var lat : Double? = null
    var long : Double? = null
    private var locationMarker: Marker? = null
    private var geoFenceLimits: Circle? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add)


        mapsView = findViewById<MapView>(R.id.mv_location)
        mapsView.onCreate(savedInstanceState)
        mapsView.getMapAsync(this)

        var radius:Double = 0.0

        sb_radius.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {

            override fun onProgressChanged(seekBar: SeekBar, i: Int, b: Boolean) {
                radius = i * 1.0
                addCircle(radius)
            }

            override fun onStartTrackingTouch(seekBar: SeekBar) {

            }

            override fun onStopTrackingTouch(seekBar: SeekBar) {

            }
        })

        var etName = et_name

        fab.setOnClickListener { view ->
            if (long != null && lat != null) {
                closeActivity()
                var location = Location(etName.text.toString(), long!!, lat!!, radius)
                saveLocation(location)
            }
        }
    }

    fun addCircle(radius:Double){

        if(geoFenceLimits != null){
            geoFenceLimits!!.remove()
        }
        val circleOptions = CircleOptions()
            .center(locationMarker!!.getPosition())
            .strokeColor(Color.argb(50, 70, 70, 70))
            .fillColor(Color.argb(100, 150, 150, 150))
            .radius(radius)
        geoFenceLimits = mMap!!.addCircle(circleOptions)

    }

    fun closeActivity(){
        finish()
    }

    fun saveLocation(location: Location){
        AddPresenterImpl().addLocation(this, location)
    }

    fun addMarker(latLng: LatLng){
        long = latLng.longitude
        lat = latLng.latitude

        val markerOptions = MarkerOptions()

        markerOptions.position(latLng)
        markerOptions
            .title("" + latLng.latitude + " : " + latLng.longitude)
            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE))

        if (locationMarker != null) {
            locationMarker!!.remove()
        }
        var zoom = 18f
        var cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, zoom)
        mMap!!.animateCamera(cameraUpdate)
        locationMarker = mMap!!.addMarker(markerOptions)

    }

    override fun onMapReady(googleMap: GoogleMap?) {
        mMap = googleMap!!
        mMap.setOnMapClickListener {
            addMarker(it)
        }

        val vianen = LatLng(51.985, 5.106)
        var zoom = 14f
        var cameraUpdate = CameraUpdateFactory.newLatLngZoom(vianen, zoom)
        mMap!!.animateCamera(cameraUpdate)
        mapsView.onResume()
    }

}
