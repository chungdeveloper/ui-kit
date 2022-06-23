package com.chungld.uipack.snackbar

import android.app.Activity
import android.content.Context
import android.graphics.drawable.Drawable
import androidx.annotation.*
import com.google.android.material.snackbar.BaseTransientBottomBar
import com.google.android.material.snackbar.Snackbar
import androidx.fragment.app.Fragment
import androidx.core.content.ContextCompat
import androidx.core.os.ConfigurationCompat
import androidx.core.view.GravityCompat
import androidx.core.widget.TextViewCompat
import android.text.SpannableStringBuilder
import android.text.style.ImageSpan
import android.util.TypedValue
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.chungld.uipack.R
import com.chungld.uipack.common.TrpText
import com.chungld.uipack.text.font.TrpFontSpan
import com.chungld.uipack.text.font.getFont

/**
 * Show snackBar from bottom
 */
open class TrpSnackBar private constructor(
    private val context: Context,
    view: View,
    var text: CharSequence = "",
    duration: Int,
    @ColorInt private val textColor: Int,
    @ColorInt actionColor: Int,
    @ColorInt backgroundColor: Int
) {
    private val titleFontSpan = TrpFontSpan(context, TrpText.BODY1, TrpText.Weight.MEDIUM)
    private val textFontSpan = TrpFontSpan(context, TrpText.BODY2, TrpText.Weight.REGULAR)

    private val callbacks = ArrayList<BaseTransientBottomBar.BaseCallback<TrpSnackBar>>()
    private val snackBar = Snackbar.make(view, text, duration).apply {
        setBackgroundColorCompat(backgroundColor)
        addCallback(object : BaseTransientBottomBar.BaseCallback<Snackbar>() {
            override fun onShown(transientBottomBar: Snackbar?) {
                callbacks.forEach { it.onShown(this@TrpSnackBar) }
            }

            override fun onDismissed(transientBottomBar: Snackbar?, event: Int) {
                callbacks.forEach { it.onDismissed(this@TrpSnackBar, event) }
            }
        })
    }

    fun getContext() = context

    private val textView = snackBar.view.findViewById<TextView>(R.id.snackbar_text).apply {
        gravity = GravityCompat.START or Gravity.CENTER_VERTICAL
        compoundDrawablePadding = context.resources.getDimensionPixelSize(R.dimen.trpIconPadding)
        setTextColor(textColor)
    }

    private val actionView = snackBar.view.findViewById<TextView>(R.id.snackbar_action).apply {
        setTextColor(actionColor)
        getFont(context, TrpText.BUTTON, TrpText.Weight.MEDIUM).applyTo(this)
        transformationMethod = null
    }

    private var title: CharSequence? = null

    fun setTitle(title: CharSequence): TrpSnackBar = apply {
        this.title = title.toString()
            .toUpperCase(ConfigurationCompat.getLocales(context.resources.configuration).get(0))
        updateTitleIfShown(isShown)
    }

    fun setText(message: CharSequence): TrpSnackBar = apply {
        this.text = message
        updateTitleIfShown(isShown)
    }

    fun setIcon(icon: Drawable?): TrpSnackBar = apply {
        TextViewCompat.setCompoundDrawablesRelative(
            textView,
            createIconDrawable(icon, textColor), null, null, null
        )
    }

    fun setIcon(@DrawableRes icon: Int): TrpSnackBar =
        setIcon(ContextCompat.getDrawable(context, icon))

    fun setText(@StringRes resId: Int): TrpSnackBar =
        setText(context.getString(resId))

    fun setAction(text: CharSequence, listener: View.OnClickListener): TrpSnackBar = apply {
        setActionInternal(text = text, icon = null, callback = listener)
    }

    fun setAction(@AnyRes resId: Int, listener: View.OnClickListener): TrpSnackBar = apply {
        if (context.resources.getResourceTypeName(resId) == "drawable") {
            return setAction(ContextCompat.getDrawable(context, resId)!!, listener)
        }
        return setAction(context.getText(resId), listener)
    }

    fun setAction(icon: Drawable, listener: View.OnClickListener): TrpSnackBar = apply {
        setActionInternal(text = null, icon = icon, callback = listener)
    }

    val duration: Int
        get() = snackBar.duration

    fun setDuration(duration: Int): TrpSnackBar = apply {
        snackBar.duration = duration
    }

    val isShown: Boolean
        get() = snackBar.isShown

    fun show() =
        snackBar
            .show()
            .also { updateTitleIfShown(true) }

    fun dismiss() =
        snackBar.dismiss()

    fun addCallback(callback: BaseTransientBottomBar.BaseCallback<TrpSnackBar>?): TrpSnackBar =
        apply {
            callback?.let(callbacks::add)
        }

    fun removeCallback(callback: BaseTransientBottomBar.BaseCallback<TrpSnackBar>?): TrpSnackBar =
        apply {
            callbacks.remove(callback)
        }

    val behaviour: BaseTransientBottomBar.Behavior?
        get() = snackBar.behavior

    fun setBehaviour(behavior: BaseTransientBottomBar.Behavior?): TrpSnackBar = apply {
        snackBar.behavior = behavior
    }

    val rawSnackbar: Snackbar =
        snackBar

    private fun updateTitleIfShown(isShown: Boolean) {
        if (isShown) {
            val ssb = SpannableStringBuilder()
            title?.let {
                ssb.append(customiseText(it, titleFontSpan))
                ssb.append(" ")
            }
            text.let {
                ssb.append(customiseText(it, textFontSpan))
            }

            snackBar.setText(ssb)
        }
    }

    private fun setActionInternal(
        text: CharSequence?,
        icon: Drawable?,
        callback: View.OnClickListener
    ) {
        actionView.gravity = when {
            icon != null -> Gravity.CENTER
            else -> GravityCompat.START or Gravity.CENTER_VERTICAL
        }
        snackBar.setAction(
            when {
                !text.isNullOrEmpty() -> text
                icon != null -> customiseText(" ", ImageSpan(createIconDrawable(icon, textColor)!!))
                else -> ""
            }, callback
        )
    }


    @IntDef(TRP_LENGTH_SHORT, TRP_LENGTH_LONG, TRP_LENGTH_INDEFINITE)
    annotation class SnackDuration
    companion object {
        const val TRP_LENGTH_SHORT = Snackbar.LENGTH_SHORT
        const val TRP_LENGTH_LONG = Snackbar.LENGTH_LONG
        const val TRP_LENGTH_INDEFINITE = Snackbar.LENGTH_INDEFINITE

        @Suppress("MemberVisibilityCanBePrivate")
        fun make(container: View, content: String, duration: Int = TRP_LENGTH_SHORT): TrpSnackBar {
            val context = container.context

            @ColorInt var textColor = ContextCompat.getColor(context, R.color.trpColorTextSnackBar)
            @ColorInt var actionColor = ContextCompat.getColor(context, R.color.trpColorAction)
            @ColorInt var backgroundColor =
                ContextCompat.getColor(context, R.color.trpBackgroundColor)

            val outValue = TypedValue()
            context.theme.resolveAttribute(R.attr.TrpSnackStyle, outValue, true)

            val attrs =
                context.obtainStyledAttributes(outValue.resourceId, R.styleable.TrpSnackBarStyle)
            textColor = attrs.getColor(R.styleable.TrpSnackBarStyle_android_textColor, textColor)
            actionColor = attrs.getColor(R.styleable.TrpSnackBarStyle_trpActionColor, actionColor)
            backgroundColor =
                attrs.getColor(R.styleable.TrpSnackBarStyle_trpBackgroundColor, backgroundColor)
            attrs.recycle()

            return TrpSnackBar(
                context, container, content, duration, textColor, actionColor, backgroundColor
            )
        }

        @Suppress("unused")
        fun make(
            container: View,
            @StringRes content: Int,
            @SnackDuration duration: Int = TRP_LENGTH_SHORT
        ) = make(container, container.context.getString(content), duration)

        fun make(
            container: Activity,
            @StringRes content: Int,
            @SnackDuration duration: Int = TRP_LENGTH_SHORT
        ) = make(container, container.getString(content), duration)

        fun make(
            container: Fragment,
            @StringRes content: Int,
            @SnackDuration duration: Int = TRP_LENGTH_SHORT
        ) = make(container, container.getString(content), duration)

        fun make(
            container: Activity,
            content: String,
            @SnackDuration duration: Int = TRP_LENGTH_SHORT
        ) = make(
            (container.findViewById(android.R.id.content) as ViewGroup).getChildAt(0),
            content,
            duration
        )

        fun make(
            container: Fragment,
            content: String,
            @SnackDuration duration: Int = TRP_LENGTH_SHORT
        ) = container.view?.let { make(it, content, duration) }
    }
}