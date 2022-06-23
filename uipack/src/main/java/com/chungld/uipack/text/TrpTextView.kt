package com.chungld.uipack.text

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Paint
import android.graphics.drawable.Drawable
import android.os.Build
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.widget.TextViewCompat
import androidx.appcompat.widget.AppCompatTextView
import android.util.AttributeSet
import android.util.TypedValue
import com.chungld.uipack.R
import com.chungld.uipack.common.TrpDrawable
import com.chungld.uipack.common.TrpText
import com.chungld.uipack.common.TrpText.Companion.BODY2
import com.chungld.uipack.common.TrpText.Companion.LTR
import com.chungld.uipack.common.getIconSize
import com.chungld.uipack.theme.wrapContextWithDefaults
import com.chungld.uipack.utils.getHtmlText
import com.chungld.uipack.utils.removeUnderLine
import com.chungld.uipack.utils.use

/**
 * Extend from [AppCompatTextView]
 * #
 * Style default: [R.attr.TrpTextStyle] has no parent
 * #TextStyle define follow [https://material.io/design/typography/the-type-system.html#type-scale]
 */
open class TrpTextView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = R.attr.TrpTextStyle
) : AppCompatTextView(wrapContextWithDefaults(context), attrs, defStyleAttr) {

    private var textColour: ColorStateList? = null
    private var textColourHint: ColorStateList? = null
    private var linkUnderLineValue: Boolean? = true
    private var _textAllCaps: Boolean? = false
    private var _strikeThrough: Boolean? = false

    var trpStrikeThrough
        get() = _strikeThrough
        set(value) {
            _strikeThrough = value
            updateStrike()
        }

    private fun updateStrike() {
        paintFlags = if (_strikeThrough == true) {
            paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
        } else {
            paintFlags and Paint.STRIKE_THRU_TEXT_FLAG.inv()
        }
    }

    /**
     * enable html content with link underline: true(underline), false(non-underline)
     */
    var trpHtmlUnderLine
        get() = linkUnderLineValue
        set(value) {
            linkUnderLineValue = value
            setupView()
        }

    @TrpText.Styles
    private var textStyleValue: Int = BODY2

    /**
     * We have all style from [TrpText.Styles] and default by [TrpText.BODY2]
     */
    var trpTextStyle
        get() = textStyleValue
        set(value) {
            textStyleValue = value
            setupView()
        }

    private var _textColorLink: ColorStateList? = null
    var trpColorLink
        get() = _textColorLink
        set(value) {
            _textColorLink = value
            updateHtmlContent()
        }

    @TrpText.Directions
    private var layoutDirectionValue: Int = LTR

    /**
     * has values: [TrpText.Directions]
     */
    var trpLayoutDirection
        get() = layoutDirectionValue
        set(@TrpText.Directions value) {
            layoutDirectionValue = value
            updateViewDirection()
        }

    private var htmlTextValue: String? = null

    /**
     * set html content to [TrpTextView]
     */
    var trpHtmlText
        get() = htmlTextValue
        set(value) {
            htmlTextValue = value
            updateHtmlContent()
        }

    private fun updateHtmlContent() {
        trpHtmlText?.let {
            isAllCaps = false
            setText(getHtmlText(it), BufferType.SPANNABLE)
            movementMethod = null
            if (linkUnderLineValue!!) return
            text = removeUnderLine(text)
        }
    }


    private var weightValue: TrpText.Weight = TrpText.Weight.DEFAULT

    /**
     * We have all weight from [TrpText] and default by [TrpText.Weight.DEFAULT]
     */
    var trpWeight
        get() = weightValue
        set(value) {
            weightValue = value
            setupView()
        }

    fun setTrpTextColor(colors: ColorStateList?) {
        textColour = colors
        super.setTextColor(colors)
    }

    fun setTrpTextColor(color: Int) {
        textColour = ColorStateList.valueOf(color)
        super.setTextColor(color)
    }

    fun setTrpTextHintColor(colors: ColorStateList?) {
        textColourHint = colors
        super.setTextColor(colors)
    }

    fun setTrpTextHintColor(color: Int) {
        textColourHint = ColorStateList.valueOf(color)
        super.setTextColor(color)
    }

    /**
     * Resize DrawableCompound by [TrpDrawable.IconSize] or dimensions
     */
    @TrpDrawable.IconSize
    var trpIconSize: Int? = null
        set(@TrpDrawable.IconSize value) {
            field = value
            updateDrawableIconSize()
        }

    fun setIconCustomSize(value: Number?) {
        trpIconSize = value?.toInt()
    }

    fun getIconSizeValue(default: Int): Int {
        return trpIconSize?.let {
            getIconSize(context, it)
        } ?: getIconSize(context, default)
    }

    private fun updateDrawableIconSize() {
        trpIconSize?.let { it ->
            val size = getIconSize(context, it)
            val left = getCompoundDrawable(0)?.mutate()?.apply {
                val width =
                    (size * this.intrinsicWidth * 1.0f / this.intrinsicHeight).toInt()
                setBounds(0, 0, width, size)
            }
            val top = getCompoundDrawable(1)?.mutate()?.apply {
                val width =
                    (size * this.intrinsicWidth * 1.0f / this.intrinsicHeight).toInt()
                setBounds(0, 0, width, size)
            }
            val right =
                getCompoundDrawable(2)?.mutate()?.apply {
                    val width =
                        (size * this.intrinsicWidth * 1.0f / this.intrinsicHeight).toInt()
                    setBounds(0, 0, width, size)
                }
            val bottom =
                getCompoundDrawable(3)?.mutate()?.apply {
                    val width =
                        (size * this.intrinsicWidth * 1.0f / this.intrinsicHeight).toInt()
                    setBounds(0, 0, width, size)
                }
            TextViewCompat.setCompoundDrawablesRelative(this, left, top, right, bottom)
        }
    }

    private fun getCompoundDrawable(index: Int): Drawable? {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            compoundDrawablesRelative[index] ?: compoundDrawables[index]
        } else {
            compoundDrawables[index]
        }
    }

    init {
        initAttrs(attrs, defStyleAttr)
        setupView()
    }

    private fun initAttrs(attrs: AttributeSet?, defStyleAttr: Int) {
        context.theme
            .obtainStyledAttributes(attrs, R.styleable.TrpTextView, defStyleAttr, 0).use {
                textStyleValue = it.getInt(R.styleable.TrpTextView_trpTextStyle, BODY2)
                val weight = it.getInt(R.styleable.TrpTextView_trpWeight, -1)
                weightValue =
                    if (weight != -1) TrpText.Weight.values()[weight] else TrpText.Weight.DEFAULT
                if (it.hasValue(R.styleable.TrpTextView_android_textColor)) {
                    textColour = it.getColorStateList(R.styleable.TrpTextView_android_textColor)
                }
                if (it.hasValue(R.styleable.TrpTextView_android_textColorHint)) {
                    textColourHint =
                        it.getColorStateList(R.styleable.TrpTextView_android_textColorHint)
                }
                _textColorLink = if (it.hasValue(R.styleable.TrpTextView_trpColorLink)) {
                    it.getColorStateList(R.styleable.TrpTextView_trpColorLink)
                } else ContextCompat.getColorStateList(context, R.color.trpColorTextBaseLink)
                htmlTextValue = it.getString(R.styleable.TrpTextView_trpHtmlText)
                linkUnderLineValue = it.getBoolean(R.styleable.TrpTextView_trpHtmlUnderLine, true)
                layoutDirectionValue = it.getInt(R.styleable.TrpTextView_trpLayoutDirection, LTR)
                trpIconSize = if (it.hasValue(R.styleable.TrpTextView_trpIconSize)) {
                    val sizeValue = TypedValue()
                    it.getValue(R.styleable.TrpTextView_trpIconSize, sizeValue)
                    if (sizeValue.type == TypedValue.TYPE_DIMENSION) it.getDimensionPixelSize(
                        R.styleable.TrpTextView_trpIconSize,
                        0
                    ) else it.getInt(R.styleable.TrpTextView_trpIconSize, 0)
                } else null

                _textAllCaps =
                    if (it.hasValue(R.styleable.TrpTextView_android_textAllCaps)) it.getBoolean(
                        R.styleable.TrpTextView_android_textAllCaps,
                        false
                    ) else null

                _strikeThrough =
                    if (it.hasValue(R.styleable.TrpTextView_trpStrikeThrough)) it.getBoolean(
                        R.styleable.TrpTextView_trpStrikeThrough,
                        false
                    ) else null
            }
    }

    override fun setAllCaps(allCaps: Boolean) {
        super.setAllCaps(allCaps)
        _textAllCaps = allCaps
    }

    fun getTextAllCaps() = _textAllCaps

    /**
     * update all attrs to view
     */
    open fun setupView() {
        val textAppearance = TrpText.getTextStyleId(context, textStyleValue, weightValue)
        TextViewCompat.setTextAppearance(this, textAppearance)
        textColour?.let(::setTextColor)
        textColourHint?.let(::setHintTextColor)
        _textColorLink?.let(::setLinkTextColor)
        _textAllCaps?.let {
            isAllCaps = it
        }
        _strikeThrough?.let { updateStrike() }
        updateHtmlContent()
        updateViewDirection()
    }

    private fun updateViewDirection() {
        ViewCompat.setLayoutDirection(this, trpLayoutDirection)
    }
}
