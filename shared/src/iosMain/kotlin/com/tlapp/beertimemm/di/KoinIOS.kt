package com.tlapp.beertimemm.di

import com.russhwolf.settings.AppleSettings
import com.russhwolf.settings.Settings
import com.squareup.sqldelight.db.SqlDriver
import com.squareup.sqldelight.drivers.native.NativeSqliteDriver
import com.tlapp.beertimemm.drinking.DrinkNotificationScheduler
import com.tlapp.beertimemm.utils.CountDownClock
import com.tlapp.beertimemm.utils.CountDownClockImpl
import com.tlapp.beertimemm.utils.DisplayDateHelper
import com.tlapp.beertimemm.utils.DisplayDateHelperImpl
import drinkdb.Drink
import kotlinx.cinterop.ObjCClass
import kotlinx.cinterop.getOriginalKotlinClass
import org.koin.core.Koin
import org.koin.core.KoinApplication
import org.koin.core.module.Module
import org.koin.core.parameter.parametersOf
import org.koin.core.qualifier.Qualifier
import org.koin.dsl.module
import platform.Foundation.NSUserDefaults
import kotlin.time.ExperimentalTime


@ExperimentalTime
fun initKoinIos(
    userDefaults: NSUserDefaults,
    drinkNotificationScheduler: DrinkNotificationScheduler
): KoinApplication = initKoin(
    module {
        single<Settings> { AppleSettings(userDefaults) }
        single<DrinkNotificationScheduler> { drinkNotificationScheduler }
    }
)

@ExperimentalTime
actual val platformModule: Module = module {
    single<SqlDriver> {
        NativeSqliteDriver(
            Drink.Schema,
            "DrinkDb"
        )
    }

    single<DisplayDateHelper> { DisplayDateHelperImpl() }
    single<CountDownClock> { CountDownClockImpl() }
}

fun Koin.get(objCClass: ObjCClass): Any {
    val kClazz = getOriginalKotlinClass(objCClass)!!
    return get(kClazz)
}

fun Koin.get(objCClass: ObjCClass, qualifier: Qualifier?, parameter: Any): Any {
    val kClazz = getOriginalKotlinClass(objCClass)!!
    return get(kClazz, qualifier) { parametersOf(parameter) }
}

fun Koin.get(objCClass: ObjCClass, parameter: Any): Any {
    val kClazz = getOriginalKotlinClass(objCClass)!!
    return get(kClazz, null) { parametersOf(parameter) }
}

fun Koin.get(objCClass: ObjCClass, qualifier: Qualifier?): Any {
    val kClazz = getOriginalKotlinClass(objCClass)!!
    return get(kClazz, qualifier, null)
}
