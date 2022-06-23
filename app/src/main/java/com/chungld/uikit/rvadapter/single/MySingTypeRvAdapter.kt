package com.chungld.uikit.rvadapter.single

import androidx.databinding.ViewDataBinding
import com.chungld.uikit.R
import com.chungld.uikit.databinding.ItemRvSingleBinding
import com.chungld.uipack.rvadapter.RvBaseHolder
import com.chungld.uipack.rvadapter.RvSingleTypeAdapter

class MySingTypeRvAdapter : RvSingleTypeAdapter<String, MySingTypeRvAdapter.SingleHolder>() {

    override fun getLayoutId(): Int = R.layout.item_rv_single

    override fun createViewHolder(viewDataBinding: ViewDataBinding): SingleHolder {
        return SingleHolder(viewDataBinding as ItemRvSingleBinding)
    }

    inner class SingleHolder(val binding: ItemRvSingleBinding) : RvBaseHolder<String>(binding) {

        override fun onBind(data: String) {
            binding.str = data
        }
    }
}