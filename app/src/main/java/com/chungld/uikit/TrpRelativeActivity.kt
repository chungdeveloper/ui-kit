package com.chungld.uikit

import android.os.Bundle
import android.view.View
import com.chungld.uikit.databinding.ActivityTrpRelativeBinding
import com.chungld.uipack.viewGroup.TrpRelativeLayout

class TrpRelativeActivity : BaseActivity<ActivityTrpRelativeBinding>() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        findViewById<View>(R.id.changeRadius)?.let {
            it.setOnClickListener {
                findViewById<TrpRelativeLayout>(R.id.rlRelative).setCornerRadius(30f, 30f, 30f, 40f)
            }
        }
    }

    override fun getLayoutId() = R.layout.activity_trp_relative
}