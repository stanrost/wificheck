package com.example.wificheck.view

import android.app.AlertDialog
import android.app.PendingIntent
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity;
import android.widget.SeekBar
import android.widget.Toast
import com.example.wificheck.model.entity.Location
import com.example.wificheck.R
import com.example.wificheck.backgroundService.GeofenceTransitionsIntentService
import com.example.wificheck.presenter.UpdatePresenterImpl
import com.google.android.gms.location.Geofence
import com.google.android.gms.location.GeofencingClient
import com.google.android.gms.location.GeofencingRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.*
import kotlinx.android.synthetic.main.activity_update.*

class UpdateActivity : AppCompatActivity(), UpdateView, OnMapReadyCallback {
    override fun onMapReady(googleMap: GoogleMap?) {
        mMap = googleMap!!
        mMap.setOnMapClickListener {
            addMarker(it)
        }
        mMapsView.onResume()

        addMarker(LatLng(mLocation.latitude, mLocation.longitude))
        addCircle(mLocation.radius)
    }
    private lateinit var mMap: GoogleMap
    private lateinit var mMapsView: MapView
    private var mLat: Double? = null
    private var mLong: Double? = null
    private var mRadius: Double? = null
    private var mLocationMarker: Marker? = null

    private var mGeoFenceLimits: Circle? = null
    private lateinit var mGeofencingClient: GeofencingClient
    private var mGeofence :Geofence? = null

    lateinit var mLocation :Location
    private lateinit var mUpdatePresenter: UpdatePresenterImpl



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_update)

        mUpdatePresenter = UpdatePresenterImpl(this, applicationContext)
        mMapsView = findViewById<MapView>(R.id.mv_location)
        mMapsView.onCreate(savedInstanceState)
        mMapsView.getMapAsync(this)

        mLocation = intent.getSerializableExtra("mLocation") as Location
        et_name.setText(mLocation.name)
        mLong = mLocation.longitude
        mLat = mLocation.longitude
        mRadius = mLocation.radius


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            sb_radius.setProgress(mLocation.radius.toInt(), false)
        }


        sb_radius.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar, i: Int, b: Boolean) {
                mRadius = i * 1.0
                mUpdatePresenter.addCircle(i)
            }
            override fun onStartTrackingTouch(seekBar: SeekBar) {
            }
            override fun onStopTrackingTouch(seekBar: SeekBar) {
            }
        })
        mGeofencingClient = LocationServices.getGeofencingClient(this)

        fab.setOnClickListener { view ->
            mUpdatePresenter.updateLocation(mLocation.id, et_name.text.toString(), mLong, mLat, mRadius)
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

    override fun closeActivity() {
        finish()
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

        val zoom = 16f
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

    override fun addGeofence(){

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
        val builder = AlertDialog.Builder(this@UpdateActivity)

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

    override fun showError(error: String){
        val builder = AlertDialog.Builder(this@UpdateActivity)
        builder.setTitle("Fill in everything")
        builder.setMessage(error)
        builder.setPositiveButton(getString(R.string.ok)) { dialog, which ->
            dialog.cancel()
        }
        val dialog: AlertDialog = builder.create()
        dialog.show()
    }

}
