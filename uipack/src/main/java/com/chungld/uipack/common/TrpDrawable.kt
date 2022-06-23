package com.chungld.uipack.common

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.drawable.*
import android.os.Build
import androidx.annotation.ColorInt
import androidx.annotation.Dimension
import androidx.annotation.IntDef
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import com.chungld.uipack.R

private var sizeMap = mapOf(
    TrpDrawable.SMALLER to R.dimen.trpSizeSmaller,
    TrpDrawable.SMALL to R.dimen.trpSizeSmall,
    TrpDrawable.NORMAL to R.dimen.trpSizeNormal,
    TrpDrawable.MEDIUM to R.dimen.trpSizeMedium,
    TrpDrawable.BIG to R.dimen.trpSizeBig,
    TrpDrawable.BIGGER to R.dimen.trpSizeBigger
)


open class TrpDrawable {
    /**
     * has values: [SMALLER], [SMALL], [NORMAL], [MEDIUM], [BIG], [BIGGER]
     */
    @IntDef(SMALLER, SMALL, NORMAL, MEDIUM, BIG, BIGGER)
    annotation class IconSize
    companion object {
        const val SMALLER = -1
        const val SMALL = -2
        const val NORMAL = -3
        const val MEDIUM = -4
        const val BIG = -5
        const val BIGGER = -6
    }
}

internal fun getIconSize(context: Context, size: Int): Int {
    return if (size < 0) getIconSizeEntry(context, size) else size
}

internal fun getIconSizeEntry(context: Context, @TrpDrawable.IconSize type: Int): Int {
    return sizeMap[type]?.let { context.resources.getDimensionPixelSize(it) } ?: 0
}

/**
 * getBackground Drawable from StateListDrawable and corner radius (RippleDrawable API >= 21)
 */
fun getBackgroundDrawable(
    context: Context,
    content: StateListDrawable,
    @Dimension cornerRadius: Float? = 0f
): Drawable {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
        RippleDrawable(
            getRippleColorStateList(context),
            content,
            corneredDrawable(Color.WHITE, cornerRadius)
        )
    else content
}

fun getRippleColorStateList(context: Context): ColorStateList {
    return getColorSelector(
        ContextCompat.getColor(context, R.color.trpRippleBackground),
        ContextCompat.getColor(context, R.color.trpRippleBackgroundDark)
    )
}

/**
 * Create gradient drawable from drawable,
 * apply with ColorDrawable and GradientDrawable
 */
fun createDrawableContent(
    context: Context,
    style: TrpStroke.ThemeStyle,
    backgroundDrawable: Drawable,
    cornerRadius: Float?,
    strokeColor: ColorStateList?
): Drawable {
    return when (backgroundDrawable) {
        is ColorDrawable -> {
            createGradientDrawable(
                context, GradientDrawable(
                    GradientDrawable.Orientation.TOP_BOTTOM,
                    intArrayOf(backgroundDrawable.color, backgroundDrawable.color)
                ),
                cornerRadius, style, strokeColor
            )
        }
        is GradientDrawable -> {
            createGradientDrawable(context, backgroundDrawable, cornerRadius, style, strokeColor)
        }
        else -> backgroundDrawable
    }
}

fun createGradientDrawable(
    context: Context,
    gradientDrawable: GradientDrawable,
    cornerRadius: Float?,
    style: TrpStroke.ThemeStyle,
    strokeColor: ColorStateList?
): Drawable {
    gradientDrawable.shape = GradientDrawable.RECTANGLE
    cornerRadius?.let { gradientDrawable.cornerRadius = it }

    if (style == TrpStroke.ThemeStyle.OUTLINED && strokeColor != null) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            gradientDrawable.setStroke(
                context.resources.getDimensionPixelSize(R.dimen.trpStrokeWidth),
                strokeColor
            )
        } else {
            gradientDrawable.setStroke(
                context.resources.getDimensionPixelSize(R.dimen.trpStrokeWidth),
                strokeColor.defaultColor
            )
        }
    }
    return gradientDrawable
}

internal fun getColorSelector(
    normalColor: Int,
    pressedColor: Int
) = ColorStateList(
    arrayOf(
        intArrayOf(android.R.attr.state_pressed),
        intArrayOf(android.R.attr.state_focused),
        intArrayOf(android.R.attr.state_activated),
        intArrayOf()
    ),
    intArrayOf(pressedColor, pressedColor, pressedColor, normalColor)
)

internal fun getColorEnableStateSelector(
    enableColor: Int,
    disableColor: Int
) = ColorStateList(
    arrayOf(
        intArrayOf(android.R.attr.state_enabled),
        intArrayOf(-android.R.attr.state_enabled),
        intArrayOf()
    ),
    intArrayOf(enableColor, disableColor, enableColor)
)

@RequiresApi(Build.VERSION_CODES.LOLLIPOP)
private fun corneredDrawable(
    @ColorInt color: Int,
    @Dimension cornerRadius: Float? = null,
    strokeColor: ColorStateList? = null,
    strokeWidth: Int? = null
): Drawable {
    val gd = GradientDrawable()
    gd.setColor(color)
    cornerRadius?.let { gd.cornerRadius = it }
    if (strokeWidth != null && strokeColor != null) {
        gd.setStroke(strokeWidth, strokeColor)
    } else {
        gd.setStroke(0, -1)
    }
    return gd
}