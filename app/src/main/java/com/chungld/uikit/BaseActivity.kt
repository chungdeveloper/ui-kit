package com.chungld.uikit

import android.annotation.SuppressLint
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.chungld.uipack.theme.TrpTheme

@SuppressLint("Registered")
open abstract class BaseActivity<T : ViewDataBinding> : AppCompatActivity() {

    abstract fun getLayoutId(): Int
    val mBinding by lazy<T> {
        DataBindingUtil.setContentView(this, getLayoutId())
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        this.let {
            TrpTheme.applyDefaultsToContext(it)
        }
        super.onCreate(savedInstanceState)
        mBinding.lifecycleOwner = this
        mBinding.executePendingBindings()
    }

}