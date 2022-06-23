package com.chungld.uikit.pager

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.chungld.uikit.tablayoutStory.TabDemoFragment

class TestTabLayoutViewPager constructor(fm: FragmentManager) : FragmentPagerAdapter(fm) {

    private val fragments = mutableListOf<Fragment>()

    init {
        fragments.add(TabDemoFragment())
        fragments.add(TabDemoFragment())
        fragments.add(TabDemoFragment())
        fragments.add(TabDemoFragment())
    }

    override fun getItem(p0: Int): Fragment {
        return fragments[p0]
    }

    override fun getCount(): Int {
        return fragments.size
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return "Tab $position"
    }
}