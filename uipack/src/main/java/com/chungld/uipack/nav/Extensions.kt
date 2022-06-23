package com.chungld.uipack.nav

import androidx.annotation.IdRes
import com.google.android.material.bottomnavigation.BottomNavigationView
import android.view.ViewGroup

/**
 * Nhìn tên method là biết rồi không viết nữa mệt quá
 */
fun BottomNavigationView.getOrCreateBadgeAt(index: Int): TrpNavigationBadge? {
    val itemView =
        ((getChildAt(0) as ViewGroup).getChildAt(index)?.let { it as ViewGroup } ?: return null)
    return getOrCreateBadgeInItemView(itemView)
}

fun BottomNavigationView.getOrCreateBadgeById(@IdRes id: Int): TrpNavigationBadge? {
    val itemView = (getChildAt(0) as ViewGroup).findViewById<ViewGroup>(id) ?: return null
    return getOrCreateBadgeInItemView(itemView)
}

fun BottomNavigationView.getOrCreateBadgeInItemView(itemView: ViewGroup): TrpNavigationBadge? {
    if (itemView.getChildAt(itemView.childCount - 1) is TrpNavigationBadge) {
        return itemView.getChildAt(itemView.childCount - 1) as TrpNavigationBadge
    }
    val badge = TrpNavigationBadge(context)
    itemView.addView(badge)
    return badge
}