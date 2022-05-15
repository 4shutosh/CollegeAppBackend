package com.collegeapp.models.local

/*
* Here the expiry time: the significant figure should be the date, the service checks once per 24 hours: ?! Fixed time ?!, so any
* hours, minutes, and seconds would have no significance */

data class CollegeAnnouncements(
    val id: Long,
    val title: String,
    val message: String,
    val expiryDayTimeStamp: Long = 0
)