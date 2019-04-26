package com.example.wificheck.View.fragment

import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Color
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.support.v4.content.ContextCompat.getSystemService
import android.support.v7.app.AppCompatActivity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.wificheck.Model.Entity.Location
import com.example.wificheck.Presenter.Tab2PresenterImpl
import com.example.wificheck.R
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.*
import kotlinx.android.synthetic.main.tab2_maps_fragment.view.*

class Tab2Fragment : Fragment(), OnMapReadyCallback, Tab2View {


    private lateinit var mMap: GoogleMap
    private lateinit var mapsView: MapView
    private lateinit var tab2PresenterImpl: Tab2PresenterImpl
    lateinit var globalContext: Context
    lateinit var globalView: View
    lateinit var locationMarker: Marker


    private var currentLocationMarker: Marker? = null
    private var locationManager: LocationManager? = null
    private var mLastLocation: android.location.Location? = null
    var setCurrentMarker = false

    private val locationListener: LocationListener = object : LocationListener {
        override fun onLocationChanged(location: android.location.Location?) {
            mLastLocation = location
            if (!setCurrentMarker) {
                setCurrentMarker = true
                setCurrentLocationMarker(LatLng(location!!.latitude, location!!.longitude))
            }
        }

        override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {}
        override fun onProviderEnabled(provider: String?) {}
        override fun onProviderDisabled(provider: String?) {}

    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.tab2_maps_fragment, container, false)

        globalView = view
        globalContext = view.context

        mapsView = view.mapView
        mapsView.onCreate(savedInstanceState)
        mapsView.getMapAsync(this)

        return view
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        tab2PresenterImpl = Tab2PresenterImpl(this, globalContext)
        tab2PresenterImpl.getLocationsLatLng()

        mapsView.onResume()

        if (checkPermission()) {
            locationManager = globalContext.getSystemService(AppCompatActivity.LOCATION_SERVICE) as LocationManager?
            locationManager?.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0f, locationListener)
            locationManager?.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0f, locationListener)
            val loc : android.location.Location? = locationManager?.getLastKnownLocation(LocationManager.GPS_PROVIDER)
            setCurrentLocationMarker(LatLng(loc!!.latitude, loc!!.longitude))

        }
    }

    fun checkPermission(): Boolean {
        return (ContextCompat.checkSelfPermission(
            globalContext,
            android.Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED)
    }

    override fun onResume() {
        super.onResume()
        mapsView.onResume()
        mapsView.getMapAsync(this)
    }


    override fun setMarkers(locations: ArrayList<Location>) {

        val latlngBuilder = LatLngBounds.Builder()

        for (location in locations) {
            val latlong = LatLng(location.latitude, location.longitude)
            latlngBuilder.include(latlong)

            locationMarker = mMap.addMarker(MarkerOptions().position(latlong))
            addCircle(location.radius)
        }

        val latlngBound = latlngBuilder.build()
        val cameraUpdate = CameraUpdateFactory.newLatLngBounds(latlngBound, 220)
        mMap.animateCamera(cameraUpdate)

    }

    override fun setMarker(location: Location) {
        val latlong = LatLng(location.latitude, location.longitude)
        locationMarker = mMap.addMarker(MarkerOptions().position(latlong))
        addCircle(location.radius)
        val zoom = 18f
        val cameraUpdate = CameraUpdateFactory.newLatLngZoom(latlong, zoom)
        mMap.animateCamera(cameraUpdate)
    }

    override fun noMarker() {
        val markerOptions = MarkerOptions()
        val latLng = LatLng(52.005, 5.097)
        markerOptions.position(latLng)
        markerOptions.title("" + latLng.latitude + " : " + latLng.longitude)

        val zoom = 14f
        val cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, zoom)
        mMap.animateCamera(cameraUpdate)
    }

    fun addCircle(radius: Double) {
        val circleOptions = CircleOptions()
            .center(locationMarker.getPosition())
            .strokeColor(Color.argb(50, 70, 70, 70))
            .fillColor(Color.argb(100, 150, 150, 150))
            .radius(radius)
        mMap.addCircle(circleOptions)

    }

    fun setCurrentLocationMarker(latLng: LatLng) {

        val markerOptions = MarkerOptions()

        markerOptions.position(latLng)
        markerOptions.title("" + latLng.latitude + " : " + latLng.longitude)
            .icon(
                BitmapDescriptorFactory.defaultMarker(
                    BitmapDescriptorFactory.HUE_ORANGE
                )
            )

        if (currentLocationMarker != null) {
            currentLocationMarker!!.remove()
        }

        currentLocationMarker = mMap.addMarker(markerOptions)
        val zoom = 18f
        val cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, zoom)
        mMap.animateCamera(cameraUpdate)

    }
}

