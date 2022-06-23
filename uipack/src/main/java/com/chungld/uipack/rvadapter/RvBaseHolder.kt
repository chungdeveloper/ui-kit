package com.chungld.uipack.rvadapter

import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView


abstract class RvBaseHolder<T>(
    private val viewBinding: ViewDataBinding
) : RecyclerView.ViewHolder(viewBinding.root) {

    abstract fun onBind(data: T)
}