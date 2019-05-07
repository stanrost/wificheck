package com.example.wificheck.presenter.fragment

import android.content.Context
import com.example.wificheck.model.repository.SharedPreferenceUpdateImpl
import com.example.wificheck.view.fragment.Tab3View

class Tab3PresenterImpl(
    val view: Tab3View,
    val context: Context,
    val sharedPreferenceServiceImpl: SharedPreferenceUpdateImpl = SharedPreferenceUpdateImpl(
        context
    )
) : Tab3Presenter {


    override fun setNotification(boolean: Boolean) {
        if (boolean) {
            sharedPreferenceServiceImpl.setAutomaticOrNotificationOrBoth(0)
        }
    }

    override fun setAutomatic(boolean: Boolean) {
        if (boolean) {
            sharedPreferenceServiceImpl.setAutomaticOrNotificationOrBoth(1)
        }
    }

    override fun setBoth(boolean: Boolean) {
        if (boolean) {
            sharedPreferenceServiceImpl.setAutomaticOrNotificationOrBoth(2)
        }
    }

    override fun getChecked() {
        var checked = sharedPreferenceServiceImpl.getChecked()

        when (checked) {
            0 -> view.setNotification()
            1 -> view.setAutomatic()
            2 -> view.setBoth()
        }

    }

    override fun setEnteringCheck(checked: Boolean) {
        sharedPreferenceServiceImpl.setEnteringCheck(checked)
    }

    override fun setLeavingCheck(checked: Boolean) {
        sharedPreferenceServiceImpl.setLeavingCheck(checked)
    }

    override fun getEnteringCheck() {
        if (sharedPreferenceServiceImpl.getEnteringCheck()) {
            view.setEnteringSwitch()
        }
    }

    override fun getLeavingCheck() {
        if (sharedPreferenceServiceImpl.getLeavingCheck()) {
            view.setLeavingSwitch()
        }
    }

}