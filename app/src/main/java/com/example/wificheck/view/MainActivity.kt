package com.example.wificheck.view

import android.Manifest
import android.app.AlertDialog
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.ActivityInfo
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.support.design.widget.TabLayout
import android.support.v4.app.FragmentPagerAdapter
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.support.v4.view.ViewPager
import android.support.v7.app.AppCompatActivity
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import com.example.wificheck.model.entity.Location
import com.example.wificheck.presenter.MainPresenterImpl
import com.example.wificheck.R
import com.example.wificheck.view.fragment.Tab1Fragment
import com.example.wificheck.view.fragment.Tab2Fragment
import com.example.wificheck.view.fragment.Tab3Fragment
import com.example.wificheck.backgroundService.GeofenceTransitionsIntentService
import com.example.wificheck.backgroundService.MyService
import com.example.wificheck.view.adapter.SectionsPageAdapter
import com.example.wificheck.view.fragment.DetailFragment
import com.google.android.gms.location.Geofence
import com.google.android.gms.location.GeofencingClient
import com.google.android.gms.location.GeofencingRequest
import com.google.android.gms.location.LocationServices
import com.karumi.dexter.Dexter
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.single.PermissionListener
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity(), MainView {

    companion object {
        private const val SHAREDPREFERENCES = "SharedPreferences"
        private const val BACKSTACK_FRAGMENT = "fragment3"
    }

    private lateinit var mSectionsPagerAdapter: SectionsPageAdapter
    private var mViewPager: ViewPager? = null
    private lateinit var mainPresenter: MainPresenterImpl

    // Geofence
    lateinit var geofencingClient: GeofencingClient
    var geofenceList: ArrayList<Geofence> = ArrayList()
    lateinit var mainView: View

    //tabletview
    lateinit var fragment1:Fragment
    lateinit var fragment2:Fragment

    lateinit var menuSetting : MenuItem
    lateinit var menuSearch : MenuItem
    lateinit var menuNearby : MenuItem
    lateinit var menuName : MenuItem
    lateinit var menuAdded : MenuItem

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        mainPresenter = MainPresenterImpl(this, applicationContext)
        mainView = main_content
        mSectionsPagerAdapter = SectionsPageAdapter(supportFragmentManager)
        mViewPager = container
        geofencingClient = LocationServices.getGeofencingClient(this)

        askPermission()
        mainPresenter.checkView(mViewPager)
        mainPresenter.setGeofenceLocations()

        val serviceIntent = Intent(this, MyService::class.java)
        this.stopService(serviceIntent)

        fab.setOnClickListener { view ->
            openAddActivity()
        }
    }

    override fun onResume() {
        super.onResume()
        mainView = main_content
        val serviceIntent = Intent(this, MyService::class.java)
        this.stopService(serviceIntent)
    }

    override fun onStart() {
        super.onStart()
        mainPresenter.setActive(true)
    }

    override fun onStop() {
        super.onStop()
        mainPresenter.setActive(false)
        mainPresenter.setInsideLocation("")
        val intent = Intent(this, MyService::class.java)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startForegroundService(intent)
        }
        else{
            startService(intent)
        }
    }

    override fun showFloatingActionButton() {
        fab.show()
    }

    override fun hideFloatingActionButton() {
        fab.hide()
    }

    override fun tabletView() {
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
        fragment1 = Tab1Fragment().apply {
            arguments = Bundle().apply {
                putBoolean(Tab1Fragment.TABLET_VIEW, true)
            }
        }
        supportFragmentManager
            .beginTransaction()
            .add(R.id.fl_list, fragment1)
            .commit()
    }

    override fun mobileView() {
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        val tabLayout = findViewById<TabLayout>(R.id.tabs)
        tabLayout.setupWithViewPager(mViewPager)
        setupViewPager(mViewPager!!)
        mViewPager!!.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrollStateChanged(state: Int) {
            }
            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
            }
            override fun onPageSelected(position: Int) {
                mainPresenter.hideOrShowFloatingActionButton(position)

                val fragmentAdapter = mViewPager!!.adapter as FragmentPagerAdapter
                val fragment = fragmentAdapter.getItem(position)

                if (position == 1 && fragment != null) {
                    fragment.onResume();
                }
            }
        })
    }

    override fun openAddActivity() {
        val intent = Intent(this, AddActivity::class.java)
        startActivity(intent)
    }

    override fun startGeofence(locations: ArrayList<Location>) {
            for (location in locations) {
                val geofence = Geofence.Builder().setRequestId(location.name)
                    .setCircularRegion(location.latitude, location.longitude, location.radius.toFloat())
                    .setRequestId(location.name)
                    .setExpirationDuration(Geofence.NEVER_EXPIRE)
                    .setTransitionTypes(Geofence.GEOFENCE_TRANSITION_ENTER or Geofence.GEOFENCE_TRANSITION_EXIT).build()
                geofenceList.add(geofence)
            }
            if (checkPermission()) {
                geofencingClient.removeGeofences(geofencePendingIntent)?.run {}
                geofencingClient.addGeofences(getGeofencingRequest(), geofencePendingIntent)?.run {
                    addOnSuccessListener {
                    }
                    addOnFailureListener {
                        showSettingsPopUp()
                    }
                }
            }
    }

    fun setTabletView(id: Int) {
        fragment2 = DetailFragment().apply {
            arguments = Bundle().apply {
                putInt(DetailFragment.ITEM_ID, id)
            }
        }
        supportFragmentManager
            .beginTransaction()
            .add(R.id.fl_detail, fragment2)
            .commit()
    }

    fun showSettings(id: Int) {
        val fragment3 = Tab3Fragment().apply {
            arguments = Bundle().apply {
                putInt(DetailFragment.ITEM_ID, id)
            }
        }
        supportFragmentManager
            .beginTransaction()
            .add(R.id.fl_settings, fragment3)
            .addToBackStack(BACKSTACK_FRAGMENT)
            .commit()
        fab.hide()
        menuSetting.isVisible = false
        menuSearch.isVisible = false
        menuName.isVisible = false
        menuNearby.isVisible = false
        menuAdded.isVisible = false
        fl_settings.visibility = View.VISIBLE
    }

    override fun onBackPressed() {
        val count = supportFragmentManager.backStackEntryCount
        if(count == 0) {
            super.onBackPressed()
        }
        else{
            getSupportFragmentManager().popBackStack()
            fab.show()
            menuSetting.isVisible = true
            menuSearch.isVisible = true
            menuName.isVisible = true
            menuNearby.isVisible = true
            menuAdded.isVisible = true
        }
    }

    fun addMenuItems(searchItem: MenuItem?, settingsItem: MenuItem?, sortNearby: MenuItem?, sortName: MenuItem?, sortAdded: MenuItem?) {
        menuSearch = searchItem!!
        menuSetting = settingsItem!!
        menuNearby = sortNearby!!
        menuName = sortName!!
        menuAdded = sortAdded!!
    }

    private fun askPermission() {
        Dexter.withActivity(this)
            .withPermission(Manifest.permission.ACCESS_FINE_LOCATION)
            .withListener(object : PermissionListener {
                override fun onPermissionGranted(response: PermissionGrantedResponse) {
                }

                override fun onPermissionDenied(response: PermissionDeniedResponse) {
                    if (response.isPermanentlyDenied) {
                    }
                    finish()
                }

                override fun onPermissionRationaleShouldBeShown(permission: PermissionRequest, token: PermissionToken) {
                    token.continuePermissionRequest()
                }
            }).check()
    }

    private fun checkPermission(): Boolean {
        return (ContextCompat.checkSelfPermission(
            applicationContext,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED)
    }

    private fun setupViewPager(viewPager: ViewPager) {
        val adapter = SectionsPageAdapter(supportFragmentManager)
        adapter.addFragment(Tab1Fragment(), getString(R.string.title_list))
        adapter.addFragment(Tab2Fragment(), getString(R.string.title_maps))
        adapter.addFragment(Tab3Fragment(), getString(R.string.title_settings))
        viewPager.adapter = adapter
    }

    private fun getGeofencingRequest(): GeofencingRequest {
        return GeofencingRequest.Builder().apply {
            setInitialTrigger(GeofencingRequest.INITIAL_TRIGGER_ENTER)
            addGeofences(geofenceList)
        }.build()
    }

    private val geofencePendingIntent: PendingIntent by lazy {
        val intent = Intent(this, GeofenceTransitionsIntentService::class.java)
        PendingIntent.getService(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)

    }

    private fun showSettingsPopUp() {
        val builder = AlertDialog.Builder(this@MainActivity)
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