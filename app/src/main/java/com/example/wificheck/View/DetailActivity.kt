package com.example.wificheck.View

import android.content.pm.PackageManager
import android.graphics.Color
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.content.ContextCompat
import com.example.wificheck.Presenter.DetailPresenterImpl
import com.example.wificheck.R
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.*
import kotlinx.android.synthetic.main.activity_detail.*

class DetailActivity : AppCompatActivity(), OnMapReadyCallback, DetailView {

    private lateinit var mMap: GoogleMap
    private lateinit var mapsView: MapView
    private var locationId: Int? = null
    private var locationRadius: Double? = null
    private var locationLatLng: LatLng? = null


    private var locationMarker: Marker? = null
    private var currentLocationMarker: Marker? = null
    private var locationManager: LocationManager? = null
    private var mLastLocation: android.location.Location? = null
    var setCurrentMarker = false

    private val locationListener: LocationListener = object : LocationListener {

        override fun onLocationChanged(location: android.location.Location?) {
            mLastLocation = location
            if(!setCurrentMarker) {
                setCurrentMarker = true
                setCurrentLocationMarker(LatLng(location!!.latitude, location!!.longitude))
            }

        }

        override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {}
        override fun onProviderEnabled(provider: String?) {}
        override fun onProviderDisabled(provider: String?) {}

    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        locationId = intent.getSerializableExtra("ID") as Int

        DetailPresenterImpl(this).getLocationById(locationId!!)

        mapsView = mv_location
        mapsView.onCreate(savedInstanceState)
        mapsView.getMapAsync(this)

    }

    fun addCircle() {
        val circleOptions = CircleOptions()
            .center(locationMarker!!.getPosition())
            .strokeColor(Color.argb(50, 70, 70, 70))
            .fillColor(Color.argb(100, 150, 150, 150))
            .radius(locationRadius!!)
        mMap.addCircle(circleOptions)
    }

    fun checkPermission(): Boolean {
        return (ContextCompat.checkSelfPermission(
            this,
            android.Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED)
    }

    override fun onMapReady(googleMap: GoogleMap?) {
        mMap = googleMap!!

        locationMarker = mMap.addMarker(MarkerOptions().position(locationLatLng!!))
        val zoom = 15f
        val cameraUpdate = CameraUpdateFactory.newLatLngZoom(locationLatLng!!, zoom)
        mMap.animateCamera(cameraUpdate)

        addCircle()
        mapsView.onResume()

        if (checkPermission()) {
            locationManager = getSystemService(LOCATION_SERVICE) as LocationManager?
            locationManager?.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0f, locationListener)
            locationManager?.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0f, locationListener)
            val loc :Location? = locationManager?.getLastKnownLocation(LocationManager.GPS_PROVIDER)
            setCurrentLocationMarker(LatLng(loc!!.latitude, loc!!.longitude))
        }
    }

    override fun showInformation(name: String, radius: Double, pair: Pair<Double, Double>) {
        val (lat, long) = pair

        et_name.setText(name)
        locationRadius = radius
        locationLatLng = LatLng(lat, long)
    }

    fun setCurrentLocationMarker(latLng: LatLng) {

        val markerOptions = MarkerOptions()

        markerOptions.position(latLng)
        markerOptions.title("Current location")
            .icon(
                BitmapDescriptorFactory.defaultMarker(
                    BitmapDescriptorFactory.HUE_ORANGE
                )
            )

        if (currentLocationMarker != null) {
            currentLocationMarker!!.remove()
        }

        currentLocationMarker = mMap.addMarker(markerOptions)

    }
}
