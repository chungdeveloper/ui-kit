package com.chungld.uikit

import android.os.Bundle
import android.os.Handler
import com.chungld.uikit.databinding.ActivityButtonBinding
import com.chungld.uipack.button.TrpProgressButton

class ButtonActivity : BaseActivity<ActivityButtonBinding>() {

    override fun getLayoutId(): Int {
        return R.layout.activity_button
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        findViewById<TrpProgressButton>(R.id.btnProcessButton)?.let { button ->
            button.setOnClickListener {
            button.trpLoading = true
            Handler().postDelayed({
                button.trpLoading = false
            }, 3000)
        }
        }
    }
}
