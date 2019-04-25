package com.example.wificheck.Presenter

import android.content.Context
import android.view.View
import com.example.wificheck.Model.Tab1ModelImpl
import com.example.wificheck.View.fragment.Tab1View

class Tab1PresenterImpl(view: Tab1View) : Tab1Presenter{


    var view: Tab1View = view


    override fun getLocations(context: Context, v : View) {
        view.setListView(Tab1ModelImpl(view).getLocaiton(context), v)
    }

}