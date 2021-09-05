package com.pd.beertimer.util

import android.content.Context

fun Float.toPermille(): Float {
    return this*10F
}

fun Context.getIconFromName(name: String): Int? {
    return this.resources.getIdentifier(
        name,
        "drawable",
        this.packageName
    ).takeIf { it != 0 }
}