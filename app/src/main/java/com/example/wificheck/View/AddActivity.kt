package com.example.wificheck.View

import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity;
import com.example.wificheck.Model.Entity.Location
import com.example.wificheck.Presenter.AddPresenter
import com.example.wificheck.Presenter.AddPresenterImpl
import com.example.wificheck.R
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions

import kotlinx.android.synthetic.main.activity_add.*

class AddActivity : AppCompatActivity(), AddView, OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var mapsView :MapView
    var lat : Double? = null
    var long : Double? = null
    private var locationMarker: Marker? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add)


        mapsView = findViewById<MapView>(R.id.mv_location)
        mapsView.onCreate(savedInstanceState)
        mapsView.getMapAsync(this)

        var etName = et_name

        fab.setOnClickListener { view ->
            if (long != null && lat != null) {
                closeActivity()
                var location = Location(etName.text.toString(), long!!, lat!!, 50)
                saveLocation(location)
            }
        }
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

        locationMarker = mMap!!.addMarker(markerOptions)

    }

    override fun onMapReady(googleMap: GoogleMap?) {
        mMap = googleMap!!
        mMap.setOnMapClickListener {
            addMarker(it)
        }

        // Add a marker in Sydney and move the camera
        val vianen = LatLng(51.985, 5.106)
        var zoom = 14f
        var cameraUpdate = CameraUpdateFactory.newLatLngZoom(vianen, zoom)
        mMap!!.animateCamera(cameraUpdate)
        mapsView.onResume()
    }

}
