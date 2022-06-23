package com.chungld.uipack.nav

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
import androidx.core.content.ContextCompat
import android.util.AttributeSet
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import com.chungld.uipack.R
import com.chungld.uipack.common.TrpStroke
import com.chungld.uipack.common.TrpText
import com.chungld.uipack.rating.indicator.TrpRatingPoint
import com.chungld.uipack.tab.updateBadgeSize

/**
 * Extend from [FrameLayout]
 */
open class TrpNavigationBadge @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    private val badgeSize = context.resources.getDimensionPixelSize(R.dimen.trpBadgeSize)

    private var _badge = TrpRatingPoint(context).apply {
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
            LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT)
                .apply {
                    gravity = Gravity.CENTER_HORIZONTAL
                    setMargins(badgeSize * 2, (badgeSize * 1.5f).toInt(), 0, 0)
                }
        this.layoutParams = layoutParams
        visibility = View.GONE
    }

    /**
     * it is [TrpRatingPoint]
     */
    var badge
        get() = _badge
        private set(value) {
            _badge = value
        }

    init {
        initialization()
    }

    private fun initialization() {
        addView(badge)
        layoutParams = ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        )
        updateBadgeSize(_badge, badgeSize)
    }

    /**
     * Show badge(true/false) = show/hide
     */
    fun showBadge(show: Boolean): TrpNavigationBadge {
        badge.visibility = if (show) View.VISIBLE else View.GONE
        return this
    }

    /**
     * set content to badge. Ex: "99"
     */
    fun setBadgeContent(content: String): TrpNavigationBadge {
        _badge.text = content
        updateBadgeSize(_badge, badgeSize)
        return this
    }

}