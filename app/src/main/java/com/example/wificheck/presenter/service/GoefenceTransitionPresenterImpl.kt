package com.example.wificheck.presenter.service

import android.content.Context
import com.example.wificheck.model.repository.SharedPreferenceUpdateImpl

class GoefenceTransitionPresenterImpl(var context: Context) : GoefenceTransitionPresenter{

    override fun getActive(): Boolean {
        return SharedPreferenceUpdateImpl(context).getActive()
    }

    override fun getEnteringCheck(): Boolean {
        return SharedPreferenceUpdateImpl(context).getEnteringCheck()
    }

    override fun getLeavingCheck(): Boolean {
        return SharedPreferenceUpdateImpl(context).getLeavingCheck()
    }

    override fun getVibrateCheck(): Boolean {
        return SharedPreferenceUpdateImpl(context).getVibrateCheck()
    }

    override fun getLightCheck(): Boolean {
        return SharedPreferenceUpdateImpl(context).getLightCheck()
    }

}