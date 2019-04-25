package com.example.wificheck.View.fragment

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.wificheck.R
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions

class Tab2Fragment  : Fragment() ,OnMapReadyCallback, Tab2View {


    private lateinit var mMap: GoogleMap
    private lateinit var mapsView :MapView
    private var locationMarker: Marker? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.tab2_maps_fragment, container, false)

        mapsView = view.findViewById<MapView>(R.id.mapView)

        mapsView.onCreate(savedInstanceState)
        mapsView.getMapAsync(this)


        return view
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        // Add a marker in Sydney and move the camera
        val sydney = LatLng(51.985, 5.106)
        mMap.addMarker(MarkerOptions().position(sydney))
        var zoom = 14f
        var cameraUpdate = CameraUpdateFactory.newLatLngZoom(sydney, zoom)
        mMap!!.animateCamera(cameraUpdate)
        mapsView.onResume()
    }

}