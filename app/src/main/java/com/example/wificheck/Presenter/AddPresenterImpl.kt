package com.example.wificheck.presenter

import android.content.Context
import com.example.wificheck.R
import com.example.wificheck.model.entity.Location
import com.example.wificheck.model.repository.LocationRepository
import com.example.wificheck.model.repository.LocationRepositoryImpl
import com.example.wificheck.view.AddView

class AddPresenterImpl(
    private val view: AddView,
    private val context: Context,
    private val locationRepository: LocationRepository = LocationRepositoryImpl(context)
) : AddPresenter {

    override fun addLocation(name: String?, long: Double?, lat: Double?, radius: Double?) {

        val incompleteStrings = ArrayList<String>()
        var complete = true

        if (name.equals("") || name == null) {
            incompleteStrings.add(context.getString(R.string.add_name_error))
            complete = false
        }
        if (long == null || lat == null) {
            incompleteStrings.add(context.getString(R.string.add_marker_error))
            complete = false
        }
        if (radius == null || radius == 0.0) {
            incompleteStrings.add(context.getString(R.string.add_radius_error))
            complete = false
        }

        if (complete) {
            val location = Location(name!!, long!!, lat!!, radius!!)
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