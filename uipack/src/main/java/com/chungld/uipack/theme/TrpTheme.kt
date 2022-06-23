package com.chungld.uipack.theme

import android.content.Context
import android.content.ContextWrapper
import androidx.annotation.AttrRes
import androidx.annotation.DimenRes
import android.util.AttributeSet
import android.util.TypedValue
import android.view.ContextThemeWrapper
import com.chungld.uipack.R

/**
 * Control theme in context and utils
 */
class TrpTheme {
    companion object {

        /**
         * wrap context with default themes
         */
        @JvmStatic
        fun wrapContextWithDefaults(context: Context) =
            com.chungld.uipack.theme.wrapContextWithDefaults(context)

        /**
         * apply default themes to context
         */
        @JvmStatic
        fun applyDefaultsToContext(context: Context) =
            com.chungld.uipack.theme.applyDefaultsToContext(context)
    }
}

internal fun createContextThemeWrapper(
    context: Context, attributeSet: AttributeSet?, styleAttr: Int
): Context {
    val a = context.obtainStyledAttributes(attributeSet, intArrayOf(styleAttr), 0, 0)
    val themeId = a.getResourceId(0, 0)
    a.recycle()
    return ContextThemeWrapper(context, themeId)
}

internal fun applyDefaultsToContext(context: Context) {
    context.theme?.applyStyle(R.style.TrpBaseThemeStyle, false)
    context.theme?.applyStyle(R.style.TrpFontStyle, false)
}

internal fun wrapContextWithDefaults(context: Context): Context {
    val copy = ContextWrapper(context)
    applyDefaultsToContext(copy)
    return copy
}

/**
 * get dimension from attributes
 */
internal fun resolveThemeDimen(
    context: Context, @AttrRes id: Int, @DimenRes fallback: Int = 0
): Int {
    val tv = TypedValue()
    if (context.theme.resolveAttribute(id, tv, true)) {
        return TypedValue.complexToDimensionPixelSize(tv.data, context.resources.displayMetrics)
    }
    return context.resources.getDimensionPixelSize(fallback)
}