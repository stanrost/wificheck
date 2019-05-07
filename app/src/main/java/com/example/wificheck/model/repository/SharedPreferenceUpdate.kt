package com.example.wificheck.model.repository

interface SharedPreferenceUpdate{
    fun getListOrder(): Int
    fun setSort(sortId : Int)
    fun setAutomaticOrNotificationOrBoth(id: Int)
    fun getChecked(): Int
    fun setEnteringCheck(checked: Boolean)
    fun setLeavingCheck(checked: Boolean)
    fun getEnteringCheck(): Boolean
    fun getLeavingCheck(): Boolean
    fun setInsideLocation(description: String)
    fun getInsideLocation(): String
    fun setLightCheck(checked: Boolean)
    fun setVibrateCheck(checked: Boolean)
    fun getVibrateCheck(): Boolean
    fun getLightCheck(): Boolean
    fun setActive(active: Boolean)
    fun getActive(): Boolean
}