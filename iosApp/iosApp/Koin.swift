//
//  Koin.swift
//  iosApp
//
//  Created by Trym Tarjeison Lekva on 22/08/2021.
//  Copyright © 2021 orgName. All rights reserved.
//

import Foundation
import shared

func startKoin() {
    // You could just as easily define all these dependencies in Kotlin, but this helps demonstrate how you might pass platform-specific dependencies in a larger scale project where declaring them in Kotlin is more difficult, or where they're also used in iOS-specific code.
    
    let userDefaults = UserDefaults(suiteName: "BEERTIME_SETTINGS")!
    
    let koinApplication = KoinIOSKt.doInitKoinIos(userDefaults: userDefaults, drinkNotificationScheduler: NotificationScheduler.init())
    _koin = koinApplication.koin
}

private var _koin: Koin_coreKoin? = nil
var koin: Koin_coreKoin {
    return _koin!
}
