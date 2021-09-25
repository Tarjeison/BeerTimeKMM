package com.tlapp.beertimemm.storage

import com.russhwolf.settings.Settings
import com.russhwolf.settings.get
import com.tlapp.beertimemm.models.UserProfile
import com.tlapp.beertimemm.utils.PREFERRED_VOLUME_KEY
import com.tlapp.beertimemm.utils.PROFILE_KEY
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

@ExperimentalSerializationApi
class ProfileStorage(private val settings: Settings) {
    fun getUserProfile(): UserProfile? {
        return settings.get<String>(PROFILE_KEY)?.let {
            Json.decodeFromString<UserProfile>(it)
        }
    }

    fun saveUserProfile(userProfile: UserProfile) {
        settings.putString(PROFILE_KEY, Json.encodeToString(userProfile))
    }

    fun getPreferredVolume(): PreferredVolume {
        return settings.get<String>(PREFERRED_VOLUME_KEY)?.let {
            PreferredVolume.valueOf(it)
        } ?: PreferredVolume.LITER
    }

    fun savePreferredVolume(preferredVolume: PreferredVolume) {
        settings.putString(PREFERRED_VOLUME_KEY, preferredVolume.name)
    }
}

enum class PreferredVolume {
    LITER, OUNCES
}
