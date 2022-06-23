package com.chungld.uipack.checkbox

import android.content.Context
import android.content.res.ColorStateList
import androidx.core.view.ViewCompat
import androidx.core.widget.CompoundButtonCompat
import androidx.core.widget.TextViewCompat
import androidx.appcompat.widget.AppCompatCheckBox
import android.util.AttributeSet
import com.chungld.uipack.R
import com.chungld.uipack.common.TrpText
import com.chungld.uipack.theme.wrapContextWithDefaults
import com.chungld.uipack.utils.use

/**
 * Extend from [AppCompatCheckBox]
 * #
 * Style default: [R.attr.TrpCheckboxStyle] with parent [R.style.TrpCheckboxBaseStyle]
 */
class TrpCheckbox @JvmOverloads constructor(
    context: Context,
    attributes: AttributeSet? = null,
    defStyleAttributes: Int = R.attr.TrpCheckboxStyle//R.attr.checkboxStyle//
) : AppCompatCheckBox(wrapContextWithDefaults(context), attributes, defStyleAttributes) {

    @TrpText.Styles
    private var textStyleValue: Int = TrpText.BODY2

    @Suppress("unused")
            /**
             * We have all style from [TrpText.Styles] and default by [TrpText.BODY2]
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
    @Suppress("unused")
    var weight
        get() = weightValue
        set(value) {
            weightValue = value
            updateView()
        }

    private var textColour: ColorStateList? = null
    private var _isTextAllCaps: Boolean = false

    @Suppress("unused")
    var isTextAllCaps
        get() = _isTextAllCaps
        set(value) {
            _isTextAllCaps = value
            updateView()
        }

    private var _trpCheckTint: ColorStateList? = null
    var trpCheckTint
        get() = _trpCheckTint
        set(value) {
            _trpCheckTint = value
            updateButtonTint()
        }

    private fun updateButtonTint() {
        if (_trpCheckTint == null) return
        CompoundButtonCompat.setButtonTintList(this, _trpCheckTint)
    }

    @TrpText.Directions
    private var layoutDirectionValue: Int = TrpText.LTR

    /**
     * has values: [TrpText.Directions]
     */
    @Suppress("unused")
    var layoutDirections
        get() = layoutDirectionValue
        set(value) {
            layoutDirectionValue = value
            updateDirection()
        }

    init {
        initialization(context, attributes, defStyleAttributes)
        updateView()
    }

    private fun updateView() {
        val textAppearance = TrpText.getTextStyleId(context, textStyleValue, weightValue)
        TextViewCompat.setTextAppearance(this, textAppearance)
        textColour?.let(::setTextColor)
        //todo sẽ phải chỉnh lại vị trí của phần allCaps
        context.theme.obtainStyledAttributes(textAppearance, R.styleable.TrpCheckboxStyle).use {
            isAllCaps =
                it.getBoolean(R.styleable.TrpCheckboxStyle_android_textAllCaps, _isTextAllCaps)
        }
        updateDirection()
        updateButtonTint()
    }

    private fun updateDirection() {
        ViewCompat.setLayoutDirection(this, layoutDirections)
    }

    private fun initialization(
        context: Context, attributes: AttributeSet?, defStyleAttributes: Int
    ) {
        context.theme.obtainStyledAttributes(
            attributes, R.styleable.TrpCheckbox, defStyleAttributes, 0
        ).use {
            textStyleValue = it.getInt(R.styleable.TrpCheckbox_trpTextStyle, TrpText.BODY2)
            val weight = it.getInt(R.styleable.TrpCheckbox_trpWeight, -1)
            weightValue =
                if (weight != -1) TrpText.Weight.values()[weight] else TrpText.Weight.DEFAULT
            if (it.hasValue(R.styleable.TrpCheckbox_android_textColor)) {
                textColour = it.getColorStateList(R.styleable.TrpCheckbox_android_textColor)
            }
            if (it.hasValue(R.styleable.TrpCheckbox_trpCheckTint)) {
                _trpCheckTint = it.getColorStateList(R.styleable.TrpCheckbox_trpCheckTint)
            }
            _isTextAllCaps = it.getBoolean(R.styleable.TrpCheckbox_android_textAllCaps, false)
            layoutDirectionValue = it.getInt(
                R.styleable.TrpCheckbox_trpLayoutDirection,
                TrpText.LTR
            )
        }
    }

}