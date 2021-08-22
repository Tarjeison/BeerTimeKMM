package com.tlapp.beertimemm.utils

import kotlinx.datetime.LocalDateTime

actual class DisplayDateHelperImpl : DisplayDateHelper {
    actual override fun localDateTimeToHourMinuteString(
        localDateTime: LocalDateTime,
        showAmPm: Boolean
    ): String {
        // TODO: Figure out why NSDate doesn't work
        var hourString = localDateTime.hour.toString()
        if (hourString.length == 1) hourString = "0$hourString"
        var minuteString = localDateTime.minute.toString()
        if (minuteString.length == 1) minuteString = "0$minuteString"
        return "${hourString}:${minuteString}"

    }
}
