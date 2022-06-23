package com.chungld.uikit.rvadapter.holder

import com.chungld.uikit.databinding.ItemTwoBinding
import com.chungld.uikit.rvadapter.model.Two
import com.chungld.uipack.rvadapter.RvBaseHolder

class TwoViewHolder(val binding: ItemTwoBinding) : RvBaseHolder<Two>(binding) {
    override fun onBind(data: Two) {
        binding.data = data
    }
}