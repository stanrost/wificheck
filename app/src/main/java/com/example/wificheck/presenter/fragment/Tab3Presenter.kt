package com.example.wificheck.presenter.fragment

interface Tab3Presenter {

    fun setNotification(boolean: Boolean)
    fun setAutomatic(boolean: Boolean)
    fun setBoth(boolean: Boolean)
    fun getChecked()
    fun setEnteringCheck(checked: Boolean)
    fun setLeavingCheck(checked: Boolean)
    fun getEnteringCheck()
    fun getLeavingCheck()
}