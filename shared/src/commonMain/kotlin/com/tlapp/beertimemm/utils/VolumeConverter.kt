package com.tlapp.beertimemm.utils

import com.tlapp.beertimemm.storage.PreferredVolume
import com.tlapp.beertimemm.storage.ProfileStorage

const val LITER_TO_OZ_RATIO = 33.8140226F
private const val LITER_DESCRIPTION = "L"
private const val OUNCE_DESCRIPTION = " oz"

class VolumeConverter(private val profileStorage: ProfileStorage) {

    fun floatLiterToVolumeString(liter: Float): String {
        val isUsingLiter = profileStorage.getPreferredVolume() == PreferredVolume.LITER
        val floatVolume = if (isUsingLiter) liter else liter * LITER_TO_OZ_RATIO
        val formattedFloat = floatVolume.toString()

        val formattedVolume = if (formattedFloat.length > 4) {
            formattedFloat.take(5)
        } else {
            formattedFloat
        }
        return formattedVolume + if (isUsingLiter) LITER_DESCRIPTION else OUNCE_DESCRIPTION
    }
}
