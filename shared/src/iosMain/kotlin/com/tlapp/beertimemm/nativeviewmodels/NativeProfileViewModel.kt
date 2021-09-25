package com.tlapp.beertimemm.nativeviewmodels

import co.touchlab.stately.ensureNeverFrozen
import com.tlapp.beertimemm.models.Gender
import com.tlapp.beertimemm.models.ToastUiModel
import com.tlapp.beertimemm.models.UserProfile
import com.tlapp.beertimemm.storage.PreferredVolume
import com.tlapp.beertimemm.viewmodels.ProfileModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import kotlinx.serialization.ExperimentalSerializationApi
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

@ExperimentalSerializationApi
class NativeProfileViewModel(
    private val onToastMessage: (ToastUiModel) -> Unit
): KoinComponent {
    private val profileModel: ProfileModel by inject()
    private var scope: CoroutineScope? = null

    init {
        ensureNeverFrozen()
    }

    fun getUserProfile(): UserProfile? {
        return profileModel.getUserProfile()
    }

    fun saveUserProfile(weight: Int?, gender: Gender?) {
        profileModel.saveUserProfile(weight = weight, gender = gender)
    }

    val preferredVolume get() = profileModel.preferredVolume

    fun savePreferredVolume(preferredVolume: PreferredVolume) {
        profileModel.savePreferredVolume(preferredVolume)
    }

    fun observeToastFlow() {
        scope = MainScope().also { coroutineScope ->
            coroutineScope.launch {
                profileModel.toastFlow.onEach {
                    onToastMessage.invoke(it)
                }.launchIn(this)
            }
        }
    }

    fun onDestroy() {
        scope?.cancel()
        scope = null
    }
}
