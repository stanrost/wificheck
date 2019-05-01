package com.example.wificheck.Model.service

import android.content.Context
import com.example.wificheck.Model.repository.SharedPreferenceRepositoryImpl

class SharedPreferenceServiceImpl() : SharedPreferenceService{
    override fun getListOrder(context : Context): Int {
        return SharedPreferenceRepositoryImpl().getListOrder(context)
    }

    override fun setSort(sortId : Int, context: Context){
        SharedPreferenceRepositoryImpl().setSort(sortId, context)
    }

}