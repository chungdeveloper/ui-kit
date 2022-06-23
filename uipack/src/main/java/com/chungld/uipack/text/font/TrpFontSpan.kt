package com.chungld.uipack.text.font

import android.content.Context
import android.text.TextPaint
import android.text.style.CharacterStyle
import com.chungld.uipack.common.TrpText

/**
 * Apply [TrpFont] to spanned text
 */
class TrpFontSpan(private val font: TrpFont) : CharacterStyle() {

    constructor(
        context: Context,
        textStyle: Int = TrpText.BODY2,
        weight: TrpText.Weight = TrpText.Weight.REGULAR
    ) :
            this(getFont(context, textStyle, weight))

    override fun updateDrawState(tp: TextPaint) =
        font.applyTo(tp)
}
