package com.tlapp.beertimemm.models

import kotlinx.datetime.*
import kotlinx.serialization.Serializable
import kotlin.math.roundToInt
import kotlin.time.Duration
import kotlin.time.ExperimentalTime

@OptIn(ExperimentalTime::class)
@Serializable
data class DrinkingCalculator(
    val userProfile: UserProfile,
    val wantedBloodLevel: Float,
    val peakTime: Instant,
    val endTime: Instant,
    val preferredUnit: AlcoholUnit
) {
    private val genderConst = when (userProfile.gender) {
        Gender.MALE -> 0.68
        Gender.FEMALE -> 0.55
    }

    // Two intervals are added (startTime-peakTime) and (peakTime-endTime). endTime is added to the
    // end as it is used to generate notification to that it's time to go home

    fun calculateDrinkingTimes(): MutableList<Instant> {

        val startTime = Clock.System.now()
        val drinkingTimes = mutableListOf<Instant>()
        drinkingTimes.addAll(calculateDrinkInterval(startTime, peakTime, wantedBloodLevel))
        if (endTime.minus(peakTime).inWholeMinutes > 30L) {
            drinkingTimes.addAll(calculateDrinkInterval(peakTime, endTime, 0F))
        }

        drinkingTimes.add(endTime)

        return drinkingTimes
    }

    private fun calculateDrinkInterval(
        startTime: Instant, endTime: Instant, wantedBloodLevel: Float
    ): MutableList<Instant> {
        val drinkingDuration = endTime.minus(startTime)
        val neededGrams =
            ((wantedBloodLevel + 0.015 * drinkingDuration.inWholeMinutes / 60F) * ((userProfile.weight * 1000 * genderConst))) / 100
        val numberOfUnitsToDrink = (neededGrams / preferredUnit.gramPerUnit).roundToInt()
        if (numberOfUnitsToDrink > 85) return mutableListOf() // TODO: Make this return proper errors
        if (numberOfUnitsToDrink == 0) return mutableListOf() // Nothing to drink
        val dDuration = drinkingDuration.div(numberOfUnitsToDrink)
        val drinkingTimes = mutableListOf<Instant>()
        for (i in 0 until numberOfUnitsToDrink) {
            drinkingTimes.add(startTime.plus(dDuration.times(i)))
        }
        return drinkingTimes
    }

    fun generateBACPrediction(drinkingTimes: List<Instant>): List<Pair<Float, Instant>>? {
        if (drinkingTimes.size < 2) {
            // Not plotworthy
            return null
        }
        val bacEstimations = mutableListOf<Pair<Float, Instant>>()
        val startTime = drinkingTimes.first()
        for (i in drinkingTimes.indices) {
            if (i == 0) {
                bacEstimations.add(Pair(0F, drinkingTimes[i]))
            } else {
                bacEstimations.add(
                    Pair(
                        calculateBac(drinkingTimes[i].minus(startTime), i).toPermille(),
                        drinkingTimes[i]
                    )
                )
            }
        }
        return bacEstimations
    }

    private fun calculateBac(duration: Duration, nConsumed: Int): Float {
        return ((preferredUnit.gramPerUnit * nConsumed / (userProfile.weight * 1000 * genderConst)) * 100 - (duration.inWholeMinutes / 60f) * 0.015).toFloat()
    }

//    fun changeDuration(previousDrinkingTimes: List<Instant>, numConsumed: Int) {
//        val gramsConsumed = numConsumed * preferredUnit.gramPerUnit
//        val now = Clock.System.now()
//        val newDuration = peakTime.minus(now)
//        val currentBac = (gramsConsumed / (userProfile.weight * 1000 * genderConst)) * 100 -
//                now.minus(previousDrinkingTimes.first()).inWholeHours * 0.015
//
//        val bacAtEndNoMoreUnits = currentBac - (newDuration.inWholeHours * 0.015)
//        if (bacAtEndNoMoreUnits > wantedBloodLevel) {
//            // No more drinks needed
//        } else {
//            val neededGrams =
//                ((wantedBloodLevel + 0.015 * newDuration.inWholeHours) * ((userProfile.weight * 1000 * genderConst))) / 100
//        }
//    }
}


fun Float.toPermille(): Float {
    return this * 10F
}
