package com.chungld.uipack.blur

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.view.ViewGroup
import android.view.ViewTreeObserver
import androidx.annotation.ColorInt
import com.chungld.uipack.blur.BlurController.Companion.DEFAULT_BLUR_RADIUS

internal class PreDrawBlurController(
    blurView: BlurView,
    private val rootView: ViewGroup,
    @ColorInt overlayColor: Int,
    algorithm: BlurAlgorithm
) :
    BlurController {
    private var blurRadius: Float = DEFAULT_BLUR_RADIUS
    private val blurAlgorithm: BlurAlgorithm
    private var internalCanvas: BlurViewCanvas? = null
    private var internalBitmap: Bitmap? = null
    val blurView: BlurView
    private var overlayColor: Int
    private val rootLocation = IntArray(2)
    private val blurViewLocation = IntArray(2)
    private val drawListener =
        ViewTreeObserver.OnPreDrawListener { // Not invalidating a View here, just updating the Bitmap.
            // This relies on the HW accelerated bitmap drawing behavior in Android
            // If the bitmap was drawn on HW accelerated canvas, it holds a reference to it and on next
            // drawing pass the updated content of the bitmap will be rendered on the screen
            updateBlur()
            true
        }
    private var blurEnabled = true
    private var initialized = false
    private var frameClearDrawable: Drawable? = null
    fun init(measuredWidth: Int, measuredHeight: Int) {
        setBlurAutoUpdate(true)
        val sizeScaler = SizeScaler(blurAlgorithm.scaleFactor())
        if (sizeScaler.isZeroSized(measuredWidth, measuredHeight)) {
            // Will be initialized later when the View reports a size change
            blurView.setWillNotDraw(true)
            return
        }
        blurView.setWillNotDraw(false)
        val bitmapSize = sizeScaler.scale(measuredWidth, measuredHeight)
        internalBitmap = Bitmap.createBitmap(
            bitmapSize.width,
            bitmapSize.height,
            blurAlgorithm.supportedBitmapConfig
        )
        internalCanvas = BlurViewCanvas(internalBitmap ?: return)
        initialized = true
        // Usually it's not needed, because `onPreDraw` updates the blur anyway.
        // But it handles cases when the PreDraw listener is attached to a different Window, for example
        // when the BlurView is in a Dialog window, but the root is in the Activity.
        // Previously it was done in `draw`, but it was causing potential side effects and Jetpack Compose crashes
        updateBlur()
    }

    fun updateBlur() {
        if (!blurEnabled || !initialized) {
            return
        }
        if (frameClearDrawable == null) {
            internalBitmap!!.eraseColor(Color.TRANSPARENT)
        } else {
            frameClearDrawable!!.draw(internalCanvas!!)
        }
        internalCanvas!!.save()
        setupInternalCanvasMatrix()
        rootView.draw(internalCanvas)
        internalCanvas!!.restore()
        blurAndSave()
    }

    /**
     * Set up matrix to draw starting from blurView's position
     */
    private fun setupInternalCanvasMatrix() {
        rootView.getLocationOnScreen(rootLocation)
        blurView.getLocationOnScreen(blurViewLocation)
        val left = blurViewLocation[0] - rootLocation[0]
        val top = blurViewLocation[1] - rootLocation[1]

        // https://github.com/Dimezis/BlurView/issues/128
        val scaleFactorH = blurView.getHeight() as Float / internalBitmap!!.height
        val scaleFactorW = blurView.getWidth() as Float / internalBitmap!!.width
        val scaledLeftPosition = -left / scaleFactorW
        val scaledTopPosition = -top / scaleFactorH
        internalCanvas!!.translate(scaledLeftPosition, scaledTopPosition)
        internalCanvas!!.scale(1 / scaleFactorW, 1 / scaleFactorH)
    }

    override fun draw(canvas: Canvas?): Boolean {
        canvas ?: return false
        if (!blurEnabled || !initialized) {
            return true
        }
        // Not blurring itself or other BlurViews to not cause recursive draw calls
        // Related: https://github.com/Dimezis/BlurView/issues/110
        if (canvas is BlurViewCanvas) {
            return false
        }

        // https://github.com/Dimezis/BlurView/issues/128
        val scaleFactorH = blurView.getHeight() as Float / internalBitmap!!.height
        val scaleFactorW = blurView.getWidth() as Float / internalBitmap!!.width
        canvas.save()
        canvas.scale(scaleFactorW, scaleFactorH)
        blurAlgorithm.render(canvas, internalBitmap!!)
        canvas.restore()
        if (overlayColor != TRANSPARENT) {
            canvas.drawColor(overlayColor)
        }
        return true
    }

    private fun blurAndSave() {
        internalBitmap = blurAlgorithm.blur(internalBitmap, blurRadius)
        if (!blurAlgorithm.canModifyBitmap()) {
            internalCanvas!!.setBitmap(internalBitmap)
        }
    }

    override fun updateBlurViewSize() {
        val measuredWidth: Int = blurView.getMeasuredWidth()
        val measuredHeight: Int = blurView.getMeasuredHeight()
        init(measuredWidth, measuredHeight)
    }

    override fun destroy() {
        setBlurAutoUpdate(false)
        blurAlgorithm.destroy()
        initialized = false
    }

    override fun setBlurRadius(radius: Float): BlurViewFacade {
        blurRadius = radius
        return this
    }

    override fun setFrameClearDrawable(frameClearDrawable: Drawable?): BlurViewFacade {
        this.frameClearDrawable = frameClearDrawable
        return this
    }

    override fun setBlurEnabled(enabled: Boolean): BlurViewFacade {
        blurEnabled = enabled
        setBlurAutoUpdate(enabled)
        blurView.invalidate()
        return this
    }

    override fun setBlurAutoUpdate(enabled: Boolean): BlurViewFacade {
        rootView.viewTreeObserver.removeOnPreDrawListener(drawListener)
        if (enabled) {
            rootView.viewTreeObserver.addOnPreDrawListener(drawListener)
        }
        return this
    }

    override fun setOverlayColor(overlayColor: Int): BlurViewFacade {
        if (this.overlayColor != overlayColor) {
            this.overlayColor = overlayColor
            blurView.invalidate()
        }
        return this
    }

    companion object {
        @ColorInt
        val TRANSPARENT = 0
    }

    /**
     * @param blurView  View which will draw it's blurred underlying content
     * @param rootView  Root View where blurView's underlying content starts drawing.
     * Can be Activity's root content layout (android.R.id.content)
     * @param algorithm sets the blur algorithm
     */
    init {
        this.blurView = blurView
        this.overlayColor = overlayColor
        blurAlgorithm = algorithm
        val measuredWidth: Int = blurView.getMeasuredWidth()
        val measuredHeight: Int = blurView.getMeasuredHeight()
        init(measuredWidth, measuredHeight)
    }
}
