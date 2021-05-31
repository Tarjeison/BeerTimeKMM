package com.pd.beertimer

import android.app.Application
import com.pd.beertimer.module.beerTimeModules
import com.tlapp.beertimemm.initKoin

class BeerTimerApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        initKoin(beerTimeModules(this@BeerTimerApplication))
    }
}
