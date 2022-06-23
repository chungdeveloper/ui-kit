package com.chungld.uipack.seekbar

interface OnRangeChangedListener {
    fun onRangeChanged(
        view: TrpSeekBar?,
        leftValue: Float,
        rightValue: Float,
        isFromUser: Boolean
    )

    fun onStartTrackingTouch(view: TrpSeekBar?, isLeft: Boolean)
    fun onStopTrackingTouch(view: TrpSeekBar?, isLeft: Boolean)
}