package com.pd.beertimer.util

import android.content.SharedPreferences

private const val FIRST_TIME_USER_PREF_KEY = "FIRST_TIME_USER_PREF_KEY"

// TODO: Check if this can be deleted
class StorageHelper(private val sharedPreferences: SharedPreferences) {
    fun isFirstTimeUser(): Boolean {
        return sharedPreferences.getBoolean(FIRST_TIME_USER_PREF_KEY, false)
    }

    fun firstTimeSetupFinished() {
        sharedPreferences.edit().putBoolean(FIRST_TIME_USER_PREF_KEY, true).apply()
    }
}