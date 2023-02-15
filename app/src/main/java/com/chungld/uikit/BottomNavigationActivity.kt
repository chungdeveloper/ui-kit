package com.chungld.uikit

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.chungld.uipack.nav.getOrCreateBadgeAt
import com.chungld.uipack.nav.getOrCreateBadgeById
import com.google.android.material.bottomnavigation.BottomNavigationView

class BottomNavigationActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bottom_navigation)

        val nav = findViewById<BottomNavigationView>(R.id.vBottomNavigation);

        nav.getOrCreateBadgeAt(0)?.apply {
            setBadgeContent("20")
            showBadge(true)
        }

        nav.getOrCreateBadgeById(R.id.navigation_notifications)?.apply {
            setBadgeContent("20")
            showBadge(true)
        }
    }
}
