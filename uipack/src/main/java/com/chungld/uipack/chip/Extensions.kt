package com.chungld.uipack.chip

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.StateListDrawable
import androidx.core.content.ContextCompat
import com.chungld.uipack.R
import com.chungld.uipack.common.TrpStroke
import com.chungld.uipack.common.createDrawableContent


fun createDrawableList(
    context: Context,
    style: TrpStroke.ThemeStyle,
    backgroundColor: Int,
    selectedColor: Int,
    cornerRadius: Float?,
    strokeColor: ColorStateList?
): StateListDrawable {
    val content = StateListDrawable()
    content.addState(
        intArrayOf(android.R.attr.state_enabled, android.R.attr.state_selected),
        createDrawableContent(
            context,
            style,
            ColorDrawable(selectedColor),
            cornerRadius,
            strokeColor
        )
    )

    content.addState(
        intArrayOf(android.R.attr.state_enabled),
        createDrawableContent(
            context,
            style,
            ColorDrawable(backgroundColor),
            cornerRadius,
            strokeColor
        )
    )

    content.addState(
        intArrayOf(-android.R.attr.state_enabled),
        createDrawableContent(
            context,
            style,
            ColorDrawable(ContextCompat.getColor(context, R.color.trpColorDisable)),
            cornerRadius,
            strokeColor
        )
    )
    return content
}