package com.tlapp.beertimemm.utils

fun Long.toMinimumTwoPrecisionString(): String {
    return if (this > 9) {
        this.toString()
    } else {
        "0${this}"
    }
}

fun ordinal(i: Int): String {
    val suffix = arrayOf("th", "st", "nd", "rd", "th", "th", "th", "th", "th", "th")
    return when (i % 100) {
        11, 12, 13 -> i.toString() + "th"
        else -> "$i${suffix[i % 10]}"
    }
}
