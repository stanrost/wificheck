package com.example.wificheck.View

import android.graphics.Color
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.example.wificheck.Presenter.DetailPresenterImpl
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
    private var locationId: Int? = null
    private var locationRadius:Double? = null
    private var locationLatLng:LatLng? = null


    private var locationMarker: Marker? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        locationId  = intent.getSerializableExtra("ID") as Int

        DetailPresenterImpl(this).getLocationById(locationId!!)

        mapsView = mv_location
        mapsView.onCreate(savedInstanceState)
        mapsView.getMapAsync(this)
    }

    fun addCircle(){
        val circleOptions = CircleOptions()
            .center(locationMarker!!.getPosition())
            .strokeColor(Color.argb(50, 70, 70, 70))
            .fillColor(Color.argb(100, 150, 150, 150))
            .radius(locationRadius!!)
        mMap!!.addCircle(circleOptions)
    }

    override fun onMapReady(googleMap: GoogleMap?) {
        mMap = googleMap!!

        locationMarker = mMap.addMarker(MarkerOptions().position(locationLatLng!!))
        var zoom = 15f
        var cameraUpdate = CameraUpdateFactory.newLatLngZoom(locationLatLng!!, zoom)
        mMap!!.animateCamera(cameraUpdate)

        addCircle()
        mapsView.onResume()


    }

    fun showInformation(name: String, radius: Double, pair: Pair<Double, Double>) {
        var (lat, long) = pair

        et_name.setText(name)
        locationRadius = radius
        locationLatLng = LatLng(lat, long)

    }
}
