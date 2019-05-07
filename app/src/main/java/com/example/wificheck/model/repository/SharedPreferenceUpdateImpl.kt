package com.example.wificheck.model.repository

import android.content.Context
import android.content.SharedPreferences
import com.example.wificheck.view.MainActivity

class SharedPreferenceUpdateImpl(var context: Context) :
    SharedPreferenceUpdate {

    companion object {
        private const val SHARED_PREFERENCE = "SharedPreferences"
        private const val SORT = "SORT"
        private const val NOTIFICATION_OR_AUTO = "NOTIFICATION_OR_AUTO"
        private const val ENTERING_CHECK = "ENTERING_CHECK"
        private const val LEAVING_CHECK = "LEAVING_CHECK"
        private const val INSIDE_CHECK = "INSIDE_CHECK"
        private const val VIBRATE_CHECK = "VIBRATE_CHECK"
        private const val LIGHT_CHECK = "LIGHT_CHECK"
        private const val ACTIVE = "active"
    }

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
        return context.getSharedPreferences(SHARED_PREFERENCE, Context.MODE_PRIVATE)
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
        return sp.getBoolean(ENTERING_CHECK, false)
    }

    override fun getLeavingCheck(): Boolean {
        val sp = getSharedPreference()
        return sp.getBoolean(LEAVING_CHECK, false)
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

    override fun setLightCheck(checked: Boolean) {
        val sp = getSharedPreference()
        val ed = sp.edit()
        ed.putBoolean(LIGHT_CHECK, checked)
        ed.apply()
    }

    override fun setVibrateCheck(checked: Boolean) {
        val sp = getSharedPreference()
        val ed = sp.edit()
        ed.putBoolean(VIBRATE_CHECK, checked)
        ed.apply()
    }

    override fun getVibrateCheck(): Boolean {
        val sp = getSharedPreference()
        return sp.getBoolean(VIBRATE_CHECK, false)
    }

    override fun getLightCheck(): Boolean {
        val sp = getSharedPreference()
        return sp.getBoolean(LIGHT_CHECK, false)
    }

    override fun setActive(active: Boolean) {
        val sp = getSharedPreference()
        val ed = sp.edit()
        ed.putBoolean(ACTIVE, active)
        ed.apply()
    }

    override fun getActive(): Boolean {
        val sp = getSharedPreference()
        return sp.getBoolean(ACTIVE, false)
    }
}