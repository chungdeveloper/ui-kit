package com.chungld.uipack.nav

import android.content.Context
import androidx.annotation.IdRes
import com.google.android.material.bottomnavigation.BottomNavigationView
import android.util.AttributeSet
import android.view.ViewGroup
import com.chungld.uipack.R

/**
 *  extend from [BottomNavigationView]
 *  with style [R.attr.TrpBottomNavigationViewStyle] has no parent
 */
open class TrpBottomNavigationView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defAttrs: Int = R.attr.TrpBottomNavigationViewStyle
) : BottomNavigationView(context, attrs, defAttrs) {

    fun getOrCreateBadgeAt(index: Int): TrpNavigationBadge? {
        val itemView =
            ((getChildAt(0) as ViewGroup).getChildAt(index)?.let { it as ViewGroup } ?: return null)
        return getOrCreateBadgeInItemView(itemView)
    }

    fun getOrCreateBadgeById(@IdRes id: Int): TrpNavigationBadge? {
        val itemView = (getChildAt(0) as ViewGroup).findViewById<ViewGroup>(id) ?: return null
        return getOrCreateBadgeInItemView(itemView)
    }

    fun getOrCreateBadgeInItemView(itemView: ViewGroup): TrpNavigationBadge? {
        if (itemView.getChildAt(itemView.childCount - 1) is TrpNavigationBadge) {
            return itemView.getChildAt(itemView.childCount - 1) as TrpNavigationBadge
        }
        val badge = TrpNavigationBadge(context)
//        val iconView = getIconView(itemView)
        itemView.addView(badge)
        return badge
    }

//    private fun getIconView(itemView: ViewGroup): ImageView? {
//        for (i in 0..itemView.childCount) {
//            if (itemView.getChildAt(i) is ImageView) return itemView.getChildAt(i) as ImageView
//        }
//        return null
//    }
}