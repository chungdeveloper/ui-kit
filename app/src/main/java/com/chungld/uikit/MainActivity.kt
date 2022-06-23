package com.chungld.uikit

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_NO
import androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_YES
import com.chungld.uikit.databinding.ActivityMainBinding
import com.chungld.uikit.rvadapter.multi.JavaRvMultiTypeActivity
import com.chungld.uikit.rvadapter.single.RvSingleTypeActivity

class MainActivity : BaseActivity<ActivityMainBinding>() {

    override fun getLayoutId(): Int {
        return R.layout.activity_main
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.getDefaultNightMode())

        mBinding.btnUIMode.text =
            if (AppCompatDelegate.getDefaultNightMode() == MODE_NIGHT_YES) "Switch to LightMode" else "Switch to NightMode"

        mBinding.btnUIMode.setOnClickListener {
            AppCompatDelegate.setDefaultNightMode(if (AppCompatDelegate.getDefaultNightMode() == MODE_NIGHT_YES) MODE_NIGHT_NO else MODE_NIGHT_YES)
            recreate()
        }

        mBinding.btnCalendar.setOnClickListener {
            val intent = Intent(this, CalendarActivity::class.java)
            startActivity(intent)
        }

        mBinding.btnRelative.setOnClickListener {
            val intent = Intent(this, TrpRelativeActivity::class.java)
            startActivity(intent)
        }

        mBinding.btnBadgeView.setOnClickListener {
            val intent = Intent(this, BadgeViewActivity::class.java)
            startActivity(intent)
        }

        mBinding.btnText.setOnClickListener {
            val intent = Intent(this, TextActivity::class.java)
            startActivity(intent)
        }

        mBinding.btnButton.setOnClickListener {
            val intent = Intent(this, ButtonActivity::class.java)
            startActivity(intent)
        }

        mBinding.btnSnack.setOnClickListener {
            val intent = Intent(this, SnackBarActivity::class.java)
            startActivity(intent)
        }

        mBinding.btnSwitch.setOnClickListener {
            val intent = Intent(this, SwitchActivity::class.java)
            startActivity(intent)
        }

        mBinding.btnCac.setOnClickListener {
            val intent = Intent(this, CarcActivity::class.java)
            startActivity(intent)
        }

        mBinding.btnCheckbox.setOnClickListener {
            val intent = Intent(this, CheckBoxActivity::class.java)
            startActivity(intent)
        }

        mBinding.btnIcon.setOnClickListener {
            val intent = Intent(this, IconActivity::class.java)
            startActivity(intent)
        }

        mBinding.btnRating.setOnClickListener {
            val intent = Intent(this, RatingActivity::class.java)
            startActivity(intent)
        }

        mBinding.btnChip.setOnClickListener {
            val intent = Intent(this, ChipActivity::class.java)
            startActivity(intent)
        }

        mBinding.btnIndicator.setOnClickListener {
            val intent = Intent(this, IndicatorActivity::class.java)
            startActivity(intent)
        }

        mBinding.btnSpinner.setOnClickListener {
            val intent = Intent(this, SpinnerActivity::class.java)
            startActivity(intent)
        }

        mBinding.btnFab.setOnClickListener {
            val intent = Intent(this, FloatActivity::class.java)
            startActivity(intent)
        }

        mBinding.btnSeekBar.setOnClickListener {
            val intent = Intent(this, SeekBarActivity::class.java)
            startActivity(intent)
        }

        mBinding.btnRatingPoint.setOnClickListener {
            val intent = Intent(this, RatingPointActivity::class.java)
            startActivity(intent)
        }

        mBinding.btnTabLayout.setOnClickListener {
            val intent = Intent(this, TabLayoutActivity::class.java)
            startActivity(intent)
        }

        mBinding.btnNavigationBottom.setOnClickListener {
            val intent = Intent(this, BottomNavigationActivity::class.java)
            startActivity(intent)
        }

        mBinding.btnRecyclerView.setOnClickListener {
            val intent = Intent(this, JavaRvMultiTypeActivity::class.java)
            startActivity(intent)
        }

        mBinding.btnRecyclerViewSingle.setOnClickListener {
            val intent = Intent(this, RvSingleTypeActivity::class.java)
            startActivity(intent)
        }
    }
}
