package com.chungld.uipack.text.font

import android.content.Context
import android.graphics.Paint
import android.graphics.Typeface
import android.os.Build
import android.util.TypedValue
import android.widget.TextView
import com.chungld.uipack.R
import com.chungld.uipack.common.TrpText

/**
 * Store font value properties: typeface, textSize, spacingLetter
 */
class TrpFont(private val font: Typeface, val textSize: Int, private val spacing: Float?) {

    /**
     * apply to default TextView of android
     */
    fun applyTo(textView: TextView) {
        textView.typeface = font
        textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize.toFloat())
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            spacing?.let { textView.letterSpacing = it }
        }
    }

    /**
     * apply to default spanned of android
     */
    fun applyTo(paint: Paint) {
        paint.typeface = font
        paint.textSize = textSize.toFloat()
        spacing?.let {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                paint.letterSpacing = it
            }
        }
    }
}

/**
 * Get font from textStyle, weight
 */
internal fun getFont(
    context: Context,
    @TrpText.Styles textStyle: Int,
    weight: TrpText.Weight
): TrpFont {
    val textStyleAttributes = context.obtainStyledAttributes(
        TrpText.getTextStyleId(context, textStyle, weight), R.styleable.TrpTextViewStyle
    )
    val fontSize = textStyleAttributes.getDimensionPixelSize(
        R.styleable.TrpTextViewStyle_android_textSize,
        context.resources.getDimensionPixelSize(R.dimen.trpSizeNormal)
    )
    val letterSpacing =
        textStyleAttributes.getFloat(R.styleable.TrpTextViewStyle_android_letterSpacing, -1f)
            .let { if (it == -1f) null else it }

    textStyleAttributes.recycle()

    return TrpFontFamilyResolver(context, weight)?.let {
        TrpFont(it, fontSize, letterSpacing)
    } ?: throw IllegalStateException("Trp font not configured correctly")
}