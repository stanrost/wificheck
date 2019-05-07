package com.example.wificheck.presenter

import android.content.Context
import com.example.wificheck.model.entity.Location
import com.example.wificheck.model.repository.LocationRepositoryImpl
import com.example.wificheck.view.AddActivity
import com.example.wificheck.view.UpdateActivity
import com.example.wificheck.view.UpdateView

class UpdatePresenterImpl(val view: UpdateView, val context: Context, val locationRepository : LocationRepositoryImpl = LocationRepositoryImpl(context) ) : UpdatePresenter {

    override fun updateLocation(id: Int, name: String?, long: Double?, lat: Double?, radius: Double?) {

        var incompleteStrings = ArrayList<String>()
        var complete = true

        if (name.equals("") || name == null) {
            incompleteStrings.add("Fill in name")
            complete = false
        }
        if (long == null || lat == null) {
            incompleteStrings.add("Add a marker")
            complete = false
        }
        if (radius == null || radius == 0.0) {
            incompleteStrings.add("Add a radius")
            complete = false
        }

        if (complete) {
            var location = Location(id, name!!, long!!, lat!!, radius!!)
            locationRepository.updateLocation(location)
            view.addGeofence()
            view.closeActivity()
        } else {

            var index = 0
            var max = incompleteStrings.size
            var errorString = ""
            for (string in incompleteStrings) {
                index++
                errorString +=
                    if (index == max) {
                        string
                    } else {
                        "$string, \n"
                    }
            }
            view.showError(errorString)

        }
    }

    override fun addCircle(radius: Int) {
        view.addCircle(radius * 1.0)
    }

}