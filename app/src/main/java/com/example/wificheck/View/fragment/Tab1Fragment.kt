package com.example.wificheck.View.fragment

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.support.v4.content.LocalBroadcastManager
import android.support.v7.app.AppCompatActivity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.TextView
import com.example.wificheck.R

import com.example.wificheck.Presenter.Tab1PresenterImpl
import com.example.wificheck.View.DetailActivity
import com.example.wificheck.View.MainActivity
import com.google.android.gms.maps.model.LatLng
import kotlinx.android.synthetic.main.tab1_list_fragment.*
import kotlinx.android.synthetic.main.tab1_list_fragment.view.*


class Tab1Fragment : Fragment(), Tab1View {

    lateinit var listView: ListView
    private lateinit var tab1PresenterImpl: Tab1PresenterImpl
    lateinit var globalContext: Context
    lateinit var globalView: View
    lateinit var receiver: BroadcastReceiver
    lateinit var tvInside: TextView

    private var mLastLocation: android.location.Location? = null
    var onLocationChange = 0

    private val locationListener: LocationListener = object : LocationListener {
        override fun onLocationChanged(location: android.location.Location?) {
            mLastLocation = location
        }

        override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {}
        override fun onProviderEnabled(provider: String?) {}
        override fun onProviderDisabled(provider: String?) {}

    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.tab1_list_fragment, container, false)

        globalView = view
        globalContext = view.context
        listView = view.lv_locations

        tab1PresenterImpl = Tab1PresenterImpl(this, view.context)
        tab1PresenterImpl.getLocationNames()



        if (checkPermission()) {
            val locationManager = globalContext.getSystemService(AppCompatActivity.LOCATION_SERVICE) as LocationManager?
            locationManager?.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0f, locationListener)
            locationManager?.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0f, locationListener)
            mLastLocation = locationManager?.getLastKnownLocation(LocationManager.GPS_PROVIDER)
        }

        //tab1PresenterImpl.getDistancesList(mLastLocation!!.latitude, mLastLocation!!.longitude)

        tvInside = view.tv_inside

       receiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context?, intent: Intent?) {
                var description = intent!!.getSerializableExtra("DESCRIPTION" ) as String
                tvInside.setText(description)
            }
        }
        LocalBroadcastManager.getInstance(globalContext).registerReceiver(receiver, IntentFilter("EVENT_SNACKBAR"));

        return view
    }

    fun checkPermission(): Boolean {
        return (ContextCompat.checkSelfPermission(
            globalContext,
            android.Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED)
    }

    override fun onResume() {
        super.onResume()
        tab1PresenterImpl.getLocationNames()
        listView = globalView.lv_locations
        tvInside = globalView.tv_inside
        LocalBroadcastManager.getInstance(globalContext).registerReceiver(receiver, IntentFilter("EVENT_SNACKBAR"));
    }

    override fun setListView(locationNames: ArrayList<String>) {

        val arrayAdapter: ArrayAdapter<String> = ArrayAdapter<String>(
            context!!,
            android.R.layout.simple_list_item_1,
            locationNames
        )

        listView.setAdapter(arrayAdapter)
        listView.setOnItemClickListener { parent, view, position, id ->
            tab1PresenterImpl.goToDetailPage(position)
        }
    }

    override fun goToDetailPage(id: Int, context: Context) {
        val intent = Intent(context, DetailActivity::class.java)
        intent.putExtra("ID", id)
        startActivity(intent)
    }
}