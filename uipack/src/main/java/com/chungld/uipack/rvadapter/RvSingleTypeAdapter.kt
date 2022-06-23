package com.chungld.uipack.rvadapter

import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.annotation.LayoutRes
import android.view.LayoutInflater
import android.view.ViewGroup

abstract class RvSingleTypeAdapter<E, VH : RvBaseHolder<E>> : RvBaseAdapter<E, VH>() {

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): VH {
        val viewDataBinding = DataBindingUtil.inflate<ViewDataBinding>(
            LayoutInflater.from(viewGroup.context),
            getLayoutId(),
            viewGroup,
            false
        )
        return createViewHolder(viewDataBinding)
    }

    @LayoutRes
    abstract fun getLayoutId(): Int

    abstract fun createViewHolder(viewDataBinding: ViewDataBinding): VH
}