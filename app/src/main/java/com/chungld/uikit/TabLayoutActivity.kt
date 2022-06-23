package com.chungld.uikit

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.chungld.uikit.pager.TestTabLayoutViewPager
import kotlinx.android.synthetic.main.activity_tab_layout.*

class TabLayoutActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tab_layout)

//        tab0.addTab(tab0.newTab())
//        tab0.addTab(tab0.newTab())
//        tab0.addTab(tab0.newTab())
//        tab0.addTab(tab0.newTab())
//
//        tab0.getTabAt(0)?.text = "Tab0"
//        tab0.getTabAt(1)?.text = "Tab1"
//        tab0.getTabAt(2)?.text = "Tab2"
//        tab0.getTabAt(3)?.text = "Tab3"
//
//        val custom = LayoutInflater.from(applicationContext)
//            .inflate(R.layout.fake_custom_tab, container, false)
//
//        tab0.getTabAt(2)?.customView = custom

        tab1.addTab(tab1.newTab())
        tab1.addTab(tab1.newTab())
        tab1.addTab(tab1.newTab())
        tab1.addTab(tab1.newTab())

        tab1.setText(0, "Tab1")
        tab1.setText(1, "Tab2")
        tab1.setText(2, "Tab3")
        tab1.setText(3, "Tab4")

        tab1.getBadgeTabAt(0).showBadge(true)
        tab1.getBadgeTabAt(0).tabIcon = resources.getDrawable(R.drawable.ic_trp_uipack_holding_time)

        tab1.getBadgeTabAt(1).showBadge(false)
        tab1.setText(1, "Tab2")
        tab1.getBadgeTabAt(1).tabIcon = resources.getDrawable(R.drawable.ic_trp_uipack_holding_time)

        tab1.getBadgeTabAt(2).showBadge(true)
        tab1.getBadgeTabAt(2).setBadgeContent("20")

        tab1.getBadgeTabAt(2).tabIcon = resources.getDrawable(R.drawable.ic_trp_uipack_holding_time)
        tab1.getBadgeTabAt(2).showBadge(false)
        tab1.getBadgeTabAt(2).setBadgeContent("30")

        tab1.getBadgeTabAt(3).tabIcon = resources.getDrawable(R.drawable.ic_trp_uipack_holding_time)
        tab1.getBadgeTabAt(3).showBadge(true)
        tab1.getBadgeTabAt(3).setBadgeContent("30")

        val testTabLayoutViewPager = TestTabLayoutViewPager(supportFragmentManager)
        vp1.adapter = testTabLayoutViewPager
        tab2.setupWithViewPager(vp1)

        tab2.getBadgeTabAt(2).showBadge(true)
        tab2.getBadgeTabAt(2).tabIcon = resources.getDrawable(R.drawable.ic_trp_uipack_holding_time)

        tab3.addTab(tab3.newTab())
        tab3.addTab(tab3.newTab())
        tab3.addTab(tab3.newTab())
        tab3.addTab(tab3.newTab())
        tab3.addTab(tab3.newTab())
        tab3.addTab(tab3.newTab())
        tab3.addTab(tab3.newTab())

        tab3.getBadgeTabAt(0).tabIcon = resources.getDrawable(R.drawable.ic_trp_uipack_holding_time)
        tab3.getBadgeTabAt(1).tabIcon = resources.getDrawable(R.drawable.ic_trp_uipack_holding_time)
        tab3.getBadgeTabAt(1).showBadge(true)
        tab3.getBadgeTabAt(2).tabIcon = resources.getDrawable(R.drawable.ic_trp_uipack_holding_time)
        tab3.getBadgeTabAt(2).showBadge(true)
        tab3.getBadgeTabAt(2).setBadgeContent("20")
        tab3.getBadgeTabAt(3).tabIcon = resources.getDrawable(R.drawable.ic_trp_uipack_holding_time)
        tab3.getBadgeTabAt(4).tabIcon = resources.getDrawable(R.drawable.ic_flight)
        tab3.getBadgeTabAt(5).tabIcon = resources.getDrawable(R.drawable.ic_trp_uipack_holding_time)
        tab3.getBadgeTabAt(6).tabIcon = resources.getDrawable(R.drawable.ic_flight)
        tab3.getBadgeTabAt(6).showBadge(true)
//
        tab4.addTab(tab4.newTab())
        tab4.addTab(tab4.newTab())
        tab4.addTab(tab4.newTab())
        tab4.addTab(tab4.newTab())

        tab4.setText(0, "Tab0")
        tab4.setText(1, "Tab1")
        tab4.setText(2, "Tab2")
        tab4.setText(3, "Tab3")

        tab4.getBadgeTabAt(0).showBadge(true)
        tab4.getBadgeTabAt(1).showBadge(true)
        tab4.getBadgeTabAt(1).setBadgeContent("20")
    }
}
