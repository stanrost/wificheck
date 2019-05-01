package com.example.wificheck.Model.repository

import android.content.Context

interface SharedPreferenceRepository{
    fun getListOrder(context: Context): Int
    fun setSort(sortId : Int,context: Context)


}