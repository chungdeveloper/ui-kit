package com.chungld.uikit

import android.os.Bundle
import android.util.Log
import android.view.View
import com.chungld.uikit.databinding.ActivitySeekBarBinding
import com.chungld.uipack.seekbar.TrpSeekBar

class SeekBarActivity : BaseActivity<ActivitySeekBarBinding>() {

    override fun getLayoutId(): Int {
        return R.layout.activity_seek_bar
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        findViewById<View>(R.id.tvHeader0).setOnClickListener {
            findViewById<TrpSeekBar>(R.id.seekBar).TrpSeekBarState[0].value
            Log.d("asdhjg", "asdas")
        }
    }
}
