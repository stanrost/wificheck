package com.example.wificheck.presenter.service

interface GoefenceTransitionPresenter{
    fun getActive(): Boolean
    fun getEnteringCheck(): Boolean
    fun getLeavingCheck(): Boolean
    fun getVibrateCheck(): Boolean
    fun getLightCheck(): Boolean

}