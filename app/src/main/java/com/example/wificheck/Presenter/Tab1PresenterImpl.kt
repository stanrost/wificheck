package com.example.wificheck.Presenter

import android.content.Context
import com.example.wificheck.Model.service.LocationServiceImpl
import com.example.wificheck.View.fragment.Tab1View

class Tab1PresenterImpl(view: Tab1View) : Tab1Presenter{



    var view: Tab1View = view
    lateinit var pairs: ArrayList<Pair<String, Int>>
    lateinit var globalContext:Context

    override fun getLocationNames(context: Context) {

        globalContext =context
        pairs = LocationServiceImpl(context).getNamesAndIds()

        var names = ArrayList<String>()

        for ((name, id) in pairs){
            names.add(name)
        }

        view.setListView(names)
    }


    override fun goToDetailPage(index: Int) {

        var (name, id) = pairs[index]

        view.goToDetailPage(id, globalContext)
    }
}