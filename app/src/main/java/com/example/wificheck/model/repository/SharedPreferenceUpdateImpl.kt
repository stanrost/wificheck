package com.example.wificheck.model.repository

import android.content.Context
import android.content.SharedPreferences

class SharedPreferenceUpdateImpl(var context: Context) :
    SharedPreferenceUpdate {


    val SHAREDPREFERENCE = "SharedPreferences"
    val SORT = "SORT"
    val NOTIFICATION_OR_AUTO = "NOTIFICATION_OR_AUTO"
    val ENTERING_CHECK = "ENTERING_CHECK"
    val LEAVING_CHECK = "LEAVING_CHECK"
    val INSIDE_CHECK = "INSIDE_CHECK"


    override fun getListOrder(): Int {
        val sp = getSharedPreference()
        return sp.getInt(SORT, 3)
    }

    override fun setSort(sortId: Int) {
        val sp = getSharedPreference()
        val ed = sp.edit()
        ed.putInt(SORT, sortId)
        ed.apply()
    }

    override fun setAutomaticOrNotificationOrBoth(id: Int) {
        val sp = getSharedPreference()
        val ed = sp.edit()
        ed.putInt(NOTIFICATION_OR_AUTO, id)
        ed.apply()
    }

    override fun getChecked(): Int {
        val sp = getSharedPreference()
        return sp.getInt(NOTIFICATION_OR_AUTO, 0)
    }

    fun getSharedPreference(): SharedPreferences {
        return context.getSharedPreferences(SHAREDPREFERENCE, Context.MODE_PRIVATE)
    }

    override fun setEnteringCheck(checked: Boolean) {
        val sp = getSharedPreference()
        val ed = sp.edit()
        ed.putBoolean(ENTERING_CHECK, checked)
        ed.apply()
    }

    override fun setLeavingCheck(checked: Boolean) {
        val sp = getSharedPreference()
        val ed = sp.edit()
        ed.putBoolean(LEAVING_CHECK, checked)
        ed.apply()
    }

    override fun getEnteringCheck(): Boolean {
        val sp = getSharedPreference()
        return sp.getBoolean(ENTERING_CHECK, true)
    }

    override fun getLeavingCheck(): Boolean {
        val sp = getSharedPreference()
        return sp.getBoolean(LEAVING_CHECK, true)
    }

    override fun setInsideLocation(description: String) {
        val sp = getSharedPreference()
        val ed = sp.edit()
        ed.putString(INSIDE_CHECK, description)
        ed.apply()
    }

    override fun getInsideLocation(): String {
        val sp = getSharedPreference()
        return sp.getString(INSIDE_CHECK, "")!!
    }
}