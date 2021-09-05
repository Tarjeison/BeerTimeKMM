package com.tlapp.beertimemm.viewmodels

import com.tlapp.beertimemm.models.MePageItem
import com.tlapp.beertimemm.models.MePageNavigationType

class MePageModel {

    private val meItems = listOf(
        MePageItem("ic_superhero_pineapple", "Profile", MePageNavigationType.PROFILE),
        MePageItem("ic_cocktail", "My drinks", MePageNavigationType.MY_DRINKS)
    )

    fun getMeItems() = meItems
}
