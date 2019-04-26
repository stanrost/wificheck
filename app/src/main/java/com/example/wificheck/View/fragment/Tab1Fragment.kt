package com.example.wificheck.View.fragment

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ListView
import com.example.wificheck.R

import com.example.wificheck.Presenter.Tab1PresenterImpl
import com.example.wificheck.View.DetailActivity
import kotlinx.android.synthetic.main.tab1_list_fragment.view.*


class Tab1Fragment : Fragment(), Tab1View {

    lateinit var listView: ListView
    private lateinit var tab1PresenterImpl: Tab1PresenterImpl
    lateinit var globalContext: Context
    lateinit var globalView: View

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.tab1_list_fragment, container, false)

        globalView = view
        globalContext = view.context
        listView = view.lv_locations

        tab1PresenterImpl = Tab1PresenterImpl(this, view.context)
        tab1PresenterImpl.getLocationNames()
        return view
    }

    override fun onResume() {
        super.onResume()
        tab1PresenterImpl.getLocationNames()
        listView = globalView.lv_locations

    }

    override fun setListView(locationNames: ArrayList<String>) {

        val arrayAdapter: ArrayAdapter<String> = ArrayAdapter<String>(
            context!!,
            android.R.layout.simple_list_item_1,
            locationNames
        )

        listView.setAdapter(arrayAdapter)
        listView.setOnItemClickListener { parent, view, position, id ->
            tab1PresenterImpl.goToDetailPage(position)
        }
    }

    override fun goToDetailPage(id: Int, context: Context) {
        val intent = Intent(context, DetailActivity::class.java)
        intent.putExtra("ID", id)
        startActivity(intent)
    }
}