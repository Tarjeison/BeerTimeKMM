package com.pd.beertimer.module

import android.app.Application
import android.content.Context
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.ktx.Firebase
import com.pd.beertimer.feature.drinks.AddDrinkViewModel
import com.pd.beertimer.feature.drinks.MyDrinksViewModel
import com.pd.beertimer.feature.info.InfoViewModel
import com.pd.beertimer.feature.profile.ProfileViewModel
import com.pd.beertimer.feature.startdrinking.StartDrinkingViewModel
import com.pd.beertimer.util.AlarmUtils
import com.pd.beertimer.util.SHARED_PREF_BEER_TIME
import com.pd.beertimer.util.StorageHelper
import com.pd.beertimer.util.VolumeConverter
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import kotlin.time.ExperimentalTime

@ExperimentalTime
fun beerTimeModules(applicationContext: Application) = module {
    single<Context> {
        applicationContext.applicationContext
    }

    single<Application> {
        applicationContext
    }

    viewModel {
        InfoViewModel(applicationContext = get())
    }

    viewModel {
        ProfileViewModel(profileStorage = get())
    }

    factory {
        VolumeConverter(sharedPreferences = get())
    }

    factory {
        get<Context>().getSharedPreferences(
            SHARED_PREF_BEER_TIME,
            Context.MODE_PRIVATE
        )
    }

    factory {
        StorageHelper(
            sharedPreferences = get()
        )
    }

    single {
        AlarmUtils(context = get())
    }

    viewModel {
        StartDrinkingViewModel(
            databaseHelper = get(),
            applicationContext = get(),
            profileStorage = get(),
            firebaseAnalytics = get()
        )
    }

    single {
        getFirebaseAnalytics()
    }

    viewModel { MyDrinksViewModel(databaseHelper = get()) }

    viewModel { AddDrinkViewModel(databaseHelper = get(), sharedPreferences = get()) }

}

private fun getFirebaseAnalytics(): FirebaseAnalytics {
    return Firebase.analytics
}
