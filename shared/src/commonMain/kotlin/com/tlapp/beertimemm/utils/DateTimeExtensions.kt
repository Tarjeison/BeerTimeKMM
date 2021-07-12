package com.tlapp.beertimemm.utils

import kotlinx.datetime.LocalDateTime

expect object DateTimeExtensions {
    fun LocalDateTime.toHourMinuteString(showAmPm: Boolean = true): String
}
