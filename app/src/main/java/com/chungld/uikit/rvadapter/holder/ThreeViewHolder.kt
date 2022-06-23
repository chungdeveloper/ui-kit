package com.chungld.uikit.rvadapter.holder

import com.chungld.uikit.databinding.ItemThreeBinding
import com.chungld.uikit.rvadapter.model.Three
import com.chungld.uipack.rvadapter.RvBaseHolder


class ThreeViewHolder(private val binding: ItemThreeBinding) : RvBaseHolder<Three>(binding) {
    override fun onBind(data: Three) {
        binding.data = data
    }
}