package com.chungld.uipack.text.font

import android.content.Context
import android.graphics.Typeface
import android.util.TypedValue
import com.chungld.uipack.R
import com.chungld.uipack.common.TrpText

internal object TrpFontFamilyResolver {

    private val fontAttributes = intArrayOf(
        R.attr.trpFontFamilyLight,
        R.attr.trpFontFamilyRegular,
        R.attr.trpFontFamilyMedium
    )

    private fun getFontResources(context: Context): List<Typeface?> {
        return fontAttributes.map {
            val outValue = TypedValue()
            val resolved = context.theme.resolveAttribute(it, outValue, true)

            if (resolved && outValue.resourceId == 0) {
                Typeface.create(outValue.string.toString(), Typeface.NORMAL)
            } else if (resolved) {
                TrpFontCache[outValue.resourceId, context]
            } else {
                null
            }
        }
    }

    operator fun invoke(context: Context, fontWeight: TrpText.Weight): Typeface? {
        val (fontLight, fontRegular, fontMedium) = getFontResources(context)
        return when (fontWeight) {
            TrpText.Weight.LIGHT -> fontLight
            TrpText.Weight.REGULAR -> fontRegular
            TrpText.Weight.MEDIUM -> fontMedium
            else -> fontRegular
        }
    }
}
