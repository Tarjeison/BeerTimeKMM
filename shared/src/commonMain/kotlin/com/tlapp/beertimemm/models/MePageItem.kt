package com.tlapp.beertimemm.models

data class MePageItem(
    val iconName: String,
    val title: String,
    val navigation: MePageNavigationType
)

enum class MePageNavigationType {
    PROFILE, MY_DRINKS
}
