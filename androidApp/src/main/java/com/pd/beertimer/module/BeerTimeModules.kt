package com.pd.beertimer.module

import android.app.Application
import android.content.Context
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.ktx.Firebase
import com.pd.beertimer.feature.countdown.CountDownViewModel
import com.pd.beertimer.feature.drinks.AddDrinkViewModel
import com.pd.beertimer.feature.drinks.MyDrinksViewModel
import com.pd.beertimer.feature.info.InfoViewModel
import com.pd.beertimer.feature.profile.ProfileViewModel
import com.pd.beertimer.feature.startdrinking.StartDrinkingViewModel
import com.pd.beertimer.util.NotificationScheduler
import com.pd.beertimer.util.SHARED_PREF_BEER_TIME
import com.pd.beertimer.util.StorageHelper
import com.pd.beertimer.util.VolumeConverter
import com.tlapp.beertimemm.drinking.DrinkNotificationScheduler
import kotlinx.serialization.ExperimentalSerializationApi
import org.koin.android.ext.koin.androidApplication
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.bind
import org.koin.dsl.module
import kotlin.time.ExperimentalTime

@ExperimentalSerializationApi
@ExperimentalTime
fun beerTimeModules(applicationContext: Application) = module {
    single<Context> {
        applicationContext.applicationContext
    }

    single<Application> {
        applicationContext
    }

    viewModel {
        CountDownViewModel()
    }

    viewModel {
        InfoViewModel(applicationContext = get())
    }

    viewModel {
        ProfileViewModel(profileModel = get())
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
        NotificationScheduler(application = androidApplication())
    }.bind(DrinkNotificationScheduler::class)

    viewModel {
        StartDrinkingViewModel()
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
