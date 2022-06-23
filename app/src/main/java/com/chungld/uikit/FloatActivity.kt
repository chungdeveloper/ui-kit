package com.chungld.uikit

import android.os.Bundle
import androidx.core.content.ContextCompat
import com.chungld.uikit.databinding.ActivityFloatBinding

class FloatActivity : BaseActivity<ActivityFloatBinding>() {

    override fun getLayoutId(): Int {
        return R.layout.activity_float
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mBinding.floatButton.background =
            ContextCompat.getDrawable(baseContext, R.drawable.bgr_shape_round)
    }
}
