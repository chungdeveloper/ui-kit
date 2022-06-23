package com.chungld.uipack.spinner

import android.content.Context
import android.content.res.ColorStateList
import androidx.annotation.IntDef
import androidx.core.content.ContextCompat
import androidx.core.widget.TextViewCompat
import android.text.TextUtils
import android.text.method.LinkMovementMethod
import android.util.AttributeSet
import android.view.Gravity
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import com.chungld.uipack.R
import com.chungld.uipack.common.TrpText
import com.chungld.uipack.text.TrpTextView
import com.chungld.uipack.theme.wrapContextWithDefaults
import com.chungld.uipack.utils.getHtmlText
import com.chungld.uipack.utils.removeUnderLine
import com.chungld.uipack.utils.use

/**
 * Extend from [LinearLayout]
 * #
 * Style default: [R.attr.TrpSpinnerStyle] with parent [R.style.trpSpinnerStyleDef]
 */
open class TrpSpinner @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = R.attr.TrpSpinnerStyle
) : LinearLayout(wrapContextWithDefaults(context), attrs, defStyle) {

    /**
     * values: [PRG_STYLE_FULL], [PRG_STYLE_LOAD_MORE]
     */
    @IntDef(PRG_STYLE_FULL, PRG_STYLE_LOAD_MORE)
    annotation class ProgressStyle

    /**
     * values: [PRG_POS_TOP], [PRG_POS_LEFT], [PRG_POS_BOTTOM], [PRG_POS_RIGHT]
     */
    @IntDef(PRG_POS_TOP, PRG_POS_LEFT, PRG_POS_BOTTOM, PRG_POS_RIGHT)
    annotation class ProgressPosition
    companion object {
        const val PRG_POS_TOP = 0
        const val PRG_POS_LEFT = 1
        const val PRG_POS_BOTTOM = 2
        const val PRG_POS_RIGHT = 3

        const val PRG_STYLE_FULL = 0
        const val PRG_STYLE_LOAD_MORE = 1
    }

    private val progressbarLoadMoreHeight =
        context.resources.getDimensionPixelSize(R.dimen.trpSizeNormal)
    private val progressPadding = context.resources.getDimensionPixelSize(R.dimen.trpIconPadding)

    /**
     * progressbar with id = [R.id.trp_progressbar_indicator]
     */
    var trpProgressBar = ProgressBar(context).apply { id = R.id.trp_progressbar_indicator }
        private set

    /**
     * content text of progressbar is singleLine and has ellipsize at end
     */
    var trpProgressText = TrpTextView(context).apply {
        id = R.id.trp_progressbar_text
        setSingleLine()
        ellipsize = TextUtils.TruncateAt.END
        layoutParams = LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT)
    }
        private set

    /**
     * Text position with progress has values [ProgressPosition]
     */
    @ProgressPosition
    var trpTextPosition = PRG_POS_RIGHT
        set(@ProgressPosition value) {
            field = value
            updateTextPosition()
        }

    var text: String? = null
        set(value) {
            field = value
            trpProgressText.text = value
        }

    /**
     * has 2 values: [ProgressStyle]
     */
    @ProgressStyle
    var trpProgressStyle = PRG_STYLE_FULL
        set(@ProgressStyle value) {
            field = value
            updateProgressStyle()
        }

    private fun updateProgressStyle() {
        val layoutParams = trpProgressBar.layoutParams as LayoutParams
        if (trpProgressStyle == PRG_STYLE_LOAD_MORE) {
            layoutParams.width = progressbarLoadMoreHeight
            layoutParams.height = progressbarLoadMoreHeight
        } else {
            layoutParams.width = ViewGroup.LayoutParams.WRAP_CONTENT
            layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT
        }
        trpProgressBar.layoutParams = layoutParams
    }

    private fun updateTextPosition() {
        removeAllViews()
        when (trpTextPosition) {
            PRG_POS_TOP, PRG_POS_LEFT -> {
                addView(trpProgressText)
                addView(trpProgressBar)
            }
            PRG_POS_BOTTOM, PRG_POS_RIGHT -> {
                addView(trpProgressBar)
                addView(trpProgressText)
            }
        }
        orientation = if (trpTextPosition == PRG_POS_TOP || trpTextPosition == PRG_POS_BOTTOM)
            VERTICAL else HORIZONTAL
    }

    private var textColorLink: ColorStateList? = null
    private var textColour: ColorStateList? = null
    private var linkUnderLineValue: Boolean? = true

    var linkUnderLine
        get() = linkUnderLineValue
        set(value) {
            linkUnderLineValue = value
            updateView()
        }

    @TrpText.Styles
    private var textStyleValue: Int = TrpText.BODY2
    var textStyle
        get() = textStyleValue
        set(value) {
            textStyleValue = value
            updateView()
        }

    private var htmlTextValue: String? = null
    var htmlText
        get() = htmlTextValue
        set(value) {
            htmlTextValue = value
            updateHtmlContent()
        }

    private fun updateHtmlContent() {
        htmlText?.let {
            trpProgressText.isAllCaps = false
            trpProgressText.setText(getHtmlText(it), TextView.BufferType.SPANNABLE)
            trpProgressText.movementMethod = LinkMovementMethod.getInstance()
            if (linkUnderLineValue!!) return
            trpProgressText.text = removeUnderLine(trpProgressText.text)
        }
    }

    private var weightValue: TrpText.Weight = TrpText.Weight.DEFAULT
    var weight
        get() = weightValue
        set(value) {
            weightValue = value
            updateView()
        }

    init {
        initialization(context, attrs, defStyle)
        updateView()
    }

    private fun initialization(context: Context, attrs: AttributeSet?, defStyle: Int) {
        isClickable = true
        isFocusable = true
        gravity = Gravity.CENTER
        orientation = VERTICAL
        this.removeAllViews()
        this.addView(trpProgressBar)
        this.addView(trpProgressText)
        this.setPadding(progressPadding, progressPadding, progressPadding, progressPadding)

        context.theme.obtainStyledAttributes(attrs, R.styleable.TrpSpinner, defStyle, 0).use {
            trpTextPosition = it.getInt(R.styleable.TrpSpinner_trpTextPosition, PRG_POS_RIGHT)
            trpProgressStyle = it.getInt(R.styleable.TrpSpinner_trpProgressStyle, PRG_STYLE_FULL)
            text = it.getString(R.styleable.TrpSpinner_android_text)

            textStyleValue = it.getInt(R.styleable.TrpSpinner_trpTextStyle, TrpText.BODY2)
            val weight = it.getInt(R.styleable.TrpSpinner_trpWeight, -1)
            weightValue =
                if (weight != -1) TrpText.Weight.values()[weight] else TrpText.Weight.DEFAULT
            if (it.hasValue(R.styleable.TrpSpinner_android_textColor)) {
                textColour = it.getColorStateList(R.styleable.TrpSpinner_android_textColor)
            }
            textColorLink = if (it.hasValue(R.styleable.TrpSpinner_trpColorLink)) {
                it.getColorStateList(R.styleable.TrpSpinner_trpColorLink)
            } else ContextCompat.getColorStateList(context, R.color.trpColorTextBaseLink)
            htmlTextValue = it.getString(R.styleable.TrpSpinner_trpHtmlText)
            linkUnderLineValue = it.getBoolean(R.styleable.TrpSpinner_trpHtmlUnderLine, true)
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        layoutParams.width = LayoutParams.MATCH_PARENT
        layoutParams.height =
            if (trpProgressStyle == PRG_STYLE_FULL) LayoutParams.MATCH_PARENT else LayoutParams.WRAP_CONTENT
        layoutParams = layoutParams
    }

    private fun updateView() {
        updateProgressStyle()
        updateTextPosition()
        setupTextView()
    }

    private fun setupTextView() {
        val textAppearance = TrpText.getTextStyleId(context, textStyleValue, weightValue)
        TextViewCompat.setTextAppearance(trpProgressText, textAppearance)
        textColour?.let(trpProgressText::setTextColor)
        textColorLink?.let(trpProgressText::setLinkTextColor)
        updateHtmlContent()
    }
}