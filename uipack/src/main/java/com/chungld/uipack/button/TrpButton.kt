package com.chungld.uipack.button

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import androidx.annotation.Dimension
import androidx.core.content.ContextCompat
import androidx.core.widget.TextViewCompat
import androidx.appcompat.widget.AppCompatButton
import android.util.AttributeSet
import android.util.TypedValue
import com.chungld.uipack.R
import com.chungld.uipack.common.TrpStroke
import com.chungld.uipack.common.TrpStroke.Companion.ROUND
import com.chungld.uipack.common.TrpStroke.Companion.SMALL
import com.chungld.uipack.common.TrpText
import com.chungld.uipack.common.cornerMapping
import com.chungld.uipack.common.getBackgroundDrawable
import com.chungld.uipack.theme.wrapContextWithDefaults
import com.chungld.uipack.utils.use

/**
 * Extend from [AppCompatButton] with context theme default
 * #
 * Style default: [R.attr.TrpButtonStyle] with parent [R.style.TrpBaseButtonStyle]
 */
open class TrpButton @JvmOverloads constructor(
    context: Context,
    attributes: AttributeSet? = null,
    defStyleAttributes: Int = R.attr.TrpButtonStyle
) : AppCompatButton(wrapContextWithDefaults(context), attributes, defStyleAttributes) {

    @TrpText.Styles
    private var textStyleValue: Int = TrpText.BUTTON

    /**
     * We have all style from [TrpText.Styles] and default by [TrpText.BUTTON]
     */
    var textStyle
        get() = textStyleValue
        set(value) {
            textStyleValue = value
            updateView()
        }

    private var weightValue: TrpText.Weight = TrpText.Weight.DEFAULT

    /**
     * We have all weight from [TrpText] and default by [TrpText.Weight.DEFAULT]
     */
    var weight
        get() = weightValue
        set(value) {
            weightValue = value
            updateView()
        }

    private var textColour: ColorStateList? = null
    private var _isTextAllCaps: Boolean? = false

    /**
     * set text with upperCase
     */
    var isTextAllCaps
        get() = _isTextAllCaps
        set(value) {
            _isTextAllCaps = value
            updateView()
        }

    private var buttonHalfHeight = 0f

    var backgroundColor: Drawable? = null
    set(value) {
        if (value != null){
            field = value
            updateBackground()
        }
    }

    private var strokeColor: ColorStateList? = null

    fun <T> setTrpStrokeColor(value: T) {
        when (value) {
            is ColorStateList -> strokeColor = value
            is Int -> ColorStateList.valueOf(value)
        }
        updateBackground()
    }

    @TrpStroke.CornerStyle
    private var _cornerStyle = SMALL

    /**
     * We have all style from [TrpStroke.CornerStyle] and default by [TrpStroke.SMALL]
     */
    var cornerStyle
        get() = _cornerStyle
        set(@TrpStroke.CornerStyle value) {
            _cornerStyle = value
            updateView()
        }

    private var _themeStyle = TrpStroke.ThemeStyle.FILLED

    /**
     * We have all style from [TrpStroke.ThemeStyle] and default by [TrpStroke.ThemeStyle.FILLED]
     */
    var themeStyle
        get() = _themeStyle
        set(value) {
            _themeStyle = value
            updateView()
        }

    init {
        initialization(attributes, defStyleAttributes)
        updateView()
    }

    private fun updateView() {
        val textAppearance = TrpText.getTextStyleId(context, textStyleValue, weightValue)
        TextViewCompat.setTextAppearance(this, textAppearance)
        textColour?.let(::setTextColor)

        isTextAllCaps?.let {
            isAllCaps = it
        } ?: run {
            context.theme.obtainStyledAttributes(textAppearance, R.styleable.TrpButtonStyle).use {
                isAllCaps =
                    it.getBoolean(R.styleable.TrpButtonStyle_android_textAllCaps, false)
            }
        }

        updateBackground()
    }

    private fun updateBackground() {
        @Dimension
        val cornerRadius =
            when {
                cornerStyle == ROUND -> buttonHalfHeight
                cornerStyle < 0 -> cornerMapping[cornerStyle]?.let {
                    context.resources.getDimension(it)
                }
                else -> cornerStyle
            }

        backgroundColor?.mutate()?.let {
            background =
                getBackgroundDrawable(
                    context, createDrawableList(
                        context,
                        themeStyle,
                        it,
                        cornerRadius,
                        strokeColor
                    ), cornerRadius
                )
        }
    }

    private fun initialization(attributes: AttributeSet?, defStyleAttributes: Int) {
        context.theme.obtainStyledAttributes(
            attributes, R.styleable.TrpButton, defStyleAttributes, 0
        ).use {
            textStyleValue = it.getInt(R.styleable.TrpButton_trpTextStyle, TrpText.BUTTON)
            val weight = it.getInt(R.styleable.TrpButton_trpWeight, -1)
            weightValue =
                if (weight != -1) TrpText.Weight.values()[weight] else TrpText.Weight.DEFAULT
            if (it.hasValue(R.styleable.TrpButton_android_textColor)) {
                textColour = it.getColorStateList(R.styleable.TrpButton_android_textColor)
            }
            if (it.hasValue(R.styleable.TrpButton_trpCornerStyle)) {
                val typedValue = TypedValue()
                it.getValue(R.styleable.TrpButton_trpCornerStyle, typedValue)
                _cornerStyle = if (typedValue.type == TypedValue.TYPE_DIMENSION) it.getDimension(
                    R.styleable.TrpButton_trpCornerStyle,
                    0f
                ) else it.getFloat(R.styleable.TrpButton_trpCornerStyle, SMALL)
            }

            val themeStyle = it.getInt(R.styleable.TrpButton_trpThemeStyle, -1)
            _themeStyle =
                if (themeStyle != -1) TrpStroke.ThemeStyle.values()[themeStyle] else TrpStroke.ThemeStyle.FILLED
            _isTextAllCaps = if (it.hasValue(R.styleable.TrpButton_android_textAllCaps)) {
                it.getBoolean(R.styleable.TrpButton_android_textAllCaps, false)
            } else null


            backgroundColor = background?.let { background } ?: ColorDrawable(
                ContextCompat.getColor(
                    context,
                    R.color.trpColorPrimary
                )
            )

            strokeColor = if (it.hasValue(R.styleable.TrpButton_trpStrokeColor)) {
                it.getColorStateList(R.styleable.TrpButton_trpStrokeColor)
            } else {
                ContextCompat.getColorStateList(context, R.color.trpColorLine)
            }
        }
    }

    /**
     * get update corner value with round corner
     */
    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        buttonHalfHeight = measuredHeight / 2.0f
        if (_cornerStyle != ROUND) return
        updateBackground()
    }
}