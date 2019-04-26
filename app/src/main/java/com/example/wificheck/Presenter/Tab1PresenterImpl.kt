package com.example.wificheck.Presenter

import android.content.Context
import com.example.wificheck.Model.service.LocationServiceImpl
import com.example.wificheck.View.fragment.Tab1View

class Tab1PresenterImpl(var view: Tab1View, context: Context) : Tab1Presenter{


    lateinit var pairs: ArrayList<Pair<String, Int>>
    val globalContext:Context = context

    override fun getLocationNames() {

        pairs = LocationServiceImpl(globalContext).getNamesAndIds()
        val names = ArrayList<String>()

        for ((name, id) in pairs){
            names.add(name)
        }

        view.setListView(names)
    }


    override fun goToDetailPage(index: Int) {

        val (name, id) = pairs[index]
        view.goToDetailPage(id, globalContext)
    }
}