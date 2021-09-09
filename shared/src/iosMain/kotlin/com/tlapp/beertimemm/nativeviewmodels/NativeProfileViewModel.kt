package com.tlapp.beertimemm.nativeviewmodels

import com.tlapp.beertimemm.models.UserProfile
import com.tlapp.beertimemm.storage.ProfileStorage
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class NativeProfileViewModel: KoinComponent {
    private val profileStorage: ProfileStorage by inject()

    fun getUserProfile() = profileStorage.getUserProfile()

    fun saveUserProfile(userProfile: UserProfile) {
        profileStorage.saveUserProfile(userProfile)
    }
}
