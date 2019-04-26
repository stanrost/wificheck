package com.example.wificheck.View.fragment

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.support.v4.app.Fragment
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

class Tab2Fragment  : Fragment() ,OnMapReadyCallback, Tab2View {



    private lateinit var mMap: GoogleMap
    private lateinit var mapsView :MapView
    private lateinit var tab2PresenterImpl: Tab2PresenterImpl
    lateinit var globalContext: Context
    lateinit var globalView : View
    lateinit var locationMarker : Marker

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
        tab2PresenterImpl. getLocationsLatLng()

        mapsView.onResume()
    }

    override fun onResume() {
        super.onResume()
        mapsView.onResume()
        mapsView.getMapAsync(this)
    }



    override fun setMarkers(locations: ArrayList<Location>) {

        var latlngBuilder = LatLngBounds.Builder()

        for(location in locations){
            var latlong = LatLng(location.latitude, location.longitude)
            latlngBuilder.include(latlong)

            locationMarker = mMap.addMarker(MarkerOptions().position(latlong))
            addCircle(location.radius)
        }

        val latlngBound = latlngBuilder.build()
        var cameraUpdate = CameraUpdateFactory.newLatLngBounds(latlngBound, 220)
        mMap.animateCamera(cameraUpdate)

    }

    fun addCircle(radius:Double){
        val circleOptions = CircleOptions()
            .center(locationMarker!!.getPosition())
            .strokeColor(Color.argb(50, 70, 70, 70))
            .fillColor(Color.argb(100, 150, 150, 150))
            .radius(radius)
        mMap!!.addCircle(circleOptions)

    }
}