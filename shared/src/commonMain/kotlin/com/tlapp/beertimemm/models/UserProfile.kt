package com.tlapp.beertimemm.models

import kotlinx.serialization.Serializable

@Serializable
data class UserProfile(var gender: Gender,
                       var weight: Int)

enum class Gender {
    MALE,
    FEMALE;
    companion object {

        const val FILE_WEIGHT = "weight"
        const val FILE_GENDER = "gender"

        fun stringToGender(s: String): Gender {
            return when (s) {
                "MALE" -> MALE
                "FEMALE" -> FEMALE
                else -> throw (Exception("No gender found in string"))
            }
        }
    }
}
