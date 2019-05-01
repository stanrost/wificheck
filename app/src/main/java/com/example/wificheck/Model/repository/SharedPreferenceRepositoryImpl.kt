package com.example.wificheck.Model.repository

import android.content.Context
import android.content.SharedPreferences

class SharedPreferenceRepositoryImpl() : SharedPreferenceRepository {
    val SHAREDPREFERENCE = "SharedPreferences"
    val SORT = "SORT"


    override fun getListOrder(context: Context): Int {
        val sp = context.getSharedPreferences(SHAREDPREFERENCE, Context.MODE_PRIVATE)
        return sp.getInt(SORT, 3)
    }

    override fun setSort(sortId: Int, context: Context) {
        val sp = context.getSharedPreferences(SHAREDPREFERENCE, Context.MODE_PRIVATE)
        val ed = sp.edit()
        ed.putInt(SORT, sortId)
        ed.apply()
    }
}