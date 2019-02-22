package com.tranphuc.news.adapter

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter


class NewsPagerAdapter(fm: FragmentManager?) : FragmentPagerAdapter(fm) {

    private var listFragment: MutableList<Fragment> = ArrayList()
    private var listTabtitle: MutableList<String> = ArrayList()

    override fun getItem(p0: Int): Fragment {
        return listFragment.get(p0)
    }

    override fun getCount(): Int {
        return listFragment.size
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return listTabtitle.get(position)
    }

    public fun addFragment(fragemnt: Fragment, tabTitle: String) {
        listFragment.add(fragemnt)
        listTabtitle.add(tabTitle)
    }
}