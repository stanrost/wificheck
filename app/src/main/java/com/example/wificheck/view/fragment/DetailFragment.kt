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
    private var mLocationId: Int? = null
    private var mLocationRadius: Double? = null
    private var mLocationLatLng: LatLng? = null


    private var mLocationMarker: Marker? = null
    private var mCurrentLocationMarker: Marker? = null
    private var mLocationManager: LocationManager? = null
    private var mLastLocation: android.location.Location? = null
    var setCurrentMarker = false
    lateinit var mContext: Context
    lateinit var mView: View

    companion object {
        const val ARG_ITEM_ID = "item_id"
    }

    private val locationListener: LocationListener = object : LocationListener {

        override fun onLocationChanged(location: android.location.Location?) {
            mLastLocation = location
            setCurrentLocationMarker(LatLng(location!!.latitude, location!!.longitude))

        }

        override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {}
        override fun onProviderEnabled(provider: String?) {}
        override fun onProviderDisabled(provider: String?) {}

    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        var view = inflater.inflate(R.layout.fragment_detail, container, false)
        mView = view
        mContext = view.context
        var getId: Int? = null
        arguments?.let {
            if (it.containsKey(DetailFragment.ARG_ITEM_ID)) {
                getId = it.getInt(DetailFragment.ARG_ITEM_ID)
            }
        }

        mMapsView = view.mv_location_fragment

        DetailPresenterFragmentImpl(this, mContext).getLocationById(getId!!)
        mMapsView.onCreate(savedInstanceState)
        mMapsView.getMapAsync(this)

        // Inflate the layout for this fragment
        return view
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
            mContext,
            android.Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED)
    }

    override fun onMapReady(googleMap: GoogleMap?) {
        mMap = googleMap!!
        mLocationMarker = mMap.addMarker(MarkerOptions().position(mLocationLatLng!!))
        val zoom = 15f
        val cameraUpdate = CameraUpdateFactory.newLatLngZoom(mLocationLatLng!!, zoom)

        addCircle()

        mMap.moveCamera(cameraUpdate)
        mMapsView.onResume()

        if (checkPermission()) {
            mLocationManager = view!!.context.getSystemService(AppCompatActivity.LOCATION_SERVICE) as LocationManager?
            mLocationManager?.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0f, locationListener)
            mLocationManager?.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0f, locationListener)
            val loc: Location? = mLocationManager?.getLastKnownLocation(LocationManager.GPS_PROVIDER)
            if (loc != null) {
                setCurrentLocationMarker(LatLng(loc!!.latitude, loc.longitude))
            }
        }
    }

    override fun showInformation(name: String, radius: Double, pair: Pair<Double, Double>) {
        val (lat, long) = pair
        mLocationRadius = radius
        mLocationLatLng = LatLng(lat, long)

        mView.et_name_fragment.setText(name)

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
