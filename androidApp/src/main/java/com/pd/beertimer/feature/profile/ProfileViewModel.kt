package com.pd.beertimer.feature.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.tlapp.beertimemm.models.Gender
import com.tlapp.beertimemm.models.UserProfile
import com.tlapp.beertimemm.storage.PreferredVolume
import com.tlapp.beertimemm.viewmodels.ProfileModel
import kotlinx.serialization.ExperimentalSerializationApi


@ExperimentalSerializationApi
class ProfileViewModel(private val profileModel: ProfileModel): ViewModel() {

    val toastLiveData = profileModel.toastFlow.asLiveData()

    fun getUserProfile(): UserProfile? {
        return profileModel.getUserProfile()
    }

    fun saveUserProfile(weight: Int?, gender: Gender?) {
        profileModel.saveUserProfile(weight, gender)
    }

    val preferredVolume get() = profileModel.preferredVolume

    fun savePreferredVolume(preferredVolume: PreferredVolume) {
        profileModel.savePreferredVolume(preferredVolume)
    }
}
