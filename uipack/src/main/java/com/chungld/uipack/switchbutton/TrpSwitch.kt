package com.chungld.uipack.switchbutton

import android.content.Context
import android.content.res.ColorStateList
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import androidx.core.view.ViewCompat
import androidx.core.widget.TextViewCompat
import androidx.appcompat.widget.SwitchCompat
import android.util.AttributeSet
import android.widget.Switch
import com.chungld.uipack.R
import com.chungld.uipack.common.TrpText
import com.chungld.uipack.theme.wrapContextWithDefaults
import com.chungld.uipack.utils.manipulateColor
import com.chungld.uipack.utils.use

/**
 * Extend from [Switch]
 * #
 * Style default: [R.attr.TrpSwitchStyle] with parent [R.style.TrpSwitchBaseStyle]
 */
open class TrpSwitch @JvmOverloads constructor(
    context: Context,
    attributes: AttributeSet? = null,
    defStyleAttributes: Int = R.attr.TrpSwitchStyle
) : SwitchCompat(wrapContextWithDefaults(context), attributes, defStyleAttributes) {

    companion object {
        const val TRP_COLOR_ALPHA_DISABLE = 1.1f
        const val TRP_COLOR_ALPHA_TRACK = 1.2f
    }

    @TrpText.Styles
    private var textStyleValue: Int = TrpText.BUTTON
    var textStyle
        get() = textStyleValue
        set(value) {
            textStyleValue = value
            updateView()
        }

    private var weightValue: TrpText.Weight = TrpText.Weight.DEFAULT
    var weight
        get() = weightValue
        set(value) {
            weightValue = value
            updateView()
        }

    private var textColour: ColorStateList? = null
    private var _isTextAllCaps: Boolean = false
    var isTextAllCaps
        get() = _isTextAllCaps
        set(value) {
            _isTextAllCaps = value
            updateView()
        }

    @TrpText.Directions
    private var layoutDirectionValue: Int = TrpText.LTR
    var layoutDirections
        get() = layoutDirectionValue
        set(value) {
            layoutDirectionValue = value
            updateDirection()
        }

    private var activeColorValue: Int = TrpText.LTR
    var activeColor
        get() = activeColorValue
        set(value) {
            activeColorValue = value
            updateView()
        }

    private var deActiveColorValue: Int = TrpText.LTR
    var deActiveColor
        get() = deActiveColorValue
        set(value) {
            deActiveColorValue = value
            updateView()
        }

    init {
        initialization(context, attributes, defStyleAttributes)
        updateView()
    }

    private fun initialization(
        context: Context, attributes: AttributeSet?, defStyleAttributes: Int
    ) {
        context.theme.obtainStyledAttributes(
            attributes, R.styleable.TrpSwitch, defStyleAttributes, 0
        ).use {
            textStyleValue = it.getInt(R.styleable.TrpSwitch_trpTextStyle, TrpText.BODY2)
            val weight = it.getInt(R.styleable.TrpSwitch_trpWeight, -1)
            weightValue =
                if (weight != -1) TrpText.Weight.values()[weight] else TrpText.Weight.DEFAULT
            if (it.hasValue(R.styleable.TrpSwitch_android_textColor)) {
                textColour = it.getColorStateList(R.styleable.TrpSwitch_android_textColor)
            }
            layoutDirectionValue = it.getInt(
                R.styleable.TrpSwitch_trpLayoutDirection,
                TrpText.LTR
            )
            activeColorValue = it.getColor(
                R.styleable.TrpSwitch_trpIndicatorActive,
                ContextCompat.getColor(context, R.color.trpColorPrimary)
            )

            deActiveColorValue = it.getColor(
                R.styleable.TrpSwitch_trpIndicatorDisable,
                ContextCompat.getColor(context, R.color.trpColorDisable)
            )
        }
    }

    private fun updateView() {
        val textAppearance = TrpText.getTextStyleId(context, textStyleValue, weightValue)
        TextViewCompat.setTextAppearance(this, textAppearance)
        textColour?.let(::setTextColor)
        context.theme.obtainStyledAttributes(textAppearance, R.styleable.TrpSwitchStyle).use {
            isAllCaps =
                it.getBoolean(R.styleable.TrpSwitchStyle_android_textAllCaps, _isTextAllCaps)
        }

        trackDrawable = trackDrawable.mutate()
        thumbDrawable = thumbDrawable.mutate()
        DrawableCompat.setTintList(
            trackDrawable,
            getColorStateList(
                manipulateColor(activeColor, TRP_COLOR_ALPHA_TRACK),
                manipulateColor(deActiveColor, TRP_COLOR_ALPHA_TRACK)
            )
        )
        DrawableCompat.setTintList(thumbDrawable, getColorStateList(activeColor, deActiveColor))
        updateDirection()
    }

    private fun updateDirection() {
        ViewCompat.setLayoutDirection(this, layoutDirections)
    }

    private fun getColorStateList(checkedColor: Int, uncheckedColor: Int) =
        ColorStateList(
            arrayOf(
                intArrayOf(android.R.attr.state_checked, android.R.attr.state_enabled),
                intArrayOf(-android.R.attr.state_checked, android.R.attr.state_enabled),
                intArrayOf(android.R.attr.state_checked, -android.R.attr.state_enabled),
                intArrayOf(-android.R.attr.state_checked, -android.R.attr.state_enabled)
            ),
            intArrayOf(
                checkedColor,
                uncheckedColor,
                manipulateColor(checkedColor, TRP_COLOR_ALPHA_DISABLE),
                manipulateColor(uncheckedColor, TRP_COLOR_ALPHA_DISABLE)
            )
        )

}