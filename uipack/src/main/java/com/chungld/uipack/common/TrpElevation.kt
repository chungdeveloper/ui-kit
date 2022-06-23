package com.chungld.uipack.common

import com.chungld.uipack.R

/**
 * values: ELEVATION_NONE, ELEVATION_NORMAL, ELEVATION_SELECTED
 */
val elevationMap = mapOf(
    TrpElevation.ELEVATION_NONE to R.dimen.trpElevationNone,
    TrpElevation.ELEVATION_NORMAL to R.dimen.trpElevationNormal,
    TrpElevation.ELEVATION_SELECTED to R.dimen.trpElevationSelected
)

class TrpElevation {
    companion object {
        const val ELEVATION_NONE = 0f
        const val ELEVATION_NORMAL = -1f
        const val ELEVATION_SELECTED = -2f
    }
}
