package com.tlapp.beertimemm.models

import kotlinx.serialization.Serializable

@Serializable
data class UserProfile(val gender: Gender,
                       val weight: Int)

enum class Gender {
    MALE,
    FEMALE;
}
