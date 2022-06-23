package com.chungld.uikit.rvadapter.holder

import com.chungld.uikit.databinding.ItemFourBinding
import com.chungld.uikit.rvadapter.model.Four
import com.chungld.uipack.rvadapter.RvBaseHolder

class FourViewHolder(private val binding: ItemFourBinding) : RvBaseHolder<Four>(binding) {
    override fun onBind(data: Four) {
        binding.four = data
    }
}