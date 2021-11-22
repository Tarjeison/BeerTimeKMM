package com.tlapp.beertimemm.models

data class MyDrinkItem(
    val name: String,
    val volume: Float,
    val percentage: Float,
    val iconName: String,
    val key: Long
) {
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
