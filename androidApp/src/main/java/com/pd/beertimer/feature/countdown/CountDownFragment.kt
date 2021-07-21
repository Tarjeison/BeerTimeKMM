package com.pd.beertimer.feature.countdown

import android.app.AlertDialog
import android.os.Bundle
import android.os.CountDownTimer
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.IFillFormatter
import com.pd.beertimer.R
import com.pd.beertimer.feature.countdown.charts.ChartHelper
import com.pd.beertimer.feature.profile.ProfileViewModel
import com.pd.beertimer.util.ifLet
import com.pd.beertimer.util.ordinal
import com.tlapp.beertimemm.drinking.DrinkCoordinator
import com.tlapp.beertimemm.models.DrinkingCalculator
import kotlinx.android.synthetic.main.fragment_timer.*
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.concurrent.TimeUnit
import kotlin.time.Duration
import kotlin.time.ExperimentalTime


@ExperimentalTime
class CountDownFragment : Fragment() {

    private lateinit var countDownTimer: CountDownTimer
    private var drinkingTimes: List<Instant>? = null
    private var drinkingCalculator: DrinkingCalculator? = null
    private val profileViewModel: ProfileViewModel by viewModel()
    private val drinkCoordinator: DrinkCoordinator by inject()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_timer, container, false)
    }

    private fun setViewsDrinkingStarted() {
        drinkingTimes?.let {
            drinkingCalculator?.preferredUnit?.let { unit ->
                val resId = resources.getIdentifier(
                    unit.iconName,
                    "drawable",
                    context?.packageName
                )
                if (resId != 0) {
                    ivCurrentlyDrinking.setImageDrawable(
                        ContextCompat.getDrawable(
                            requireContext(),
                            resId
                        )
                    )

                }
            }
            ivCountDownPineapple.setImageDrawable(
                ContextCompat.getDrawable(
                    requireContext(),
                    R.drawable.ic_pineapple_smile
                )
            )
            ivCountDownPineapple.visibility = View.VISIBLE
            clNumOfUnits.visibility = View.VISIBLE

            val now = Clock.System.now()
            val pastUnits = it.filter { drinkTime ->
                drinkTime < now
            }.size

            if (pastUnits > 0) {
                val stringOrdinal = ordinal(pastUnits)
                tvAlcoholCount.text = String.format(
                    getString(R.string.countdown_drinks_so_far),
                    stringOrdinal
                )
            } else {
                setNoUnitsConsumed()
            }
        }

        bStopDrinking.visibility = View.VISIBLE
        bStopDrinking.setOnClickListener {
            AlertDialog.Builder(this.context)
                .setTitle(R.string.startdrinking_are_you_sure)
                .setMessage(R.string.countdown_stopdrinking_toast)
                .setPositiveButton(
                    R.string.yes
                ) { _, _ ->
                    drinkCoordinator.stopDrinking()
                    setViewsDrinkingNotStarted()
                }
                .setNegativeButton(R.string.no, null)
                .show()
        }
    }

    private fun setupCountDownTimer() {
        val duration = getDurationToNextDateTime() ?: return
        if (this::countDownTimer.isInitialized) {
            countDownTimer.cancel()
        }
        countDownTimer = object : CountDownTimer(duration.inWholeMilliseconds, 1000) {
            override fun onFinish() {
                setupCountDownTimer()
                setViewsDrinkingStarted()
            }

            override fun onTick(millisUntilFinsihed: Long) {
                tcClock.text = millisToDisplayString(millisUntilFinsihed)
            }

        }.start()
    }

    private fun getDurationToNextDateTime(): Duration? {
        val now = Clock.System.now()
        drinkingTimes?.let {
            it.forEach { drinkingTime ->
                if (drinkingTime > now) {
                    return drinkingTime.minus(now)
                }
            }
        }
        return null
    }

    private fun millisToDisplayString(mUntilFinish: Long): String {
        var millisUntilFinished: Long = mUntilFinish
        val days = TimeUnit.MILLISECONDS.toDays(millisUntilFinished)
        millisUntilFinished -= TimeUnit.DAYS.toMillis(days)

        val hours = TimeUnit.MILLISECONDS.toHours(millisUntilFinished)
        millisUntilFinished -= TimeUnit.HOURS.toMillis(hours)

        val minutes = TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished)
        millisUntilFinished -= TimeUnit.MINUTES.toMillis(minutes)

        val seconds = TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished)
        return if (hours != 0L) {
            String.format(
                "%02d:%02d:%02d",
                hours, minutes, seconds
            )
        } else {
            String.format(
                "%02d:%02d",
                minutes, seconds
            )
        }
    }

    private fun setupBacChart() {
        val profile = profileViewModel.getUserProfile()
        ifLet(
            profile,
            drinkingCalculator,
            drinkingTimes
        ) { (profile, drinkingCalculator, dTimes) ->

            val bacEstimates =
                (drinkingCalculator as DrinkingCalculator).generateBACPrediction(dTimes as List<Instant>)

            bacEstimates?.let { estimates ->
                val bac: List<Entry> = estimates.map {
                    if (estimates.indexOf(it) == estimates.lastIndex) {
                        Entry(
                            estimates.indexOf(it).toFloat(),
                            it.first,
                            ContextCompat.getDrawable(requireContext(), R.drawable.ic_finish_flag)
                        )
                    } else {
                        var resId = resources.getIdentifier(
                            drinkingCalculator.preferredUnit.iconName,
                            "drawable",
                            requireContext().packageName
                        )
                        if (resId == 0) resId = R.drawable.ic_beer
                        Entry(
                            estimates.indexOf(it).toFloat(),
                            it.first,
                            ContextCompat.getDrawable(requireContext(), resId)
                        )

                    }
                }
                val set1 = LineDataSet(bac, "")
                val chartHelper = ChartHelper()

                chartHelper.setLineDataSetAttributes(set1)

                val drawable =
                    ContextCompat.getDrawable(requireContext(), R.drawable.fade_orange)
                set1.fillDrawable = drawable

                set1.fillFormatter = IFillFormatter { _, _ ->
                    chartBac.axisLeft.axisMinimum
                }

                val axisFormatter = chartHelper.createAxisLabelFormatterFromLocalDateTimeList(
                    bacEstimates.map {
                        it.second
                    },
                    requireContext()
                )

                chartBac.xAxis.valueFormatter = axisFormatter
                chartBac.xAxis.granularity = 1f
                chartBac.axisLeft.addLimitLine(chartHelper.createLimitLine(drinkingCalculator.wantedBloodLevel * 10F))
                chartBac.axisLeft.axisMinimum = 0f
                chartBac.axisLeft.axisMaximum = (bacEstimates.maxOf { it.first }) + 0.2f
                chartBac.axisRight.setDrawGridLines(false)
                chartBac.axisRight.setDrawLabels(false)
                chartBac.description.isEnabled = false
                chartBac.legend.isEnabled = false
                chartBac.animateX(250)

                val data = LineData(set1)
                data.setValueTextSize(9f)
                data.setDrawValues(false)

                chartBac.data = data
                chartBac.invalidate()
            }
        }
    }

    private fun setViewsDrinkingNotStarted() {
        tvTimeToNext.text = getText(R.string.countdown_not_started_drinking)
        tcClock.text = getString(R.string.countdown_drinking_clock_not_started)
        if (this::countDownTimer.isInitialized) {
            countDownTimer.cancel()
        }
        ivCurrentlyDrinking.visibility = View.GONE
        chartBac.visibility = View.GONE
        bStopDrinking.visibility = View.GONE
        ivCountDownPineapple.setImageDrawable(
            AppCompatResources.getDrawable(
                requireContext(),
                R.drawable.ic_pineapple_sleeping
            )
        )
        clNumOfUnits.visibility = View.INVISIBLE
    }

    private fun setNoUnitsConsumed() {
        tvAlcoholCount.text = getString(R.string.countdown_enjoy_first)
    }

    override fun onPause() {
        super.onPause()
        if (this::countDownTimer.isInitialized) {
            countDownTimer.cancel()
        }
    }

    override fun onResume() {
        super.onResume()
        drinkingTimes = drinkCoordinator.getDrinkingTimes()
        drinkingCalculator = drinkCoordinator.getDrinkingCalculator()
        if (drinkingTimes == null) {
            setViewsDrinkingNotStarted()
        } else {
            setViewsDrinkingStarted()
            setupCountDownTimer()
            setupBacChart()
        }
    }
}
