package com.chungld.uipack.tab

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.os.Build
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import android.util.AttributeSet
import android.util.TypedValue
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.LinearLayout
import com.chungld.uipack.R
import com.chungld.uipack.common.TrpStroke
import com.chungld.uipack.common.TrpText
import com.chungld.uipack.icon.TrpIcon
import com.chungld.uipack.rating.indicator.TrpRatingPoint
import com.chungld.uipack.text.TrpTextView
import com.chungld.uipack.theme.wrapContextWithDefaults
import com.chungld.uipack.utils.use

/**
 * Extend from [LinearLayout]
 */
open class TrpBadgeView @JvmOverloads constructor(
    context: Context,
    attributes: AttributeSet? = null,
    defStyleAttributes: Int = 0
) : ConstraintLayout(wrapContextWithDefaults(context), attributes, defStyleAttributes) {

    private val contentMargin = context.resources.getDimensionPixelSize(R.dimen.trpBadgeMargin)
    private val badgeSize = context.resources.getDimensionPixelSize(R.dimen.trpBadgeSize)
    private val iconMargin = contentMargin / 2

    val trpTextContent = TrpTextView(context).apply {
        id = R.id.trpTabContent
        trpTextStyle = TrpText.BODY2
        gravity = Gravity.CENTER
        setSingleLine()
        layoutParams = LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT)
        visibility = View.GONE
    }

    val trpBadge = TrpRatingPoint(context).apply {
        id = R.id.trpTabBadge
        trpTextStyle = TrpText.NOTIFY
        setTextColor(Color.WHITE)
        gravity = Gravity.CENTER
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            textAlignment = View.TEXT_ALIGNMENT_CENTER
        }
        cornerStyle = TrpStroke.ROUND
        trpBackground =
            ColorDrawable(ContextCompat.getColor(context, android.R.color.holo_red_light))
        val layoutParams =
            FrameLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT).apply {
                gravity = Gravity.END
            }
        this.layoutParams = layoutParams
        visibility = View.GONE
    }

    private val trpIcon = TrpIcon(context).apply {
        id = R.id.trpTabIcon
        adjustViewBounds = true
        layoutParams = LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT).apply {
            setMargins(iconMargin, iconMargin, iconMargin, 0)
        }
        visibility = View.GONE
    }

    private val trpContentTab = FrameLayout(context).apply {
        id = R.id.trpTabFrame
        addView(trpIcon)
        addView(trpBadge)
        layoutParams = LayoutParams(LayoutParams.WRAP_CONTENT, 0)
        visibility = View.VISIBLE
    }

    private var _icon: Drawable? = null
    var tabIcon
        get() = _icon
        set(value) {
            value?.let {
                _icon = value
                trpIcon.setImageDrawable(_icon)
                trpIcon.visibility = View.VISIBLE
                trpTextContent.trpTextStyle = TrpText.CAPTION
            } ?: run {
                trpTextContent.trpTextStyle = TrpText.BODY2
                trpIcon.visibility = View.GONE
            }
            updateBadgePosition()
            updateTranslation()
        }

    var trpTextStyle: Int? = trpTextContent.trpTextStyle
        set(value) {
            value?.let {
                field = it
                trpTextContent.trpTextStyle = it
            }
        }

    var trpWeight: TrpText.Weight? = trpTextContent.trpWeight
        set(value) {
            value?.let {
                field = it
                trpTextContent.trpWeight = it
            }
        }

    var trpHtmlText: String? = null
        set(value) {
            value?.let {
                field = it
                trpTextContent.trpHtmlText = it
            }
        }

    var textAllCaps: Boolean? = null
        set(value) {
            value?.let {
                field = it
                trpTextContent.isAllCaps = it
            }
        }

    var trpIconSize: Int? = trpIcon.trpIconSize
        set(value) {
            value?.let {
                field = it
                trpIcon.trpIconSize = it
            }
        }

    var trpRatio: String? = trpIcon.trpRatio
        set(value) {
            value?.let {
                field = it
                trpIcon.trpRatio = it
            }
        }

    private var _trpIconTint: ColorStateList? = null
    var trpIconTint
        get() = _trpIconTint
        set(value) {
            _trpIconTint = value
            updateIconTint()
        }

    init {
        initialization(attributes, defStyleAttributes)
    }

    private fun initialization(
        attributes: AttributeSet?,
        defStyleAttributes: Int
    ) {

        layoutParams = MarginLayoutParams(
            ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT
        ).apply {
//            setMargins(0, contentMargin / 2, 0, contentMargin)
        }
        this.addView(trpTextContent)
        this.addView(trpContentTab)

        updateBadgeSize(trpBadge, badgeSize)
        context.theme.obtainStyledAttributes(
            attributes, R.styleable.TrpBadgeView, defStyleAttributes, 0
        ).use {
            showBadge(it.getBoolean(R.styleable.TrpBadgeView_showBadge, false))
            tabIcon = it.getDrawable(R.styleable.TrpBadgeView_tabIcon)
            setBadgeContent(it.getString(R.styleable.TrpBadgeView_badgeContent))
            text = (it.getString(R.styleable.TrpBadgeView_android_text))

            trpTextStyle =
                it.getInt(R.styleable.TrpBadgeView_trpTextStyle, trpTextContent.trpTextStyle)

            val weight = it.getInt(R.styleable.TrpBadgeView_trpWeight, -1)
            trpWeight =
                if (weight != -1) TrpText.Weight.values()[weight] else TrpText.Weight.DEFAULT

            if (it.hasValue(R.styleable.TrpBadgeView_android_textColor)) {
                setTextColor(it.getColorStateList(R.styleable.TrpBadgeView_android_textColor))
            }

            trpHtmlText = it.getString(R.styleable.TrpBadgeView_trpHtmlText)
            textAllCaps = it.getBoolean(
                R.styleable.TrpBadgeView_android_textAllCaps,
                trpTextContent.getTextAllCaps()?.let { allCaps -> allCaps } ?: false
            )

            trpIconSize = if (it.hasValue(R.styleable.TrpBadgeView_trpIconSize)) {
                val sizeValue = TypedValue()
                it.getValue(R.styleable.TrpBadgeView_trpIconSize, sizeValue)
                if (sizeValue.type == TypedValue.TYPE_DIMENSION) it.getDimensionPixelSize(
                    R.styleable.TrpBadgeView_trpIconSize,
                    0
                ) else it.getInt(R.styleable.TrpBadgeView_trpIconSize, 0)
            } else null

            trpRatio = it.getString(R.styleable.TrpBadgeView_trpRatio)

            trpIconTint = it.getColorStateList(R.styleable.TrpBadgeView_trpIconTint)
        }
    }

    private fun updateIconTint() {
        trpIconTint?.let {
            _icon?.mutate()?.let { DrawableCompat.setTintList(it, _trpIconTint) }
        }
    }

    private fun updateBadgePosition() {
        val topMargin: Int
        val badgeLayoutParams: ViewGroup.LayoutParams
        if (trpIcon.visibility == View.VISIBLE) {
            topMargin = contentMargin / 2
            badgeLayoutParams = FrameLayout.LayoutParams(
                trpBadge.layoutParams.width, trpBadge.layoutParams.height
            ).apply { gravity = Gravity.END }
            (layoutParams as MarginLayoutParams).setMargins(0, topMargin, 0, contentMargin)
        } else {
            topMargin = 0
            badgeLayoutParams =
                FrameLayout.LayoutParams(trpBadge.layoutParams.width, trpBadge.layoutParams.height)
                    .apply { gravity = Gravity.END }
            (layoutParams as MarginLayoutParams).setMargins(0, topMargin, 0, 0)
        }
        createConstraintSet(trpIcon.visibility == View.VISIBLE).applyTo(this)
        trpBadge.layoutParams = badgeLayoutParams
    }

    private fun createConstraintSet(hasIcon: Boolean): ConstraintSet {
        val set = ConstraintSet()
        set.clone(this)
        set.clear(trpContentTab.id, ConstraintSet.TOP)
        set.clear(trpTextContent.id, ConstraintSet.TOP)
        //int startID, int startSide, int endID, int endSide
        set.connect(trpContentTab.id, ConstraintSet.LEFT, trpTextContent.id, ConstraintSet.LEFT)
        set.connect(
            trpContentTab.id, ConstraintSet.RIGHT, trpTextContent.id, ConstraintSet.RIGHT
        )
        set.connect(
            trpContentTab.id, ConstraintSet.BOTTOM, trpTextContent.id, ConstraintSet.TOP
        )
        set.connect(
            trpTextContent.id, ConstraintSet.LEFT, ConstraintSet.PARENT_ID, ConstraintSet.LEFT
        )
        set.connect(
            trpTextContent.id, ConstraintSet.RIGHT, ConstraintSet.PARENT_ID, ConstraintSet.RIGHT
        )
        set.connect(
            trpTextContent.id, ConstraintSet.BOTTOM,
            ConstraintSet.PARENT_ID, ConstraintSet.BOTTOM
        )
        if (!hasIcon) {
            set.connect(
                trpTextContent.id, ConstraintSet.TOP, ConstraintSet.PARENT_ID, ConstraintSet.TOP
            )
        } else if (this.layoutParams != null &&
            this.layoutParams.height == ViewGroup.LayoutParams.MATCH_PARENT || this.layoutParams.height >= 0
        ) {
            set.connect(
                trpContentTab.id, ConstraintSet.TOP, ConstraintSet.PARENT_ID, ConstraintSet.TOP
            )
        }
        return set
    }

    /**
     * set content to Badge
     */
    fun setBadgeContent(content: String?) {
        trpBadge.text = content
        updateBadgeSize(trpBadge, badgeSize)
    }

    /**
     * show badge
     */
    fun showBadge(isShow: Boolean) {
        trpBadge.visibility = if (isShow) View.VISIBLE else View.INVISIBLE
        updateBadgeSize(trpBadge, badgeSize)
    }

    fun setTextColor(tabTextColors: ColorStateList?) {
        trpTextContent.setTrpTextColor(tabTextColors)
    }

    /**
     * Text to contentTab
     */
    var text: CharSequence?
        get() = trpTextContent.text
        set(value) {
            trpTextContent.text = value
            if (value.isNullOrEmpty()) {
                trpTextContent.visibility = View.GONE
                (trpContentTab.layoutParams as LayoutParams).apply {
                    setMargins(0, contentMargin, 0, contentMargin)
                }
            } else {
                trpTextContent.visibility = View.VISIBLE
                (trpContentTab.layoutParams as LayoutParams).apply {
                    setMargins(0, 0, 0, 0)
                }
            }
        }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        updateTranslation()
    }

    private fun updateTranslation() {
        if (trpIcon.visibility == View.VISIBLE) {
            trpContentTab.translationY = 0f
            trpContentTab.minimumWidth = 0
            return
        }
        trpContentTab.translationY = trpBadge.measuredHeight / 2.0f
        trpContentTab.minimumWidth = trpTextContent.measuredWidth + trpBadge.measuredHeight
    }
}


internal fun updateBadgeSize(trpBadge: TrpRatingPoint, badgeSize: Int): TrpRatingPoint {
    val textParams = trpBadge.layoutParams
    val size = if (trpBadge.text.isNotEmpty()) ViewGroup.LayoutParams.WRAP_CONTENT else badgeSize
    textParams.width = size
    textParams.height = size
    trpBadge.layoutParams = textParams
    return trpBadge
}