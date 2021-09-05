package com.pd.beertimer.feature.me

import androidx.lifecycle.ViewModel
import com.tlapp.beertimemm.viewmodels.MePageModel

class MeViewModel : ViewModel() {

    private val model = MePageModel()

    fun getMeItems() = model.getMeItems()
}
