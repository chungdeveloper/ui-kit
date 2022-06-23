package com.chungld.uipack.common

import android.content.Context
import androidx.annotation.Dimension
import com.chungld.uipack.R

val cornerMapping = mapOf(
    TrpStroke.RECTANGLE to R.dimen.trp_corner_rectangle,
    TrpStroke.ROUND to -1,
    TrpStroke.SMALL to R.dimen.trp_corner_small,
    TrpStroke.REGULAR to R.dimen.trp_corner_regular,
    TrpStroke.MEDIUM to R.dimen.trp_corner_medium
)

open class TrpStroke {

    /**
     * has [RECTANGLE], [ROUND], [SMALL], [REGULAR], [MEDIUM]
     */
    annotation class CornerStyle
    companion object {
        const val RECTANGLE = 0f
        const val ROUND = -1f
        const val SMALL = -2f
        const val REGULAR = -3f
        const val MEDIUM = -4f

        @Dimension
        @JvmStatic
        fun getCornerRadius(
            context: Context, cornerStyle: Float = SMALL, buttonHeight: Float = 0f
        ) = getCornerRadiusValue(context, cornerStyle, buttonHeight)
    }

    enum class ThemeStyle {
        FILLED, OUTLINED
    }
}

@Dimension
internal fun getCornerRadiusValue(
    context: Context,
    cornerStyle: Float = 0f,
    buttonHeight: Float = 0f
): Float? {
    return when {
        cornerStyle == TrpStroke.ROUND -> buttonHeight
        cornerStyle < 0 -> cornerMapping[cornerStyle]?.let {
            context.resources.getDimension(it)
        }
        else -> cornerStyle
    }
}