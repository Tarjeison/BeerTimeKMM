package com.tlapp.beertimemm.viewmodels

import com.tlapp.beertimemm.models.Gender
import com.tlapp.beertimemm.models.ToastUiModel
import com.tlapp.beertimemm.models.UserProfile
import com.tlapp.beertimemm.storage.PreferredVolume
import com.tlapp.beertimemm.storage.ProfileStorage
import com.tlapp.beertimemm.utils.AlwaysDistinct
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.map
import kotlinx.serialization.ExperimentalSerializationApi
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

@ExperimentalSerializationApi
class ProfileModel : KoinComponent {
    private val profileStorage: ProfileStorage by inject()

    private val _toastFlow = MutableStateFlow<AlwaysDistinct<ToastUiModel>?>(null)
    val toastFlow: Flow<ToastUiModel> get() = _toastFlow.filterNotNull().map { it.value }

    fun getUserProfile(): UserProfile? {
        return profileStorage.getUserProfile()
    }

    fun saveUserProfile(weight: Int?, gender: Gender?) {
        val validWeight = weight ?: run {
            _toastFlow.value =
                AlwaysDistinct(ToastUiModel("Please add your weight", "ic_pineapple_confused"))
            return
        }

        val validGender = gender ?: run {
            _toastFlow.value =
                AlwaysDistinct(ToastUiModel("Please select a gender", "ic_pineapple_superhero"))

            return
        }
        profileStorage.saveUserProfile(UserProfile(weight = validWeight, gender = validGender))
        _toastFlow.value =
            AlwaysDistinct(ToastUiModel("Profile saved", "ic_pineapple_superhero"))
    }

    val preferredVolume get() = profileStorage.getPreferredVolume()

    fun savePreferredVolume(preferredVolume: PreferredVolume) {
        profileStorage.savePreferredVolume(preferredVolume)
    }

}
