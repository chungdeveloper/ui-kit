package com.chungld.uikit

import android.os.Bundle
import android.os.Handler
import com.chungld.uikit.databinding.ActivityButtonBinding
import kotlinx.android.synthetic.main.activity_button.*

class ButtonActivity : BaseActivity<ActivityButtonBinding>() {

    override fun getLayoutId(): Int {
        return R.layout.activity_button
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        btnProcessButton.setOnClickListener {
            btnProcessButton.trpLoading = true
            Handler().postDelayed({
                btnProcessButton.trpLoading = false
            }, 3000)
        }
    }
}
