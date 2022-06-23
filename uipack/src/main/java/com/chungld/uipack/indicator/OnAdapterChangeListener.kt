package com.chungld.uipack.indicator

import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager.widget.ViewPager

abstract class OnAdapterChangeListener : ViewPager.OnAdapterChangeListener {

    override fun onAdapterChanged(
        viewPager: ViewPager,
        oldAdapter: PagerAdapter?,
        newAdapter: PagerAdapter?
    ) {
        onAdapterChangedListener(newAdapter?.count ?: 0)
    }

    abstract fun onAdapterChangedListener(size: Int)
}