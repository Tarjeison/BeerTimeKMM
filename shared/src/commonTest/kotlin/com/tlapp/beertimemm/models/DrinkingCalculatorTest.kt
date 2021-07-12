package com.tlapp.beertimemm.models

import kotlinx.datetime.Clock
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue
import kotlin.time.Duration
import kotlin.time.ExperimentalTime

@ExperimentalTime
internal class DrinkingCalculatorTest {

    private val userProfileMale = UserProfile(Gender.MALE, 85)
    private val userProfileFemale = UserProfile(Gender.FEMALE, 85)
    private val beer = AlcoholUnit("Beer", 0.5F, 0.047F, "test", false)

    @Test
    fun correctDrinkingCalculation_forMaleUserProfiler() {
        val now = Clock.System.now()
        val drinkingCalculator = DrinkingCalculator(
            userProfile = userProfileMale,
            wantedBloodLevel = 0.1F,
            peakTime = now.plus(Duration.Companion.hours(2)),
            endTime = now.plus(Duration.Companion.hours(4)),
            preferredUnit = beer
        )
        val drinkingTimes = drinkingCalculator.calculateDrinkingTimes()
        assertEquals(6, drinkingTimes.size)
        assertTrue(drinkingTimes.first().minus(now).inWholeSeconds in 0..1)
        assertEquals(4, drinkingTimes.last().minus(now).inWholeHours.toInt(), )
    }

    @Test
    fun correctDrinkingCalculation_forFemaleUserProfiler() {
        val now = Clock.System.now()
        val drinkingCalculator = DrinkingCalculator(
            userProfile = userProfileFemale,
            wantedBloodLevel = 0.1F,
            peakTime = now.plus(Duration.Companion.hours(2)),
            endTime = now.plus(Duration.Companion.hours(4)),
            preferredUnit = beer
        )
        val drinkingTimes = drinkingCalculator.calculateDrinkingTimes()
        assertEquals(5, drinkingTimes.size)
        assertTrue(drinkingTimes.first().minus(now).inWholeSeconds in 0..1)
        assertEquals(4, drinkingTimes.last().minus(now).inWholeHours.toInt())
    }

    @Test
    fun bacPredictionIsGeneratedCorrectly() {
        val now = Clock.System.now()
        val drinkingCalculator = DrinkingCalculator(
            userProfile = userProfileFemale,
            wantedBloodLevel = 0.1F,
            peakTime = now.plus(Duration.Companion.hours(2)),
            endTime = now.plus(Duration.Companion.hours(4)),
            preferredUnit = beer
        )
        val drinkingTimes = drinkingCalculator.calculateDrinkingTimes()
        val bacPrediction = drinkingCalculator.generateBACPrediction(drinkingTimes)
        assertEquals(5, bacPrediction!!.size)
        assertEquals(0F, bacPrediction.first().first)
        assertEquals(0.29F, bacPrediction[1].first, 0.01F)
        assertEquals(0.59F, bacPrediction[2].first, 0.01F)
        assertEquals(0.89F, bacPrediction[3].first, 0.01F)
        assertEquals(0.98F, bacPrediction[4].first, 0.01F)

        assertEquals(drinkingTimes, bacPrediction.map { it.second })
    }
}
