package com.chungld.uipack.snackbar

import android.content.res.ColorStateList
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import androidx.annotation.ColorInt
import com.google.android.material.snackbar.Snackbar
import androidx.core.graphics.drawable.DrawableCompat
import android.text.SpannableStringBuilder
import android.text.Spanned
import com.chungld.uipack.R


internal fun Snackbar.setBackgroundColorCompat(@ColorInt color: Int) {
    var background = view.background
    if (background != null) {
        background = DrawableCompat.wrap(background.mutate())
        DrawableCompat.setTintList(background, ColorStateList.valueOf(color))
    } else {
        background = ColorDrawable(color)
    }
    view.background = background
}

internal fun TrpSnackBar.createIconDrawable(drawable: Drawable?, @ColorInt tint: Int) =
    drawable
        ?.mutate()
        ?.let { DrawableCompat.wrap(it) }
        ?.apply { DrawableCompat.setTint(this, tint) }
        ?.apply {
            val size = getContext().resources.getDimensionPixelSize(R.dimen.trpSizeNormal)
            setBounds(0, 0, size, size)
        }

internal fun customiseText(text: CharSequence, span: Any): CharSequence =
    SpannableStringBuilder(text).apply {
        setSpan(span, 0, length, Spanned.SPAN_INCLUSIVE_INCLUSIVE)
    }