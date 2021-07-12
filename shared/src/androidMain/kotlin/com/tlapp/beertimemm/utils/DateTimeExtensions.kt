package com.tlapp.beertimemm.utils

import android.annotation.SuppressLint
import android.app.Application
import android.text.format.DateFormat
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.toJavaLocalDateTime
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import java.time.format.DateTimeFormatter

actual object DateTimeExtensions: KoinComponent {

    private val applicationContext: Application by inject()

    @SuppressLint("NewApi")
    actual fun LocalDateTime.toHourMinuteString(showAmPm: Boolean): String {
        val pattern = if (DateFormat.is24HourFormat(applicationContext)) {
            "HH:mm"
        } else {
            if (showAmPm) "hh:mm a" else "hh:mm"
        }
        return this.toJavaLocalDateTime().format(DateTimeFormatter.ofPattern(pattern)).replace(" ", "")
    }
}
