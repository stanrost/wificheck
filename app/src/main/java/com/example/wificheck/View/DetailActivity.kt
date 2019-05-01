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
    private lateinit var mMapsView: MapView
    private var mLocationId: Int? = null
    private var mLocationRadius: Double? = null
    private var mLocationLatLng: LatLng? = null


    private var mLocationMarker: Marker? = null
    private var mCurrentLocationMarker: Marker? = null
    private var mLocationManager: LocationManager? = null
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

        mLocationId = intent.getSerializableExtra("ID") as Int
        mMapsView = mv_location

        DetailPresenterImpl(this).getLocationById(mLocationId!!)
        mMapsView.onCreate(savedInstanceState)
        mMapsView.getMapAsync(this)

    }

    fun addCircle() {
        val circleOptions = CircleOptions()
            .center(mLocationMarker!!.getPosition())
            .strokeColor(Color.argb(50, 70, 70, 70))
            .fillColor(Color.argb(100, 150, 150, 150))
            .radius(mLocationRadius!!)
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
        mLocationMarker = mMap.addMarker(MarkerOptions().position(mLocationLatLng!!))
        val zoom = 15f
        val cameraUpdate = CameraUpdateFactory.newLatLngZoom(mLocationLatLng!!, zoom)

        addCircle()

        mMap.animateCamera(cameraUpdate)
        mMapsView.onResume()

        if (checkPermission()) {
            mLocationManager = getSystemService(LOCATION_SERVICE) as LocationManager?
            mLocationManager?.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 10f, locationListener)
            mLocationManager?.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 1000, 10f, locationListener)
            val loc :Location? = mLocationManager?.getLastKnownLocation(LocationManager.GPS_PROVIDER)
            setCurrentLocationMarker(LatLng(loc!!.latitude, loc.longitude))
        }
    }

    override fun showInformation(name: String, radius: Double, pair: Pair<Double, Double>) {
        val (lat, long) = pair
        mLocationRadius = radius
        mLocationLatLng = LatLng(lat, long)

        et_name.setText(name)

    }

    fun setCurrentLocationMarker(latLng: LatLng) {

        val markerOptions = MarkerOptions()

        markerOptions.position(latLng)
        markerOptions.title(getString(R.string.current_location))
            .icon(
                BitmapDescriptorFactory.defaultMarker(
                    BitmapDescriptorFactory.HUE_ORANGE
                )
            )

        if (mCurrentLocationMarker != null) {
            mCurrentLocationMarker!!.remove()
        }
        mCurrentLocationMarker = mMap.addMarker(markerOptions)

    }
}
