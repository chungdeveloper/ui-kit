package com.chungld.uipack.viewGroup

import android.content.Context
import android.graphics.Canvas
import android.graphics.Path
import android.graphics.RectF
import android.util.AttributeSet
import android.widget.RelativeLayout
import com.chungld.uipack.R
import com.chungld.uipack.icon.TrpIcon
import com.chungld.uipack.theme.wrapContextWithDefaults
import com.chungld.uipack.utils.use

/**
 * Extend from [RelativeLayout]
 * #
 * Style default: [R.attr.TrpRelativeLayout] has no parent
 */
open class TrpRelativeLayout @JvmOverloads constructor(
    context: Context,
    attributes: AttributeSet? = null,
    defStyleAttributes: Int = R.attr.TrpRelativeLayout
) : RelativeLayout(wrapContextWithDefaults(context), attributes, defStyleAttributes) {

    private var clipPath: Path? = null
    private val _cornerRadius = arrayListOf(0f, 0f, 0f, 0f)
    private var viewRatio: String? = null

    init {
        initialization(context, attributes, defStyleAttributes)
    }

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
                    if (ratioEntries[0].equals(TrpIcon.WIDTH, true)) {
                        params.height =
                            ((if (params.width <= 0) measuredWidth else params.width) / ratioEntries[1].toFloat() * ratioEntries[2].toFloat()).toInt()
                    }
                    if (ratioEntries[0].equals(TrpIcon.HEIGHT, true)) {
                        params.width =
                            ((if (params.height <= 0) measuredHeight else params.height) / ratioEntries[2].toFloat() * ratioEntries[1].toFloat()).toInt()
                    }
                }
            }
        }
        requestLayout()
    }

    private fun initialization(
        context: Context,
        attributes: AttributeSet?,
        defStyleAttributes: Int
    ) {
        context.theme.obtainStyledAttributes(
            attributes,
            R.styleable.TrpRelativeLayout,
            defStyleAttributes,
            0
        ).use {
            viewRatio = it.getString(R.styleable.TrpRelativeLayout_trpRatio)

            if (it.hasValue(R.styleable.TrpRelativeLayout_trpCornerRadius)) {
                val radius =
                    it.getDimensionPixelSize(R.styleable.TrpRelativeLayout_trpCornerRadius, 0)
                        .toFloat()
                setCornerRadiusLocal(radius, radius, radius, radius)
            } else {
                setCornerRadiusLocal(
                    it.getDimensionPixelSize(
                        R.styleable.TrpRelativeLayout_trpCornerRadiusTopLeft,
                        0
                    )
                        .toFloat(),
                    it.getDimensionPixelSize(
                        R.styleable.TrpRelativeLayout_trpCornerRadiusTopRight,
                        0
                    )
                        .toFloat(),
                    it.getDimensionPixelSize(
                        R.styleable.TrpRelativeLayout_trpCornerRadiusBottomLeft,
                        0
                    )
                        .toFloat(),
                    it.getDimensionPixelSize(
                        R.styleable.TrpRelativeLayout_trpCornerRadiusBottomRight,
                        0
                    )
                        .toFloat()
                )
            }
        }
    }

    private fun updateCornerRadius(w: Int, h: Int) {
        if (!hasRadius()) {
            setWillNotDraw(true)
            clipPath = null
            return
        }
        setWillNotDraw(false)
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

    override fun onDraw(canvas: Canvas?) {

        super.onDraw(canvas)
    }

    override fun dispatchDraw(canvas: Canvas?) {
        clipPath?.let { canvas?.clipPath(it) }
        super.dispatchDraw(canvas)
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

    private fun hasRadius() = _cornerRadius.sum() > 0

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        updateRatio()
        updateCornerRadius(measuredWidth, measuredHeight)
    }

    companion object {
        const val WIDTH = "w"
        const val HEIGHT = "h"
    }
}