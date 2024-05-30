package com.alcorp.kolamin.utils

import android.text.Editable
import com.google.android.material.timepicker.TimeFormat
import java.text.DateFormat
import java.text.SimpleDateFormat

fun setTimeFormat(hour: Int, minute: Int): String {
    val newHour: String = if (hour < 10) {
        "0$hour"
    } else {
        hour.toString()
    }
    val newMinute: String = if (minute < 10) {
        "0$minute"
    } else {
        minute.toString()
    }
    return "$newHour:$newMinute"
}

fun Int.setHourFormat(): String {
    val newHour: String = if (this < 10) {
        "0$this"
    } else {
        this.toString()
    }
    return newHour
}

fun Int.setMinuteFormat(): String {
    val newMinute: String = if (this < 10) {
        "0$this"
    } else {
        this.toString()
    }
    return newMinute
}