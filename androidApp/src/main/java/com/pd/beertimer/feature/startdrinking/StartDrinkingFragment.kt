package com.pd.beertimer.feature.startdrinking

import android.app.AlertDialog
import android.os.Bundle
import android.view.View
import android.widget.SeekBar
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.pd.beertimer.BuildConfig
import com.pd.beertimer.R
import com.pd.beertimer.databinding.FragmentStartdrinkingBinding
import com.pd.beertimer.util.ToastHelper
import com.pd.beertimer.util.observe
import com.pd.beertimer.util.viewBinding
import com.tlapp.beertimemm.models.AlcoholUnit
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.koin.androidx.viewmodel.ext.android.viewModel
import kotlin.time.ExperimentalTime

@ExperimentalTime
class StartDrinkingFragment : Fragment(R.layout.fragment_startdrinking) {

    private val binding by viewBinding(FragmentStartdrinkingBinding::bind)

    private val startDrinkingViewModel: StartDrinkingViewModel by viewModel()
    private lateinit var alcoholAdapter: AlcoholAdapterV2

    @ExperimentalCoroutinesApi
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        observe(startDrinkingViewModel.drinksLiveData) {
            getAlcoholAdapter().setData(it)
            binding.bStartDrinking.visibility = View.VISIBLE
        }
        observe(startDrinkingViewModel.finishBarLiveData) {
            binding.tvHoursValue.text = it
        }
        observe(startDrinkingViewModel.peakHourLiveData) {
            binding.tvPeakValue.text = it
        }
        observe(startDrinkingViewModel.wantedBloodLevelLiveData) {
            binding.tvBloodLevelValue.text = it
        }
        observe(startDrinkingViewModel.toastLiveData) {
            ToastHelper.createToast(
                layoutInflater,
                context,
                it,
                R.drawable.ic_pineapple_confused
            )
        }
        observe(startDrinkingViewModel.alertDialogLiveData) {
            AlertDialog.Builder(this.context)
                .setTitle(it.title)
                .setMessage(it.message)
                .setPositiveButton(
                    it.positiveButtonText
                ) { _, _ -> it.onClick.invoke() }
                .setNegativeButton(it.negativeButtonText, null)
                .show()
        }
        observe(startDrinkingViewModel.navigationLiveData) {
            findNavController().navigate(R.id.action_global_countDownFragment)
        }

        binding.rvAlcoholUnit.layoutManager = LinearLayoutManager(context)
        binding.rvAlcoholUnit.adapter = getAlcoholAdapter().also {
            it.notifyDataSetChanged()
        }

        if (BuildConfig.DEBUG) {
            binding.sbBloodLevel.max = 60
        }

        initSeekBars()
        initStartDrinkingButton()
    }

    private fun getAlcoholAdapter(): AlcoholAdapterV2 {
        if (!this::alcoholAdapter.isInitialized) {
            alcoholAdapter = AlcoholAdapterV2(mutableListOf(), onUnitSelected())
        }
        return alcoholAdapter
    }

    private fun onUnitSelected() = { selectedUnit: AlcoholUnit ->
        startDrinkingViewModel.setSelectedUnit(selectedUnit)
    }

    private fun initSeekBars() {
        binding.sbBloodLevel.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(p0: SeekBar?, p1: Int, p2: Boolean) {
                startDrinkingViewModel.setWantedBloodLevel(p1)
            }

            override fun onStartTrackingTouch(p0: SeekBar?) {}

            override fun onStopTrackingTouch(p0: SeekBar?) {}

        })

        binding.sbHours.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(p0: SeekBar?, p1: Int, p2: Boolean) {
                startDrinkingViewModel.setFinishDrinkingInHoursMinutes(p1)
                binding.sbPeak.max = p1
//                if (startDrinkingViewModel.peakTimeNotSet()) {
//                    startDrinkingViewModel.setPeakTimeInHoursMinutes(p1)
//                    binding.sbPeak.progress = p1
//                }
            }

            override fun onStartTrackingTouch(p0: SeekBar?) {}

            override fun onStopTrackingTouch(p0: SeekBar?) {}

        })

        binding.sbPeak.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(p0: SeekBar?, p1: Int, p2: Boolean) {
                startDrinkingViewModel.setPeakTimeInHoursMinutes(p1)
            }

            override fun onStartTrackingTouch(p0: SeekBar?) {}

            override fun onStopTrackingTouch(p0: SeekBar?) {}

        })
    }

    private fun initStartDrinkingButton() {
        binding.bStartDrinking.setOnClickListener {
            startDrinkingViewModel.startDrinking()
        }
    }
}
