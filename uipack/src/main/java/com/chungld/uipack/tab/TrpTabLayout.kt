package com.chungld.uipack.tab

import android.content.Context
import com.google.android.material.tabs.TabLayout
import android.util.AttributeSet
import android.view.ViewGroup
import com.chungld.uipack.R
import com.chungld.uipack.theme.wrapContextWithDefaults

/**
 * Extend from [TabLayout]
 * #
 * Style default: [R.attr.TrpTabLayoutStyle] with parent [R.style.TrpTabLayoutDefaultStyle]
 * #Disable ALlCap by [tabTextAppearance] = [R.style.TrpTabTextAppearanceDefault]
 */
open class TrpTabLayout @JvmOverloads constructor(
    context: Context,
    attributes: AttributeSet? = null,
    defStyleAttributes: Int = R.attr.TrpTabLayoutStyle
) : TabLayout(wrapContextWithDefaults(context), attributes, defStyleAttributes),
    TabLayout.BaseOnTabSelectedListener<TabLayout.Tab> {

    init {
        initialization()
    }

    private fun initialization() {
        addOnTabSelectedListener(this)
    }

    /**
     * add new [TrpBadgeView]
     */
    fun addTab(badgeView: TrpBadgeView) {
        val tab = this.newTab()
        badgeView.setTextColor(tabTextColors)
        tab.customView = badgeView
        addTab(tab)
    }

    /**
     * replace default tab at index to [TrpBadgeView]
     */
    fun replaceTab(index: Int, badgeView: TrpBadgeView) {
        replaceTab(getTabAt(index), badgeView)
    }

    /**
     * replace from [TabLayout.Tab] to [TrpBadgeView]
     */
    fun replaceTab(tab: Tab?, badgeView: TrpBadgeView) {
        badgeView.text = if (badgeView.text?.isEmpty() != false) tab?.text else badgeView.text
        badgeView.setTextColor(tabTextColors)
        badgeView.tabIcon = if (badgeView.tabIcon == null) tab?.icon else badgeView.tabIcon
//        badgeView.layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT
        badgeView.layoutParams.height = ViewGroup.LayoutParams.MATCH_PARENT
        tab?.customView = badgeView
    }

    /**
     * replace default tab to new [TrpBadgeView]
     */
    fun replaceWithBadgeViewAt(index: Int) {
        replaceTab(index, TrpBadgeView(context))
    }

    override fun onTabReselected(p0: Tab?) {
        updateTabSelected(p0, true)
    }

    override fun onTabUnselected(p0: Tab?) {
        updateTabSelected(p0, false)
    }

    override fun onTabSelected(p0: Tab?) {
        updateTabSelected(p0, true)
    }

    /**
     * add tabView like [TabLayout.Tab] but this will change [TabLayout.Tab] to [TrpBadgeView]
     */
    override fun addTab(tab: Tab, position: Int, setSelected: Boolean) {
        super.addTab(tab, position, setSelected)
        replaceTab(tab, TrpBadgeView(context))
    }

    /**
     * setText to tab
     */
    fun setText(index: Int, text: CharSequence): Tab? {
        val tab = getTabAt(index)
        if (tab?.customView !is TrpBadgeView) return tab
        (tab.customView as TrpBadgeView).text = text
        return tab
    }

    /**
     * getBadge at index
     */
    fun getBadgeTabAt(index: Int): TrpBadgeView {
        return getTabAt(index)?.customView as TrpBadgeView
    }

    private fun updateTabSelected(tab: Tab?, selected: Boolean) {
        tab?.customView?.isActivated = selected
        if (tab?.customView !is TrpBadgeView) return
        (tab.customView as TrpBadgeView).trpTextContent.isSelected = selected
        (tab.customView as TrpBadgeView).trpTextContent.isActivated = selected
    }
}