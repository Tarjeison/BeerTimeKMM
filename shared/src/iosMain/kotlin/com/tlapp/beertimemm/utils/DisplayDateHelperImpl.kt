package com.tlapp.beertimemm.utils

import kotlinx.datetime.LocalDateTime

actual class DisplayDateHelperImpl: DisplayDateHelper {
    actual override fun localDateTimeToHourMinuteString(localDateTime: LocalDateTime, showAmPm: Boolean): String = "iOS"
}
