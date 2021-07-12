package com.tlapp.beertimemm.storage

import com.tlapp.beertimemm.models.UserProfile

expect class ProfileStorage {
    fun getUserProfile(): UserProfile?
    fun saveUserProfile(userProfile: UserProfile)
}