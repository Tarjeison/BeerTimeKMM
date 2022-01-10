package com.tlapp.beertimemm.viewmodels

import com.tlapp.beertimemm.models.DrinkIconItem
import com.tlapp.beertimemm.resources.Strings.BABY_DRINK_LITER
import com.tlapp.beertimemm.resources.Strings.BABY_DRINK_OZ
import com.tlapp.beertimemm.resources.Strings.DRINK_ADDED
import com.tlapp.beertimemm.resources.Strings.NO_ALCOHOL_INPUT_ERROR
import com.tlapp.beertimemm.resources.Strings.NO_NAMELESS_DRINK
import com.tlapp.beertimemm.resources.Strings.NO_VOLUME_DRINK
import com.tlapp.beertimemm.resources.Strings.TOO_BIG_DRINK_LITER
import com.tlapp.beertimemm.resources.Strings.TOO_BIG_DRINK_OZ
import com.tlapp.beertimemm.resources.Strings.TOO_STRONG_DRINK
import com.tlapp.beertimemm.resources.Strings.TOO_WEAK_DRINK
import com.tlapp.beertimemm.sqldelight.DatabaseHelper
import com.tlapp.beertimemm.storage.PreferredVolume
import com.tlapp.beertimemm.storage.ProfileStorage
import com.tlapp.beertimemm.utils.Failure
import com.tlapp.beertimemm.utils.LITER_TO_OZ_RATIO
import com.tlapp.beertimemm.utils.Result
import com.tlapp.beertimemm.utils.Success
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.filterNotNull
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class AddDrinkModel : KoinComponent {

    private val databaseHelper: DatabaseHelper by inject()
    private val profileStorage: ProfileStorage by inject()

    private val _addDrinkResultStateFlow = MutableStateFlow<Result<String, Pair<AddDrinkInputField, String>>?>(null)
    val addDrinkResultFlow: Flow<Result<String, Pair<AddDrinkInputField, String>>> get() = _addDrinkResultStateFlow.filterNotNull()

    private val drinkIconItems = listOf(
        DrinkIconItem(
            iconString = "ic_beer",
            tag = 1
        ),
        DrinkIconItem(
            iconString = "ic_wine",
            tag = 2
        ),
        DrinkIconItem(
            iconString = "ic_cocktail",
            tag = 3
        )
    )

    fun getDrinkIcons(): List<DrinkIconItem> {
        return drinkIconItems
    }

    fun addDrink(
        drinkName: String?,
        drinkPercentage: String?,
        drinkVolume: String?,
        drinkIconTag: Int?
    ) {
        val iconName = drinkIconItems.firstOrNull { it.tag == drinkIconTag }?.iconString
        addDrink(drinkName, drinkPercentage, drinkVolume, iconName)
    }

    fun addDrink(
        drinkName: String?,
        drinkPercentage: String?,
        drinkVolume: String?,
        drinkIconName: String?
    ) {
        val drinkNameValid = drinkName.takeIf { it?.isNotEmpty() == true } ?: run {
            _addDrinkResultStateFlow.value = Failure(
                Pair(AddDrinkInputField.DRINK_NAME, NO_NAMELESS_DRINK)
            )
            return
        }

        val drinkPercentageValid = validateDrinkPercentage(drinkPercentage) ?: return
        val drinkVolumeValid = validateDrinkVolume(drinkVolume) ?: return
        val drinkIconNameValid = drinkIconName ?: "ic_beer"
        databaseHelper.insertDrink(
            name = drinkNameValid,
            percentage = drinkPercentageValid,
            volume = drinkVolumeValid,
            iconName = drinkIconNameValid
        )
        _addDrinkResultStateFlow.value = Success(DRINK_ADDED)
    }

    private fun validateDrinkPercentage(drinkPercentage: String?): Float? {
        val drinkPercentageNonNullFloat = drinkPercentage?.replace(",", ".")?.toFloat() ?: run {
            _addDrinkResultStateFlow.value =
                Failure(
                    Pair(
                        AddDrinkInputField.DRINK_PERCENTAGE,
                        NO_ALCOHOL_INPUT_ERROR
                    )
                )
            return null
        }
        return when {
            drinkPercentageNonNullFloat > 100F -> {
                _addDrinkResultStateFlow.value =
                    Failure(
                        Pair(AddDrinkInputField.DRINK_PERCENTAGE, TOO_STRONG_DRINK)
                    )
                null
            }
            drinkPercentageNonNullFloat < 2F -> {
                _addDrinkResultStateFlow.value =
                    Failure(
                        Pair(AddDrinkInputField.DRINK_PERCENTAGE, TOO_WEAK_DRINK)
                    )
                null
            }
            else -> {
                drinkPercentageNonNullFloat / 100F
            }
        }
    }

    private fun validateDrinkVolume(drinkVolume: String?): Float? {
        val isUsingLiters = profileStorage.getPreferredVolume() == PreferredVolume.LITER
        val drinkValidFormat = drinkVolume?.replace(",", ".")?.toFloat() ?: run {
            _addDrinkResultStateFlow.value =
                Failure(
                    Pair(AddDrinkInputField.DRINK_VOLUME, NO_VOLUME_DRINK)
                )
            return null
        }
        val drinkInLiters = if (isUsingLiters) drinkValidFormat else drinkValidFormat / LITER_TO_OZ_RATIO
        return when {
            drinkInLiters < 0.02F -> {
                val errorString = if (isUsingLiters) {
                    BABY_DRINK_LITER
                } else {
                    BABY_DRINK_OZ
                }
                _addDrinkResultStateFlow.value =
                    Failure(
                        Pair(AddDrinkInputField.DRINK_VOLUME, errorString)
                    )
                null
            }
            drinkInLiters > 1F -> {
                val errorString = if (isUsingLiters) {
                    TOO_BIG_DRINK_LITER
                } else {
                    TOO_BIG_DRINK_OZ
                }
                _addDrinkResultStateFlow.value =
                    Failure(
                        Pair(AddDrinkInputField.DRINK_VOLUME, errorString)
                    )
                null
            }
            else -> {
                drinkInLiters
            }
        }
    }
}

enum class AddDrinkInputField {
    DRINK_NAME, DRINK_VOLUME, DRINK_PERCENTAGE
}
