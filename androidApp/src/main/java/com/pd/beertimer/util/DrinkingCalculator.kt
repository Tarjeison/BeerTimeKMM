package com.pd.beertimer.util

import com.pd.beertimer.models.AlcoholUnit
import com.pd.beertimer.models.Gender
import com.pd.beertimer.models.UserProfile
import kotlinx.serialization.Serializable
import java.time.Duration
import java.time.LocalDateTime
import kotlin.math.roundToInt

@Serializable
data class DrinkingCalculator(
    val userProfile: UserProfile,
    val wantedBloodLevel: Float,
    @Serializable(with = LocalDateTimeSerializer::class)
    val peakTime: LocalDateTime,
    @Serializable(with = LocalDateTimeSerializer::class)
    val endTime: LocalDateTime,
    val preferredUnit: AlcoholUnit
) {
    private val genderConst = when (userProfile.gender) {
        Gender.MALE -> 0.68
        Gender.FEMALE -> 0.55
    }

    // Two intervals are added (startTime-peakTime) and (peakTime-endTime). endTime is added to the
    // end as it is used to generate notification to that it's time to go home

    fun calculateDrinkingTimes(): MutableList<LocalDateTime> {

        val startTime = LocalDateTime.now()
        val drinkingTimes = mutableListOf<LocalDateTime>()
        drinkingTimes.addAll(calculateDrinkInterval(startTime, peakTime, wantedBloodLevel))

        if (Duration.between(peakTime, endTime).toMinutes() > 30F) {
            drinkingTimes.addAll(calculateDrinkInterval(peakTime, endTime, 0F))
        }

        drinkingTimes.add(endTime)

        return drinkingTimes
    }

    private fun calculateDrinkInterval(
        startTime: LocalDateTime, endTime: LocalDateTime, wantedBloodLevel: Float
    ): MutableList<LocalDateTime> {
        val drinkingDuration = Duration.between(startTime, endTime)
        val neededGrams =
            ((wantedBloodLevel + 0.015 * drinkingDuration.toMinutes() / 60f) * ((userProfile.weight * 1000 * genderConst))) / 100
        val numberOfUnitsToDrink = (neededGrams / preferredUnit.gramPerUnit).roundToInt()
        if (numberOfUnitsToDrink > 85) return mutableListOf() // TODO: Make this return proper errors
        if (numberOfUnitsToDrink == 0) return mutableListOf() // Nothing to drink
        val dDuration = drinkingDuration.dividedBy(numberOfUnitsToDrink.toLong())
        val drinkingTimes = mutableListOf<LocalDateTime>()
        for (i in 0 until numberOfUnitsToDrink) {
            drinkingTimes.add(startTime.plus(dDuration.multipliedBy(i.toLong())))
        }
        return drinkingTimes
    }

    fun generateBACPrediction(drinkingTimes: List<LocalDateTime>): List<Pair<Float, LocalDateTime>>? {
        if (drinkingTimes.size < 2) {
            // Not plotworthy
            return null
        }
        val bacEstimations = mutableListOf<Pair<Float, LocalDateTime>>()
        val startTime = drinkingTimes.first()
        for (i in drinkingTimes.indices) {
            if (i == 0) {
                bacEstimations.add(Pair(0F, drinkingTimes[i]))
            } else {
                bacEstimations.add(
                    Pair(
                        calculateBac(Duration.between(startTime, drinkingTimes[i]), i).toPermille(),
                        drinkingTimes[i]
                    )
                )
            }
        }
        return bacEstimations
    }

    private fun calculateBac(duration: Duration, nConsumed: Int): Float {
        return ((preferredUnit.gramPerUnit * nConsumed / (userProfile.weight * 1000 * genderConst)) * 100 - (duration.toMinutes() / 60f) * 0.015).toFloat()
    }

    fun changeDuration(previousDrinkingTimes: List<LocalDateTime>, numConsumed: Int) {
        val gramsConsumed = numConsumed * preferredUnit.gramPerUnit
        val now = LocalDateTime.now()
        val newDuration = Duration.between(now, peakTime)
        val currentBac = (gramsConsumed / (userProfile.weight * 1000 * genderConst)) * 100 -
                Duration.between(previousDrinkingTimes.first(), now).toHours() * 0.015

        val bacAtEndNoMoreUnits = currentBac - (newDuration.toHours() * 0.015)
        if (bacAtEndNoMoreUnits > wantedBloodLevel) {
            // No more drinks needed
        } else {
            val neededGrams =
                ((wantedBloodLevel + 0.015 * newDuration.toHours()) * ((userProfile.weight * 1000 * genderConst))) / 100
        }
    }
}
