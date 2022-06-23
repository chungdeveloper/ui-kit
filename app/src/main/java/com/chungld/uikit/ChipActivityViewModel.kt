package com.chungld.uikit

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class ChipActivityViewModel : ViewModel() {

    val selected by lazy { MutableLiveData<Boolean>() }

}