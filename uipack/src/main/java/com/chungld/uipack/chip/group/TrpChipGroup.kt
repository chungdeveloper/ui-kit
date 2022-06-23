package com.chungld.uipack.chip.group

import android.content.Context
import com.google.android.material.chip.ChipGroup
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import com.chungld.uipack.R
import com.chungld.uipack.chip.OnCheckedChangeListener
import com.chungld.uipack.chip.TrpChip
import com.chungld.uipack.theme.wrapContextWithDefaults

/**
 * Extend from [ChipGroup]
 * #
 * Style default: [R.attr.TrpChipGroupStyle] has no parent
 */
class TrpChipGroup @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttrs: Int = R.attr.TrpChipGroupStyle
) : ChipGroup(
    wrapContextWithDefaults(context), attrs, defStyleAttrs
), OnCheckedChangeListener {

    private var onChipCheckedChangeListener: ((TrpChip, Boolean) -> Unit)? = null
    var protectedChangeListener = false
    var currentCheckedChip: TrpChip? = null
    private var _alwaysSelectOne = false

    override fun addView(child: View?, index: Int, params: ViewGroup.LayoutParams?) {
        super.addView(child, index, params)
        if (child !is TrpChip) return
        child.alwaysSelectedByUser = alwaysSelectOne
        setCurrentChipWithChild(child)
        child.addOnCheckedChangeListener(this)
    }

    var alwaysSelectOne
        get() = _alwaysSelectOne
        set(value) {
            _alwaysSelectOne = value
            repeat(childCount) {
                (getChildAt(it) as? TrpChip)?.alwaysSelectedByUser = value
            }
        }

    private fun setCurrentChipWithChild(child: TrpChip) {
        currentCheckedChip =
            if (child.trpSelected) child else if (child == currentCheckedChip && !_alwaysSelectOne) null else currentCheckedChip
    }

    fun setOnChipCheckedChangeListener(listener: (TrpChip, Boolean) -> Unit): TrpChipGroup {
        onChipCheckedChangeListener = listener
        return this
    }

    /**
     * get current chip selected in Group
     * chip should be child of group
     */
    fun getChipSelected(): MutableList<TrpChip> {
        protectedChangeListener = true
        val list = mutableListOf<TrpChip>()
        if (isSingleSelection) {
            currentCheckedChip?.let { list.add(it) }
            return list
        }
        for (i in 0..childCount) {
            val view = getChildAt(i)
            if (view is TrpChip && view.trpSelected) {
                list.add(view)
            }
        }
        protectedChangeListener = false
        return list
    }

    /**
     * get current chip unSelected in Group
     * chip should be child of group
     */
    fun getChipUnSelected(): MutableList<TrpChip> {
        protectedChangeListener = true
        val list = mutableListOf<TrpChip>()
        for (i in 0..childCount) {
            val view = getChildAt(i)
            if (view is TrpChip && !view.trpSelected) {
                list.add(view)
            }
        }
        protectedChangeListener = false
        return list
    }

    override fun onChange(view: TrpChip, selected: Boolean) {
        if (protectedChangeListener) {
            view.updateStateWithoutNotify(!selected)
            return
        }

        if (isSingleSelection && selected && currentCheckedChip != null && currentCheckedChip != view) {
            currentCheckedChip?.trpSelected = false
        }
        setCurrentChipWithChild(view)
        onChipCheckedChangeListener?.invoke(view, selected)
    }

    override fun clearCheck() {
        protectedChangeListener = true
        for (i in 0..childCount) {
            val view = getChildAt(i)
            if (view is TrpChip && view.trpSelected) {
                view.trpSelected = false
            }
        }
        protectedChangeListener = false
    }
}