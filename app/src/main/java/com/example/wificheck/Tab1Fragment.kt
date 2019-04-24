package com.example.wificheck

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ListView

class Tab1Fragment  : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.tab1_list_fragment, container, false)

        var locations :ArrayList<String> = ArrayList()

        locations.add("Home")
        locations.add("Parents")
        locations.add("Word")

        var listView = view.findViewById<ListView>(R.id.lv_locations)

        var arrayAdapter: ArrayAdapter<String> = ArrayAdapter<String>(
            activity,
            android.R.layout.simple_list_item_1,
            locations
        )

        listView.setAdapter(arrayAdapter)

        return view
    }
}