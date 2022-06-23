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
    ),

    TrpText.DISPLAY_M to arrayListOf(
        R.attr.trpTextDISPLAY_MDefaultAppearance,
        R.attr.trpTextDISPLAY_MLightAppearance,
        R.attr.trpTextDISPLAY_MRegularAppearance,
        R.attr.trpTextDISPLAY_MMediumAppearance
    ),
    TrpText.DISPLAY_L to arrayListOf(
        R.attr.trpTextDISPLAY_LDefaultAppearance,
        R.attr.trpTextDISPLAY_LLightAppearance,
        R.attr.trpTextDISPLAY_LRegularAppearance,
        R.attr.trpTextDISPLAY_LMediumAppearance
    ),
    TrpText.DISPLAY_S to arrayListOf(
        R.attr.trpTextDISPLAY_SDefaultAppearance,
        R.attr.trpTextDISPLAY_SLightAppearance,
        R.attr.trpTextDISPLAY_SRegularAppearance,
        R.attr.trpTextDISPLAY_SMediumAppearance
    ),
    TrpText.HEADLINE_M to arrayListOf(
        R.attr.trpTextHEADLINE_MDefaultAppearance,
        R.attr.trpTextHEADLINE_MLightAppearance,
        R.attr.trpTextHEADLINE_MRegularAppearance,
        R.attr.trpTextHEADLINE_MMediumAppearance
    ),
    TrpText.HEADLINE_L to arrayListOf(
        R.attr.trpTextHEADLINE_LDefaultAppearance,
        R.attr.trpTextHEADLINE_LLightAppearance,
        R.attr.trpTextHEADLINE_LRegularAppearance,
        R.attr.trpTextHEADLINE_LMediumAppearance
    ),
    TrpText.HEADLINE_S to arrayListOf(
        R.attr.trpTextHEADLINE_SDefaultAppearance,
        R.attr.trpTextHEADLINE_SLightAppearance,
        R.attr.trpTextHEADLINE_SRegularAppearance,
        R.attr.trpTextHEADLINE_SMediumAppearance
    ),
    TrpText.TITLE_M to arrayListOf(
        R.attr.trpTextTITLE_MDefaultAppearance,
        R.attr.trpTextTITLE_MLightAppearance,
        R.attr.trpTextTITLE_MRegularAppearance,
        R.attr.trpTextTITLE_MMediumAppearance
    ),
    TrpText.TITLE_L to arrayListOf(
        R.attr.trpTextTITLE_LDefaultAppearance,
        R.attr.trpTextTITLE_LLightAppearance,
        R.attr.trpTextTITLE_LRegularAppearance,
        R.attr.trpTextTITLE_LMediumAppearance
    ),
    TrpText.TITLE_S to arrayListOf(
        R.attr.trpTextTITLE_SDefaultAppearance,
        R.attr.trpTextTITLE_SLightAppearance,
        R.attr.trpTextTITLE_SRegularAppearance,
        R.attr.trpTextTITLE_SMediumAppearance
    ),
    TrpText.LABEL_M to arrayListOf(
        R.attr.trpTextLABEL_MDefaultAppearance,
        R.attr.trpTextLABEL_MLightAppearance,
        R.attr.trpTextLABEL_MRegularAppearance,
        R.attr.trpTextLABEL_MMediumAppearance
    ),
    TrpText.LABEL_L to arrayListOf(
        R.attr.trpTextLABEL_LDefaultAppearance,
        R.attr.trpTextLABEL_LLightAppearance,
        R.attr.trpTextLABEL_LRegularAppearance,
        R.attr.trpTextLABEL_LMediumAppearance
    ),
    TrpText.LABEL_S to arrayListOf(
        R.attr.trpTextLABEL_SDefaultAppearance,
        R.attr.trpTextLABEL_SLightAppearance,
        R.attr.trpTextLABEL_SRegularAppearance,
        R.attr.trpTextLABEL_SMediumAppearance
    ),
    TrpText.BODY_M to arrayListOf(
        R.attr.trpTextBODY_MDefaultAppearance,
        R.attr.trpTextBODY_MLightAppearance,
        R.attr.trpTextBODY_MRegularAppearance,
        R.attr.trpTextBODY_MMediumAppearance
    ),
    TrpText.BODY_L to arrayListOf(
        R.attr.trpTextBODY_LDefaultAppearance,
        R.attr.trpTextBODY_LLightAppearance,
        R.attr.trpTextBODY_LRegularAppearance,
        R.attr.trpTextBODY_LMediumAppearance
    ),
    TrpText.BODY_S to arrayListOf(
        R.attr.trpTextBODY_SDefaultAppearance,
        R.attr.trpTextBODY_SLightAppearance,
        R.attr.trpTextBODY_SRegularAppearance,
        R.attr.trpTextBODY_SMediumAppearance
    ),
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
     *
     * Text style material 3:
     * [DISPLAY_M], [DISPLAY_L], [DISPLAY_S], [HEADLINE_M], [HEADLINE_L], [HEADLINE_S], [TITLE_M], [TITLE_L], [TITLE_S], [LABEL_M], [LABEL_L], [LABEL_S], [BODY_M], [BODY_L], [BODY_S]
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
        DISPLAY_M,
        DISPLAY_L,
        DISPLAY_S,
        HEADLINE_M,
        HEADLINE_L,
        HEADLINE_S,
        TITLE_M,
        TITLE_L,
        TITLE_S,
        LABEL_M,
        LABEL_L,
        LABEL_S,
        BODY_M,
        BODY_L,
        BODY_S,
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
         * Typography material 3
         */

        const val DISPLAY_M = 16
        const val DISPLAY_L = 17
        const val DISPLAY_S = 18

        const val HEADLINE_L = 30
        const val HEADLINE_M = 19
        const val HEADLINE_S = 20

        const val TITLE_M = 21
        const val TITLE_L = 22
        const val TITLE_S = 23

        const val LABEL_M = 24
        const val LABEL_L = 25
        const val LABEL_S = 26

        const val BODY_M = 27
        const val BODY_L = 28
        const val BODY_S = 29

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