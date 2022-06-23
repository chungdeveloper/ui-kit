package com.chungld.uipack.chip

import androidx.annotation.RestrictTo
import androidx.databinding.BindingAdapter
import androidx.databinding.InverseBindingListener
import androidx.databinding.InverseBindingMethod
import androidx.databinding.InverseBindingMethods
import com.chungld.uipack.R

@RestrictTo(RestrictTo.Scope.LIBRARY)
@InverseBindingMethods(
    InverseBindingMethod(
        type = TrpChip::class,
        attribute = "app:trpSelected",
        method = "isTrpSelected",
        event = "trpSelectedAttrChanged"
    )
)
class TrpChipDataBinding {

    companion object {

        @BindingAdapter("trpSelected", "trpSelectedAttrChanged", requireAll = false)
        @JvmStatic
        fun setTrpSelected(
            chip: TrpChip,
            checked: Boolean?,
            trpSelectedListener: InverseBindingListener?
        ) {
            chip.trpSelected = checked ?: false
            (chip.getTag(R.id.chip_listener) as? OnCheckedChangeListener)?.let {
                chip.removeOnCheckedChangeListener(it)
            }
            val inverseChange = object : OnCheckedChangeListener {
                override fun onChange(view: TrpChip, selected: Boolean) {
                    trpSelectedListener?.onChange()
                }
            }
            chip.addOnCheckedChangeListener(inverseChange)
            chip.setTag(R.id.chip_listener, inverseChange)
        }
    }
}