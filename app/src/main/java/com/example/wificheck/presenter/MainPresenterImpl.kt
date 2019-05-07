package com.example.wificheck.presenter

import android.content.Context
import android.support.v4.view.ViewPager
import com.example.wificheck.model.repository.LocationRepository
import com.example.wificheck.model.repository.LocationRepositoryImpl
import com.example.wificheck.model.repository.SharedPreferenceUpdate
import com.example.wificheck.model.repository.SharedPreferenceUpdateImpl
import com.example.wificheck.view.MainView

class MainPresenterImpl(
    private val view: MainView,
    private val context: Context,
    private val locationRepository: LocationRepository = LocationRepositoryImpl(context),
    private val mSharedPreferences: SharedPreferenceUpdate = SharedPreferenceUpdateImpl(context)
) : MainPresenter {

    override fun hideOrShowFloatingActionButton(position: Int) {
        if (position == 0) {
            view.showFloatingActionButton()
        } else if (position == 1) {
            view.showFloatingActionButton()
        } else {
            view.hideFloatingActionButton()
        }
    }

    override fun setGeofenceLocations() {
        val location = locationRepository.getLocation()
        if (location.size > 0) {
            view.startGeofence(location)
        }
    }

    override fun setInsideLocation(description: String) {
        mSharedPreferences.setInsideLocation(description)
    }

    override fun checkView(viewPager: ViewPager?) {
        if (viewPager == null){
            view.tabletView()
        }
        else{
            view.mobileView()
        }
    }

    override fun setActive(active: Boolean) {
        mSharedPreferences.setActive(active)
    }

}