package com.example.wificheck.View.fragment

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ListView
import com.example.wificheck.Model.Entity.Location
import com.example.wificheck.R

import com.example.wificheck.Presenter.Tab1PresenterImpl


class Tab1Fragment : Fragment(), Tab1View {

    lateinit var listView: ListView
    private lateinit var tablePresenterImpl: Tab1PresenterImpl
    lateinit var globalContext: Context
    lateinit var globalView : View

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.tab1_list_fragment, container, false)

        globalView = view
        globalContext = view.context

        tablePresenterImpl = Tab1PresenterImpl(this)
        tablePresenterImpl.getLocations(view.context, view)

        return view
    }


    override fun onResume() {
        super.onResume()
        tablePresenterImpl.getLocations(globalContext, globalView)

    }

    override fun setListView(locations: ArrayList<Location>, view:View) {

        var strings = ArrayList<String>()
        listView = view.findViewById(R.id.lv_locations)

        for (location in locations){
            strings.add(location.name)
        }

        var arrayAdapter: ArrayAdapter<String> = ArrayAdapter<String>(
            activity,
            android.R.layout.simple_list_item_1,
            strings
        )

        listView.setAdapter(arrayAdapter)
    }
}