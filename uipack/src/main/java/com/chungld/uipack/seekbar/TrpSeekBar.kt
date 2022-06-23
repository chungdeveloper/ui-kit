package com.chungld.uipack.seekbar

import android.content.Context
import android.graphics.*
import android.os.Parcelable
import androidx.annotation.ColorInt
import androidx.annotation.DrawableRes
import androidx.annotation.IntDef
import android.text.TextUtils
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import com.chungld.uipack.R
import com.chungld.uipack.seekbar.SeekBar.Companion.INDICATOR_ALWAYS_HIDE
import com.chungld.uipack.seekbar.SeekBar.Companion.INDICATOR_ALWAYS_SHOW
import com.chungld.uipack.theme.wrapContextWithDefaults
import com.chungld.uipack.utils.use
import java.lang.Math.max
import java.math.BigDecimal
import java.math.RoundingMode
import java.util.*

/**
 *  SeekBar with mode SINGLE and RANGE
 *  #
 *  Style default: [R.attr.TrpSeekBarStyle] with parent [R.style.TrpSeekBarStyleDefault]
 */

class TrpSeekBar @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = R.attr.TrpSeekBarStyle
) : View(wrapContextWithDefaults(context), attrs, defStyle) {

    @IntDef(SEEKBAR_MODE_SINGLE, SEEKBAR_MODE_RANGE)
    annotation class SeekBarModeDef

    /**
     * @hide
     */
    @IntDef(
        TRICK_MARK_MODE_NUMBER,
        TRICK_MARK_MODE_OTHER
    )
    annotation class TickMarkModeDef

    /**
     * @hide
     */
    @IntDef(
        TICK_MARK_GRAVITY_LEFT,
        TICK_MARK_GRAVITY_CENTER,
        TICK_MARK_GRAVITY_RIGHT
    )
    annotation class TickMarkGravityDef

    /**
     * @hide
     */
    @IntDef(Gravity.TOP, Gravity.BOTTOM)
    annotation class TickMarkLayoutGravityDef

    /**
     * @hide
     */
    @IntDef(Gravity.TOP, Gravity.CENTER, Gravity.BOTTOM)
    annotation class GravityDef
    object Gravity {
        const val TOP = 0
        const val BOTTOM = 1
        const val CENTER = 2
    }

    var progressTop = 0
    var progressBottom = 0
    var progressLeft = 0
    var progressRight = 0
    private var seekBarMode = 0

    var tickMarkMode = 0
    var tickMarkTextMargin = 0
    var tickMarkTextSize = 0
    var tickMarkGravity = 0
    var tickMarkLayoutGravity = 0
    var tickMarkTextColor = 0
    var tickMarkInRangeTextColor = 0
    var tickMarkTextArray: Array<CharSequence>? = null
    var progressRadius = 0f
    var progressColor = 0
    var progressDefaultColor = 0
    private var progressDrawableId = 0
    private var progressDefaultDrawableId = 0
    var progressHeight = 0
    var progressWidth = 0
    var minInterval = 0f
        private set

    /**
     * the TrpSeekBar gravity
     * Gravity.TOP and Gravity.BOTTOM
     * @param gravity
     */
    var gravity = 0

    //enable TrpSeekBar two thumb Overlap
    var isEnableThumbOverlap = false

    //the color of step divs
    var stepsColor = 0

    //the width of each step
    var stepsWidth = 0f

    private var _trpProgressPrimary: Float = 0f
    var trpProgressPrimary
        get() = _trpProgressPrimary
        set(value) {
            _trpProgressPrimary = value
            applyAttr()
        }

    private var _trpProgressSecondary = 0f
    var trpProgressSecondary
        get() = _trpProgressSecondary
        set(value) {
            _trpProgressSecondary = value
            applyAttr()
        }

    //the height of each step
    var stepsHeight = 0f

    //the radius of step divs
    var stepsRadius = 0f

    //steps is 0 will disable StepSeekBar
    var steps = 0

    //the thumb will automatic bonding close to its value
    var isStepsAutoBonding = false
    private var stepsDrawableId = 0

    //True values set by the user
    var minProgress = 0f
        private set
    var maxProgress = 0f
        private set

    //****************** the above is attr value  ******************//
    private var isEnable = true
    var touchDownX = 0f
    var touchDownY = 0f

    var reservePercent = 0f
    var isScaleThumb = false
    var paint = Paint()
    var progressDefaultDstRect = RectF()
    var progressDstRect = RectF()
    var progressSrcRect = Rect()
    var stepDivRect = RectF()
    var tickMarkTextRect = Rect()

    /**
     * if is single mode, please use it to get the SeekBar
     *
     * @return left seek bar
     */
    var leftSeekBar: SeekBar? = null
    var rightSeekBar: SeekBar? = null
    var currTouchSB: SeekBar? = null
    var progressBitmap: Bitmap? = null
    var progressDefaultBitmap: Bitmap? = null
    var stepsBitmaps: MutableList<Bitmap> = mutableListOf()
        set(stepsBitmaps) {
            require(!(stepsBitmaps.isEmpty() || stepsBitmaps.size <= steps)) { "stepsBitmaps must > steps !" }
            this.stepsBitmaps.clear()
            this.stepsBitmaps.addAll(stepsBitmaps)
        }
    var progressPaddingRight = 0
        private set
    private var callback: OnRangeChangedListener? = null
    private fun initProgressBitmap() {
        if (progressBitmap == null) {
            progressBitmap = TrpSeekBarUtils.drawableToBitmap(
                context,
                progressWidth,
                progressHeight,
                progressDrawableId
            )
        }
        if (progressDefaultBitmap == null) {
            progressDefaultBitmap = TrpSeekBarUtils.drawableToBitmap(
                context,
                progressWidth,
                progressHeight,
                progressDefaultDrawableId
            )
        }
    }

    private fun verifyStepsMode(): Boolean {
        return steps >= 1 && stepsHeight > 0 && stepsWidth > 0
    }

    private fun initStepsBitmap() {
        if (!verifyStepsMode() || stepsDrawableId == 0) return
        if (stepsBitmaps.isEmpty()) {
            TrpSeekBarUtils.drawableToBitmap(
                context, stepsWidth.toInt(), stepsHeight.toInt(), stepsDrawableId
            )?.let {
                for (i in 0..steps) {
                    stepsBitmaps.add(it)
                }
            }
        }
    }

    private fun initSeekBar(attrs: AttributeSet?) {
        leftSeekBar = SeekBar(this, attrs!!, true)
        rightSeekBar = SeekBar(this, attrs, false)
        rightSeekBar?.apply { isVisible = (seekBarMode != SEEKBAR_MODE_SINGLE) }
    }

    private fun initAttrs(attrs: AttributeSet?, defStyle: Int) {
        context.theme.obtainStyledAttributes(attrs, R.styleable.TrpSeekBar, defStyle, 0).use {
            seekBarMode = it.getInt(
                R.styleable.TrpSeekBar_trp_mode,
                SEEKBAR_MODE_RANGE
            )
            minProgress = it.getFloat(R.styleable.TrpSeekBar_trpMin, 0f)
            maxProgress = it.getFloat(R.styleable.TrpSeekBar_trpMax, 100f)
            minInterval = it.getFloat(R.styleable.TrpSeekBar_trp_min_interval, 0f)
            gravity = it.getInt(R.styleable.TrpSeekBar_trp_gravity, Gravity.TOP)
            progressColor = it.getColor(R.styleable.TrpSeekBar_trp_progress_color, -0xb4269e)
            progressRadius =
                it.getDimension(R.styleable.TrpSeekBar_trp_progress_radius, -1f)
            progressDefaultColor =
                it.getColor(R.styleable.TrpSeekBar_trp_progress_default_color, -0x282829)
            progressDrawableId = it.getResourceId(R.styleable.TrpSeekBar_trp_progress_drawable, 0)
            progressDefaultDrawableId =
                it.getResourceId(R.styleable.TrpSeekBar_trp_progress_drawable_default, 0)
            progressHeight = it.getDimension(
                R.styleable.TrpSeekBar_trp_progress_height,
                TrpSeekBarUtils.dp2px(context, 2f).toFloat()
            ).toInt()
            tickMarkMode = it.getInt(
                R.styleable.TrpSeekBar_trp_tick_mark_mode,
                TRICK_MARK_MODE_NUMBER
            )
            tickMarkGravity = it.getInt(
                R.styleable.TrpSeekBar_trp_tick_mark_gravity,
                TICK_MARK_GRAVITY_CENTER
            )
            tickMarkLayoutGravity =
                it.getInt(R.styleable.TrpSeekBar_trp_tick_mark_layout_gravity, Gravity.TOP)
            tickMarkTextArray = it.getTextArray(R.styleable.TrpSeekBar_trp_tick_mark_text_array)
            tickMarkTextMargin = it.getDimension(
                R.styleable.TrpSeekBar_trp_tick_mark_text_margin,
                TrpSeekBarUtils.dp2px(context, 7f).toFloat()
            ).toInt()
            tickMarkTextSize = it.getDimension(
                R.styleable.TrpSeekBar_trp_tick_mark_text_size,
                TrpSeekBarUtils.dp2px(context, 12f).toFloat()
            ).toInt()
            tickMarkTextColor =
                it.getColor(R.styleable.TrpSeekBar_trp_tick_mark_text_color, progressDefaultColor)
            tickMarkInRangeTextColor =
                it.getColor(R.styleable.TrpSeekBar_trp_tick_mark_text_color, progressColor)
            steps = it.getInt(R.styleable.TrpSeekBar_trp_steps, 0)
            stepsColor = it.getColor(R.styleable.TrpSeekBar_trp_step_color, -0x626263)
            stepsRadius = it.getDimension(R.styleable.TrpSeekBar_trp_step_radius, 0f)
            stepsWidth = it.getDimension(R.styleable.TrpSeekBar_trp_step_width, 0f)
            stepsHeight = it.getDimension(R.styleable.TrpSeekBar_trp_step_height, 0f)
            stepsDrawableId = it.getResourceId(R.styleable.TrpSeekBar_trp_step_drawable, 0)
            isStepsAutoBonding = it.getBoolean(R.styleable.TrpSeekBar_trp_step_auto_bonding, true)
            _trpProgressPrimary =
                it.getFloat(R.styleable.TrpSeekBar_trpProgressPrimary, minProgress)
            _trpProgressSecondary =
                it.getFloat(R.styleable.TrpSeekBar_trpProgressSecondary, minProgress)
        }
    }

    fun setTrpMax(max: Float) {
        maxProgress = max
        setRange(minProgress, maxProgress)
        applyAttr()
    }

    fun setTrpMin(min: Float) {
        minProgress = min
        setRange(minProgress, maxProgress)
        applyAttr()
    }

    /**
     * measure progress bar position
     */
    protected fun onMeasureProgress(w: Int, h: Int) {
        val viewHeight = h - paddingBottom - paddingTop
        if (h <= 0) return
        if (gravity == Gravity.TOP) {
            //calculate the height of indicator and thumb exceeds the part of the progress
            var maxIndicatorHeight = 0f

            if (leftSeekBar?.indicatorShowMode != INDICATOR_ALWAYS_HIDE || rightSeekBar!!.indicatorShowMode != INDICATOR_ALWAYS_HIDE) {
                maxIndicatorHeight = leftSeekBar!!.indicatorRawHeight.toFloat()
                    .coerceAtLeast(rightSeekBar!!.indicatorRawHeight.toFloat())
            }
            var thumbHeight: Float =
                leftSeekBar!!.thumbScaleHeight.coerceAtLeast(rightSeekBar!!.thumbScaleHeight)
            thumbHeight -= progressHeight / 2f

            //default height is indicator + thumb exceeds the part of the progress bar
            //if tickMark height is greater than (indicator + thumb exceeds the part of the progress)
            progressTop = (maxIndicatorHeight + (thumbHeight - progressHeight) / 2f).toInt()
            if (tickMarkTextArray != null && tickMarkLayoutGravity == Gravity.TOP) {
                progressTop = tickMarkRawHeight.toFloat()
                    .coerceAtLeast(maxIndicatorHeight + (thumbHeight - progressHeight) / 2f).toInt()
            }
            progressBottom = progressTop + progressHeight
        } else if (gravity == Gravity.BOTTOM) {
            progressBottom =
                if (tickMarkTextArray != null && tickMarkLayoutGravity == Gravity.BOTTOM) {
                    viewHeight - tickMarkRawHeight
                } else {
                    (viewHeight - leftSeekBar!!.thumbScaleHeight.coerceAtLeast(rightSeekBar!!.thumbScaleHeight) / 2f
                            + progressHeight / 2f).toInt()
                }
            progressTop = progressBottom - progressHeight
        } else {
            progressTop = (viewHeight - progressHeight) / 2
            progressBottom = progressTop + progressHeight
        }
        val maxThumbWidth =
            leftSeekBar!!.thumbScaleWidth.coerceAtLeast(rightSeekBar!!.thumbScaleWidth).toInt()
        progressLeft = maxThumbWidth / 2 + paddingLeft
        progressRight = w - maxThumbWidth / 2 - paddingRight
        progressWidth = progressRight - progressLeft
        progressDefaultDstRect[progressLeft.toFloat(), progressTop.toFloat(), progressRight.toFloat()] =
            progressBottom.toFloat()
        progressPaddingRight = w - progressRight
        //default value
        if (progressRadius <= 0) {
            progressRadius = ((progressBottom - progressTop) * 0.45f)
        }
        initProgressBitmap()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        var heightSize = MeasureSpec.getSize(heightMeasureSpec)
        val heightMode = MeasureSpec.getMode(heightMeasureSpec)
        if (heightMode == MeasureSpec.EXACTLY) {
            heightSize = MeasureSpec.makeMeasureSpec(heightSize, MeasureSpec.EXACTLY)
        } else if (heightMode == MeasureSpec.AT_MOST && parent is ViewGroup
            && heightSize == ViewGroup.LayoutParams.MATCH_PARENT
        ) {
            heightSize = MeasureSpec.makeMeasureSpec(
                (parent as ViewGroup).measuredHeight,
                MeasureSpec.AT_MOST
            )
        } else {
            val heightNeeded: Int
            heightNeeded = if (gravity == Gravity.CENTER) {
                if (tickMarkTextArray != null && tickMarkLayoutGravity == Gravity.BOTTOM) {
                    (2 * (rawHeight - tickMarkRawHeight)).toInt()
                } else {
                    val toInt = (2 * (rawHeight - max(
                        leftSeekBar!!.thumbScaleHeight,
                        rightSeekBar!!.thumbScaleHeight
                    ) / 2)).toInt()
                    toInt
                }
            } else {
                rawHeight.toInt()
            }
            heightSize = MeasureSpec.makeMeasureSpec(heightNeeded, MeasureSpec.EXACTLY)
        }
        super.onMeasure(widthMeasureSpec, heightSize)
    }

    protected val tickMarkRawHeight: Int
        protected get() = if (tickMarkTextArray != null && tickMarkTextArray!!.size > 0) {
            tickMarkTextMargin + TrpSeekBarUtils.measureText(
                tickMarkTextArray!![0].toString(),
                tickMarkTextSize.toFloat()
            ).height() + 3
        } else 0

    protected val rawHeight: Float
        protected get() {
            var rawHeight: Float
            if (seekBarMode == SEEKBAR_MODE_SINGLE) {
                rawHeight = leftSeekBar!!.rawHeight
                if (tickMarkLayoutGravity == Gravity.BOTTOM && tickMarkTextArray != null) {
                    val h = ((leftSeekBar!!.thumbScaleHeight - progressHeight) / 2).coerceAtLeast(
                        tickMarkRawHeight.toFloat()
                    )
                    rawHeight =
                        rawHeight - leftSeekBar!!.thumbScaleHeight / 2 + progressHeight / 2f + h
                }
            } else {
                rawHeight = leftSeekBar!!.rawHeight.coerceAtLeast(rightSeekBar!!.rawHeight)
                if (tickMarkLayoutGravity == Gravity.BOTTOM && tickMarkTextArray != null) {
                    val thumbHeight: Float =
                        leftSeekBar!!.thumbScaleHeight.coerceAtLeast(rightSeekBar!!.thumbScaleHeight)
                    val h =
                        ((thumbHeight - progressHeight) / 2).coerceAtLeast(tickMarkRawHeight.toFloat())
                    rawHeight = rawHeight - thumbHeight / 2 + progressHeight / 2f + h
                }
            }
            return rawHeight
        }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        onMeasureProgress(w, h)
        //set default value
        setRange(minProgress, maxProgress, minInterval)
        // initializes the positions of the two thumbs
        val lineCenterY = (progressBottom + progressTop) / 2
        leftSeekBar!!.onSizeChanged(progressLeft, lineCenterY)
        if (seekBarMode == SEEKBAR_MODE_RANGE) {
            rightSeekBar!!.onSizeChanged(progressLeft, lineCenterY)
        }
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        onDrawTickMark(canvas, paint)
        onDrawProgressBar(canvas, paint)
        onDrawSteps(canvas, paint)
        onDrawSeekBar(canvas)
    }

    // Draw the scales, and according to the current position is set within
    // the scale range of different color display
    protected fun onDrawTickMark(
        canvas: Canvas,
        paint: Paint
    ) {
        if (tickMarkTextArray != null) {
            val trickPartWidth = progressWidth / (tickMarkTextArray!!.size - 1)
            for (i in tickMarkTextArray!!.indices) {
                val text2Draw = tickMarkTextArray!![i].toString()
                if (TextUtils.isEmpty(text2Draw)) continue
                paint.getTextBounds(text2Draw, 0, text2Draw.length, tickMarkTextRect)
                paint.color = tickMarkTextColor
                var x: Float
                if (tickMarkMode == TRICK_MARK_MODE_OTHER) {
                    x = if (tickMarkGravity == TICK_MARK_GRAVITY_RIGHT) {
                        progressLeft + i * trickPartWidth - tickMarkTextRect.width()
                            .toFloat()
                    } else if (tickMarkGravity == TICK_MARK_GRAVITY_CENTER) {
                        progressLeft + i * trickPartWidth - tickMarkTextRect.width() / 2f
                    } else {
                        progressLeft + i * trickPartWidth.toFloat()
                    }
                } else {
                    val num: Float = TrpSeekBarUtils.parseFloat(text2Draw)
                    val states: Array<SeekBarState> = TrpSeekBarState
                    if (TrpSeekBarUtils.compareFloat(
                            num,
                            states[0].value
                        ) !== -1 && TrpSeekBarUtils.compareFloat(
                            num,
                            states[1].value
                        ) !== 1 && seekBarMode == SEEKBAR_MODE_RANGE
                    ) {
                        paint.color = tickMarkInRangeTextColor
                    }
                    x =
                        (progressLeft + progressWidth * (num - minProgress) / (maxProgress - minProgress)
                                - tickMarkTextRect.width() / 2f)
                }
                var y: Float
                y = if (tickMarkLayoutGravity == Gravity.TOP) {
                    progressTop - tickMarkTextMargin.toFloat()
                } else {
                    progressBottom + tickMarkTextMargin + tickMarkTextRect.height()
                        .toFloat()
                }
                canvas.drawText(text2Draw, x, y, paint)
            }
        }
    }

    // draw the progress bar
    protected fun onDrawProgressBar(
        canvas: Canvas,
        paint: Paint
    ) {

        //draw default progress
        if (TrpSeekBarUtils.verifyBitmap(progressDefaultBitmap)) {
            canvas.drawBitmap(progressDefaultBitmap!!, null, progressDefaultDstRect, paint)
        } else {
            paint.color = progressDefaultColor
            canvas.drawRoundRect(progressDefaultDstRect, progressRadius, progressRadius, paint)
        }

        //draw progress
        if (seekBarMode == SEEKBAR_MODE_RANGE) {
            progressDstRect.top = progressTop.toFloat()
            progressDstRect.left =
                leftSeekBar!!.left + leftSeekBar!!.thumbScaleWidth / 2f + progressWidth * leftSeekBar!!.currPercent
            progressDstRect.right =
                rightSeekBar!!.left + rightSeekBar!!.thumbScaleWidth / 2f + progressWidth * rightSeekBar!!.currPercent
            progressDstRect.bottom = progressBottom.toFloat()
        } else {
            progressDstRect.top = progressTop.toFloat()
            progressDstRect.left = leftSeekBar!!.left + leftSeekBar!!.thumbScaleWidth / 2f
            progressDstRect.right =
                leftSeekBar!!.left + leftSeekBar!!.thumbScaleWidth / 2f + progressWidth * leftSeekBar!!.currPercent
            progressDstRect.bottom = progressBottom.toFloat()
        }
        if (TrpSeekBarUtils.verifyBitmap(progressBitmap)) {
            progressSrcRect.top = 0
            progressSrcRect.bottom = progressBitmap!!.height
            val bitmapWidth = progressBitmap!!.width
            if (seekBarMode == SEEKBAR_MODE_RANGE) {
                progressSrcRect.left = (bitmapWidth * leftSeekBar!!.currPercent).toInt()
                progressSrcRect.right = (bitmapWidth * rightSeekBar!!.currPercent).toInt()
            } else {
                progressSrcRect.left = 0
                progressSrcRect.right = (bitmapWidth * leftSeekBar!!.currPercent).toInt()
            }
            canvas.drawBitmap(progressBitmap!!, progressSrcRect, progressDstRect, null)
        } else {
            paint.color = progressColor
            canvas.drawRoundRect(progressDstRect, progressRadius, progressRadius, paint)
        }
    }

    //draw steps
    protected fun onDrawSteps(
        canvas: Canvas,
        paint: Paint
    ) {
        if (!verifyStepsMode()) return
        val stepMarks = progressWidth / steps
        val extHeight = (stepsHeight - progressHeight) / 2f
        for (k in 0..steps) {
            val x = progressLeft + k * stepMarks - stepsWidth / 2f
            stepDivRect[x, progressTop - extHeight, x + stepsWidth] = progressBottom + extHeight
            if (stepsBitmaps.isEmpty() || stepsBitmaps.size <= k) {
                paint.color = stepsColor
                canvas.drawRoundRect(stepDivRect, stepsRadius, stepsRadius, paint)
            } else {
                canvas.drawBitmap(stepsBitmaps[k], null, stepDivRect, paint)
            }
        }
    }

    protected fun onDrawSeekBar(canvas: Canvas?) {
        //draw left SeekBar
        if (leftSeekBar!!.indicatorShowMode == INDICATOR_ALWAYS_SHOW) {
            leftSeekBar!!.setShowIndicatorEnable(true)
        }
        leftSeekBar!!.draw(canvas!!)
        //draw right SeekBar
        if (seekBarMode == SEEKBAR_MODE_RANGE) {
            if (rightSeekBar!!.indicatorShowMode == INDICATOR_ALWAYS_SHOW) {
                rightSeekBar!!.setShowIndicatorEnable(true)
            }
            rightSeekBar!!.draw(canvas)
        }
    }

    private fun initPaint() {
        paint.style = Paint.Style.FILL
        paint.color = progressDefaultColor
        paint.textSize = tickMarkTextSize.toFloat()
    }

    private fun changeThumbActivateState(hasActivate: Boolean) {
        if (hasActivate && currTouchSB != null) {
            val state = currTouchSB === leftSeekBar
            leftSeekBar!!.activate = (state)
            if (seekBarMode == SEEKBAR_MODE_RANGE) rightSeekBar!!.activate = (!state)
        } else {
            leftSeekBar!!.activate = (false)
            if (seekBarMode == SEEKBAR_MODE_RANGE) rightSeekBar!!.activate = (false)
        }
    }

    protected fun getEventX(event: MotionEvent): Float {
        return event.x
    }

    protected fun getEventY(event: MotionEvent): Float {
        return event.y
    }

    /**
     * scale the touch seekBar thumb
     */
    private fun scaleCurrentSeekBarThumb() {
        if (currTouchSB != null && currTouchSB!!.thumbScaleRatio > 1f && !isScaleThumb) {
            isScaleThumb = true
            currTouchSB!!.scaleThumb()
        }
    }

    /**
     * reset the touch seekBar thumb
     */
    private fun resetCurrentSeekBarThumb() {
        if (currTouchSB != null && currTouchSB!!.thumbScaleRatio > 1f && isScaleThumb) {
            isScaleThumb = false
            currTouchSB!!.resetThumb()
        }
    }

    //calculate currTouchSB percent by MotionEvent
    protected fun calculateCurrentSeekBarPercent(touchDownX: Float): Float {
        if (currTouchSB == null) return 0f
        var percent = (touchDownX - progressLeft) * 1f / progressWidth
        if (touchDownX < progressLeft) {
            percent = 0f
        } else if (touchDownX > progressRight) {
            percent = 1f
        }
        //RangeMode minimum interval
        if (seekBarMode == SEEKBAR_MODE_RANGE) {
            if (currTouchSB === leftSeekBar) {
                if (percent > rightSeekBar!!.currPercent - reservePercent) {
                    percent = rightSeekBar!!.currPercent - reservePercent
                }
            } else if (currTouchSB == rightSeekBar) {
                if (percent < leftSeekBar!!.currPercent + reservePercent) {
                    percent = leftSeekBar!!.currPercent + reservePercent
                }
            }
        }
        return percent
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        if (!isEnable) return true
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                touchDownX = getEventX(event)
                touchDownY = getEventY(event)
                if (seekBarMode == SEEKBAR_MODE_RANGE) {
                    if (rightSeekBar!!.currPercent >= 1 && leftSeekBar!!.collide(
                            getEventX(event),
                            getEventY(event)
                        )
                    ) {
                        currTouchSB = leftSeekBar
                        scaleCurrentSeekBarThumb()
                    } else if (rightSeekBar!!.collide(getEventX(event), getEventY(event))) {
                        currTouchSB = rightSeekBar
                        scaleCurrentSeekBarThumb()
                    } else {
                        var performClick =
                            (touchDownX - progressLeft) * 1f / progressWidth
                        val distanceLeft =
                            Math.abs(leftSeekBar!!.currPercent - performClick)
                        val distanceRight =
                            Math.abs(rightSeekBar!!.currPercent - performClick)
                        currTouchSB = if (distanceLeft < distanceRight) {
                            leftSeekBar
                        } else {
                            rightSeekBar
                        }
                        performClick = calculateCurrentSeekBarPercent(touchDownX)
                        currTouchSB!!.slide(performClick)
                    }
                } else {
                    currTouchSB = leftSeekBar
                    scaleCurrentSeekBarThumb()
                }

                //Intercept parent TouchEvent
                if (parent != null) {
                    parent.requestDisallowInterceptTouchEvent(true)
                }
                callback?.onStartTrackingTouch(this, currTouchSB === leftSeekBar)
                changeThumbActivateState(true)
                return true
            }
            MotionEvent.ACTION_MOVE -> {
                val x = getEventX(event)
                if (seekBarMode == SEEKBAR_MODE_RANGE && leftSeekBar!!.currPercent == rightSeekBar!!.currPercent) {
                    currTouchSB!!.materialRestore()
                    if (callback != null) {
                        callback?.onStopTrackingTouch(this, currTouchSB === leftSeekBar)
                    }
                    if (x - touchDownX > 0) {
                        //method to move right
                        if (currTouchSB !== rightSeekBar) {
                            currTouchSB!!.setShowIndicatorEnable(false)
                            resetCurrentSeekBarThumb()
                            currTouchSB = rightSeekBar
                        }
                    } else {
                        //method to move left
                        if (currTouchSB !== leftSeekBar) {
                            currTouchSB!!.setShowIndicatorEnable(false)
                            resetCurrentSeekBarThumb()
                            currTouchSB = leftSeekBar
                        }
                    }
                    if (callback != null) {
                        callback?.onStartTrackingTouch(this, currTouchSB === leftSeekBar)
                    }
                }
                scaleCurrentSeekBarThumb()
                currTouchSB!!.material =
                    if (currTouchSB!!.material >= 1) 1f else currTouchSB!!.material + 0.1f
                touchDownX = x
                currTouchSB!!.slide(calculateCurrentSeekBarPercent(touchDownX))
                currTouchSB!!.setShowIndicatorEnable(true)
                if (callback != null) {
                    val states: Array<SeekBarState> = TrpSeekBarState
                    callback?.onRangeChanged(this, states[0].value, states[1].value, true)
                }
                invalidate()
                //Intercept parent TouchEvent
                if (parent != null) {
                    parent.requestDisallowInterceptTouchEvent(true)
                }
                changeThumbActivateState(true)
            }
            MotionEvent.ACTION_CANCEL -> {
                if (seekBarMode == SEEKBAR_MODE_RANGE) {
                    rightSeekBar!!.setShowIndicatorEnable(false)
                }
                if (currTouchSB === leftSeekBar) {
                    resetCurrentSeekBarThumb()
                } else if (currTouchSB === rightSeekBar) {
                    resetCurrentSeekBarThumb()
                }
                leftSeekBar!!.setShowIndicatorEnable(false)
                if (callback != null) {
                    val states: Array<SeekBarState> = TrpSeekBarState
                    callback?.onRangeChanged(this, states[0].value, states[1].value, false)
                }
                //Intercept parent TouchEvent
                if (parent != null) {
                    parent.requestDisallowInterceptTouchEvent(true)
                }
                changeThumbActivateState(false)
            }
            MotionEvent.ACTION_UP -> {
                if (verifyStepsMode() && isStepsAutoBonding) {
                    val percent = calculateCurrentSeekBarPercent(getEventX(event))
                    val stepPercent = 1.0f / steps
                    val stepSelected: Int = BigDecimal((percent / stepPercent).toDouble())
                        .setScale(0, RoundingMode.HALF_UP).toInt()
                    currTouchSB!!.slide(stepSelected * stepPercent)
                }
                if (seekBarMode == SEEKBAR_MODE_RANGE) {
                    rightSeekBar!!.setShowIndicatorEnable(false)
                }
                leftSeekBar!!.setShowIndicatorEnable(false)
                currTouchSB!!.materialRestore()
                resetCurrentSeekBarThumb()
                if (callback != null) {
                    val states: Array<SeekBarState> = TrpSeekBarState
                    callback?.onRangeChanged(this, states[0].value, states[1].value, false)
                }
                //Intercept parent TouchEvent
                if (parent != null) {
                    parent.requestDisallowInterceptTouchEvent(true)
                }
                if (callback != null) {
                    callback?.onStopTrackingTouch(this, currTouchSB === leftSeekBar)
                }
                changeThumbActivateState(false)
            }
        }
        return super.onTouchEvent(event)
    }

    public override fun onSaveInstanceState(): Parcelable? {
        val superState = super.onSaveInstanceState()
        val ss = SavedState(superState)
        ss.minValue = minProgress
        ss.maxValue = maxProgress
        ss.rangeInterval = minInterval
        val results: Array<SeekBarState> = TrpSeekBarState
        ss.currSelectedMin = results[0].value
        ss.currSelectedMax = results[1].value
        return ss
    }

    public override fun onRestoreInstanceState(state: Parcelable) {
        try {
            val ss: SavedState = state as SavedState
            super.onRestoreInstanceState(ss.getSuperState())
            val min: Float = ss.minValue
            val max: Float = ss.maxValue
            val rangeInterval: Float = ss.rangeInterval
            setRange(min, max, rangeInterval)
            val currSelectedMin: Float = ss.currSelectedMin
            val currSelectedMax: Float = ss.currSelectedMax
            setProgress(currSelectedMin, currSelectedMax)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    //******************* Attributes getter and setter *******************//
    fun setOnRangeChangedListener(listener: OnRangeChangedListener?) {
        callback = listener
    }

    fun setProgress(value: Float) {
        setProgress(value, maxProgress)
    }

    fun setProgress(leftValue: Float, rightValue: Float) {
        var leftValueShadow = leftValue.coerceAtMost(rightValue)
        val rightValueShadow = leftValue.coerceAtLeast(rightValue)
        if (rightValueShadow - leftValueShadow < minInterval) {
            leftValueShadow = rightValueShadow - minInterval
        }
        require(leftValueShadow >= minProgress) { "setProgress() min < (preset min - offsetValue) . #min:$leftValueShadow #preset min:$rightValueShadow" }
        require(rightValueShadow <= maxProgress) { "setProgress() max > (preset max - offsetValue) . #max:$rightValueShadow #preset max:$rightValueShadow" }
        val range = maxProgress - minProgress
        leftSeekBar!!.currPercent = Math.abs(leftValueShadow - minProgress) / range
        if (seekBarMode == SEEKBAR_MODE_RANGE) {
            rightSeekBar!!.currPercent = Math.abs(rightValueShadow - minProgress) / range
        }
        if (callback != null) {
            callback?.onRangeChanged(this, leftValueShadow, rightValueShadow, false)
        }
        invalidate()
    }

    fun setRange(min: Float, max: Float) {
        _trpProgressPrimary = getValidValue(trpProgressPrimary, min, max)
        _trpProgressSecondary = getValidValue(trpProgressSecondary, min, max)
        setRange(min, max, minInterval)
    }

    private fun getValidValue(value: Float, min: Float, max: Float): Float {
        return if (value < min) min else if (value > max) max else value
    }

    fun setRange(min: Float, max: Float, minInterval: Float) {
        require(max > min) { "setRange() max must be greater than min ! #max:$max #min:$min" }
        require(minInterval >= 0) { "setRange() interval must be greater than zero ! #minInterval:$minInterval" }
        require(minInterval < max - min) { "setRange() interval must be less than (max - min) ! #minInterval:" + minInterval + " #max - min:" + (max - min) }
        maxProgress = max
        minProgress = min
        this.minInterval = minInterval
        reservePercent = minInterval / (max - min)

        //set default value
        if (seekBarMode == SEEKBAR_MODE_RANGE) {
            if (leftSeekBar!!.currPercent + reservePercent <= 1 && leftSeekBar!!.currPercent + reservePercent > rightSeekBar!!.currPercent) {
                rightSeekBar!!.currPercent = leftSeekBar!!.currPercent + reservePercent
            } else if (rightSeekBar!!.currPercent - reservePercent >= 0 && rightSeekBar!!.currPercent - reservePercent < leftSeekBar!!.currPercent) {
                leftSeekBar!!.currPercent = rightSeekBar!!.currPercent - reservePercent
            }
        }
        invalidate()
    }

    /**
     * @return the two seekBar state , see [SeekBarState]
     */
    val TrpSeekBarState: Array<SeekBarState>
        get() {
            val leftSeekBarState = SeekBarState()
            leftSeekBarState.value = leftSeekBar!!.progress
            leftSeekBarState.indicatorText = java.lang.String.valueOf(leftSeekBarState.value)
            if (TrpSeekBarUtils.compareFloat(leftSeekBarState.value, minProgress) == 0) {
                leftSeekBarState.isMin = true
            } else if (TrpSeekBarUtils.compareFloat(leftSeekBarState.value, maxProgress) == 0) {
                leftSeekBarState.isMax = true
            }
            val rightSeekBarState = SeekBarState()
            if (seekBarMode == SEEKBAR_MODE_RANGE) {
                rightSeekBarState.value = rightSeekBar!!.progress
                rightSeekBarState.indicatorText = java.lang.String.valueOf(rightSeekBarState.value)
                if (TrpSeekBarUtils.compareFloat(rightSeekBar!!.currPercent, minProgress) == 0) {
                    rightSeekBarState.isMin = true
                } else if (TrpSeekBarUtils.compareFloat(
                        rightSeekBar!!.currPercent,
                        maxProgress
                    ) == 0
                ) {
                    rightSeekBarState.isMax = true
                }
            }
            return arrayOf(leftSeekBarState, rightSeekBarState)
        }

    override fun setEnabled(enabled: Boolean) {
        super.setEnabled(enabled)
        isEnable = enabled
    }

    fun setIndicatorText(progress: String?) {
        leftSeekBar!!.setIndicatorText(progress)
        if (seekBarMode == SEEKBAR_MODE_RANGE) {
            rightSeekBar!!.setIndicatorText(progress)
        }
    }

    /**
     * format number indicator text
     *
     * @param formatPattern format rules
     */
    fun setIndicatorTextDecimalFormat(formatPattern: String?) {
        leftSeekBar!!.setIndicatorTextDecimalFormat(formatPattern)
        if (seekBarMode == SEEKBAR_MODE_RANGE) {
            rightSeekBar!!.setIndicatorTextDecimalFormat(formatPattern)
        }
    }

    /**
     * format string indicator text
     *
     * @param formatPattern format rules
     */
    fun setIndicatorTextStringFormat(formatPattern: String?) {
        leftSeekBar!!.indicatorTextStringFormat = (formatPattern)
        if (seekBarMode == SEEKBAR_MODE_RANGE) {
            rightSeekBar!!.indicatorTextStringFormat = (formatPattern)
        }
    }

    fun setProgressColor(
        @ColorInt progressDefaultColor: Int,
        @ColorInt progressColor: Int
    ) {
        this.progressDefaultColor = progressDefaultColor
        this.progressColor = progressColor
    }

    fun getSeekBarMode(): Int {
        return seekBarMode
    }

    /**
     * [.SEEKBAR_MODE_SINGLE] is single SeekBar
     * [.SEEKBAR_MODE_RANGE] is range SeekBar
     * @param seekBarMode
     */
    fun setSeekBarMode(@SeekBarModeDef seekBarMode: Int) {
        this.seekBarMode = seekBarMode
        rightSeekBar?.isVisible = (seekBarMode != SEEKBAR_MODE_SINGLE)
    }

    fun getProgressDrawableId(): Int {
        return progressDrawableId
    }

    fun setProgressDrawableId(@DrawableRes progressDrawableId: Int) {
        this.progressDrawableId = progressDrawableId
        progressBitmap = null
        initProgressBitmap()
    }

    fun getProgressDefaultDrawableId(): Int {
        return progressDefaultDrawableId
    }

    fun setProgressDefaultDrawableId(@DrawableRes progressDefaultDrawableId: Int) {
        this.progressDefaultDrawableId = progressDefaultDrawableId
        progressDefaultBitmap = null
        initProgressBitmap()
    }

    fun setTypeface(typeFace: Typeface?) {
        paint.typeface = typeFace
    }

    fun getStepsDrawableId(): Int {
        return stepsDrawableId
    }

    fun setStepsDrawableId(@DrawableRes stepsDrawableId: Int) {
        stepsBitmaps.clear()
        this.stepsDrawableId = stepsDrawableId
        initStepsBitmap()
    }

    fun setStepsDrawable(stepsDrawableIds: List<Int?>?) {
        require(!(stepsDrawableIds == null || stepsDrawableIds.isEmpty() || stepsDrawableIds.size <= steps)) { "stepsDrawableIds must > steps !" }
        require(verifyStepsMode()) { "stepsWidth must > 0, stepsHeight must > 0,steps must > 0 First!!" }
        val stepsBitmaps: MutableList<Bitmap> = ArrayList()
        for (i in stepsDrawableIds.indices) {
            stepsDrawableIds[i]?.let {
                TrpSeekBarUtils.drawableToBitmap(
                    context, stepsWidth.toInt(), stepsHeight.toInt(), it
                )?.let { bitmap -> stepsBitmaps.add(bitmap) }
            }
        }
        this.stepsBitmaps = stepsBitmaps
    }

    companion object {
        private const val MIN_INTERCEPT_DISTANCE = 100

        //normal seekBar mode
        const val SEEKBAR_MODE_SINGLE = 1

        //TrpSeekBar
        const val SEEKBAR_MODE_RANGE = 2

        //number according to the actual proportion of the number of arranged;
        const val TRICK_MARK_MODE_NUMBER = 0

        //other equally arranged
        const val TRICK_MARK_MODE_OTHER = 1

        //tick mark text gravity
        const val TICK_MARK_GRAVITY_LEFT = 0
        const val TICK_MARK_GRAVITY_CENTER = 1
        const val TICK_MARK_GRAVITY_RIGHT = 2
    }

    init {
        initAttrs(attrs, defStyle)
        initPaint()
        initSeekBar(attrs)
        initStepsBitmap()
        applyAttr()
    }

    private fun applyAttr() {
        when (seekBarMode) {
            SEEKBAR_MODE_SINGLE -> setProgress(trpProgressPrimary)
            SEEKBAR_MODE_RANGE -> setProgress(trpProgressPrimary, _trpProgressSecondary)
        }
    }
}
