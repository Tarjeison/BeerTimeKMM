package com.tlapp.beertimemm.viewmodels

import com.tlapp.beertimemm.drinking.DrinkCoordinator
import com.tlapp.beertimemm.models.AlcoholUnit
import com.tlapp.beertimemm.models.AlertDialogUiModel
import com.tlapp.beertimemm.models.DrinkingCalculator
import com.tlapp.beertimemm.resources.Strings.ALREADY_DRINKING_ALERT_MESSAGE
import com.tlapp.beertimemm.resources.Strings.ALREADY_DRINKING_ALERT_NEGATIVE_BUTTON
import com.tlapp.beertimemm.resources.Strings.ALREADY_DRINKING_ALERT_POSITIVE_BUTTON
import com.tlapp.beertimemm.resources.Strings.ALREADY_DRINKING_ALERT_TITLE
import com.tlapp.beertimemm.resources.Strings.ERROR_NO_BLOOD_LEVEL_SET
import com.tlapp.beertimemm.resources.Strings.ERROR_NO_DRINK_SELECTED
import com.tlapp.beertimemm.resources.Strings.ERROR_NO_PROFILE_FOUND
import com.tlapp.beertimemm.resources.Strings.ERROR_TOO_SHORT_DRINK_INTERVAL
import com.tlapp.beertimemm.sqldelight.DatabaseHelper
import com.tlapp.beertimemm.sqldelight.toAlcoholUnit
import com.tlapp.beertimemm.storage.ProfileStorage
import com.tlapp.beertimemm.utils.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import kotlin.time.Duration
import kotlin.time.ExperimentalTime

@OptIn(ExperimentalTime::class)
class StartDrinkingModel : KoinComponent {
    private val databaseHelper: DatabaseHelper by inject()
    private val profileStorage: ProfileStorage by inject()
    private val drinkCoordinator: DrinkCoordinator by inject()
    private val clock: Clock by inject()
    private val displayDateHelper: DisplayDateHelper by inject()

    private var wantedBloodLevel = 0f
    private var finishDrinkingInHoursMinutes: Pair<Int, Int> = Pair(0, 0)
    private var peakInHoursMinutes: Pair<Int, Int> = Pair(0, 0)

    private val selectedUnitFlow = MutableStateFlow<AlcoholUnit?>(null)

    private val _wantedBloodLevelUiModelFlow = MutableStateFlow<String?>(null)
    val wantedBloodLevelUiModelFlow: StateFlow<String?> get() = _wantedBloodLevelUiModelFlow

    private val _finishDrinkingSeekBarUiModelFlow = MutableStateFlow<String?>(null)
    val finishDrinkingSeekBarUiModelFlow: StateFlow<String?> get() = _finishDrinkingSeekBarUiModelFlow

    private val _peakTimeSeekBarUiModelFlow = MutableStateFlow<String?>(null)
    val peakTimeSeekBarUiModelFlow: StateFlow<String?> get() = _peakTimeSeekBarUiModelFlow

    private val _errorToastFlow = MutableStateFlow<AlwaysDistinct<String>?>(null)
    val errorToastFlow: StateFlow<AlwaysDistinct<String>?> get() = _errorToastFlow

    private val _alertFlow = MutableStateFlow<AlwaysDistinct<AlertDialogUiModel>?>(null)
    val alertFlow: StateFlow<AlwaysDistinct<AlertDialogUiModel>?> get() = _alertFlow

    private val _navigateToCountDownFlow = MutableStateFlow<AlwaysDistinct<Boolean>?>(null)
    val navigateToCountDownFlow: StateFlow<AlwaysDistinct<Boolean>?> get() = _navigateToCountDownFlow

    fun getDrinks(): Flow<List<AlcoholUnit>> {
        return databaseHelper.selectAllItems().combine(selectedUnitFlow) { drinks, selectedUnit ->
            drinks.map { drinkEntity ->
                drinkEntity.toAlcoholUnit().also {
                    it.isSelected = it == selectedUnit
                }
            }
        }
    }

    fun setWantedBloodLevel(wantedBloodLevelProgress: Int) {
        this.wantedBloodLevel = (wantedBloodLevelProgress.toFloat() / 100)
        val wantedBloodLevelProgressDisplayValue = (wantedBloodLevelProgress.toFloat() / 10).toString()
        _wantedBloodLevelUiModelFlow.value = "$wantedBloodLevelProgressDisplayValue‰"
    }

