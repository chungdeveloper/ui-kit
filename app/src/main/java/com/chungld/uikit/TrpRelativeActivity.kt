package com.chungld.uikit

import android.os.Bundle
import com.chungld.uikit.databinding.ActivityTrpRelativeBinding
import kotlinx.android.synthetic.main.activity_trp_relative.*

class TrpRelativeActivity : BaseActivity<ActivityTrpRelativeBinding>() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        changeRadius.setOnClickListener {
            rlRelative.setCornerRadius(30f, 30f, 30f, 40f)
        }
    }

    override fun getLayoutId() = R.layout.activity_trp_relative
}