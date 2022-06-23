package com.chungld.uipack.button

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.drawable.*
import androidx.core.content.ContextCompat
import com.chungld.uipack.R
import com.chungld.uipack.common.TrpStroke
import com.chungld.uipack.common.createDrawableContent

fun createDrawableList(
    context: Context,
    style: TrpStroke.ThemeStyle,
    backgroundColor: Drawable,
    cornerRadius: Float?,
    strokeColor: ColorStateList?
): StateListDrawable {
    val content = StateListDrawable()
    content.addState(
        intArrayOf(android.R.attr.state_enabled),
        createDrawableContent(context, style, backgroundColor, cornerRadius, strokeColor)
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
