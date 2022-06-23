package com.chungld.uipack.common

import android.content.Context
import androidx.annotation.FontRes
import androidx.annotation.IntDef
import androidx.annotation.StyleRes
import android.util.TypedValue
import com.chungld.uipack.R

private val styleMapping = mapOf(
    TrpText.H1 to arrayListOf(
        R.attr.trpTextH1DefaultAppearance,
        R.attr.trpTextH1LightAppearance,
        R.attr.trpTextH1RegularAppearance,
        R.attr.trpTextH1MediumAppearance
    ),
    TrpText.H2 to arrayListOf(
        R.attr.trpTextH2DefaultAppearance,
        R.attr.trpTextH2LightAppearance,
        R.attr.trpTextH2RegularAppearance,
        R.attr.trpTextH2MediumAppearance
    ),
    TrpText.H3 to arrayListOf(
        R.attr.trpTextH3DefaultAppearance,
        R.attr.trpTextH3LightAppearance,
        R.attr.trpTextH3RegularAppearance,
        R.attr.trpTextH3MediumAppearance
    ),
    TrpText.H4 to arrayListOf(
        R.attr.trpTextH4DefaultAppearance,
        R.attr.trpTextH4LightAppearance,
        R.attr.trpTextH4RegularAppearance,
        R.attr.trpTextH4MediumAppearance
    ),
    TrpText.H5 to arrayListOf(
        R.attr.trpTextH5DefaultAppearance,
        R.attr.trpTextH5LightAppearance,
        R.attr.trpTextH5RegularAppearance,
        R.attr.trpTextH5MediumAppearance
    ),
    TrpText.H6 to arrayListOf(
        R.attr.trpTextH6DefaultAppearance,
        R.attr.trpTextH6LightAppearance,
        R.attr.trpTextH6RegularAppearance,
        R.attr.trpTextH6MediumAppearance
    ),
    TrpText.SUB1 to arrayListOf(
        R.attr.trpTextSUB1DefaultAppearance,
        R.attr.trpTextSUB1LightAppearance,
        R.attr.trpTextSUB1RegularAppearance,
        R.attr.trpTextSUB1MediumAppearance
    ),
    TrpText.SUB2 to arrayListOf(
        R.attr.trpTextSUB2DefaultAppearance,
        R.attr.trpTextSUB2LightAppearance,
        R.attr.trpTextSUB2RegularAppearance,
        R.attr.trpTextSUB2MediumAppearance
    ),
    TrpText.SUB3 to arrayListOf(
        R.attr.trpTextSUB3DefaultAppearance,
        R.attr.trpTextSUB3LightAppearance,
        R.attr.trpTextSUB3RegularAppearance,
        R.attr.trpTextSUB3MediumAppearance
    ),
    TrpText.SUB4 to arrayListOf(
        R.attr.trpTextSUB4DefaultAppearance,
        R.attr.trpTextSUB4LightAppearance,
        R.attr.trpTextSUB4RegularAppearance,
        R.attr.trpTextSUB4MediumAppearance
    ),
    TrpText.BODY1 to arrayListOf(
        R.attr.trpTextBODY1DefaultAppearance,
        R.attr.trpTextBODY1LightAppearance,
        R.attr.trpTextBODY1RegularAppearance,
        R.attr.trpTextBODY1MediumAppearance
    ),
    TrpText.BODY2 to arrayListOf(
        R.attr.trpTextBODY2DefaultAppearance,
        R.attr.trpTextBODY2LightAppearance,
        R.attr.trpTextBODY2RegularAppearance,
        R.attr.trpTextBODY2MediumAppearance
    ),
    TrpText.BUTTON to arrayListOf(
        R.attr.trpTextBUTTONDefaultAppearance,
        R.attr.trpTextBUTTONLightAppearance,
        R.attr.trpTextBUTTONRegularAppearance,
        R.attr.trpTextBUTTONMediumAppearance
    ),
    TrpText.CAPTION to arrayListOf(
        R.attr.trpTextCAPTIONDefaultAppearance,
        R.attr.trpTextCAPTIONLightAppearance,
        R.attr.trpTextCAPTIONRegularAppearance,
        R.attr.trpTextCAPTIONMediumAppearance
    ),
    TrpText.OVERLINE to arrayListOf(
        R.attr.trpTextOVERLINEDefaultAppearance,
        R.attr.trpTextOVERLINELightAppearance,
        R.attr.trpTextOVERLINERegularAppearance,
        R.attr.trpTextOVERLINEMediumAppearance
    ),
    TrpText.NOTIFY to arrayListOf(
        R.attr.trpTextNotifyDefaultAppearance,
        R.attr.trpTextNotifyLightAppearance,
        R.attr.trpTextNotifyRegularAppearance,
        R.attr.trpTextNotifyMediumAppearance
    )
)

open class TrpText {

    /**
     * has value: [DEFAULT], [LIGHT], [REGULAR], [MEDIUM]
     */
    enum class Weight {
        DEFAULT,
        LIGHT,
        REGULAR,
        MEDIUM
    }

    /**
     * Text style material:
     * [H1], [H2], [H3], [H4], [H5], [H6], [SUB1], [SUB2], [SUB3], [SUB4], [BODY1], [BODY2], [BUTTON], [CAPTION], [OVERLINE], [NOTIFY]
     */
    @IntDef(
        H1,
        H2,
        H3,
        H4,
        H5,
        H6,
        SUB1,
        SUB2,
        SUB3,
        SUB4,
        BODY1,
        BODY2,
        BUTTON,
        CAPTION,
        OVERLINE,
        NOTIFY,
    )
    annotation class Styles

    /**
     * 4 directions:
     * [RTL], [LTR], [INHERIT], [LOCALE]
     */
    @IntDef(RTL, LTR, INHERIT, LOCALE)
    annotation class Directions

    companion object {
        const val H1 = 0
        const val H2 = 1
        const val H3 = 2
        const val H4 = 3
        const val H5 = 4
        const val H6 = 5
        const val SUB1 = 6
        const val SUB2 = 7
        const val BODY1 = 8
        const val BODY2 = 9
        const val BUTTON = 10
        const val CAPTION = 11
        const val OVERLINE = 12
        const val NOTIFY = 13
        const val SUB3 = 14
        const val SUB4 = 15

        const val LTR = 0
        const val RTL = 1
        const val INHERIT = 2
        const val LOCALE = 3

        /**
         * get textStyleID attributes from textStyle, weight.
         * default: [BODY2] - [Weight.DEFAULT]
         */
        @JvmStatic
        fun getTextStyleId(
            context: Context,
            textStyle: Int = BODY2,
            weight: Weight = Weight.DEFAULT
        ) = getStyleId(context, textStyle, weight)


        /**
         * get fontID from attributeID
         */
        @JvmStatic
        fun getFontId(context: Context, attrId: Int) =
            com.chungld.uipack.common.getFontId(context, attrId)
    }
}

@FontRes
private fun getFontId(context: Context, attrId: Int): Int {
    val outValue = TypedValue()
    val resolved = context.theme.resolveAttribute(attrId, outValue, true)
    if (resolved && outValue.resourceId != 0) {
        return outValue.resourceId
    }
    return 0
}

@StyleRes
private fun getStyleId(context: Context, textStyleValue: Int, weight: TrpText.Weight): Int {
    val textAppearanceArray = styleMapping[textStyleValue]
        ?: throw IllegalStateException("Invalid style")
    val outValue = TypedValue()
    return if (context.theme.resolveAttribute(
            textAppearanceArray[weight.ordinal],
            outValue,
            true
        )
    ) outValue.resourceId else 0
}