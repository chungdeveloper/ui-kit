package com.chungld.uipack.card

import android.content.Context
import androidx.cardview.widget.CardView
import android.util.AttributeSet
import android.util.TypedValue
import com.chungld.uipack.R
import com.chungld.uipack.common.TrpElevation.Companion.ELEVATION_NORMAL
import com.chungld.uipack.common.TrpStroke
import com.chungld.uipack.common.elevationMap
import com.chungld.uipack.theme.wrapContextWithDefaults
import com.chungld.uipack.utils.use

/**
 * values: PADDING_NONE, PADDING_NORMAL
 */
val paddingMap = mapOf(
    TrpCard.PADDING_NONE to R.dimen.trpPaddingNone,
    TrpCard.PADDING_NORMAL to R.dimen.trpPaddingNormal
)

/**
 * Extend from [CardView]
 * #
 * Style default: [R.attr.TrpCardStyle] has no parent
 */
open class TrpCard @JvmOverloads constructor(
    context: Context,
    attributes: AttributeSet? = null,
    defStyleAttributes: Int = R.attr.TrpCardStyle
) : CardView(wrapContextWithDefaults(context), attributes, defStyleAttributes) {

    companion object {
        const val PADDING_NONE = 0f
        const val PADDING_NORMAL = -1f
    }

    private var buttonHalfHeight = 0f
    private var cornerCard = TrpStroke.SMALL

    /**
     * values from [TrpStroke.CornerStyle] or customize dimensions
     */
    var trpCornerStyle
        get() = cornerCard
        set(value) {
            cornerCard = value
            updateCardRadius()
        }

    private var contentPadding: Float? = PADDING_NONE

    /**
     * values from [paddingMap] or customize dimensions
     */
    var trpContentPadding
        get() = contentPadding
        set(value) {
            contentPadding = value
            updateContentPadding()
        }

    private var cardElevationValue: Float? = ELEVATION_NORMAL

    /**
     * values from [elevationMap] or customize dimensions
     */
    var trpCardElevation
        get() = cardElevationValue
        set(value) {
            cardElevationValue = value
            updateElevation()
        }

    private fun updateElevation() {
        cardElevationValue?.let {
            this.cardElevation = if (it < 0) elevationMap[it]?.let { res ->
                context.resources.getDimension(res)
            } ?: 0f else it
        }
    }

    private fun updateContentPadding() {
        contentPadding?.let {
            val padding = if (it < 0) paddingMap[it]?.let { res ->
                context.resources.getDimension(res)
            } ?: 0f else it
            padding.toInt().let { value ->
                setContentPadding(value, value, value, value)
            }
        }
    }

    init {
        initialization(context, attributes, defStyleAttributes)
        updateView()
    }

    private fun initialization(context: Context, attrs: AttributeSet?, defStyleAttr: Int) {
        context.theme.obtainStyledAttributes(attrs, R.styleable.TrpCard, defStyleAttr, 0).use {
            if (it.hasValue(R.styleable.TrpCard_trpCornerStyle)) {
                val typedValue = TypedValue()
                it.getValue(R.styleable.TrpCard_trpCornerStyle, typedValue)
                cornerCard = if (typedValue.type == TypedValue.TYPE_DIMENSION) it.getDimension(
                    R.styleable.TrpCard_trpCornerStyle,
                    0f
                ) else it.getFloat(R.styleable.TrpCard_trpCornerStyle, TrpStroke.SMALL)
            }

            contentPadding = if (it.hasValue(R.styleable.TrpCard_trpContentPadding)) {
                val typedValue = TypedValue()
                it.getValue(R.styleable.TrpCard_trpContentPadding, typedValue)
                if (typedValue.type == TypedValue.TYPE_DIMENSION) it.getDimension(
                    R.styleable.TrpCard_trpContentPadding,
                    0f
                ) else it.getFloat(R.styleable.TrpCard_trpContentPadding, PADDING_NONE)
            } else null

            cardElevationValue = if (it.hasValue(R.styleable.TrpCard_trpElevation)) {
                val typedValue = TypedValue()
                it.getValue(R.styleable.TrpCard_trpElevation, typedValue)
                if (typedValue.type == TypedValue.TYPE_DIMENSION) it.getDimension(
                    R.styleable.TrpCard_trpElevation,
                    0f
                ) else it.getFloat(R.styleable.TrpCard_trpElevation, ELEVATION_NORMAL)
            } else ELEVATION_NORMAL
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        buttonHalfHeight = (measuredHeight / 2.0f)
        if (cornerCard != TrpStroke.ROUND) return
        updateCardRadius()
    }

    private fun updateCardRadius() {
        TrpStroke.getCornerRadius(context, cornerCard, buttonHalfHeight)?.let {
            radius = it
        }
    }

    private fun updateView() {
        updateCardRadius()
        updateContentPadding()
        updateElevation()
    }
}