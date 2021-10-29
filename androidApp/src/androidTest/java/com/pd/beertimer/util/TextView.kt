package com.pd.beertimer.util

import android.text.TextUtils
import android.view.View
import android.widget.EditText
import android.widget.TextView
import org.hamcrest.Description
import org.hamcrest.TypeSafeMatcher

class TextIsNotEmptyMatcher : TypeSafeMatcher<View?>() {
    override fun describeTo(description: Description) {
        description.appendText("The TextView/EditText has value")
    }

    override fun matchesSafely(view: View?): Boolean {
        if (view !is TextView && view !is EditText) {
            return false
        }
        val text: String = if (view is TextView) {
            view.text.toString()
        } else {
            (view as EditText).text.toString()
        }
        return !TextUtils.isEmpty(text)
    }
}

fun hasTextValue(): TypeSafeMatcher<View?> {
    return TextIsNotEmptyMatcher()
}
