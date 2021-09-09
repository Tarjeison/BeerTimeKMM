package com.pd.beertimer.feature.profile

import androidx.lifecycle.ViewModel
import com.tlapp.beertimemm.storage.ProfileStorage
import com.tlapp.beertimemm.models.UserProfile
import com.tlapp.beertimemm.storage.PreferredVolume
import kotlinx.serialization.ExperimentalSerializationApi


@ExperimentalSerializationApi
class ProfileViewModel(private val profileStorage: ProfileStorage): ViewModel() {

    fun getUserProfile(): UserProfile? {
        return profileStorage.getUserProfile()
    }

    fun saveUserProfile(userProfile: UserProfile) {
        profileStorage.saveUserProfile(userProfile)
    }

    val preferredVolume get() = profileStorage.getPreferredVolume()

    fun savePreferredVolume(preferredVolume: PreferredVolume) {
        profileStorage.savePreferredVolume(preferredVolume)
    }
}