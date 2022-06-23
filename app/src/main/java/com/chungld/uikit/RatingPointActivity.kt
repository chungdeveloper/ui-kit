package com.chungld.uikit

import android.os.Bundle
import com.chungld.uikit.databinding.ActivityRatingPointBinding
import kotlinx.android.synthetic.main.activity_rating_point.*

class RatingPointActivity : BaseActivity<ActivityRatingPointBinding>() {

    override fun getLayoutId(): Int {
        return R.layout.activity_rating_point
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        btnChangeValue.setOnClickListener {
            rating0.text = "30"
        }
    }
}
