package com.tlapp.beertimemm.storage

import com.russhwolf.settings.Settings
import com.russhwolf.settings.get
import com.tlapp.beertimemm.models.UserProfile
import com.tlapp.beertimemm.utils.PROFILE_KEY
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class ProfileStorage(private val settings: Settings) {
    fun getUserProfile(): UserProfile? {
        return settings.get<String>(PROFILE_KEY)?.let {
            Json.decodeFromString<UserProfile>(it)
        }
    }

    fun saveUserProfile(userProfile: UserProfile) {
        settings.putString(PROFILE_KEY, Json.encodeToString(userProfile))
    }
}
