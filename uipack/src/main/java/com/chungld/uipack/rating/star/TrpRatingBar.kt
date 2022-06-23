package com.chungld.uipack.rating.star

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Bitmap
import android.graphics.BitmapShader
import android.graphics.Canvas
import android.graphics.Shader
import android.graphics.drawable.*
import android.graphics.drawable.shapes.RoundRectShape
import android.graphics.drawable.shapes.Shape
import androidx.annotation.IntDef
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import androidx.appcompat.widget.AppCompatRatingBar
import android.util.AttributeSet
import android.util.TypedValue
import android.view.Gravity
import com.chungld.uipack.R
import com.chungld.uipack.rating.star.TrpRatingBar.Companion.SIZE_NORMAL
import com.chungld.uipack.rating.star.TrpRatingBar.Companion.SIZE_SMALL
import com.chungld.uipack.utils.use

private val mapSize = mapOf(
    SIZE_SMALL to R.dimen.trp_rating_small,
    SIZE_NORMAL to R.dimen.trp_rating_normal
)

/**
 * Extend from [AppCompatRatingBar] to show rating like star
 * #
 * Style default: [R.attr.TrpChipStyle] has no parent
 */
open class TrpRatingBar @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = R.attr.TrpRatingBarStyle
) : AppCompatRatingBar(context, attrs, defStyleAttr) {

    private val drawableShape: Shape
        get() {
            val roundedCorners = floatArrayOf(5f, 5f, 5f, 5f, 5f, 5f, 5f, 5f)
            return RoundRectShape(roundedCorners, null, null)
        }
    private var sample: Bitmap? = null

    private var _trpSize: Int = SIZE_NORMAL

    /**
     * value from [RatingSize] or dimensions
     */
    @RatingSize
    var trpSize
        get() = _trpSize
        set(@RatingSize value) {
            _trpSize = value
        }

    private var _trpDrawableProcess: Drawable? = null

    /**
     * drawable when highLight
     */
    var drawableProcess
        get() = _trpDrawableProcess
        set(value) {
            _trpDrawableProcess = value?.mutate()
            updateProgressDrawable()
        }

    private var _trpDrawableSecondary: Drawable? = null

    /**
     * drawable below highLight
     */
    var drawableSecondary
        get() = _trpDrawableSecondary
        set(value) {
            _trpDrawableSecondary = value?.mutate()
            updateProgressDrawable()
        }

    private var _trpDrawableBackground: Drawable? = null

    /**
     * drawable background will be show when non-highLight
     */
    var drawableBackground
        get() = _trpDrawableBackground
        set(value) {
            _trpDrawableBackground = value?.mutate()
            updateProgressDrawable()
        }

    private var _rating: Int = 0

    private var _trpProgressTint: ColorStateList? = null

    var progressTint
        get() = _trpProgressTint
        set(value) {
            _trpProgressTint = value
            updateDrawableTint(drawableProcess, progressTint)
            updateProgressDrawable()
        }

    private fun updateDrawableTint(drawable: Drawable?, tint: ColorStateList?) {
        tint?.let { tintColor -> drawable?.let { DrawableCompat.setTintList(it, tintColor) } }
    }

    private var _trpProgressSecondaryTint: ColorStateList? = null
    var progressSecondaryTint
        get() = _trpProgressSecondaryTint
        set(value) {
            _trpProgressSecondaryTint = value
            updateDrawableTint(drawableSecondary, progressSecondaryTint)
            updateProgressDrawable()
        }

    private var _trpBackgroundTint: ColorStateList? = null
    var backgroundTint
        get() = _trpBackgroundTint
        set(value) {
            _trpBackgroundTint = value
            updateDrawableTint(drawableBackground, backgroundTint)
            updateProgressDrawable()
        }

    init {
        initialization(attrs, defStyleAttr)
        updateView()
    }

    private fun updateView() {
        updateDrawableTint(drawableProcess, progressTint)
        updateDrawableTint(drawableSecondary, progressSecondaryTint)
        updateDrawableTint(drawableBackground, backgroundTint)
        updateProgressDrawable()
    }

    private fun initialization(attrs: AttributeSet?, defStyleAttr: Int) {
        context.theme.obtainStyledAttributes(attrs, R.styleable.TrpRatingBar, defStyleAttr, 0).use {
            it.getDrawable(R.styleable.TrpRatingBar_trpDrawableProgress)
                ?.let { drawable -> _trpDrawableProcess = drawable.mutate() }
                ?: run {
                    _trpDrawableProcess =
                        ContextCompat.getDrawable(context, R.drawable.trp_ic_star_rating_default)
                            ?.mutate()
                }

            it.getDrawable(R.styleable.TrpRatingBar_trpDrawableSecondary)
                ?.let { drawable -> _trpDrawableSecondary = drawable.mutate() }
                ?: run {
                    _trpDrawableSecondary = _trpDrawableProcess?.constantState?.newDrawable()
                }

            it.getDrawable(R.styleable.TrpRatingBar_trpDrawableBackground)?.let { drawable ->
                _trpDrawableBackground = drawable.mutate()
            } ?: run {
                _trpDrawableBackground = _trpDrawableSecondary?.constantState?.newDrawable()
            }

            if (it.hasValue(R.styleable.TrpRatingBar_trpRatingSize)) {
                val typedValue = TypedValue()
                it.getValue(R.styleable.TrpRatingBar_trpRatingSize, typedValue)
                _trpSize =
                    if (typedValue.type == TypedValue.TYPE_DIMENSION) it.getDimensionPixelSize(
                        R.styleable.TrpRatingBar_trpRatingSize, 0
                    ) else it.getInt(R.styleable.TrpRatingBar_trpRatingSize, SIZE_NORMAL)
            }

            _trpProgressTint =
                it.getColorStateList(R.styleable.TrpRatingBar_trpDrawableProgressTint)

            _trpProgressSecondaryTint =
                it.getColorStateList(R.styleable.TrpRatingBar_trpDrawableSecondaryTint)

            _trpBackgroundTint =
                it.getColorStateList(R.styleable.TrpRatingBar_trpDrawableBackgroundTint)

            rating = it.getFloat(R.styleable.TrpRatingBar_android_rating, 0f)
        }
    }

    private fun updateProgressDrawable() {
        drawableProcess?.let { applyDrawableToProcessDrawable(android.R.id.progress, it, true) }

        _trpDrawableSecondary =
            if (drawableSecondary == null) drawableProcess else drawableSecondary
        (drawableSecondary)?.let {
            it.alpha = 75
            applyDrawableToProcessDrawable(
                android.R.id.secondaryProgress, it, true
            )
        }

        _trpDrawableBackground =
            if (drawableBackground == null) drawableSecondary else drawableBackground
        (drawableBackground)?.let {
            applyDrawableToProcessDrawable(
                android.R.id.background, it, false
            )
        }
        notifyProgressDrawable()
    }

    private fun notifyProgressDrawable() {
        progressDrawable = progressDrawable
        notifyProgress()
    }

    /**
     * this suck notify progress value but it work =))
     */
    private fun notifyProgress() {
        progress += 1
        progress -= 1
    }

    private fun applyDrawableToProcessDrawable(
        id: Int, drawable: Drawable, clip: Boolean
    ) {
        val tileBitmap = getBitmapDrawableFromVectorDrawable(drawable.mutate()).bitmap
        sample = tileBitmap
        val shapeDrawable = ShapeDrawable(drawableShape)
        val bitmapShader = BitmapShader(
            tileBitmap,
            Shader.TileMode.REPEAT, Shader.TileMode.CLAMP
        )
        shapeDrawable.paint.shader = bitmapShader
        (progressDrawable as LayerDrawable).setDrawableByLayerId(
            id, if (clip) createClipDrawable(shapeDrawable) else shapeDrawable
        )
    }

    private fun createClipDrawable(shapeDrawable: ShapeDrawable): ClipDrawable {
        return ClipDrawable(shapeDrawable, Gravity.START, ClipDrawable.HORIZONTAL)
    }

    private fun getBitmapDrawableFromVectorDrawable(drawable: Drawable): BitmapDrawable {
        val width = getSizePx(_trpSize, drawable.intrinsicWidth)
        val height = (width * (drawable.intrinsicHeight * 1.0 / drawable.intrinsicWidth)).toInt()
        val bitmap = Bitmap.createBitmap(
            width + (2).toInt(), height, Bitmap.Config.ARGB_8888
        )
        val canvas = Canvas(bitmap)
        drawable.setBounds(0, 0, width, height)
        drawable.draw(canvas)
        return BitmapDrawable(resources, bitmap)
    }

    @Synchronized
    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        if (sample != null) {
            val width = sample!!.width * numStars
            val height = sample!!.height
            setMeasuredDimension(
                resolveSizeAndState(width, widthMeasureSpec, 0),
                height
            )
        }
    }

    private fun getSizePx(@RatingSize size: Int, def: Int): Int {
        return when {
            size < 0 -> mapSize[size]?.let { context.resources.getDimensionPixelSize(it) } ?: def
            size != 0 -> size
            else -> def
        }
    }

    @IntDef(SIZE_SMALL, SIZE_NORMAL)
    annotation class RatingSize
    companion object {
        const val SIZE_SMALL = -1
        const val SIZE_NORMAL = -2
    }
}