package com.chungld.uikit

import android.os.Bundle
import com.chungld.uikit.databinding.ActivityIconBinding

class IconActivity : BaseActivity<ActivityIconBinding>() {

    override fun getLayoutId(): Int {
        return R.layout.activity_icon
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val badgeView = mBinding.badgeView//TrpBadgeView(applicationContext)

//        mBinding.container.addView(badgeView, 0)
//        badgeView.tabIcon = ContextCompat.getDrawable(applicationContext, R.drawable.ic_flight)
//        badgeView.text = "234adfasd"
//        badgeView.setBadgeContent("20")
//        badgeView.showBadge(true)
    }
}
