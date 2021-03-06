package com.example.wificheck.view.fragment

import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Color
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.wificheck.R
import com.example.wificheck.model.entity.Location
import com.example.wificheck.presenter.fragment.Tab2Presenter
import com.example.wificheck.presenter.fragment.Tab2PresenterImpl
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.*
import kotlinx.android.synthetic.main.tab2_maps_fragment.view.*

class Tab2Fragment : Fragment(), OnMapReadyCallback, Tab2View {

    companion object {
        private const val ZOOM_14 = 14f
        private const val ZOOM_18 = 18f
        private const val BOUND = 220
        private const val INTERVAL:Long = 0
        private const val DISTANCE = 0f
    }

    var mMap: GoogleMap? = null
    var mMapsView: MapView? = null
    private lateinit var mTab2PresenterImpl: Tab2Presenter
    lateinit var mContext: Context
    lateinit var mView: View
    lateinit var mLocationMarker: Marker
    var cameraUpdate : CameraUpdate? = null

    private var mCurrentLocationMarker: Marker? = null
    private var mLocationManager: LocationManager? = null
    private var mLastLocation: android.location.Location? = null

    private val mLocationListener: LocationListener = object : LocationListener {
        override fun onLocationChanged(location: android.location.Location?) {
            mLastLocation = location
            setCurrentLocationMarker(LatLng(location!!.latitude, location.longitude))
        }
        override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {}
        override fun onProviderEnabled(provider: String?) {}
        override fun onProviderDisabled(provider: String?) {}
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.tab2_maps_fragment, container, false)
        mView = view
        mContext = view.context
        mMapsView = view.mapView

        mMapsView!!.onCreate(savedInstanceState)
        mMapsView!!.getMapAsync(this)

        return view
    }

    override fun onResume() {
        super.onResume()
        if (mMapsView != null) {
            mMapsView!!.onResume()
            mMapsView!!.getMapAsync(this)
        }
        if (mMap != null) {
            mMap!!.clear()
        }
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        mTab2PresenterImpl = Tab2PresenterImpl(this, mContext)
        mMapsView!!.onResume()
        if (checkPermission()) {
            mLocationManager = mContext.getSystemService(AppCompatActivity.LOCATION_SERVICE) as LocationManager?
            mLocationManager?.requestLocationUpdates(LocationManager.GPS_PROVIDER, INTERVAL, DISTANCE, mLocationListener)
            mLocationManager?.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, INTERVAL, DISTANCE, mLocationListener)
            mLastLocation = mLocationManager?.getLastKnownLocation(LocationManager.GPS_PROVIDER)
            if (mLastLocation == null){
                mLastLocation = mLocationManager?.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)
            }
            if (mLastLocation != null) {
                setCurrentLocationMarker(LatLng(mLastLocation!!.latitude, mLastLocation!!.longitude))
            }
        }
        mTab2PresenterImpl.getLocationsLatLng()
    }

    override fun setMarkers(locations: ArrayList<Location>) {
        mMap!!.clear()
        val latlngBuilder = LatLngBounds.Builder()
        for (location in locations) {
            val latlong = LatLng(location.latitude, location.longitude)
            latlngBuilder.include(latlong)
            mLocationMarker = mMap!!.addMarker(MarkerOptions().position(latlong))
            addCircle(location.radius)
        }
        val latlngBound = latlngBuilder.build()
        cameraUpdate = CameraUpdateFactory.newLatLngBounds(latlngBound, BOUND)

        if (mMap != null) {
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                mMap!!.animateCamera(cameraUpdate)
            }
        }
    }

    override fun setMarker(location: Location) {
        val latlong = LatLng(location.latitude, location.longitude)
        mLocationMarker = mMap!!.addMarker(MarkerOptions().position(latlong))
        addCircle(location.radius)
        val cameraUpdate = CameraUpdateFactory.newLatLngZoom(latlong, ZOOM_18)
        mMap!!.animateCamera(cameraUpdate)
    }

    override fun noMarker() {
        val markerOptions = MarkerOptions()
        if (mLastLocation != null) {
            val latLng = LatLng(mLastLocation!!.latitude, mLastLocation!!.longitude)
            markerOptions.position(latLng)
            markerOptions.title("" + latLng.latitude + " : " + latLng.longitude)
            val cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, ZOOM_14)
            mMap!!.animateCamera(cameraUpdate)
        }
    }

    private fun checkPermission(): Boolean {
        return (ContextCompat.checkSelfPermission(
            mContext,
            android.Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED)
    }

    private fun addCircle(radius: Double) {
        val circleOptions = CircleOptions()
            .center(mLocationMarker.getPosition())
            .strokeColor(Color.argb(50, 70, 70, 70))
            .fillColor(Color.argb(100, 150, 150, 150))
            .radius(radius)
        mMap!!.addCircle(circleOptions)
    }

    private fun setCurrentLocationMarker(latLng: LatLng) {
        val markerOptions = MarkerOptions()
        markerOptions.position(latLng)
        markerOptions.title("" + latLng.latitude + " : " + latLng.longitude)
            .icon(
                BitmapDescriptorFactory.defaultMarker(
                    BitmapDescriptorFactory.HUE_ORANGE
                )
            )
        if (mCurrentLocationMarker != null) {
            mCurrentLocationMarker!!.remove()
        }
        mCurrentLocationMarker = mMap!!.addMarker(markerOptions)
    }
}