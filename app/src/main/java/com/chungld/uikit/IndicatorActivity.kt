package com.chungld.uikit

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.chungld.uikit.databinding.ActivityIndicatorBinding
import com.chungld.uikit.databinding.ItemViewPageFakeBinding
import kotlin.random.Random

class IndicatorActivity : BaseActivity<ActivityIndicatorBinding>() {

    override fun getLayoutId(): Int {
        return R.layout.activity_indicator
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding.vp1.adapter = Adapter()
        mBinding.tvIndicator.setupWithViewPager(mBinding.vp1)
        mBinding.tvIndicator.updatePageSize(mBinding.vp1)
    }

    private class Adapter : RecyclerView.Adapter<ViewHolder>() {

        private val items = mutableListOf(
            "Lorem ${Random.nextInt()}",
            "Lorem ${Random.nextInt()}",
            "Lorem ${Random.nextInt()}",
            "Lorem ${Random.nextInt()}",
            "Lorem ${Random.nextInt()}",
        )

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ViewHolder(
            ItemViewPageFakeBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            holder.bindData(items[position])
        }

        override fun getItemCount() = items.size

    }

    private class ViewHolder(val binding: ItemViewPageFakeBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bindData(model: String) {
            binding.model = model
        }

    }

}
