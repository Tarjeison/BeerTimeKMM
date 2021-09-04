package com.tlapp.beertimemm.utils

import kotlinx.datetime.*
import platform.Foundation.*
actual class DisplayDateHelperImpl : DisplayDateHelper {

    private val timeFormatter = NSDateFormatter().apply {
        dateStyle = NSDateFormatterNoStyle
        timeStyle = NSDateFormatterShortStyle
    }

    actual override fun localDateTimeToHourMinuteString(
        localDateTime: LocalDateTime,
        showAmPm: Boolean
    ): String {
        val nsDate = localDateTime.toInstant(TimeZone.currentSystemDefault()).toNSDate()
        return timeFormatter.stringFromDate(nsDate)
    }
}

fun Instant.toDisplayValue(): String {
    val timeFormatter = NSDateFormatter().apply {
        dateStyle = NSDateFormatterNoStyle
        timeStyle = NSDateFormatterShortStyle
    }
    return timeFormatter.stringFromDate(this.toNSDate())
}
