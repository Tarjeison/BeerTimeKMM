package com.pd.beertimer.feature.startdrinking

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.logEvent
import com.pd.beertimer.BuildConfig
import com.pd.beertimer.R
import com.pd.beertimer.feature.drinks.DrinkRepository
import com.pd.beertimer.util.Failure
import com.pd.beertimer.util.Result
import com.pd.beertimer.util.Success
import com.pd.beertimer.util.toHourMinuteString
import com.tlapp.beertimemm.ProfileStorage
import com.tlapp.beertimemm.models.AlcoholUnit
import com.tlapp.beertimemm.models.DrinkingCalculator
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlin.time.Duration
import kotlin.time.ExperimentalTime

@ExperimentalTime
class StartDrinkingViewModel(
    private val applicationContext: Application,
    private val drinkRepository: DrinkRepository,
    private val profileStorage: ProfileStorage,
    private val firebaseAnalytics: FirebaseAnalytics
) : ViewModel() {

    private var wantedBloodLevel = 0f
    private var finishDrinkingInHoursMinutes: Pair<Int, Int> = Pair(0, 0)
    private var peakInHoursMinutes: Pair<Int, Int> = Pair(0, 0)
    private var hasSetPeakTime = false

    private val _drinksLiveData = MutableLiveData<List<AlcoholUnit>>()
    val drinksLiveData: LiveData<List<AlcoholUnit>> get() = _drinksLiveData

    private val _peakHourLiveData = MutableLiveData<SeekBarUIModel>()
    val peakHourLiveData: LiveData<SeekBarUIModel> get() = _peakHourLiveData

    private val _finishBarLiveData = MutableLiveData<SeekBarUIModel>()
    val finishBarLiveData: LiveData<SeekBarUIModel> get() = _finishBarLiveData

    private val _wantedBloodLevelLiveData = MutableLiveData<String>()
    val wantedBloodLevelLiveData: LiveData<String> get() = _wantedBloodLevelLiveData

    @ExperimentalCoroutinesApi
    fun getDrinks() {
        drinkRepository.getDrinks().onEach {
            _drinksLiveData.postValue(it.map { drink ->
                drink.toAlcoholUnit()
            })
        }.launchIn(viewModelScope)
    }

    fun setWantedBloodLevel(wantedBloodLevelProgress: Int) {
        this.wantedBloodLevel = (wantedBloodLevelProgress.toFloat() / 100)
        _wantedBloodLevelLiveData.postValue(
            String.format(
                applicationContext.getString(R.string.startdrinking_permille),
                (wantedBloodLevelProgress.toFloat() / 10).toString()
            )
        )
    }

    fun setFinishDrinkingInHoursMinutes(seekBarValue: Int) {
        val minDrinkingTimeInHours = if (BuildConfig.DEBUG) 0 else 1
        val hoursDrinking = (seekBarValue / 60) + minDrinkingTimeInHours
        Clock.System.now().plus(Duration.Companion.minutes(2)).plus(Duration.Companion.minutes(2))
        var minutesDrinking = (seekBarValue - ((seekBarValue / 60) * 60) + (10 - seekBarValue % 10))
        minutesDrinking -= Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).minute % 10
        finishDrinkingInHoursMinutes = Pair(hoursDrinking, minutesDrinking)
        val seekBarUIModel = SeekBarUIModel(
            displayString = Clock.System.now().plus(Duration.Companion.hours(hoursDrinking)).plus(Duration.minutes(minutesDrinking)).toLocalDateTime(
                TimeZone.currentSystemDefault()
            ).toHourMinuteString(applicationContext, true)
        )
        _finishBarLiveData.postValue(seekBarUIModel)
    }

    fun setPeakTimeInHoursMinutes(seekBarValue: Int) {
        hasSetPeakTime = true
        val minDrinkingTimeInHours = if (BuildConfig.DEBUG) 0 else 1
        val hoursDrinking = (seekBarValue / 60) + minDrinkingTimeInHours
        var minutesDrinking = (seekBarValue - ((seekBarValue / 60) * 60)) + (10 - seekBarValue % 10)
        minutesDrinking -= Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).minute % 10

        peakInHoursMinutes = Pair(hoursDrinking, minutesDrinking)
        val seekBarUIModel = SeekBarUIModel(
            displayString = Clock.System.now().plus(Duration.Companion.hours(hoursDrinking)).plus(Duration.minutes(minutesDrinking)).toLocalDateTime(
                TimeZone.currentSystemDefault()
            ).toHourMinuteString(applicationContext, true)
        )
        _peakHourLiveData.postValue(seekBarUIModel)
    }

    fun peakTimeNotSet(): Boolean {
        return !hasSetPeakTime
    }


    fun validateValuesAndCreateCalculation(selectedUnit: AlcoholUnit): Result<DrinkingCalculator, Int> {
        val selectedBloodLevel = if (wantedBloodLevel != 0f) {
            wantedBloodLevel
        } else {
            return Failure(R.string.error_startdrinking_wanted_level_0)
        }

        val selectedHoursDrinking = if (finishDrinkingInHoursMinutes.first != 0 || BuildConfig.DEBUG) {
            finishDrinkingInHoursMinutes
        } else {
            return Failure(R.string.error_startdrinking_hours_drinking_0)
        }

        val selectedPeakHour = if (peakInHoursMinutes.first != 0 || BuildConfig.DEBUG) {
            peakInHoursMinutes
        } else {
            return Failure(R.string.error_startdrinking_hours_drinking_0)

        }

        val profile = profileStorage.getUserProfile() ?: kotlin.run {
            return Failure(R.string.error_no_user_profile_found)
        }

        firebaseAnalytics.logEvent("pressed_start_drinking") {
            param("bac", selectedBloodLevel.toDouble())
            param("hours", selectedHoursDrinking.first.toDouble())
            param("unit", selectedUnit.toString())
            param("profile", profile.toString())
        }

        return Success(
            DrinkingCalculator(
                userProfile = profile,
                wantedBloodLevel = selectedBloodLevel,
                endTime = Clock.System.now().plus(Duration.hours(selectedHoursDrinking.first)
                    .plus(Duration.minutes(selectedHoursDrinking.second))),
                peakTime = Clock.System.now().plus(Duration.hours(selectedPeakHour.first)
                    .plus(Duration.minutes(selectedPeakHour.second))),
                preferredUnit = selectedUnit
            )
        )
    }
}
