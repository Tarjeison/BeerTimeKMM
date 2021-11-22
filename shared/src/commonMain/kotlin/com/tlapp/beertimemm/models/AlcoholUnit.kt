package com.tlapp.beertimemm.models

import kotlinx.serialization.Serializable

@Serializable
data class AlcoholUnit(
    val name: String,
    val volume: Float,
    val percentage: Float,
    val iconName: String,
    var isSelected: Boolean = false
) {
    val gramPerUnit = volume * 1000 * percentage * 0.789
    // TODO: Move volume converter to KMM
    fun getDescription(): String {
        val formattedFloat = volume.toString()
        val formattedVolume = if (formattedFloat.length > 4) {
            formattedFloat.take(5)
        } else {
            formattedFloat
        }

        return "${(percentage * 100)}%, $formattedVolume L"
    }
}
