package com.chungld.uipack.rvadapter

import androidx.databinding.ViewDataBinding

interface Strategy<out T, VH : RvBaseHolder<out Any>> {
    fun createHolder(viewDataBinding: ViewDataBinding): VH
}