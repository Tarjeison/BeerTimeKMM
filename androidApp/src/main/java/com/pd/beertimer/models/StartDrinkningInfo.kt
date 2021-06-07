package com.pd.beertimer.models

import com.tlapp.beertimemm.models.AlcoholUnit

data class StartDrinkningInfo(
    val wantedBloodLevel: Float,
    val plannedHoursDrinking: Int,
    val alcoholType: AlcoholUnit
)