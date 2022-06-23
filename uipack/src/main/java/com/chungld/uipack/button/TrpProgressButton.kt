package com.chungld.uipack.button

import android.animation.LayoutTransition
import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.os.Build
import android.util.AttributeSet
import android.util.TypedValue
import android.view.Gravity
import android.view.View
import android.widget.LinearLayout
import android.widget.ProgressBar
import androidx.annotation.ColorRes
import androidx.annotation.Dimension
import androidx.core.content.ContextCompat
import androidx.core.widget.TextViewCompat
import com.chungld.uipack.R
import com.chungld.uipack.common.TrpStroke
import com.chungld.uipack.common.TrpText
import com.chungld.uipack.common.cornerMapping
import com.chungld.uipack.common.getBackgroundDrawable
import com.chungld.uipack.spinner.TrpSpinner
import com.chungld.uipack.spinner.TrpSpinner.ProgressPosition
import com.chungld.uipack.text.TrpTextView
import com.chungld.uipack.theme.wrapContextWithDefaults
import com.chungld.uipack.utils.use
import kotlin.math.roundToInt


class TrpProgressButton @JvmOverloads constructor(
    context: Context,
    attributes: AttributeSet? = null,
    defStyleAttributes: Int = R.attr.TrpButtonProcessStyle
) : LinearLayout(wrapContextWithDefaults(context), attributes, defStyleAttributes) {

    val trpTextView by lazy { TrpTextView(context).apply { setBackgroundColor(Color.TRANSPARENT) } }

    val trpProgress by lazy { ProgressBar(context).apply { setBackgroundColor(Color.TRANSPARENT) } }

    var backgroundColor: Drawable? = null

    private var buttonHalfHeight = 0f

    private var textColour: ColorStateList? = null

    private var _isTextAllCaps: Boolean? = false

    private var _themeStyle = TrpStroke.ThemeStyle.FILLED

    private var weightValue: TrpText.Weight = TrpText.Weight.DEFAULT

    private var strokeColor: ColorStateList? = null

    var trpLoading = false
        set(value) {
            field = value
            updateView()
            isClickable = !value
            isEnabled = !value
        }

    @ColorRes
    var trpProcessColor: Int? = null
        set(value) {
            field = value
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                trpProgress.indeterminateTintList =
                    trpProcessColor?.let { ColorStateList.valueOf(it) }
            }
        }

    @TrpText.Styles
    private var textStyleValue: Int = TrpText.BUTTON

    @TrpStroke.CornerStyle
    private var _cornerStyle = TrpStroke.SMALL

    var trpProgressPadding = 0
        set(value) {
            field = value
            updateTextPosition()
        }

    /**
     * We have all style from [TrpStroke.ThemeStyle] and default by [TrpStroke.ThemeStyle.FILLED]
     */
    var themeStyle
        get() = _themeStyle
        set(value) {
            _themeStyle = value
            updateView()
        }

    /**
     * Text position with progress has values [ProgressPosition]
     */
    @ProgressPosition
    var trpTextPosition = TrpSpinner.PRG_POS_LEFT
        set(@ProgressPosition value) {
            field = value
            updateTextPosition()
        }

    /**
     * set text with upperCase
     */
    var isTextAllCaps
        get() = _isTextAllCaps
        set(value) {
            _isTextAllCaps = value
            updateView()
        }

    var text: String? = ""
        set(value) {
            field = value
            updateView()
        }

    /**
     * We have all weight from [TrpText] and default by [TrpText.Weight.DEFAULT]
     */
    var weight
        get() = weightValue
        set(value) {
            weightValue = value
            updateView()
        }

    /**
     * We have all style from [TrpStroke.CornerStyle] and default by [TrpStroke.SMALL]
     */
    var cornerStyle
        get() = _cornerStyle
        set(@TrpStroke.CornerStyle value) {
            _cornerStyle = value
            updateView()
        }


    fun <T> setTrpStrokeColor(value: T) {
        when (value) {
            is ColorStateList -> strokeColor = value
            is Int -> ColorStateList.valueOf(value)
        }
        updateBackground()
    }

    init {
        initialization(attributes, defStyleAttributes)
        updateView()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)

        val lp = trpProgress.layoutParams as LayoutParams
        lp.width = (measuredHeight / 1.6).roundToInt()
        lp.height = (measuredHeight / 1.6).roundToInt()
        lp.gravity = Gravity.CENTER
        trpProgress.layoutParams = lp
    }

    private fun initialization(attributes: AttributeSet?, defStyleAttributes: Int) {
        updateTextPosition()
        gravity = Gravity.CENTER
        orientation = HORIZONTAL
        layoutTransition = LayoutTransition()

        context.theme.obtainStyledAttributes(
            attributes, R.styleable.TrpProgressButton, defStyleAttributes, 0
        ).use {
            textStyleValue = it.getInt(R.styleable.TrpProgressButton_trpTextStyle, TrpText.BUTTON)
            val weight = it.getInt(R.styleable.TrpProgressButton_trpWeight, -1)
            weightValue =
                if (weight != -1) TrpText.Weight.values()[weight] else TrpText.Weight.DEFAULT
            if (it.hasValue(R.styleable.TrpProgressButton_android_textColor)) {
                textColour = it.getColorStateList(R.styleable.TrpProgressButton_android_textColor)
            }
            if (it.hasValue(R.styleable.TrpProgressButton_trpCornerStyle)) {
                val typedValue = TypedValue()
                it.getValue(R.styleable.TrpProgressButton_trpCornerStyle, typedValue)
                _cornerStyle = if (typedValue.type == TypedValue.TYPE_DIMENSION) it.getDimension(
                    R.styleable.TrpProgressButton_trpCornerStyle,
                    0f
                ) else it.getFloat(R.styleable.TrpProgressButton_trpCornerStyle, TrpStroke.SMALL)
            }

            val themeStyle = it.getInt(R.styleable.TrpProgressButton_trpThemeStyle, -1)
            _themeStyle =
                if (themeStyle != -1) TrpStroke.ThemeStyle.values()[themeStyle] else TrpStroke.ThemeStyle.FILLED
            _isTextAllCaps = if (it.hasValue(R.styleable.TrpProgressButton_android_textAllCaps)) {
                it.getBoolean(R.styleable.TrpProgressButton_android_textAllCaps, false)
            } else null

            backgroundColor = background?.let { background } ?: ColorDrawable(
                ContextCompat.getColor(
                    context,
                    R.color.trpColorPrimary
                )
            )

            strokeColor = if (it.hasValue(R.styleable.TrpProgressButton_trpStrokeColor)) {
                it.getColorStateList(R.styleable.TrpProgressButton_trpStrokeColor)
            } else {
                ContextCompat.getColorStateList(context, R.color.trpColorLine)
            }

            text = it.getString(R.styleable.TrpProgressButton_android_text)

            trpProgressPadding =
                it.getDimensionPixelOffset(R.styleable.TrpProgressButton_trpProgressPadding, 8)

            trpTextPosition =
                it.getInt(R.styleable.TrpProgressButton_trpTextPosition, TrpSpinner.PRG_POS_RIGHT)

            trpLoading = it.getBoolean(R.styleable.TrpProgressButton_trpLoading, false)

            trpProcessColor = it.getColor(
                R.styleable.TrpProgressButton_trpProcessColor,
                ContextCompat.getColor(context, R.color.trpColorPrimary)
            )
        }
    }

    private fun updateView() {
        val textAppearance = TrpText.getTextStyleId(context, textStyleValue, weightValue)
        TextViewCompat.setTextAppearance(trpTextView, textAppearance)
        textColour?.let {
            trpTextView.setTrpTextColor(it)
        }

        isTextAllCaps?.let {
            trpTextView.isAllCaps = it
        } ?: run {
            context.theme.obtainStyledAttributes(textAppearance, R.styleable.TrpButtonStyle).use {
                trpTextView.isAllCaps =
                    it.getBoolean(R.styleable.TrpButtonStyle_android_textAllCaps, false)
            }
        }

        text?.let {
            trpTextView.text = it
        }

        updateBackground()
    }


    private fun updateBackground() {
        @Dimension
        val cornerRadius =
            when {
                cornerStyle == TrpStroke.ROUND -> buttonHalfHeight
                cornerStyle < 0 -> cornerMapping[cornerStyle]?.let {
                    context.resources.getDimension(it)
                }
                else -> cornerStyle
            }

        backgroundColor?.mutate()?.let {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
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

        trpProgress.visibility = if (trpLoading) View.VISIBLE else View.GONE
    }

    private fun updateTextPosition() {
        removeAllViews()
        when (trpTextPosition) {
            TrpSpinner.PRG_POS_TOP -> {
                addView(trpTextView)
                addView(trpProgress)
                setMarginProgress(0, trpProgressPadding, 0, 0)
            }

            TrpSpinner.PRG_POS_LEFT -> {
                addView(trpTextView)
                addView(trpProgress)
                setMarginProgress(trpProgressPadding, 0, 0, 0)
            }

            TrpSpinner.PRG_POS_BOTTOM -> {
                addView(trpProgress)
                addView(trpTextView)
                setMarginProgress(0, 0, 0, trpProgressPadding)
            }

            TrpSpinner.PRG_POS_RIGHT -> {
                addView(trpProgress)
                addView(trpTextView)
                setMarginProgress(0, 0, trpProgressPadding, 0)
            }
        }
        orientation =
            if (trpTextPosition == TrpSpinner.PRG_POS_TOP || trpTextPosition == TrpSpinner.PRG_POS_BOTTOM)
                VERTICAL else HORIZONTAL
    }

    private fun setMarginProgress(left: Int, top: Int, right: Int, bottom: Int) {
        val lp = trpProgress.layoutParams as LayoutParams
        lp.setMargins(left, top, right, bottom)
        trpProgress.layoutParams = lp
    }
}