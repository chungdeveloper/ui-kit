package com.chungld.uikit

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.chungld.uipack.nav.getOrCreateBadgeAt
import com.chungld.uipack.nav.getOrCreateBadgeById
import kotlinx.android.synthetic.main.activity_bottom_navigation.*

class BottomNavigationActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bottom_navigation)

        vBottomNavigation.getOrCreateBadgeAt(0)?.apply {
            setBadgeContent("20")
            showBadge(true)
        }

        vBottomNavigation.getOrCreateBadgeById(R.id.navigation_notifications)?.apply {
            setBadgeContent("20")
            showBadge(true)
        }
    }
}
