package com.example.wificheck.presenter

import android.content.Context
import com.example.wificheck.model.repository.LocationRepositoryImpl
import com.example.wificheck.model.repository.SharedPreferenceUpdateImpl
import com.example.wificheck.view.MainView

class MainPresenterImpl(
    val view: MainView,
    val context: Context,
    val locationRepository: LocationRepositoryImpl = LocationRepositoryImpl(context),
    val mSharedPreferences: SharedPreferenceUpdateImpl = SharedPreferenceUpdateImpl(context)
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
        view.startGeofence(locationRepository.getLocation())
    }

    override fun setInsideLocation(description: String) {
        mSharedPreferences.setInsideLocation(description)
    }

}