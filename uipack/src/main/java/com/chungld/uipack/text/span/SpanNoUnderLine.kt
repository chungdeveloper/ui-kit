package com.chungld.uipack.text.span

import android.text.TextPaint
import android.text.style.URLSpan

class SpanNoUnderLine(url: String?) : URLSpan(url) {

    override fun updateDrawState(ds: TextPaint) {
        super.updateDrawState(ds)
        ds.isUnderlineText = false
    }
}