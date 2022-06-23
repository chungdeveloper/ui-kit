package com.chungld.uikit.rvadapter.holder

import com.chungld.uikit.databinding.ItemFiveBinding
import com.chungld.uikit.rvadapter.model.Five
import com.chungld.uipack.rvadapter.RvBaseHolder

class FiveViewHolder(private val binding: ItemFiveBinding) : RvBaseHolder<Five>(binding) {
    override fun onBind(data: Five) {
        binding.five = data
    }
}