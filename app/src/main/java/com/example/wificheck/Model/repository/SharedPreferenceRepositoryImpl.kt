package com.example.wificheck.Model.repository

import android.content.Context
import android.content.SharedPreferences

class SharedPreferenceRepositoryImpl() : SharedPreferenceRepository {

    override fun getListOrder(context: Context): Int {
        var sp = context.getSharedPreferences("SharedPreferences", Context.MODE_PRIVATE)

        return sp.getInt("SORT", 3)
    }

    override fun setSort(sortId: Int, context: Context) {
        var sp = context.getSharedPreferences("SharedPreferences", Context.MODE_PRIVATE)
        var ed = sp.edit()
        ed.putInt("SORT", sortId)
        ed.apply()
    }
}