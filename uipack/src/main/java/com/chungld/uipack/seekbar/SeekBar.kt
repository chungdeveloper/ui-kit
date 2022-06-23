package com.chungld.uipack.seekbar

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ValueAnimator
import android.animation.ValueAnimator.AnimatorUpdateListener
import android.content.Context
import android.content.res.Resources
import android.graphics.*
import android.os.Build
import androidx.annotation.DrawableRes
import androidx.annotation.IntDef
import androidx.core.content.ContextCompat
import android.text.TextUtils
import android.util.AttributeSet
import com.chungld.uipack.R
import java.text.DecimalFormat

class SeekBar(
    trpSeekBar: TrpSeekBar?,
    attrs: AttributeSet,
    isLeft: Boolean
) {
    @IntDef(
        INDICATOR_SHOW_WHEN_TOUCH,
        INDICATOR_ALWAYS_HIDE,
        INDICATOR_ALWAYS_SHOW_AFTER_TOUCH,
        INDICATOR_ALWAYS_SHOW
    )
    annotation class IndicatorModeDef

    /**
     * the indicator show mode
     * [.INDICATOR_SHOW_WHEN_TOUCH]
     * [.INDICATOR_ALWAYS_SHOW]
     * [.INDICATOR_ALWAYS_SHOW_AFTER_TOUCH]
     * [.INDICATOR_ALWAYS_SHOW]
     * @param indicatorShowMode
     */
    var indicatorShowMode = 0

    //Progress prompted the background height, width,
    var indicatorHeight = 0
    var indicatorWidth = 0

    //The progress indicates the distance between the background and the button
    var indicatorMargin = 0
    private var indicatorDrawableId = 0
    var indicatorArrowSize = 0
    var indicatorTextSize = 0
    var indicatorTextColor = 0
    var indicatorRadius = 0f
    var indicatorBackgroundColor = 0
    var indicatorPaddingLeft = 0
    var indicatorPaddingRight = 0
    var indicatorPaddingTop = 0
    var indicatorPaddingBottom = 0
    private var thumbDrawableId = 0
    var thumbInactivatedDrawableId = 0
        private set
    var thumbWidth = 0
    var thumbHeight = 0

    /**
     * when you touch or move, the thumb will scale, default not scale
     *
     * @return default 1.0f
     */
    //when you touch or move, the thumb will scale, default not scale
    var thumbScaleRatio = 0f

    //****************** the above is attr value  ******************//
    var left = 0
    var right = 0
    var top = 0
    var bottom = 0
    var currPercent = 0f
    var material = 0f
    var isShowIndicator = false
        private set
    var isLeft: Boolean
    var thumbBitmap: Bitmap? = null
    var thumbInactivatedBitmap: Bitmap? = null
    var indicatorBitmap: Bitmap? = null
    var anim: ValueAnimator? = null
    var userText2Draw: String? = null
    var activate = false

    /**
     * if visble is false, will clear the Canvas
     *
     * @param visible
     */
    var isVisible = true
    var trpSeekBar: TrpSeekBar? = null
    var indicatorTextStringFormat: String? = null
    var indicatorArrowPath = Path()
    var indicatorTextRect = Rect()
    var indicatorRect = Rect()
    var paint =
        Paint(Paint.ANTI_ALIAS_FLAG)
    var indicatorTextDecimalFormat: DecimalFormat? = null
    var scaleThumbWidth = 0
    var scaleThumbHeight = 0
    private fun initAttrs(attrs: AttributeSet) {
        val t = context!!.obtainStyledAttributes(attrs, R.styleable.TrpSeekBar) ?: return
        indicatorMargin = t.getDimension(R.styleable.TrpSeekBar_trp_indicator_margin, 0f).toInt()
        indicatorDrawableId = t.getResourceId(R.styleable.TrpSeekBar_trp_indicator_drawable, 0)
        indicatorShowMode = t.getInt(
            R.styleable.TrpSeekBar_trp_indicator_show_mode,
            INDICATOR_ALWAYS_HIDE
        )
        indicatorHeight = t.getLayoutDimension(
            R.styleable.TrpSeekBar_trp_indicator_height,
            WRAP_CONTENT
        )
        indicatorWidth = t.getLayoutDimension(
            R.styleable.TrpSeekBar_trp_indicator_width,
            WRAP_CONTENT
        )
        indicatorTextSize = t.getDimension(
            R.styleable.TrpSeekBar_trp_indicator_text_size,
            TrpSeekBarUtils.dp2px(context, 14f).toFloat()
        ).toInt()
        indicatorTextColor = t.getColor(
            R.styleable.TrpSeekBar_trp_indicator_text_color,
            Color.WHITE
        )
        indicatorBackgroundColor = t.getColor(
            R.styleable.TrpSeekBar_trp_indicator_background_color,
            ContextCompat.getColor(context!!, R.color.trpColorAccent)
        )
        indicatorPaddingLeft =
            t.getDimension(R.styleable.TrpSeekBar_trp_indicator_padding_left, 0f).toInt()
        indicatorPaddingRight =
            t.getDimension(R.styleable.TrpSeekBar_trp_indicator_padding_right, 0f).toInt()
        indicatorPaddingTop =
            t.getDimension(R.styleable.TrpSeekBar_trp_indicator_padding_top, 0f).toInt()
        indicatorPaddingBottom =
            t.getDimension(R.styleable.TrpSeekBar_trp_indicator_padding_bottom, 0f).toInt()
        indicatorArrowSize =
            t.getDimension(R.styleable.TrpSeekBar_trp_indicator_arrow_size, 0f).toInt()
        thumbDrawableId = t.getResourceId(
            R.styleable.TrpSeekBar_trp_thumb_drawable,
            R.drawable.trp_seekbar_default_thumb
        )
        thumbInactivatedDrawableId =
            t.getResourceId(R.styleable.TrpSeekBar_trp_thumb_inactivated_drawable, 0)
        thumbWidth = t.getDimension(
            R.styleable.TrpSeekBar_trp_thumb_width,
            TrpSeekBarUtils.dp2px(context, 26f).toFloat()
        ).toInt()
        thumbHeight = t.getDimension(
            R.styleable.TrpSeekBar_trp_thumb_height,
            TrpSeekBarUtils.dp2px(context, 26f).toFloat()
        ).toInt()
        thumbScaleRatio = t.getFloat(R.styleable.TrpSeekBar_trp_thumb_scale_ratio, 1f)
        indicatorRadius = t.getDimension(R.styleable.TrpSeekBar_trp_indicator_radius, 0f)
        t.recycle()
    }

    protected fun initVariables() {
        scaleThumbWidth = thumbWidth
        scaleThumbHeight = thumbHeight
        if (indicatorHeight == WRAP_CONTENT) {
            indicatorHeight = TrpSeekBarUtils.measureText("8", indicatorTextSize.toFloat())
                .height() + indicatorPaddingTop + indicatorPaddingBottom
        }
        if (indicatorArrowSize <= 0) {
            indicatorArrowSize = (thumbWidth / 4)
        }
    }

    val context: Context?
        get() = trpSeekBar?.context

    val resources: Resources?
        get() = if (context != null) context!!.resources else null

    private fun initBitmap() {
        setIndicatorDrawableId(indicatorDrawableId)
        setThumbDrawableId(thumbDrawableId, thumbWidth, thumbHeight)
        setThumbInactivatedDrawableId(thumbInactivatedDrawableId, thumbWidth, thumbHeight)
    }

    fun onSizeChanged(x: Int, y: Int) {
        initVariables()
        initBitmap()
        left = (x - thumbScaleWidth / 2).toInt()
        right = (x + thumbScaleWidth / 2).toInt()
        top = y - thumbHeight / 2
        bottom = y + thumbHeight / 2
    }

    fun scaleThumb() {
        scaleThumbWidth = thumbScaleWidth.toInt()
        scaleThumbHeight = thumbScaleHeight.toInt()
        val y: Int = trpSeekBar!!.progressBottom
        top = y - scaleThumbHeight / 2
        bottom = y + scaleThumbHeight / 2
        setThumbDrawableId(thumbDrawableId, scaleThumbWidth, scaleThumbHeight)
    }

    fun resetThumb() {
        scaleThumbWidth = thumbWidth
        scaleThumbHeight = thumbHeight
        val y: Int = trpSeekBar!!.progressBottom
        top = y - scaleThumbHeight / 2
        bottom = y + scaleThumbHeight / 2
        setThumbDrawableId(thumbDrawableId, scaleThumbWidth, scaleThumbHeight)
    }

    val rawHeight: Float
        get() = indicatorHeight + indicatorArrowSize + indicatorMargin + thumbScaleHeight

    /**
     * Draw buttons and tips for background and text
     *
     * @param canvas Canvas
     */
    fun draw(canvas: Canvas) {
        if (!isVisible) {
            return
        }
        val offset = trpSeekBar?.let { (it.progressWidth * currPercent).toInt() }
        canvas.save()
        canvas.translate(offset?.toFloat() ?: 0f, 0f)
        // translate canvas, then don't care left
        canvas.translate(left.toFloat(), 0f)
        if (isShowIndicator) {
            onDrawIndicator(canvas, paint, formatCurrentIndicatorText(userText2Draw))
        }
        onDrawThumb(canvas)
        canvas.restore()
    }

    /**
     * draw the thumb button
     * If there is no image resource, draw the default button
     *
     * @param canvas canvas
     */
    protected fun onDrawThumb(canvas: Canvas) {
        if (thumbInactivatedBitmap != null && !activate) {
            canvas.drawBitmap(
                thumbInactivatedBitmap!!,
                0f,
                trpSeekBar!!.progressTop + (trpSeekBar!!.progressHeight - scaleThumbHeight) / 2f,
                null
            )
        } else if (thumbBitmap != null) {
            canvas.drawBitmap(
                thumbBitmap!!,
                0f,
                trpSeekBar!!.progressTop + (trpSeekBar!!.progressHeight - scaleThumbHeight) / 2f,
                null
            )
        }
    }

    /**
     * format the indicator text
     *
     * @param text2Draw
     * @return
     */
    protected fun formatCurrentIndicatorText(text2Draw: String?): String? {
        var text2DrawShadow = text2Draw
        val states: Array<SeekBarState> = trpSeekBar!!.TrpSeekBarState
        if (TextUtils.isEmpty(text2DrawShadow)) {
            text2DrawShadow = if (isLeft) {
                indicatorTextDecimalFormat?.format(states[0].value) ?: states[0].indicatorText
            } else {
                indicatorTextDecimalFormat?.format(states[1].value) ?: states[1].indicatorText
            }
        }
        if (indicatorTextStringFormat != null) {
            text2DrawShadow = String.format(indicatorTextStringFormat!!, text2DrawShadow)
        }
        return text2DrawShadow
    }

    /**
     * This method will draw the indicator background dynamically according to the text.
     * you can use to set padding
     *
     * @param canvas    Canvas
     * @param text2Draw Indicator text
     */
    protected fun onDrawIndicator(
        canvas: Canvas,
        paint: Paint,
        text2Draw: String?
    ) {
        if (text2Draw == null) return
        paint.textSize = indicatorTextSize.toFloat()
        paint.style = Paint.Style.FILL
        paint.color = indicatorBackgroundColor
        paint.getTextBounds(text2Draw, 0, text2Draw.length, indicatorTextRect)
        var realIndicatorWidth =
            indicatorTextRect.width() + indicatorPaddingLeft + indicatorPaddingRight
        if (indicatorWidth > realIndicatorWidth) {
            realIndicatorWidth = indicatorWidth
        }
        var realIndicatorHeight =
            indicatorTextRect.height() + indicatorPaddingTop + indicatorPaddingBottom
        if (indicatorHeight > realIndicatorHeight) {
            realIndicatorHeight = indicatorHeight
        }
        indicatorRect.left = (scaleThumbWidth / 2f - realIndicatorWidth / 2f).toInt()
        indicatorRect.top = bottom - realIndicatorHeight - scaleThumbHeight - indicatorMargin
        indicatorRect.right = indicatorRect.left + realIndicatorWidth
        indicatorRect.bottom = indicatorRect.top + realIndicatorHeight
        //draw default indicator arrow
        if (indicatorBitmap == null) {
            //arrow three point
            //  b   c
            //    a
            val ax = scaleThumbWidth / 2
            val ay = indicatorRect.bottom
            val bx = ax - indicatorArrowSize
            val by = ay - indicatorArrowSize
            val cx = ax + indicatorArrowSize
            indicatorArrowPath.reset()
            indicatorArrowPath.moveTo(ax.toFloat(), ay.toFloat())
            indicatorArrowPath.lineTo(bx.toFloat(), by.toFloat())
            indicatorArrowPath.lineTo(cx.toFloat(), by.toFloat())
            indicatorArrowPath.close()
            canvas.drawPath(indicatorArrowPath, paint)
            indicatorRect.bottom -= indicatorArrowSize
            indicatorRect.top -= indicatorArrowSize
        }

        //indicator background edge processing
        val defaultPaddingOffset: Int = TrpSeekBarUtils.dp2px(context, 1f)
        val leftOffset: Int =
            indicatorRect.width() / 2 - (trpSeekBar!!.progressWidth * currPercent).toInt() - trpSeekBar!!.progressLeft + defaultPaddingOffset
        val rightOffset: Int =
            indicatorRect.width() / 2 - (trpSeekBar!!.progressWidth * (1 - currPercent)).toInt() - trpSeekBar!!.progressPaddingRight + defaultPaddingOffset
        if (leftOffset > 0) {
            indicatorRect.left += leftOffset
            indicatorRect.right += leftOffset
        } else if (rightOffset > 0) {
            indicatorRect.left -= rightOffset
            indicatorRect.right -= rightOffset
        }

        //draw indicator background
        indicatorBitmap?.let {
            TrpSeekBarUtils.drawBitmap(canvas, paint, it, indicatorRect)
        } ?: if (indicatorRadius > 0f) {
            canvas.drawRoundRect(RectF(indicatorRect), indicatorRadius, indicatorRadius, paint)
        } else {
            canvas.drawRect(indicatorRect, paint)
        }

        //draw indicator content text
        val tx: Int
        val ty: Int
        tx = if (indicatorPaddingLeft > 0) {
            indicatorRect.left + indicatorPaddingLeft
        } else if (indicatorPaddingRight > 0) {
            indicatorRect.right - indicatorPaddingRight - indicatorTextRect.width()
        } else {
            indicatorRect.left + (realIndicatorWidth - indicatorTextRect.width()) / 2
        }
        ty = if (indicatorPaddingTop > 0) {
            indicatorRect.top + indicatorTextRect.height() + indicatorPaddingTop
        } else if (indicatorPaddingBottom > 0) {
            indicatorRect.bottom - indicatorTextRect.height() - indicatorPaddingBottom
        } else {
            indicatorRect.bottom - (realIndicatorHeight - indicatorTextRect.height()) / 2 + 1
        }

        //draw indicator text
        paint.color = indicatorTextColor
        canvas.drawText(text2Draw, tx.toFloat(), ty.toFloat(), paint)
    }

    /**
     * @return is collide
     */
    fun collide(x: Float, y: Float): Boolean {
        val offset = (trpSeekBar!!.progressWidth * currPercent).toInt()
        return x > left + offset && x < right + offset && y > top && y < bottom
    }

    fun slide(percent: Float) {
        var percent = percent
        if (percent < 0) percent = 0f else if (percent > 1) percent = 1f
        currPercent = percent
    }

    fun setShowIndicatorEnable(isEnable: Boolean) {
        when (indicatorShowMode) {
            INDICATOR_SHOW_WHEN_TOUCH -> isShowIndicator = isEnable
            INDICATOR_ALWAYS_SHOW, INDICATOR_ALWAYS_SHOW_AFTER_TOUCH -> isShowIndicator =
                true
            INDICATOR_ALWAYS_HIDE -> isShowIndicator = false
        }
    }

    fun materialRestore() {
        if (anim != null) anim!!.cancel()
        anim = ValueAnimator.ofFloat(material, 0f)
        anim?.apply {
            addUpdateListener(AnimatorUpdateListener { animation ->
                material = animation.animatedValue as Float
                if (trpSeekBar != null) trpSeekBar!!.invalidate()
            })
            addListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator) {
                    material = 0f
                    if (trpSeekBar != null) trpSeekBar!!.invalidate()
                }
            })
            start()
        }
    }

    fun setIndicatorText(text: String?) {
        userText2Draw = text
    }

    fun setIndicatorTextDecimalFormat(formatPattern: String?) {
        indicatorTextDecimalFormat = DecimalFormat(formatPattern)
    }

    fun getIndicatorDrawableId(): Int {
        return indicatorDrawableId
    }

    fun setIndicatorDrawableId(@DrawableRes indicatorDrawableId: Int) {
        if (indicatorDrawableId != 0) {
            this.indicatorDrawableId = indicatorDrawableId
            indicatorBitmap = BitmapFactory.decodeResource(resources, indicatorDrawableId)
        }
    }

    fun showIndicator(isShown: Boolean) {
        isShowIndicator = isShown
    }

    /**
     * include indicator text Height、padding、margin
     *
     * @return The actual occupation height of indicator
     */
    val indicatorRawHeight: Int
        get() = if (indicatorHeight > 0) {
            if (indicatorBitmap != null) {
                indicatorHeight + indicatorMargin
            } else {
                indicatorHeight + indicatorArrowSize + indicatorMargin
            }
        } else {
            if (indicatorBitmap != null) {
                TrpSeekBarUtils.measureText("8", indicatorTextSize.toFloat())
                    .height() + indicatorPaddingTop + indicatorPaddingBottom + indicatorMargin
            } else {
                TrpSeekBarUtils.measureText("8", indicatorTextSize.toFloat())
                    .height() + indicatorPaddingTop + indicatorPaddingBottom + indicatorMargin + indicatorArrowSize
            }
        }

    fun setThumbInactivatedDrawableId(
        @DrawableRes thumbInactivatedDrawableId: Int,
        width: Int,
        height: Int
    ) {
        if (thumbInactivatedDrawableId != 0 && resources != null) {
            this.thumbInactivatedDrawableId = thumbInactivatedDrawableId
            thumbInactivatedBitmap = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                TrpSeekBarUtils.drawableToBitmap(
                    width,
                    height,
                    resources!!.getDrawable(thumbInactivatedDrawableId, null)
                )
            } else {
                TrpSeekBarUtils.drawableToBitmap(
                    width,
                    height,
                    resources!!.getDrawable(thumbInactivatedDrawableId)
                )
            }
        }
    }

    fun getThumbDrawableId(): Int {
        return thumbDrawableId
    }

    fun setThumbDrawableId(
        @DrawableRes thumbDrawableId: Int,
        width: Int,
        height: Int
    ) {
        if (thumbDrawableId != 0 && resources != null && width > 0 && height > 0) {
            this.thumbDrawableId = thumbDrawableId
            thumbBitmap = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                TrpSeekBarUtils.drawableToBitmap(
                    width,
                    height,
                    resources!!.getDrawable(thumbDrawableId, null)
                )
            } else {
                TrpSeekBarUtils.drawableToBitmap(
                    width,
                    height,
                    resources!!.getDrawable(thumbDrawableId)
                )
            }
        }
    }

    fun setThumbDrawableId(@DrawableRes thumbDrawableId: Int) {
        require(!(thumbWidth <= 0 || thumbHeight <= 0)) { "please set thumbWidth and thumbHeight first!" }
        if (thumbDrawableId != 0 && resources != null) {
            this.thumbDrawableId = thumbDrawableId
            thumbBitmap = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                TrpSeekBarUtils.drawableToBitmap(
                    thumbWidth,
                    thumbHeight,
                    resources!!.getDrawable(thumbDrawableId, null)
                )
            } else {
                TrpSeekBarUtils.drawableToBitmap(
                    thumbWidth,
                    thumbHeight,
                    resources!!.getDrawable(thumbDrawableId)
                )
            }
        }
    }

    val thumbScaleHeight: Float
        get() = thumbHeight * thumbScaleRatio

    val thumbScaleWidth: Float
        get() = thumbWidth * thumbScaleRatio

    fun setTypeface(typeFace: Typeface?) {
        paint.typeface = typeFace
    }

    val progress: Float
        get() {
            val range: Float = trpSeekBar!!.maxProgress - trpSeekBar!!.minProgress
            return trpSeekBar!!.minProgress + range * currPercent
        }

    companion object {
        //the indicator show mode
        const val INDICATOR_SHOW_WHEN_TOUCH = 0
        const val INDICATOR_ALWAYS_HIDE = 1
        const val INDICATOR_ALWAYS_SHOW_AFTER_TOUCH = 2
        const val INDICATOR_ALWAYS_SHOW = 3
        const val WRAP_CONTENT = -1
        const val MATCH_PARENT = -2
    }

    init {
        this.trpSeekBar = trpSeekBar
        this.isLeft = isLeft
        initAttrs(attrs)
        initBitmap()
        initVariables()
    }
}
