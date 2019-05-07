package com.example.wificheck.view

import android.app.AlertDialog
import android.app.PendingIntent
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.provider.Settings
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.widget.SeekBar
import android.widget.Toast
import com.example.wificheck.R
import com.example.wificheck.backgroundService.GeofenceTransitionsIntentService
import com.example.wificheck.presenter.AddPresenterImpl
import com.google.android.gms.location.Geofence
import com.google.android.gms.location.GeofencingClient
import com.google.android.gms.location.GeofencingRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.*
import kotlinx.android.synthetic.main.activity_add.*

class AddActivity : AppCompatActivity(), AddView, OnMapReadyCallback {

    companion object {
        private const val ZOOM_16 = 16f
        private const val ZOOM_18 = 18f
        private const val INTERVAL:Long = 0
        private const val DISTANCE = 0f

    }
    private lateinit var mMap: GoogleMap
    private lateinit var mMapsView: MapView
    private var mLat: Double? = null
    private var mLong: Double? = null
    private var mRadius: Double? = null
    private var mLocationMarker: Marker? = null

    private var mGeoFenceLimits: Circle? = null
    private var mSetMarker = false
    private var mUpdateView = true
    private lateinit var mGeofencingClient: GeofencingClient
    private var mGeofence: Geofence? = null

    private var mLocationManager: LocationManager? = null
    private var mLastLocation: android.location.Location? = null
    private lateinit var mAddPresenter: AddPresenterImpl

    private val mLocationListener: LocationListener = object : LocationListener {
        override fun onLocationChanged(location: android.location.Location?) {
            mLastLocation = location
            if(!mSetMarker) {
                setCurrentLocationMarker(LatLng(location!!.latitude, location.longitude))
                mUpdateView = false
            }
        }
        override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {}
        override fun onProviderEnabled(provider: String?) {}
        override fun onProviderDisabled(provider: String?) {}
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add)

        mAddPresenter = AddPresenterImpl(this, applicationContext)
        mMapsView = findViewById<MapView>(R.id.mv_location)
        mMapsView.onCreate(savedInstanceState)
        mMapsView.getMapAsync(this)

        sb_radius.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar, i: Int, b: Boolean) {
                mSetMarker = true
                mRadius = i * 1.0
                mAddPresenter.addCircle(i)
            }
            override fun onStartTrackingTouch(seekBar: SeekBar) {
            }
            override fun onStopTrackingTouch(seekBar: SeekBar) {
            }
        })
        val etName = et_name
        mGeofencingClient = LocationServices.getGeofencingClient(this)

        fab.setOnClickListener { view ->
            mAddPresenter.addLocation(etName.text.toString(), mLong, mLat, mRadius)
        }
    }

    override fun addCircle(radius: Double) {
        if (mGeoFenceLimits != null) {
            mGeoFenceLimits!!.remove()
        }
        val circleOptions = CircleOptions()
            .center(mLocationMarker!!.getPosition())
            .strokeColor(Color.argb(50, 70, 70, 70))
            .fillColor(Color.argb(100, 150, 150, 150))
            .radius(radius)
        mGeoFenceLimits = mMap.addCircle(circleOptions)
    }

    override fun showError(error: String) {
        val builder = AlertDialog.Builder(this@AddActivity)
        builder.setTitle(getString(R.string.fill_everything_in))
        builder.setMessage(error)
        builder.setPositiveButton(getString(R.string.ok)) { dialog, which ->
            dialog.cancel()
        }
        val dialog: AlertDialog = builder.create()
        dialog.show()
    }

    override fun closeActivity() {
        finish()
    }

    override fun onMapReady(googleMap: GoogleMap?) {
        mMap = googleMap!!
        mMap.setOnMapClickListener {
            addMarker(it)
        }
        mMapsView.onResume()
        if (checkPermission()) {
            mLocationManager = getSystemService(LOCATION_SERVICE) as LocationManager?
            mLocationManager?.requestLocationUpdates(LocationManager.GPS_PROVIDER, INTERVAL, DISTANCE, mLocationListener)
            mLocationManager?.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, INTERVAL, DISTANCE, mLocationListener)
            val loc: android.location.Location? = mLocationManager?.getLastKnownLocation(LocationManager.GPS_PROVIDER)
            if (loc != null) {
                setCurrentLocationMarker(LatLng(loc.latitude, loc.longitude))
            }
        }
    }

    override fun addGeofence() {
        mGeofence = Geofence.Builder().setRequestId(et_name.text.toString())
            .setCircularRegion(mLat!!, mLong!!, mRadius!!.toFloat())
            .setExpirationDuration(Geofence.NEVER_EXPIRE)
            .setTransitionTypes(Geofence.GEOFENCE_TRANSITION_ENTER or Geofence.GEOFENCE_TRANSITION_EXIT).build()

        if (checkPermission()) {
            mGeofencingClient.addGeofences(getGeofencingRequest(), geofencePendingIntent)?.run {
                addOnSuccessListener {
                }
                addOnFailureListener {
                    showSettingsPopUp()
                }
            }
        }
    }

    private fun addMarker(latLng: LatLng) {
        mLong = latLng.longitude
        mLat = latLng.latitude
        mSetMarker = true

        val markerOptions = MarkerOptions()

        markerOptions.position(latLng)
        markerOptions
            .title("" + latLng.latitude + " : " + latLng.longitude)
            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE))

        if (mLocationMarker != null) {
            mLocationMarker!!.remove()
        }
        if (mGeoFenceLimits != null) {
            mGeoFenceLimits!!.remove()
        }

        val zoom = ZOOM_18
        val cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, zoom)
        mMap.animateCamera(cameraUpdate)
        mLocationMarker = mMap.addMarker(markerOptions)

    }

    private fun checkPermission(): Boolean {
        return (ContextCompat.checkSelfPermission(
            this,
            android.Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED)
    }

    private fun setCurrentLocationMarker(latLng: LatLng) {
        val markerOptions = MarkerOptions()
        mLong = latLng.longitude
        mLat = latLng.latitude

        markerOptions.position(latLng)
        markerOptions.title("" + latLng.latitude + " : " + latLng.longitude)

        if (mLocationMarker != null) {
            mLocationMarker!!.remove()
        }

        mLocationMarker = mMap.addMarker(markerOptions)
        val zoom = ZOOM_16

        if(mUpdateView) {
            val cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, zoom)
            mMap.animateCamera(cameraUpdate)
        }
    }

    private fun getGeofencingRequest(): GeofencingRequest {
        return GeofencingRequest.Builder().apply {
            setInitialTrigger(GeofencingRequest.INITIAL_TRIGGER_ENTER)
            addGeofence(mGeofence)
        }.build()
    }

    private val geofencePendingIntent: PendingIntent by lazy {
        val intent = Intent(this, GeofenceTransitionsIntentService::class.java)
        PendingIntent.getService(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)
    }

    private fun showSettingsPopUp() {
        val builder = AlertDialog.Builder(this@AddActivity)
        builder.setTitle(getString(R.string.change_settings))
        builder.setMessage(getString(R.string.hight_accuracy))
        builder.setPositiveButton(getString(R.string.ok)) { dialog, which ->
            goToSettings()
        }
        builder.setNegativeButton(getString(R.string.cancel)) { dialog, which ->
            Toast.makeText(applicationContext, getString(R.string.not_agree), Toast.LENGTH_SHORT).show()
        }
        val dialog: AlertDialog = builder.create()
        dialog.show()
    }

    private fun goToSettings() {
        val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
        startActivity(intent)
    }
}
