package com.collegeapp.models.local

/*
* Here the expiry time: the significant figure should be the date, the service checks once per 24 hours: ?! Fixed time ?!, so any
* hours, minutes, and seconds would have no significance */

data class CollegeAnnouncements(
    val announcementId: String,
    val title: String,
    val description: String,
    val link: String? = null,
    val timeStampCreation: Long,
    val timeStampExpiry: Long = 0
)