package com.chungld.uipack.blur

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import android.os.Build
import android.renderscript.Allocation
import android.renderscript.Element
import android.renderscript.RenderScript
import android.renderscript.ScriptIntrinsicBlur
import androidx.annotation.RequiresApi
import com.chungld.uipack.blur.BlurController.Companion.DEFAULT_SCALE_FACTOR

class RenderScriptBlur @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1) constructor(context: Context?) :
    BlurAlgorithm {
    private val paint = Paint(Paint.FILTER_BITMAP_FLAG)
    private val renderScript: RenderScript
    private val blurScript: ScriptIntrinsicBlur
    private var outAllocation: Allocation? = null
    private var lastBitmapWidth = -1
    private var lastBitmapHeight = -1
    private fun canReuseAllocation(bitmap: Bitmap): Boolean {
        return bitmap.height == lastBitmapHeight && bitmap.width == lastBitmapWidth
    }

    /**
     * @param bitmap     bitmap to blur
     * @param blurRadius blur radius (1..25)
     * @return blurred bitmap
     */
    override fun blur(bitmap: Bitmap?, blurRadius: Float): Bitmap? {
        bitmap ?: return bitmap
        val inAllocation = Allocation.createFromBitmap(renderScript, bitmap)
        if (!canReuseAllocation(bitmap)) {
            if (outAllocation != null) {
                outAllocation!!.destroy()
            }
            outAllocation = Allocation.createTyped(renderScript, inAllocation.type)
            lastBitmapWidth = bitmap.width
            lastBitmapHeight = bitmap.height
        }
        blurScript.setRadius(blurRadius)
        blurScript.setInput(inAllocation)
        //do not use inAllocation in forEach. it will cause visual artifacts on blurred Bitmap
        blurScript.forEach(outAllocation)
        outAllocation!!.copyTo(bitmap)
        inAllocation.destroy()
        return bitmap
    }

    override fun destroy() {
        blurScript.destroy()
        renderScript.destroy()
        if (outAllocation != null) {
            outAllocation!!.destroy()
        }
    }

    override fun canModifyBitmap(): Boolean {
        return true
    }

    override val supportedBitmapConfig: Bitmap.Config
        get() = Bitmap.Config.ARGB_8888

    override fun scaleFactor(): Float {
        return DEFAULT_SCALE_FACTOR
    }

    override fun render(canvas: Canvas, bitmap: Bitmap) {
        canvas.drawBitmap(bitmap, 0f, 0f, paint)
    }

    /**
     * @param context Context to create the [RenderScript]
     */
    init {
        renderScript = RenderScript.create(context)
        blurScript = ScriptIntrinsicBlur.create(renderScript, Element.U8_4(renderScript))
    }
}