package com.chungld.uipack.seekbar

class SeekBarState {
    var indicatorText: String? = null
    var value //now progress value
            = 0f
    var isMin = false
    var isMax = false
    override fun toString(): String {
        return "indicatorText: $indicatorText ,isMin: $isMin ,isMax: $isMax"
    }
}