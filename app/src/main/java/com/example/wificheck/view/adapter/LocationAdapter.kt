package com.example.wificheck.view.adapter

import android.content.Context
import android.database.DataSetObserver
import android.support.v7.widget.CardView
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.example.wificheck.Model.Entity.Location
import com.example.wificheck.Presenter.Tab1PresenterImpl
import com.example.wificheck.R
import com.google.android.gms.maps.model.LatLng


class LocationAdapter(
    var list: ArrayList<String>,
    var context: Context,
    var mTab1PresenterImpl: Tab1PresenterImpl,
    var locations: ArrayList<Location>,
    var latLng: LatLng
) : ListAdapter {


    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        var view = convertView

        if (view == null) {
            val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            view = inflater.inflate(R.layout.list_item_location, null)
        }

        var tvItem = view!!.findViewById<TextView>(R.id.tv_item)
        tvItem.setText(list[position])

        val item = view!!.findViewById<CardView>(R.id.list_item)

        item.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                mTab1PresenterImpl.goToDetailPage(locations[position].id)
            }

        })

        val btnItem = view!!.findViewById<ImageButton>(R.id.btn_item)
        btnItem.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                showMenu(v!!, position)
                //mTab1PresenterImpl.removeLocation(locations[position], latLng.latitude, latLng.longitude)
            }
        })
        return view!!;

    }

    fun showMenu(view: View, position: Int) {
        var popupMenu = PopupMenu(context, view)
        popupMenu.inflate(R.menu.menu_item)

        popupMenu.setOnMenuItemClickListener(object : PopupMenu.OnMenuItemClickListener {
            override fun onMenuItemClick(item: MenuItem?): Boolean {
                if (item!!.itemId == R.id.menu_delete) {
                    mTab1PresenterImpl.removeLocation(locations[position], latLng.latitude, latLng.longitude)
                    return true
                }
                return false
            }

        })
        popupMenu.show()
    }


    override fun getItem(position: Int): Any {
        return list[position]
    }

    override fun getItemId(position: Int): Long {
        return 0
    }

    override fun getCount(): Int {
        return list.size
    }

    override fun isEmpty(): Boolean {
        return false
    }

    override fun registerDataSetObserver(observer: DataSetObserver?) {
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    override fun getViewTypeCount(): Int {
        return 1
    }

    override fun isEnabled(position: Int): Boolean {
        return false
    }

    override fun hasStableIds(): Boolean {
        return false
    }

    override fun areAllItemsEnabled(): Boolean {
        return false
    }

    override fun unregisterDataSetObserver(observer: DataSetObserver?) {
    }


}
