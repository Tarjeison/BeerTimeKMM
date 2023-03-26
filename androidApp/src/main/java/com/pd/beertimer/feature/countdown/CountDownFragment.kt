package com.pd.beertimer.feature.countdown

import android.app.AlertDialog
import android.os.Bundle
import android.os.CountDownTimer
import android.view.View
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.IFillFormatter
import com.pd.beertimer.R
import com.pd.beertimer.databinding.FragmentTimerBinding
import com.pd.beertimer.feature.countdown.charts.ChartHelper
import com.pd.beertimer.util.observe
import com.pd.beertimer.util.viewBinding
import com.tlapp.beertimemm.viewmodels.DrinkStatusModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import kotlin.time.ExperimentalTime


@ExperimentalTime
class CountDownFragment : Fragment(R.layout.fragment_timer) {

    private lateinit var countDownTimer: CountDownTimer
    private val countDownViewModel: CountDownViewModel by viewModel()

    private val binding by viewBinding(FragmentTimerBinding::bind)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observe(countDownViewModel.countDownTimerDisplayStringLiveData) {
            binding.tcClock.text = it
        }
        observe(countDownViewModel.drinkStatusLiveData) {
            when (it) {
                is DrinkStatusModel.Drinking -> {
                    setupViewsCurrentlyDrinking()
                    setupBacChart(it)
                }
                DrinkStatusModel.Finished -> {
                    setViewsDrinkingNotStarted()
                }
                DrinkStatusModel.NotStarted -> {
                    setViewsDrinkingNotStarted()
                }
            }
        }
        observe(countDownViewModel.nUnitsDisplayValueLiveData) {
            binding.tvAlcoholCount.text = it
        }
        observe(countDownViewModel.countDownTimerDescriptionLiveData) {
            binding.tvTimeToNext.text = it
        }
    }

    private fun setupViewsCurrentlyDrinking() {
        binding.ivCountDownPineapple.setImageDrawable(
            ContextCompat.getDrawable(
                requireContext(),
                R.drawable.ic_pineapple_smile
            )
        )
        binding.ivCountDownPineapple.visibility = View.VISIBLE
        binding.clNumOfUnits.visibility = View.VISIBLE

        binding.bStopDrinking.visibility = View.VISIBLE
        binding.bStopDrinking.setOnClickListener {
            AlertDialog.Builder(this.context)
                .setTitle(R.string.startdrinking_are_you_sure)
                .setMessage(R.string.countdown_stopdrinking_toast)
                .setPositiveButton(
                    R.string.yes
                ) { _, _ ->
                    countDownViewModel.stopDrinking()
                }
                .setNegativeButton(R.string.no, null)
                .show()
        }
    }

    private fun setupBacChart(drinkingModel: DrinkStatusModel.Drinking) {
        val entryList =
            drinkingModel.graphList.map { Entry(it.x, it.y, ContextCompat.getDrawable(requireContext(), getResIdFromString(it.iconName))) }

        val set1 = LineDataSet(entryList, "")
        val chartHelper = ChartHelper()

        chartHelper.setLineDataSetAttributes(set1)

        val drawable =
            ContextCompat.getDrawable(requireContext(), R.drawable.fade_orange)
        set1.fillDrawable = drawable

        set1.fillFormatter = IFillFormatter { _, _ ->
            binding.chartBac.axisLeft.axisMinimum
        }

        val axisFormatter = chartHelper.createAxisLabelFormatterFromLocalDateTimeList(
            drinkingModel.graphList.map { it.drinkAt },
            requireContext()
        )

        binding.chartBac.xAxis.valueFormatter = axisFormatter
        binding.chartBac.xAxis.granularity = 1f
        binding.chartBac.axisLeft.addLimitLine(chartHelper.createLimitLine(drinkingModel.wantedBloodLevel * 10F))
        binding.chartBac.axisLeft.axisMinimum = 0f
        binding.chartBac.axisLeft.axisMaximum = (drinkingModel.graphList.maxOf { it.y }) + 0.2f
        binding.chartBac.axisRight.setDrawGridLines(false)
        binding.chartBac.axisRight.setDrawLabels(false)
        binding.chartBac.description.isEnabled = false
        binding.chartBac.legend.isEnabled = false
        binding.chartBac.animateX(250)

        val data = LineData(set1)
        data.setValueTextSize(9f)
        data.setDrawValues(false)
        binding.chartBac.data = data
        binding.chartBac.invalidate()

    }

    private fun getResIdFromString(resIdAsString: String): Int {
        var resId = resources.getIdentifier(
            resIdAsString,
            "drawable",
            requireContext().packageName
        )
        if (resId == 0) resId = R.drawable.ic_beer
        return resId
    }

    private fun setViewsDrinkingNotStarted() {
        binding.tvTimeToNext.text = getText(R.string.countdown_not_started_drinking)
        binding.tcClock.text = getString(R.string.countdown_drinking_clock_not_started)
        if (this::countDownTimer.isInitialized) {
            countDownTimer.cancel()
        }
        binding.ivCurrentlyDrinking.visibility = View.GONE
        binding.chartBac.visibility = View.GONE
        binding.bStopDrinking.visibility = View.GONE
        binding.ivCountDownPineapple.setImageDrawable(
            AppCompatResources.getDrawable(
                requireContext(),
                R.drawable.ic_pineapple_sleeping
            )
        )
        binding.clNumOfUnits.visibility = View.INVISIBLE
    }

    override fun onPause() {
        super.onPause()
        countDownViewModel.stopCountDown()
    }

    override fun onResume() {
        super.onResume()
        countDownViewModel.startCountDown()
    }
}