    fun setFinishDrinkingInHoursMinutes(seekBarValue: Int) {
        val minDrinkingTimeInHours = if (isDebug()) 0 else 1
        val hoursDrinking = (seekBarValue / 60) + minDrinkingTimeInHours
        clock.now().plus(Duration.Companion.minutes(2)).plus(Duration.Companion.minutes(2))
        var minutesDrinking = (seekBarValue - ((seekBarValue / 60) * 60) + (10 - seekBarValue % 10))
        minutesDrinking -= clock.now().toLocalDateTime(TimeZone.currentSystemDefault()).minute % 10
        finishDrinkingInHoursMinutes = Pair(hoursDrinking, minutesDrinking)
        _finishDrinkingSeekBarUiModelFlow.value =
            displayDateHelper.localDateTimeToHourMinuteString(
                clock.now().plus(Duration.Companion.hours(hoursDrinking)).plus(Duration.minutes(minutesDrinking)).toLocalDateTime(
                    TimeZone.currentSystemDefault()
                ), true
            )
    }

    fun setPeakTimeInHoursMinutes(seekBarValue: Int) {
        val minDrinkingTimeInHours = if (isDebug()) 0 else 1
        val hoursDrinking = (seekBarValue / 60) + minDrinkingTimeInHours
        var minutesDrinking = (seekBarValue - ((seekBarValue / 60) * 60)) + (10 - seekBarValue % 10)
        minutesDrinking -= clock.now().toLocalDateTime(TimeZone.currentSystemDefault()).minute % 10

        peakInHoursMinutes = Pair(hoursDrinking, minutesDrinking)
        _peakTimeSeekBarUiModelFlow.value =
            displayDateHelper.localDateTimeToHourMinuteString(
                clock.now().plus(Duration.Companion.hours(hoursDrinking)).plus(Duration.minutes(minutesDrinking)).toLocalDateTime(
                    TimeZone.currentSystemDefault()
                ), true
            )
    }

    fun setSelectedUnit(selectedUnit: AlcoholUnit) {
        selectedUnitFlow.value = selectedUnit
    }

    fun onDestroy() {
        _alertFlow.value = null
    }

    fun startDrinking() {
        if (drinkCoordinator.isDrinking()) {
            val alertDialogUiModel = AlertDialogUiModel(
                title = ALREADY_DRINKING_ALERT_TITLE,
                message = ALREADY_DRINKING_ALERT_MESSAGE,
                positiveButtonText = ALREADY_DRINKING_ALERT_POSITIVE_BUTTON,
                negativeButtonText = ALREADY_DRINKING_ALERT_NEGATIVE_BUTTON,
                onClick = {
                    validateValuesAndCreateCalculation()
                }
            )
            _alertFlow.value = AlwaysDistinct(alertDialogUiModel)
        } else {
            validateValuesAndCreateCalculation()
        }
    }

    private fun validateValuesAndCreateCalculation() {

        val selectedUnitNonNull = selectedUnitFlow.value ?: run {
            _errorToastFlow.value = AlwaysDistinct(ERROR_NO_DRINK_SELECTED)
            return
        }

        val selectedBloodLevel = if (wantedBloodLevel != 0f) {
            wantedBloodLevel
        } else {
            _errorToastFlow.value = AlwaysDistinct(ERROR_NO_BLOOD_LEVEL_SET)
            return
        }

        val selectedHoursDrinking = if (finishDrinkingInHoursMinutes.first != 0 || isDebug()) {
            finishDrinkingInHoursMinutes
        } else {
            _errorToastFlow.value = AlwaysDistinct(ERROR_TOO_SHORT_DRINK_INTERVAL)
            return
        }

        val selectedPeakHour = if (peakInHoursMinutes.first != 0 || isDebug()) {
            peakInHoursMinutes
        } else {
            _errorToastFlow.value = AlwaysDistinct(ERROR_TOO_SHORT_DRINK_INTERVAL)
            return
        }

        val profile = profileStorage.getUserProfile() ?: kotlin.run {
            _errorToastFlow.value = AlwaysDistinct(ERROR_NO_PROFILE_FOUND)
            return
        }

//        firebaseAnalytics.logEvent("pressed_start_drinking") {
//            param("bac", selectedBloodLevel.toDouble())
//            param("hours", selectedHoursDrinking.first.toDouble())
//            param("unit", selectedUnit.toString())
//            param("profile", profile.toString())
//        }

        val drinkingCalculator = DrinkingCalculator(
            userProfile = profile,
            wantedBloodLevel = selectedBloodLevel,
            endTime = Clock.System.now().plus(
                Duration.hours(selectedHoursDrinking.first)
                    .plus(Duration.minutes(selectedHoursDrinking.second))
            ),
            peakTime = Clock.System.now().plus(
                Duration.hours(selectedPeakHour.first)
                    .plus(Duration.minutes(selectedPeakHour.second))
            ),
            preferredUnit = selectedUnitNonNull
        )

        when (val result = drinkCoordinator.startDrinking(drinkingCalculator)) {
            is Success -> _navigateToCountDownFlow.value = AlwaysDistinct(true)
            is Failure -> _errorToastFlow.value = AlwaysDistinct(result.reason)
        }
    }
}
