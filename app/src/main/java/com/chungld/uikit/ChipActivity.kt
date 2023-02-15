package com.chungld.uikit

import android.os.Bundle
import com.chungld.uikit.databinding.ActivityChipBinding
import com.chungld.uipack.chip.group.TrpChipGroup

class ChipActivity : BaseActivity<ActivityChipBinding>() {

    val viewModel = ChipActivityViewModel()

    override fun getLayoutId(): Int {
        return R.layout.activity_chip
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mBinding.setVariable(BR.viewModel, viewModel)

        (findViewById<TrpChipGroup>(R.id.chip)).alwaysSelectOne = true
//        cb1.setOnCheckedChangeListener { buttonView, isChecked ->
//            buttonView.isChecked = !buttonView.isChecked
//        }
//
//        chip1.addOnCheckedChangeListener(
//            object : OnCheckedChangeListener {
//                override fun onChange(view: TrpChip, selected: Boolean) {
////                    view.trpSelected = !view.trpSelected
//                }
//            })

//        mBinding.chip1.text = "Chung\nLe"
//
//        mBinding.chip1.setOnClickListener {
//            viewModel.selected
//        }

//        chipGroup.onChipCheckedChangeListener = { trpChip: TrpChip, isSelected: Boolean ->
//            Log.d("ChipCheckedChange", "$trpChip status $isSelected")
//        }
    }
}
