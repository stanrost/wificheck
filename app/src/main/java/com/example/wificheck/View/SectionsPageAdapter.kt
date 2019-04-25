package com.example.wificheck.View

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter

class SectionsPageAdapter : FragmentPagerAdapter {


    var mFragmentList:ArrayList<Fragment> = ArrayList<Fragment>()
    var mFragmentTitleList:ArrayList<String> = ArrayList<String>()

    constructor(fm:FragmentManager) : super(fm)

    fun addFragment(fragment: Fragment, title:String){
        mFragmentList.add(fragment)
        mFragmentTitleList.add(title)
    }


    override fun getPageTitle(position: Int): CharSequence? {
        return mFragmentTitleList.get(position)
    }

    override fun getItem(position: Int): Fragment {
        return mFragmentList.get(position)
    }

    override fun getCount(): Int {
        return mFragmentList.size
    }

}
