package com.chungld.uipack.indicator

import android.content.Context
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.viewpager.widget.ViewPager
import androidx.viewpager2.widget.ViewPager2
import com.chungld.uipack.R
import com.chungld.uipack.icon.TrpIcon
import com.chungld.uipack.theme.wrapContextWithDefaults
import com.chungld.uipack.utils.use

class TrpIndicator @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = R.attr.TrpIndicatorStyle
) : LinearLayout(wrapContextWithDefaults(context), attrs, defStyleAttr) {

    private var _indicator: Drawable? = null
    private var _indicatorSelected: Drawable? = null
    private var _indicatorWidth: Int = ViewGroup.LayoutParams.WRAP_CONTENT
    private var _indicatorHeight: Int = ViewGroup.LayoutParams.WRAP_CONTENT
    private var _indicatorSpacing: Int = 0
    private var onAdapterChangeListener: OnAdapterChangeListener? = null
    private var onPageChangeListener: OnPageChangeListener? = null
    private var currentSelectedIndex = 0
    private val indicatorEntries by lazy { mutableListOf<TrpIcon>() }

    /**
     * Check drawable by state_selected
     */
    var trpIndicatorDrawable
        get() = _indicator
        set(value) {
            _indicator = value
            updateDrawable()
        }

    var trpIndicatorSelectedDrawable
        get() = _indicatorSelected
        set(value) {
            _indicatorSelected = value
            updateDrawable()
        }

    var trpIndicatorWidth
        get() = _indicatorWidth
        set(value) {
            _indicatorWidth = value
            updateIndicatorSize()
        }

    var trpIndicatorHeight
        get() = _indicatorHeight
        set(value) {
            _indicatorHeight = value
            updateIndicatorSize()
        }

    var trpIndicatorSpacing
        get() = _indicatorSpacing * 2
        set(value) {
            _indicatorSpacing = value
            updateIndicatorSpacing()
        }


    init {
        initialization(attrs, defStyleAttr)
        updateView()
    }

    private fun initialization(attributes: AttributeSet?, defStyleAttributes: Int) {
        orientation = HORIZONTAL
        context.theme.obtainStyledAttributes(
            attributes, R.styleable.TrpIndicator, defStyleAttributes, 0
        ).use {
            _indicator = it.getDrawable(R.styleable.TrpIndicator_trp_indicator_drawable)
            _indicatorSelected =
                it.getDrawable(R.styleable.TrpIndicator_trp_indicator_drawable_selected)
            _indicatorWidth = it.getDimensionPixelSize(
                R.styleable.TrpIndicator_trp_indicator_width,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
            _indicatorHeight = it.getDimensionPixelSize(
                R.styleable.TrpIndicator_trp_indicator_height,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
            if (it.hasValue(R.styleable.TrpIndicator_trp_indicator_size)) {
                val size = it.getDimensionPixelSize(
                    R.styleable.TrpIndicator_trp_indicator_size,
                    ViewGroup.LayoutParams.WRAP_CONTENT
                )
                _indicatorWidth = size
                _indicatorHeight = size
            }

            _indicatorSpacing = it.getDimensionPixelSize(
                R.styleable.TrpIndicator_trp_indicator_spacing,
                context.resources.getDimensionPixelSize(R.dimen.trp_uipack_dip_8)
            ) / 2
        }
    }

    private fun updateView() {
        indicatorEntries.onEachIndexed { index, trpIcon ->
            updateEntrySpacing(index, updateEntryDrawable(index, updateEntrySize(index, trpIcon)))
            trpIcon.isEnabled = index == currentSelectedIndex
        }
    }

    private fun updateDrawable() = indicatorEntries.onEachIndexed(this::updateEntryDrawable)

    private fun updateEntryDrawable(index: Int, entry: TrpIcon): TrpIcon {
        entry.setImageDrawable(getDrawable(index))
        return entry
    }

    private fun getDrawable(index: Int) =
        if (index == currentSelectedIndex) trpIndicatorSelectedDrawable else trpIndicatorDrawable

    private fun updateIndicatorSize() = indicatorEntries.onEachIndexed(this::updateEntrySize)

    private fun updateIndicatorSpacing() = indicatorEntries.onEachIndexed(this::updateEntrySpacing)

    private fun updateEntrySize(index: Int, entry: TrpIcon): TrpIcon {
        entry.layoutParams = LayoutParams(_indicatorWidth, _indicatorHeight)
        entry.scaleType = ImageView.ScaleType.FIT_CENTER
        return entry
    }

    private fun updateEntrySpacing(index: Int, entry: TrpIcon): TrpIcon {
        entry.layoutParams = (entry.layoutParams as MarginLayoutParams).apply {
            setMargins(_indicatorSpacing, 0, _indicatorSpacing, 0)
        }
        return entry
    }

    fun setTrpIndicatorSize(value: Int) {
        _indicatorWidth = value
        _indicatorHeight = value
        updateIndicatorSize()
    }

    fun setupWithViewPager(viewPager: ViewPager) {
        onPageChangeListener?.let { listener ->
            viewPager.removeOnPageChangeListener(listener)
        }

        onAdapterChangeListener?.let { listener ->
            viewPager.removeOnAdapterChangeListener(listener)
        }

        onPageChangeListener = object : OnPageChangeListener() {
            override fun onPageChanged(position: Int) {
                if (position >= indicatorEntries.size) return
                indicatorEntries[currentSelectedIndex].isEnabled = false
                currentSelectedIndex = position
                indicatorEntries[currentSelectedIndex].isEnabled = true
            }
        }

        onAdapterChangeListener = object : OnAdapterChangeListener() {
            override fun onAdapterChangedListener(size: Int) {
                currentSelectedIndex = viewPager.currentItem
                initListIndicator(size)
            }

        }
        currentSelectedIndex = viewPager.currentItem
        onPageChangeListener?.let { viewPager.addOnPageChangeListener(it) }
        onAdapterChangeListener?.let { viewPager.addOnAdapterChangeListener(it) }
    }

    fun setupWithViewPager(viewPager: ViewPager2) {
        onPageChangeListener?.let { listener ->
            viewPager.unregisterOnPageChangeCallback(listener)
        }

        onPageChangeListener = object : OnPageChangeListener() {
            override fun onPageChanged(position: Int) {
                if (position >= indicatorEntries.size) return
                val oldPos = currentSelectedIndex
                currentSelectedIndex = position
                updateEntryDrawable(oldPos, indicatorEntries[oldPos])
                updateEntryDrawable(currentSelectedIndex, indicatorEntries[currentSelectedIndex])
            }
        }

        currentSelectedIndex = viewPager.currentItem
        onPageChangeListener?.let { viewPager.registerOnPageChangeCallback(it) }
    }

    fun updatePageSize(viewPager: ViewPager2) {
        currentSelectedIndex = viewPager.currentItem
        initListIndicator(viewPager.adapter?.itemCount ?: 0)
    }

    private fun initListIndicator(size: Int) {
        removeAllViews()
        indicatorEntries.clear()
        repeat(size) { index ->
            indicatorEntries.add(
                updateEntrySpacing(
                    index, updateEntryDrawable(
                        index, updateEntrySize(index, TrpIcon(context))
                    )
                ).also {
                    this@TrpIndicator.addView(it)
                }
            )
        }
    }
}