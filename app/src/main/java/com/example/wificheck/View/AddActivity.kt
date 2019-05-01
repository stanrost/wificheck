package com.example.wificheck.View

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
import android.support.v7.app.AppCompatActivity;
import android.widget.SeekBar
import android.widget.Toast
import com.example.wificheck.Model.Entity.Location
import com.example.wificheck.Presenter.AddPresenterImpl
import com.example.wificheck.R
import com.example.wificheck.backgroundService.GeofenceTransitionsIntentService
import com.example.wificheck.backgroundService.MyService
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

    private lateinit var mMap: GoogleMap
    private lateinit var mMapsView: MapView
    private var mLat: Double? = null
    private var mLong: Double? = null
    private var mRadius: Double? = null
    private var mLocationMarker: Marker? = null

    private var mGeoFenceLimits: Circle? = null
    private var mSetCurrentMarker = false
    private lateinit var mGeofencingClient: GeofencingClient
    private var mGeofence :Geofence? = null

    private var locationManager: LocationManager? = null
    private var mLastLocation: android.location.Location? = null

    private lateinit var mAddPresenter: AddPresenterImpl

    private val mLocationListener: LocationListener = object : LocationListener {
        override fun onLocationChanged(location: android.location.Location?) {
            mLastLocation = location
            if (!mSetCurrentMarker) {
                mSetCurrentMarker = true
                setCurrentLocationMarker(LatLng(location!!.latitude, location!!.longitude))
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
            if (mLong != null && mLat != null) {
                closeActivity()
                val location = Location(etName.text.toString(), mLong!!, mLat!!, mRadius!!)
                saveLocation(location)
                addGeofence()
            }
        }

        // stop myservice when app is on
        val serviceIntent = Intent(this, MyService::class.java)
        this.stopService(serviceIntent)
    }

    fun addCircle(radius: Double) {

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

    fun closeActivity() {
        finish()
    }

    fun saveLocation(location: Location) {
        mAddPresenter.addLocation(location)
    }

    fun addMarker(latLng: LatLng) {
        mLong = latLng.longitude
        mLat = latLng.latitude

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

        val zoom = 18f
        val cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, zoom)
        mMap.animateCamera(cameraUpdate)
        mLocationMarker = mMap.addMarker(markerOptions)

    }

    fun checkPermission(): Boolean {
        return (ContextCompat.checkSelfPermission(
            this,
            android.Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED)
    }

    fun setCurrentLocationMarker(latLng: LatLng) {

        val markerOptions = MarkerOptions()

        mLong = latLng.longitude
        mLat = latLng.latitude

        markerOptions.position(latLng)
        markerOptions.title("" + latLng.latitude + " : " + latLng.longitude)

        if (mLocationMarker != null) {
            mLocationMarker!!.remove()
        }

        mLocationMarker = mMap.addMarker(markerOptions)
        val zoom = 18f
        val cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, zoom)
        mMap.animateCamera(cameraUpdate)

    }

    override fun onMapReady(googleMap: GoogleMap?) {
        mMap = googleMap!!
        mMap.setOnMapClickListener {
            addMarker(it)
        }
        mMapsView.onResume()

        if (checkPermission()) {
            locationManager = getSystemService(LOCATION_SERVICE) as LocationManager?
            locationManager?.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000 * 60, 10f, mLocationListener)
            locationManager?.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 1000 * 60, 10f, mLocationListener)
            val loc: android.location.Location? = locationManager?.getLastKnownLocation(LocationManager.GPS_PROVIDER)
            setCurrentLocationMarker(LatLng(loc!!.latitude, loc.longitude))
        }
    }

    // mGeofence

    fun addGeofence(){

         mGeofence = Geofence.Builder().setRequestId(et_name.text.toString())
            .setCircularRegion(mLat!!, mLong!!, mRadius!!.toFloat())
            .setExpirationDuration(Geofence.NEVER_EXPIRE)
            .setTransitionTypes(Geofence.GEOFENCE_TRANSITION_ENTER or Geofence.GEOFENCE_TRANSITION_EXIT).build()

        if(checkPermission()) {
            mGeofencingClient.addGeofences(getGeofencingRequest(), geofencePendingIntent)?.run {
                addOnSuccessListener {
                    // ---------------- show popup
                }
                addOnFailureListener {
                    showSettingsPopUp()
                }

            }
        }
    }

    fun getGeofencingRequest(): GeofencingRequest {
        return GeofencingRequest.Builder().apply {
            setInitialTrigger(GeofencingRequest.INITIAL_TRIGGER_ENTER)
            addGeofence(mGeofence)
        }.build()
    }

    private val geofencePendingIntent: PendingIntent by lazy {
        val intent = Intent(this, GeofenceTransitionsIntentService::class.java)
        PendingIntent.getService(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)

    }

    fun showSettingsPopUp() {
        val builder = AlertDialog.Builder(this@AddActivity)

        // Set the alert dialog title
        builder.setTitle(getString(R.string.change_settings))
        // Display a message on alert dialog
        builder.setMessage(getString(R.string.hight_accuracy))
        // Set a positive button and its click listener on alert dialog
        builder.setPositiveButton(getString(R.string.ok)) { dialog, which ->
            // Do something when user press the positive button
            goToSettings()
            // Change the app background color
        }
        // Display a negative button on alert dialog
        builder.setNegativeButton(getString(R.string.cancel)) { dialog, which ->
            Toast.makeText(applicationContext, getString(R.string.not_agree), Toast.LENGTH_SHORT).show()
        }
        // Finally, make the alert dialog using mBuilder
        val dialog: AlertDialog = builder.create()
        // Display the alert dialog on app interface
        dialog.show()
    }

    fun goToSettings() {
        var intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
        startActivity(intent)
    }

}
