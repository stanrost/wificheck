package com.example.wificheck.presenter

import android.content.Context
import com.example.wificheck.model.entity.Location
import com.example.wificheck.model.repository.LocationRepositoryImpl
import com.example.wificheck.view.AddView

class AddPresenterImpl(
    val view: AddView,
    val context: Context,
    val locationRepository: LocationRepositoryImpl = LocationRepositoryImpl(context)
) : AddPresenter {

    override fun addLocation(name: String?, long: Double?, lat: Double?, radius: Double?) {

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
            var location = Location(name!!, long!!, lat!!, radius!!)
            locationRepository.addLocation(location)
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