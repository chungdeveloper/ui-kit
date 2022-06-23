package com.chungld.uipack.indicator

import androidx.viewpager.widget.ViewPager
import androidx.viewpager2.widget.ViewPager2

abstract class OnPageChangeListener : ViewPager.OnPageChangeListener,
    ViewPager2.OnPageChangeCallback() {
    override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
    }

    override fun onPageSelected(position: Int) = onPageChanged(position)

    override fun onPageScrollStateChanged(state: Int) {
    }

    abstract fun onPageChanged(position: Int)
}