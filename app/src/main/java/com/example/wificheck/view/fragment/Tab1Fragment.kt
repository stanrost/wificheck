package com.example.wificheck.view.fragment

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
import android.view.*
import android.widget.ListView
import android.widget.TextView
import com.example.wificheck.R
import com.example.wificheck.model.entity.Location
import com.example.wificheck.presenter.fragment.Tab1PresenterImpl
import com.example.wificheck.view.DetailActivity
import com.example.wificheck.view.MainActivity
import com.example.wificheck.view.adapter.LocationAdapter
import com.google.android.gms.maps.model.LatLng
import kotlinx.android.synthetic.main.tab1_list_fragment.view.*

class Tab1Fragment() : Fragment(), Tab1View {
    override fun goToDetailFragment(id: Int, mContext: Context) {
        var act = activity as MainActivity
        act.setTabletView(id)
    }

    val DISCRIPTION = "DESCRIPTION"
    val SHOW_DISCRIPTION = "SHOW_DESCRIPTION"
    val ID = "ID"

    companion object {
        /**
         * The fragment argument representing the item ID that this fragment
         * represents.
         */
        const val ARG_ITEM_ID = "item_id"
    }


    lateinit var mListView: ListView
    private lateinit var mTab1PresenterImpl: Tab1PresenterImpl
    lateinit var mContext: Context
    lateinit var mView: View
    lateinit var mBroadcastReceiver: BroadcastReceiver
    lateinit var mTvInside: TextView
    private var mLastLocation: android.location.Location? = null
    lateinit var mDescription: String
    var tabletView: Boolean = false

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

        mView = view
        mContext = view.context
        mListView = view.lv_locations
        mTab1PresenterImpl = Tab1PresenterImpl(this, view.context)
        mTvInside = view.tv_inside
        mTvInside.text = mTab1PresenterImpl.getInsideLocation()
        mBroadcastReceiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context?, intent: Intent?) {
                val description = intent!!.getSerializableExtra(DISCRIPTION) as String
                mTab1PresenterImpl.setInsideLocation(description)
                mDescription = description
                mTvInside.text = description
            }
        }

        arguments?.let {
            if (it.containsKey(ARG_ITEM_ID)) {
                tabletView = it.getBoolean(ARG_ITEM_ID)
            }
        }

        if (checkPermission()) {
            val locationManager = mContext.getSystemService(AppCompatActivity.LOCATION_SERVICE) as LocationManager?
            locationManager?.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0f, locationListener)
            locationManager?.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0f, locationListener)
            mLastLocation = locationManager?.getLastKnownLocation(LocationManager.GPS_PROVIDER)
            if (mLastLocation == null) {
                mLastLocation = locationManager?.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)
            }

            LocalBroadcastManager.getInstance(mContext)
                .registerReceiver(mBroadcastReceiver, IntentFilter(SHOW_DISCRIPTION))
            if (mLastLocation != null) {
                mTab1PresenterImpl.getList(mLastLocation!!.latitude, mLastLocation!!.longitude)
            } else {
                mTab1PresenterImpl.getLocationsByName()
            }
        }



        return view
    }

    private fun checkPermission(): Boolean {
        return (ContextCompat.checkSelfPermission(
            mContext,
            android.Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED)
    }

    override fun onResume() {
        super.onResume()
        mListView = mView.lv_locations
        mTvInside = mView.tv_inside
        mView.tv_inside.text = mTab1PresenterImpl.getInsideLocation()
        arguments?.let {
            if (it.containsKey(ARG_ITEM_ID)) {
                tabletView = it.getBoolean(ARG_ITEM_ID)
            }
        }

//        mTvInside.setText(mDescription)
        if (checkPermission()) {
            if (mLastLocation != null) {
                mTab1PresenterImpl.getList(mLastLocation!!.latitude, mLastLocation!!.longitude)
                LocalBroadcastManager.getInstance(mContext)
                    .registerReceiver(mBroadcastReceiver, IntentFilter(SHOW_DISCRIPTION));
            }
        }
    }

    override fun setListView(locationNames: ArrayList<Location>) {

        var stringlist = ArrayList<String>()

        for (location in locationNames) {
            stringlist.add(location.name)
        }
        var locationAdapter: LocationAdapter? = null
        if (mLastLocation != null) {
            locationAdapter = LocationAdapter(
                stringlist,
                context!!,
                mTab1PresenterImpl,
                locationNames,
                LatLng(mLastLocation!!.latitude, mLastLocation!!.longitude),
                tabletView!!
            )
        }

        mListView.setAdapter(locationAdapter)
        mListView.setOnItemClickListener { parent, view, position, id ->
            mTab1PresenterImpl.goToDetailPage(locationNames[position].id)
        }
    }

    override fun goToDetailPage(id: Int, context: Context) {
        val intent = Intent(context, DetailActivity::class.java)
        intent.putExtra(ID, id)
        startActivity(intent)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        setHasOptionsMenu(true)
        super.onCreate(savedInstanceState)
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {

        inflater!!.inflate(R.menu.menu_main, menu)

        val sortNearby = menu!!.findItem(R.id.action_nearby)
        val sortName = menu!!.findItem(R.id.action_name)
        val sortAdded = menu!!.findItem(R.id.action_latest)
        val searchItem = menu!!.findItem(R.id.action_search)
        val settingsItem = menu!!.findItem(R.id.action_settings)
        if (!tabletView) {
            settingsItem.setVisible(false)
        }
        var searchView = searchItem!!.actionView as android.support.v7.widget.SearchView

        searchView.setOnQueryTextListener(object : android.support.v7.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                mTab1PresenterImpl.changeList(newText!!)
                return true
            }

        })

        var act = activity as MainActivity
        act.addMenuItems(searchItem, settingsItem, sortNearby, sortName, sortAdded)

        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if (item!!.itemId == R.id.action_nearby) {
            sortNearby()
            return true
        } else if (item.itemId == R.id.action_name) {
            sortByName()
            return true
        } else if (item.itemId == R.id.action_latest) {
            sortLatest()
            return true
        } else if (item.itemId == R.id.action_settings) {
            openSettings()
            return true
        }

        return super.onOptionsItemSelected(item)
    }

    fun sortNearby() {
        mTab1PresenterImpl.setSort(0)
        mTab1PresenterImpl.getDistancesList(mLastLocation!!.latitude, mLastLocation!!.longitude)
    }

    fun sortLatest() {
        mTab1PresenterImpl.setSort(1)
        mTab1PresenterImpl.getLocations()
    }

    fun sortByName() {
        mTab1PresenterImpl.setSort(2)
        mTab1PresenterImpl.getLocationsByName()
    }

    fun openSettings(){
        var act = activity as MainActivity
        act.showSettings(id)
    }
}