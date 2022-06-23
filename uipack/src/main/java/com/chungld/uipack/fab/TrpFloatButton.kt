package com.chungld.uipack.fab

import android.content.Context
import com.google.android.material.floatingactionbutton.FloatingActionButton
import android.util.AttributeSet
import com.chungld.uipack.R
import com.chungld.uipack.theme.wrapContextWithDefaults

/**
 * Extend from [FloatingActionButton] with UIKit default style
 * #
 * Style default: [R.attr.TrpFABDefStyle] with parent [R.style.TrpFloatButtonDefault]
 */

open class TrpFloatButton @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = R.attr.TrpFABDefStyle
) : FloatingActionButton(wrapContextWithDefaults(context), attrs, defStyle)