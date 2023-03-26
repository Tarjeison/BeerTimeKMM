package com.tlapp.beertimemm.viewmodels

import com.tlapp.beertimemm.drinking.DrinkCoordinator
import com.tlapp.beertimemm.models.DrinkingCalculator
import com.tlapp.beertimemm.resources.Strings.COUNTDOWN_DESCRIPTION
import com.tlapp.beertimemm.resources.Strings.COUNTDOWN_LAST_DRINK_DESCRIPTION
import com.tlapp.beertimemm.resources.Strings.N_DRINKS_FIRST_DRINK
import com.tlapp.beertimemm.resources.Strings.N_DRINKS_LAST_DRINK
import com.tlapp.beertimemm.utils.CountDownClock
import com.tlapp.beertimemm.utils.ordinal
import com.tlapp.beertimemm.utils.toMinimumTwoPrecisionString
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import kotlin.time.Duration
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.ExperimentalTime

@OptIn(ExperimentalTime::class)
class CountDownModel : KoinComponent {

    private val clock: Clock by inject()
    private val countDownClock: CountDownClock by inject()
    private val drinkCoordinator: DrinkCoordinator by inject()

    private val _countDownDisplayValueFlow: MutableStateFlow<String> = MutableStateFlow("00:00")
    val countDownDisplayValueFlow: StateFlow<String> get() = _countDownDisplayValueFlow

    private val _countDownDescriptionDisplayValueFlow: MutableStateFlow<String> = MutableStateFlow(COUNTDOWN_DESCRIPTION)
    val countDownDescriptionDisplayValueFlow: StateFlow<String> get() = _countDownDescriptionDisplayValueFlow

    private val _drinkStatusModelFlow: MutableStateFlow<DrinkStatusModel?> = MutableStateFlow(null)
    val drinkStatusModelFlow: StateFlow<DrinkStatusModel?> get() = _drinkStatusModelFlow

    private val _nOfUnitsDisplayValueFlow: MutableStateFlow<String?> = MutableStateFlow(null)
    val nOfUnitsDisplayValueFlow: StateFlow<String?> get() = _nOfUnitsDisplayValueFlow

    init {
        initModels()
    }

    fun initModels() {
        drinkCoordinator.getDrinkingCalculator()?.let {
            val drinkTimes = drinkCoordinator.getDrinkingTimes()
            when {
                drinkTimes == null -> _drinkStatusModelFlow.value = DrinkStatusModel.NotStarted
                drinkTimes.last() > clock.now() -> calculateDrinkGraphAndUpdateFlow(it, drinkTimes)
                else -> _drinkStatusModelFlow.value = DrinkStatusModel.Finished
            }
        } ?: kotlin.run { _drinkStatusModelFlow.value = DrinkStatusModel.NotStarted }
    }

    suspend fun startCountDownTimer(onFinished: () -> Unit) {
        drinkCoordinator.getDrinkingTimes()?.let { drinkingTimes ->
            drinkingTimes.find { it > clock.now() }?.let {
                countDownClock.countDownEachSecond(
                    duration = it.minus(clock.now()),
                    onTick = { millisUntilFinished ->
                        _countDownDisplayValueFlow.value = millisToDisplayString(millisUntilFinished)
                    },
                    onFinished = {
                        updateNUnitsConsumedFlow()
                        onFinished.invoke()
                    }
                )
            }
        }
    }

    fun stopCountDown() {
        countDownClock.stop()
    }

    fun stopDrinking() {
        countDownClock.stop()
        drinkCoordinator.stopDrinking()
        _drinkStatusModelFlow.value = DrinkStatusModel.NotStarted
        _countDownDisplayValueFlow.value = "00:00"
    }

    private fun calculateDrinkGraphAndUpdateFlow(drinkingCalculator: DrinkingCalculator, drinkingTimes: List<Instant>) {
        val estimates = drinkingCalculator.generateBACPrediction(drinkingTimes)
        val graphList = estimates?.mapIndexed { index: Int, pair: Pair<Float, Instant> ->
            if (index == estimates.lastIndex) {
                GraphEntry(
                    index.toFloat(),
                    pair.first,
                    "ic_finish_flag",
                    pair.second
                )
            } else {
                GraphEntry(
                    index.toFloat(),
                    pair.first,
                    drinkingCalculator.preferredUnit.iconName,
                    pair.second
                )
            }
        }

        updateNUnitsConsumedFlow()

        graphList?.let {
            _drinkStatusModelFlow.value = DrinkStatusModel.Drinking(it, drinkingCalculator.wantedBloodLevel)
        }
    }

    private fun updateNUnitsConsumedFlow() {
        drinkCoordinator.getDrinkingTimes()?.let { drinkingTimes ->
            if (drinkingTimes.any { it > clock.now() }) {
                val nUnitsStarted = drinkingTimes.filter { it < clock.now() }.size
                val isLastDrink = drinkingTimes.size - nUnitsStarted == 1
                val isFirstDrink = nUnitsStarted == 1
                val displayValue = when {
                    isLastDrink -> {
                        _countDownDescriptionDisplayValueFlow.value = COUNTDOWN_LAST_DRINK_DESCRIPTION
                        N_DRINKS_LAST_DRINK
                    }
                    isFirstDrink -> N_DRINKS_FIRST_DRINK
                    else -> {
                        val stringOrdinal = ordinal(nUnitsStarted)
                        "Enjoy your $stringOrdinal drink!"
                    }
                }
                _nOfUnitsDisplayValueFlow.value = displayValue
            } else {
                _drinkStatusModelFlow.value = DrinkStatusModel.Finished
            }
        }
    }

    private fun millisToDisplayString(millis: Long): String {
        val duration = millis.milliseconds
        val minutes = duration.inWholeMinutes - duration.inWholeHours * 60
        val seconds = duration.inWholeSeconds - duration.inWholeMinutes * 60
        return if (duration.inWholeHours != 0L) {
            "${duration.inWholeHours.toMinimumTwoPrecisionString()}:" +
                    "${minutes.toMinimumTwoPrecisionString()}:" +
                    seconds.toMinimumTwoPrecisionString()
        } else {
            "${minutes.toMinimumTwoPrecisionString()}:" +
                    seconds.toMinimumTwoPrecisionString()
        }
    }
}

sealed class DrinkStatusModel {
    object NotStarted : DrinkStatusModel()
    data class Drinking(val graphList: List<GraphEntry>, val wantedBloodLevel: Float) : DrinkStatusModel()
    object Finished : DrinkStatusModel()
}

data class GraphEntry(val x: Float, val y: Float, val iconName: String, val drinkAt: Instant)
