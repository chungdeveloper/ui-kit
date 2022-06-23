package com.chungld.uipack.icon

import android.content.Context
import android.graphics.Canvas
import android.graphics.Path
import android.graphics.RectF
import android.util.AttributeSet
import android.util.TypedValue
import androidx.appcompat.widget.AppCompatImageView
import com.chungld.uipack.R
import com.chungld.uipack.common.TrpDrawable
import com.chungld.uipack.common.TrpDrawable.Companion.NORMAL
import com.chungld.uipack.common.getIconSize
import com.chungld.uipack.theme.wrapContextWithDefaults
import com.chungld.uipack.utils.use

/**
 * Extend from [AppCompatImageView]
 * #
 * Style default: [R.attr.TrpIconStyle] has no parent
 */
open class TrpIcon @JvmOverloads constructor(
    context: Context,
    attributes: AttributeSet? = null,
    defStyleAttributes: Int = R.attr.TrpIconStyle
) : AppCompatImageView(wrapContextWithDefaults(context), attributes, defStyleAttributes) {
    private var iconSize: Int? = NORMAL
    private var clipPath: Path? = null
    private val _cornerRadius = arrayListOf(0f, 0f, 0f, 0f)

    /**
     * values from [TrpDrawable.IconSize] and customize dimensions
     */
    @Suppress("unused")
    @TrpDrawable.IconSize
    var trpIconSize
        get() = iconSize
        set(@TrpDrawable.IconSize value) {
            iconSize = value
            updateSize()
        }

    private var viewRatio: String? = null

    /**
     * value is a string with format "w:h". Ex: "1:1" or "h:1:1" or "w:1:1"
     */
    @Suppress("unused")
    var trpRatio
        get() = viewRatio
        set(value) {
            viewRatio = value
            updateRatio()
        }

    private fun updateRatio() {
        viewRatio?.let {
            val ratioEntries = it.split(":")
            layoutParams?.let { params ->
                if (ratioEntries.size == 2) {
                    params.height =
                        ((if (params.width <= 0) measuredWidth else params.width) / ratioEntries[0].toFloat() * ratioEntries[1].toFloat()).toInt()
                }
                if (ratioEntries.size == 3) {
                    if (ratioEntries[0].equals(WIDTH, true)) {
                        params.height =
                            ((if (params.width <= 0) measuredWidth else params.width) / ratioEntries[1].toFloat() * ratioEntries[2].toFloat()).toInt()
                    }
                    if (ratioEntries[0].equals(HEIGHT, true)) {
                        params.width =
                            ((if (params.height <= 0) measuredHeight else params.height) / ratioEntries[2].toFloat() * ratioEntries[1].toFloat()).toInt()
                    }
                }
            }
        }
        requestLayout()
    }

    private fun updateSize() {
        iconSize?.let {
            val size = getIconSize(context, it)
            layoutParams?.let { par -> par.width = size }
        }
        updateRatio()
    }

    private fun updateCornerRadius(w: Int, h: Int) {
        if (!hasRadius()) {
            clipPath = null
            return
        }
        clipPath = Path().apply {
            addRoundRect(
                RectF(0f, 0f, w.toFloat(), h.toFloat()),
                floatArrayOf(
                    _cornerRadius[0], _cornerRadius[0],
                    _cornerRadius[1], _cornerRadius[1],
                    _cornerRadius[3], _cornerRadius[3],
                    _cornerRadius[2], _cornerRadius[2],
                ),
                Path.Direction.CW
            )
        }
    }

    init {
        initialization(context, attributes, defStyleAttributes)
        updateView()
    }

    private fun updateView() {
        updateSize()
    }

    private fun initialization(
        context: Context,
        attributes: AttributeSet?,
        defStyleAttributes: Int
    ) {
        context.theme.obtainStyledAttributes(
            attributes,
            R.styleable.TrpIcon,
            defStyleAttributes,
            0
        ).use {
            iconSize = if (it.hasValue(R.styleable.TrpIcon_trpIconSize)) {
                val sizeValue = TypedValue()
                it.getValue(R.styleable.TrpIcon_trpIconSize, sizeValue)
                if (sizeValue.type == TypedValue.TYPE_DIMENSION) it.getDimensionPixelSize(
                    R.styleable.TrpIcon_trpIconSize,
                    0
                ) else it.getInt(R.styleable.TrpIcon_trpIconSize, 0)
            } else null
            viewRatio = it.getString(R.styleable.TrpIcon_trpRatio)

            if (it.hasValue(R.styleable.TrpIcon_trpCornerRadius)) {
                val radius = it.getDimensionPixelSize(R.styleable.TrpIcon_trpCornerRadius, 0)
                    .toFloat()
                setCornerRadiusLocal(radius, radius, radius, radius)
            } else {
                setCornerRadiusLocal(
                    it.getDimensionPixelSize(R.styleable.TrpIcon_trpCornerRadiusTopLeft, 0)
                        .toFloat(),
                    it.getDimensionPixelSize(R.styleable.TrpIcon_trpCornerRadiusTopRight, 0)
                        .toFloat(),
                    it.getDimensionPixelSize(R.styleable.TrpIcon_trpCornerRadiusBottomLeft, 0)
                        .toFloat(),
                    it.getDimensionPixelSize(R.styleable.TrpIcon_trpCornerRadiusBottomRight, 0)
                        .toFloat()
                )
            }
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        updateSize()
        updateCornerRadius(measuredWidth, measuredHeight)
    }

    override fun onDraw(canvas: Canvas?) {
        clipPath?.let { canvas?.clipPath(it) }
        super.onDraw(canvas)
    }

    private fun setCornerRadiusLocal(
        topLeft: Float,
        topRight: Float,
        bottomLeft: Float,
        bottomRight: Float
    ) {
        _cornerRadius[0] = topLeft
        _cornerRadius[1] = topRight
        _cornerRadius[2] = bottomLeft
        _cornerRadius[3] = bottomRight
    }

    fun setCornerRadius(
        topLeft: Float,
        topRight: Float,
        bottomLeft: Float,
        bottomRight: Float
    ) {
        setCornerRadiusLocal(topLeft, topRight, bottomLeft, bottomRight)
        requestLayout()
    }

    fun setCornerRadius(
        radius: Float
    ) {
        setCornerRadiusLocal(radius, radius, radius, radius)
        requestLayout()
    }

    private fun hasRadius() = _cornerRadius.sum() > 0

    companion object {
        const val WIDTH = "w"
        const val HEIGHT = "h"
    }

}