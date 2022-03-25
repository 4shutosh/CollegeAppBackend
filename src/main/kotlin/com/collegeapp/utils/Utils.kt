package com.collegeapp.utils

import java.util.*


fun getDateFromTimeStamp(timeInMillis: Long): Int {
    val calendar = Calendar.getInstance()
    calendar.time = Date(timeInMillis)
    return calendar.get(Calendar.DATE)
}