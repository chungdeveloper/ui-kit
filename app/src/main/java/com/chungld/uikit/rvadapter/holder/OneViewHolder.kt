package com.chungld.uikit.rvadapter.holder

import com.chungld.uikit.databinding.*
import com.chungld.uikit.rvadapter.model.*
import com.chungld.uipack.rvadapter.RvBaseHolder

class OneViewHolder(val binding: ItemOneBinding) : RvBaseHolder<One>(binding) {
    override fun onBind(data: One) {
        binding.data = data
    }
}





