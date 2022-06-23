package com.chungld.uipack.chip

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.graphics.drawable.RippleDrawable
import android.os.Build
import androidx.annotation.Dimension
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import androidx.core.view.ViewCompat
import androidx.core.widget.TextViewCompat
import android.util.AttributeSet
import android.util.TypedValue
import android.view.Gravity
import com.chungld.uipack.R
import com.chungld.uipack.common.*
import com.chungld.uipack.common.TrpDrawable.Companion.NORMAL
import com.chungld.uipack.text.TrpTextView
import com.chungld.uipack.theme.wrapContextWithDefaults
import com.chungld.uipack.utils.use

/**
 * Extend from [TrpTextView]
 * #
 * Style default: [R.attr.TrpChipStyle] has no parent
 */


open class TrpChip @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttrs: Int = R.attr.TrpChipStyle
) : TrpTextView(
    wrapContextWithDefaults(context), attrs, defStyleAttrs
) {
    private var strokeColor: ColorStateList? = null
    private var chipPadding = context.resources.getDimensionPixelSize(R.dimen.trpIconPadding)
    private var _trpBackgroundColor: Int? = null
    private var chipHeight: Float? = 0f
    private var _trpBackgroundSelected = 0
    private var _gravity: Int? = Gravity.CENTER
    private var onClickedListener: OnClickListener? = null

    var alwaysSelectedByUser = false

    private var chipElevationValue: Float? = TrpElevation.ELEVATION_NONE

    private var _trpIconPadding = chipPadding
    var trpIconPadding
        get() = _trpIconPadding
        set(value) {
            _trpIconPadding = value
            updateIconPadding()
        }

    private fun updateIconPadding() {
        this.compoundDrawablePadding = trpIconPadding
    }

    /**
     * values from [elevationMap] or customize dimensions
     */
    var trpChipElevation
        get() = chipElevationValue
        set(value) {
            chipElevationValue = value
            updateElevation()
        }

    private fun updateElevation() {
        chipElevationValue?.let {
            ViewCompat.setElevation(this, if (it < 0) elevationMap[it]?.let { res ->
                context.resources.getDimension(res)
            } ?: 0f else it)
        }
    }

    var trpBackgroundColor
        get() = _trpBackgroundColor
        set(value) {
            _trpBackgroundColor = value
            updateBackground()
        }

    var trpBackgroundSelectedColor
        get() = _trpBackgroundSelected
        set(value) {
            _trpBackgroundSelected = value
            updateBackground()
        }

    /**
     * stroke colorStateList
     */
    @Suppress("unused")
    var trpStrokeColor
        get() = strokeColor
        set(value) {
            strokeColor = value
            updateView()
        }

    /**
     * can selectable(true, false)
     */
    var trpSelectable: Boolean? = false
        set(value) {
            field = value
            updateSelectable()
        }

    /**
     * icon by drawable
     */
    var trpChipIcon: Drawable? = null
        set(value) {
            field = value?.mutate()?.let {
                DrawableCompat.wrap(it)
            }?.apply {
                val size = getIconSizeValue(NORMAL)
                val width =
                    (size * this.intrinsicWidth * 1.0f / this.intrinsicHeight).toInt()
                setBounds(0, 0, width, size)
            }?.also {
                DrawableCompat.setTintList(it, textColors)
                TextViewCompat.setCompoundDrawablesRelative(this, it, null, null, null)
                updateIconPadding()
            }
        }

    override fun setTextColor(colors: ColorStateList?) {
        super.setTrpTextColor(colors)
        trpChipIcon?.let { DrawableCompat.setTintList(it, colors) }
    }

    private var _themeStyle = TrpStroke.ThemeStyle.FILLED

    /**
     * values from [TrpStroke.ThemeStyle] default by FILLED
     */
    var themeStyle
        get() = _themeStyle
        set(value) {
            _themeStyle = value
            updateView()
        }

    private var onCheckedChangesListener = mutableListOf<OnCheckedChangeListener>()

    private var _trpSingleLine = true
    var trpSingleLine
        get() = _trpSingleLine
        set(value) {
            _trpSingleLine = value
            isSingleLine = value
        }

    private var _trpSelected = false
    var trpSelected
        get() = _trpSelected
        set(value) {
            if (value == _trpSelected) return
            _trpSelected = value
            isSelected = value
            notifyAllListener()
        }

    fun isTrpSelected() = _trpSelected

    private fun notifyAllListener() {
        onCheckedChangesListener.forEach {
            it.onChange(this, isSelected)
        }
    }

    fun updateStateWithoutNotify(isSelected: Boolean) {
        if (_trpSelected == isSelected) return
        _trpSelected = isSelected
        this.isSelected = isSelected
    }

    @TrpStroke.CornerStyle
    private var _cornerStyle = TrpStroke.ROUND

    /**
     * We have all style from [TrpStroke.CornerStyle] and default by [TrpStroke.SMALL]
     */
    var cornerStyle
        get() = _cornerStyle
        set(@TrpStroke.CornerStyle value) {
            _cornerStyle = value
            updateView()
        }

    init {
        initialization(context, attrs, defStyleAttrs)
        updateView()
        measure(0, 0)
    }

    private fun initialization(context: Context, attrs: AttributeSet?, defStyleAttrs: Int) {
//        setSingleLine()
        super.setOnClickListener {
            if (alwaysSelectedByUser && isSelected) return@setOnClickListener
            trpSelected = !isSelected
            onClickedListener?.onClick(it)
        }
//        setTextColor(getTextColorsDef())
        context.theme.obtainStyledAttributes(attrs, R.styleable.TrpChip, defStyleAttrs, 0)
            .use {
                _trpBackgroundColor = it.getColor(
                    R.styleable.TrpChip_trpBackgroundColor,
                    ContextCompat.getColor(context, R.color.trpColorPrimary)
                )
                trpSelectable = it.getBoolean(R.styleable.TrpChip_trpSelectable, false)
                _trpBackgroundSelected = it.getColor(
                    R.styleable.TrpChip_trpBackgroundSelectedColor,
                    ContextCompat.getColor(context, R.color.trpColorPrimaryDark)
                )
                trpSelected = it.getBoolean(R.styleable.TrpChip_trpSelected, false)
                trpSingleLine = it.getBoolean(R.styleable.TrpChip_android_singleLine, true)
                strokeColor = if (it.hasValue(R.styleable.TrpChip_trpStrokeColor)) {
                    it.getColorStateList(R.styleable.TrpChip_trpStrokeColor)
                } else {
                    ContextCompat.getColorStateList(context, R.color.trpColorLine)
                }

                if (it.hasValue(R.styleable.TrpChip_android_textColor)) {
                    setTextColor(it.getColorStateList(R.styleable.TrpChip_android_textColor))
                }

                val themeStyle = it.getInt(R.styleable.TrpChip_trpThemeStyle, -1)
                _themeStyle =
                    if (themeStyle != -1) TrpStroke.ThemeStyle.values()[themeStyle] else TrpStroke.ThemeStyle.FILLED
                trpChipIcon = it.getDrawable(R.styleable.TrpChip_trpChipIcon)
                _gravity = if (it.hasValue(R.styleable.TrpChip_android_gravity)) {
                    it.getInt(R.styleable.TrpChip_android_gravity, Gravity.CENTER)
                } else Gravity.CENTER

                _trpIconPadding =
                    it.getDimensionPixelSize(R.styleable.TrpChip_trpIconPadding, chipPadding)

                if (it.hasValue(R.styleable.TrpChip_trpCornerStyle)) {
                    val typedValue = TypedValue()
                    it.getValue(R.styleable.TrpChip_trpCornerStyle, typedValue)
                    _cornerStyle =
                        if (typedValue.type == TypedValue.TYPE_DIMENSION) it.getDimension(
                            R.styleable.TrpChip_trpCornerStyle,
                            0f
                        ) else it.getFloat(R.styleable.TrpChip_trpCornerStyle, TrpStroke.ROUND)
                }
                chipElevationValue = if (it.hasValue(R.styleable.TrpChip_trpElevation)) {
                    val typedValue = TypedValue()
                    it.getValue(R.styleable.TrpChip_trpElevation, typedValue)
                    if (typedValue.type == TypedValue.TYPE_DIMENSION) it.getDimension(
                        R.styleable.TrpChip_trpElevation,
                        TrpElevation.ELEVATION_NONE
                    ) else it.getFloat(
                        R.styleable.TrpChip_trpElevation,
                        TrpElevation.ELEVATION_NONE
                    )
                } else TrpElevation.ELEVATION_NONE
            }
    }

    private fun updateChipPadding(defLeft: Int, defTop: Int, defRight: Int, defBottom: Int) {
        val left = if (paddingLeft == 0) defLeft else paddingLeft
        val top = if (paddingTop == 0) defTop else paddingTop
        val right = if (paddingRight == 0) defRight else paddingRight
        val bottom = if (paddingBottom == 0) defBottom else paddingBottom
        setPadding(left, top, right, bottom)
    }

    fun addOnCheckedChangeListener(onCheckedChangeListener: OnCheckedChangeListener) {
        onCheckedChangesListener.add(onCheckedChangeListener)
    }

    fun removeOnCheckedChangeListener(onCheckedChangeListener: OnCheckedChangeListener) {
        onCheckedChangesListener.remove(onCheckedChangeListener)
    }

    fun removeAllCheckedChangeListener() {
        onCheckedChangesListener.clear()
    }

    override fun setOnClickListener(l: OnClickListener?) {
        onClickedListener = l
    }

    private fun updateView() {
        _gravity?.let {
            gravity = it
        }
        updateBackground()
        updateSelectable()
        updateElevation()
        updateIconPadding()
    }

    override fun setGravity(gravity: Int) {
        _gravity = gravity
        super.setGravity(gravity)
    }

    private fun updateSelectable() {
        trpSelectable?.let {
            isClickable = it
            isFocusable = it
            updateRippleState(it)
        }
    }

    private fun updateRippleState(enable: Boolean) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) return
        if (background !is RippleDrawable) return
        (background as RippleDrawable).setColor(
            if (enable) getRippleColorStateList(context) else ColorStateList.valueOf(Color.TRANSPARENT)
        )
    }

    private fun updateBackground() {
        @Dimension
        val cornerRadius =
            when {
                cornerStyle == TrpStroke.ROUND -> chipHeight
                cornerStyle < 0 -> cornerMapping[cornerStyle]?.let {
                    context.resources.getDimension(it)
                }
                else -> cornerStyle
            }

        _trpBackgroundColor?.let {
            background =
                getBackgroundDrawable(
                    context,
                    createDrawableList(
                        context, themeStyle, it, _trpBackgroundSelected, cornerRadius,
                        strokeColor
                    ), cornerRadius
                )
            updateRippleState(trpSelectable ?: false)
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        chipHeight = measuredHeight / 2.0f
        chipHeight?.let { updateChipPadding(it.toInt(), chipPadding, it.toInt(), chipPadding) }
        if (_cornerStyle != TrpStroke.ROUND) return
        updateBackground()
    }
}