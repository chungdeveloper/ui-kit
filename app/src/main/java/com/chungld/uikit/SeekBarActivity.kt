package com.chungld.uikit

import android.os.Bundle
import android.util.Log
import com.chungld.uikit.databinding.ActivitySeekBarBinding
import kotlinx.android.synthetic.main.activity_seek_bar.*

class SeekBarActivity : BaseActivity<ActivitySeekBarBinding>() {

    override fun getLayoutId(): Int {
        return R.layout.activity_seek_bar
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        tvHeader0.setOnClickListener {
            seekBar.TrpSeekBarState[0].value
            Log.d("asdhjg", "asdas")
        }
    }
}
