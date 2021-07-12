package com.tlapp.beertimemm.storage

import android.content.SharedPreferences
import com.tlapp.beertimemm.models.UserProfile
import com.tlapp.beertimemm.utils.PROFILE_KEY
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

actual class ProfileStorage(private val sharedPreferences: SharedPreferences) {
    actual fun getUserProfile(): UserProfile? {
        return sharedPreferences.getString(PROFILE_KEY, null)?.let {
            Json.decodeFromString<UserProfile>(it)
        }
    }

    actual fun saveUserProfile(userProfile: UserProfile) {
        sharedPreferences
            .edit()
            .putString(PROFILE_KEY, Json.encodeToString(userProfile))
            .apply()
    }
}