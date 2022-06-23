package com.chungld.uipack.rating.indicator

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.os.Build
import androidx.core.content.ContextCompat
import android.util.AttributeSet
import android.util.TypedValue
import android.view.Gravity
import android.view.View
import android.widget.LinearLayout
import com.chungld.uipack.R
import com.chungld.uipack.button.createDrawableList
import com.chungld.uipack.common.TrpStroke
import com.chungld.uipack.common.cornerMapping
import com.chungld.uipack.common.getBackgroundDrawable
import com.chungld.uipack.text.TrpTextView
import com.chungld.uipack.theme.wrapContextWithDefaults
import com.chungld.uipack.utils.use

/**
 * Extend from [TrpTextView] to show rating
 * #
 * Style default: [R.attr.TrpRatingPointStyle] has no parent
 */
class TrpRatingPoint @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = R.attr.TrpRatingPointStyle
) : TrpTextView(wrapContextWithDefaults(context), attrs, defStyleAttr) {

    private var strokeColor: ColorStateList? = null
    private var ratingHalfHeight: Float = 0f
    private var _background: Drawable? = null
    private var _padding = 0
    private var hasUpdateMeasure: Boolean = true

    /**
     * setBackground rating
     */
    var trpBackground
        get() = _background
        set(value) {
            _background = value
            updateBackground()
        }

    private var _cornerStyle = TrpStroke.SMALL

    /**
     * values corner from [TrpStroke.CornerStyle]
     */
    var cornerStyle
        get() = _cornerStyle
        set(@TrpStroke.CornerStyle value) {
            _cornerStyle = value
            updateView()
        }

    private var _isRound = false

    /**
     * View can't scale and always show with ratio 1:1
     */
    var trpIsRound
        get() = _isRound
        set(value) {
            _isRound = value
            updateView()
        }

    init {
        initialization(attrs, defStyleAttr)
        updateView()
    }

    override fun setupView() {
        super.setupView()
        updatePadding()
    }

    private fun updatePadding() {
        if (_padding == 0) {
            (textSize / 3).toInt().let { setPadding(it, it, it, it) }
        } else {
            setPadding(_padding, _padding, _padding, _padding)
        }
    }

    override fun setText(text: CharSequence?, type: BufferType?) {
        super.setText(text, type)
        layoutParams?.apply {
            width = LinearLayout.LayoutParams.WRAP_CONTENT
            height = LinearLayout.LayoutParams.WRAP_CONTENT
        }?.let {
            layoutParams = it
            hasUpdateMeasure = true
            measure(0,0)
            measure(0,0)
            hasUpdateMeasure = true
        }
        updateRoundRating()
    }

    private fun initialization(attrs: AttributeSet?, defStyleAttr: Int) {
        gravity = Gravity.CENTER
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            textAlignment = View.TEXT_ALIGNMENT_CENTER
        }
        context.theme.obtainStyledAttributes(attrs, R.styleable.TrpRatingPoint, defStyleAttr, 0)
            .use {
                _background = background?.let { background } ?: ColorDrawable(
                    ContextCompat.getColor(context, R.color.trpColorPrimary)
                )

                strokeColor = if (it.hasValue(R.styleable.TrpRatingPoint_trpStrokeColor)) {
                    it.getColorStateList(R.styleable.TrpRatingPoint_trpStrokeColor)
                } else {
                    ContextCompat.getColorStateList(context, R.color.trpColorLine)
                }

                if (it.hasValue(R.styleable.TrpRatingPoint_trpCornerStyle)) {
                    val typedValue = TypedValue()
                    it.getValue(R.styleable.TrpRatingPoint_trpCornerStyle, typedValue)
                    _cornerStyle =
                        if (typedValue.type == TypedValue.TYPE_DIMENSION) it.getDimension(
                            R.styleable.TrpRatingPoint_trpCornerStyle, 0f
                        ) else it.getFloat(
                            R.styleable.TrpRatingPoint_trpCornerStyle, TrpStroke.ROUND
                        )
                }

                _isRound = it.getBoolean(R.styleable.TrpRatingPoint_trpIsRound, false)
                _padding = it.getDimensionPixelSize(R.styleable.TrpRatingPoint_android_padding, 0)
            }
    }

    private fun updateView() {
        updatePadding()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        if (!hasUpdateMeasure) return
        ratingHalfHeight = (measuredHeight / 2.0f)
        minWidth = measuredHeight
        updateBackground()
        if (!trpIsRound) return
        updateRoundRating()
    }

    private fun updateRoundRating() {
        if (layoutParams == null || (layoutParams.width == measuredWidth && layoutParams.height == measuredWidth)) return
        layoutParams.width = measuredWidth
        layoutParams.height = measuredWidth
        applyRatingLayoutParams()
    }

    private fun applyRatingLayoutParams() {
        setSingleLine()
        layoutParams = layoutParams
        val nonPadding = (textSize / 3f).toInt()
        setPadding(nonPadding, nonPadding, nonPadding, nonPadding)
    }

    private fun updateBackground() {
        val cornerRadius = when {
            cornerStyle == TrpStroke.ROUND -> ratingHalfHeight
            cornerStyle < 0 -> cornerMapping[cornerStyle]?.let {
                context.resources.getDimension(it)
            }
            else -> cornerStyle
        }

        _background?.mutate()?.let {
            background =
                getBackgroundDrawable(
                    context, createDrawableList(
                        context,
                        TrpStroke.ThemeStyle.FILLED,
                        it,
                        cornerRadius,
                        strokeColor
                    ), cornerRadius
                )
        }
    }
}