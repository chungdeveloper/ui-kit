package com.chungld.uipack.recycler.holder

import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView

abstract class TrpViewHolder(viewDataBinding: ViewDataBinding) :
    RecyclerView.ViewHolder(viewDataBinding.root) {

    abstract fun bindData(model: Any)

}