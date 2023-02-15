package com.chungld.uikit

import android.os.Bundle
import com.chungld.uikit.databinding.ActivityBadgeViewBinding

class BadgeViewActivity : BaseActivity<ActivityBadgeViewBinding>() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun getLayoutId() = R.layout.activity_badge_view
}
