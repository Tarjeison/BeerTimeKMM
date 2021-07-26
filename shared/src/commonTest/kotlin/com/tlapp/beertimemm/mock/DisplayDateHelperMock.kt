package com.tlapp.beertimemm.mock

import com.tlapp.beertimemm.utils.DisplayDateHelper
import kotlinx.datetime.LocalDateTime

class DisplayDateHelperMock: DisplayDateHelper {
    var lastInvokedWith: LocalDateTime? = null
    override fun localDateTimeToHourMinuteString(localDateTime: LocalDateTime, showAmPm: Boolean): String {
        lastInvokedWith = localDateTime
        return "Test"
    }
}
