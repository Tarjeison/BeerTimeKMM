package com.pd.beertimer.feature.profile

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.Fragment
import com.pd.beertimer.R
import com.pd.beertimer.databinding.FragmentProfileBinding
import com.pd.beertimer.util.ToastHelper
import com.pd.beertimer.util.getIconFromName
import com.pd.beertimer.util.observe
import com.pd.beertimer.util.viewBinding
import com.tlapp.beertimemm.models.Gender
import com.tlapp.beertimemm.storage.PreferredVolume
import kotlinx.serialization.ExperimentalSerializationApi
import org.koin.androidx.viewmodel.ext.android.viewModel

@ExperimentalSerializationApi
class ProfileFragment : Fragment(R.layout.fragment_profile) {

    private val profileViewModel: ProfileViewModel by viewModel()
    private val binding by viewBinding(FragmentProfileBinding::bind)


    @SuppressLint("ClickableViewAccessibility")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        observe(profileViewModel.toastLiveData) {
            ToastHelper.createToast(
                layoutInflater,
                context,
                it.displayMessage,
                context?.getIconFromName(it.iconName) ?: R.drawable.ic_superhero_pineapple
            )
        }

        binding.clProfile.setOnTouchListener { v, _ ->
            val imm = context?.getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
            imm?.hideSoftInputFromWindow(v?.windowToken, 0)
            binding.etWeight.clearFocus()
            true
        }

        val userProfile = profileViewModel.getUserProfile()
        userProfile?.let {
            binding.etWeight.setText(it.weight.toString())
            when (it.gender) {
                Gender.FEMALE -> {
                    binding.ibFemale.isSelected = true
                    binding.ibFemale.setBackgroundColor(Color.LTGRAY)
                }
                Gender.MALE -> {
                    binding.ibMale.isSelected = true
                    binding.ibMale.setBackgroundColor(Color.LTGRAY)
                }
            }
        }
        binding.bSave.setOnClickListener(createSaveClickListener())
        binding.ibMale.setOnClickListener {
            it.setBackgroundColor(Color.LTGRAY)
            it.isSelected = true
            binding.ibFemale.setBackgroundColor(Color.WHITE)
            binding.ibFemale.isSelected = false
        }
        binding.ibFemale.setOnClickListener {
            it.setBackgroundColor(Color.LTGRAY)
            it.isSelected = true
            binding.ibMale.setBackgroundColor(Color.WHITE)
            binding.ibMale.isSelected = false
        }
        setupRadioGroup()
    }

    private fun setupRadioGroup() {
        val isUsingLiters = profileViewModel.preferredVolume == PreferredVolume.LITER
        binding.rUnit.check(if (isUsingLiters) R.id.bLiter else R.id.bOunce)
        binding.rUnit.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                R.id.bLiter -> saveUnitToSharedPreferences(PreferredVolume.LITER)
                R.id.bOunce -> saveUnitToSharedPreferences(PreferredVolume.OUNCES)
            }
        }
    }

    private fun saveUnitToSharedPreferences(preferredVolume: PreferredVolume) {
        profileViewModel.savePreferredVolume(preferredVolume)
    }

    private fun createSaveClickListener(): View.OnClickListener {
        return View.OnClickListener {
            val gender = when {
                binding.ibFemale.isSelected -> Gender.FEMALE
                binding.ibMale.isSelected -> Gender.MALE
                else -> null
            }
            val weight = binding.etWeight.text?.takeIf { it.isNotEmpty() }
                ?.toString()?.toInt()
            profileViewModel.saveUserProfile(weight, gender)
        }
    }
}
