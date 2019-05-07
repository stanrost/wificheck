package com.example.wificheck.view.fragment

import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Color
import android.location.Location
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
import com.example.wificheck.presenter.DetailPresenterFragmentImpl
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.*
import kotlinx.android.synthetic.main.fragment_detail.view.*

class DetailFragment : Fragment(), OnMapReadyCallback, DetailFragmentView {

    private lateinit var mMap: GoogleMap
    private lateinit var mMapsView: MapView
    private var mLocationRadius: Double? = null
    private var mLocationLatLng: LatLng? = null


    private var mLocationMarker: Marker? = null
    private var mCurrentLocationMarker: Marker? = null
    private var mLocationManager: LocationManager? = null
    private var mLastLocation: Location? = null
    lateinit var mContext: Context
    lateinit var mView: View

    companion object {
        const val ITEM_ID = "item_id"
        private const val ZOOM = 15f
        private const val INTERVAL:Long = 0
        private const val DISTANCE = 0f
    }

    private val locationListener: LocationListener = object : LocationListener {

        override fun onLocationChanged(location: Location?) {
            mLastLocation = location
            setCurrentLocationMarker(LatLng(location!!.latitude, location.longitude))
        }
        override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {}
        override fun onProviderEnabled(provider: String?) {}
        override fun onProviderDisabled(provider: String?) {}
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_detail, container, false)
        mView = view
        mContext = view.context
        mMapsView = view.mv_location_fragment

        var getId: Int? = null
        arguments?.let {
            if (it.containsKey(ITEM_ID)) {
                getId = it.getInt(ITEM_ID)
            }
        }

        DetailPresenterFragmentImpl(this, mContext).getLocationById(getId!!)
        mMapsView.onCreate(savedInstanceState)
        mMapsView.getMapAsync(this)

        return view
    }

    override fun onMapReady(googleMap: GoogleMap?) {
        mMap = googleMap!!
        mLocationMarker = mMap.addMarker(MarkerOptions().position(mLocationLatLng!!))
        val cameraUpdate = CameraUpdateFactory.newLatLngZoom(mLocationLatLng!!, ZOOM)

        addCircle()
        mMap.moveCamera(cameraUpdate)
        mMapsView.onResume()

        if (checkPermission()) {
            mLocationManager = view!!.context.getSystemService(AppCompatActivity.LOCATION_SERVICE) as LocationManager?
            mLocationManager?.requestLocationUpdates(LocationManager.GPS_PROVIDER, INTERVAL, DISTANCE, locationListener)
            mLocationManager?.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, INTERVAL, DISTANCE, locationListener)
            val loc: Location? = mLocationManager?.getLastKnownLocation(LocationManager.GPS_PROVIDER)
            if (loc != null) {
                setCurrentLocationMarker(LatLng(loc.latitude, loc.longitude))
            }
        }
    }

    override fun showInformation(name: String, radius: Double, pair: Pair<Double, Double>) {
        val (lat, long) = pair
        mLocationRadius = radius
        mLocationLatLng = LatLng(lat, long)
        mView.et_name_fragment.setText(name)
    }

    private fun addCircle() {
        val circleOptions = CircleOptions()
            .center(mLocationMarker!!.getPosition())
            .strokeColor(Color.argb(50, 70, 70, 70))
            .fillColor(Color.argb(100, 150, 150, 150))
            .radius(mLocationRadius!!)
        mMap.addCircle(circleOptions)
    }

    private fun checkPermission(): Boolean {
        return (ContextCompat.checkSelfPermission(
            mContext,
            android.Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED)
    }

    private fun setCurrentLocationMarker(latLng: LatLng) {

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
