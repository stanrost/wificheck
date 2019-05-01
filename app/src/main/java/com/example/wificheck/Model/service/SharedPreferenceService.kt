package com.example.wificheck.Model.service

import android.content.Context

interface SharedPreferenceService{
    fun getListOrder(context: Context): Int
    fun setSort(sortId : Int,context: Context)


}