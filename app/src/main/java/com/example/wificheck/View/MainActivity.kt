package com.example.wificheck.View

import android.support.design.widget.TabLayout
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity

import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.v4.view.ViewPager
import com.example.wificheck.*
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), MainView{


    private lateinit var mSectionsPagerAdapter: SectionsPageAdapter
    private lateinit var mViewPager: ViewPager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mSectionsPagerAdapter = SectionsPageAdapter(supportFragmentManager)
        mViewPager = findViewById(R.id.container)
        var tabLayout = findViewById<TabLayout>(R.id.tabs)
        tabLayout.setupWithViewPager(mViewPager)
        setupViewPager(mViewPager)


        fab.setOnClickListener { view ->
        }
    }



    private fun setupViewPager(viewPager:ViewPager){
        var adapter = SectionsPageAdapter(supportFragmentManager)
        adapter.addFragment(Tab1Fragment(), getString(R.string.title_list))
        adapter.addFragment(Tab2Fragment(), getString(R.string.title_maps))
        adapter.addFragment(Tab3Fragment(), getString(R.string.title_settings))
        viewPager.setAdapter(adapter)
    }



}
