package com.tlapp.beertimemm.utils

import kotlinx.datetime.LocalDateTime

interface DisplayDateHelper {
    fun localDateTimeToHourMinuteString(localDateTime: LocalDateTime, showAmPm: Boolean = true): String
}

expect class DisplayDateHelperImpl: DisplayDateHelper {
    override fun localDateTimeToHourMinuteString(localDateTime: LocalDateTime, showAmPm: Boolean): String
}
