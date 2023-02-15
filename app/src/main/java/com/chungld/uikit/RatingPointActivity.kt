package com.chungld.uikit

import android.os.Bundle
import android.view.View
import com.chungld.uikit.databinding.ActivityRatingPointBinding
import com.chungld.uipack.rating.indicator.TrpRatingPoint

class RatingPointActivity : BaseActivity<ActivityRatingPointBinding>() {

    override fun getLayoutId(): Int {
        return R.layout.activity_rating_point
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        findViewById<View>(R.id.btnChangeValue).setOnClickListener {
            findViewById<TrpRatingPoint>(R.id.rating0).text = "30"
        }
    }
}
