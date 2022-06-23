package com.chungld.uipack.utils

import android.content.res.TypedArray
import android.graphics.Color
import android.os.Build
import android.text.Html
import android.text.Spannable
import android.text.SpannableString
import android.text.Spanned
import android.text.style.URLSpan
import com.chungld.uipack.text.span.SpanNoUnderLine
import kotlin.math.roundToInt

internal inline fun <R> TypedArray?.use(block: (TypedArray) -> R): R? {
    try {
        return this?.let(block)
    } finally {
        this?.recycle()
    }
}

fun getHtmlText(desc: String?): Spanned? = desc?.let {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
        Html.fromHtml(it, Html.FROM_HTML_MODE_COMPACT)
    else
        Html.fromHtml(it)
}

fun removeUnderLine(text: CharSequence): Spannable {
    val s: Spannable = SpannableString(text)
    val spans = s.getSpans(0, s.length, URLSpan::class.java)
    spans.forEachIndexed { index, span ->
        val start = s.getSpanStart(span)
        val end = s.getSpanEnd(span)
        s.removeSpan(span)
        spans[index] = SpanNoUnderLine(span.url)
        s.setSpan(spans[index], start, end, 0)
    }
    return s
}

fun manipulateColor(color: Int, factor: Float): Int {
    val a: Int = Color.alpha(color)
    val r = (Color.red(color) * factor).roundToInt()
    val g = (Color.green(color) * factor).roundToInt()
    val b = (Color.blue(color) * factor).roundToInt()
    return Color.argb(
        a,
        r.coerceAtMost(255),
        g.coerceAtMost(255),
        b.coerceAtMost(255)
    )
}