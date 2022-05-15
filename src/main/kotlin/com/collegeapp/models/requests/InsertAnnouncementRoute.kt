package com.collegeapp.models.requests

data class InsertAnnouncementRoute(
    val title: String,
    val message: String,
    val expiryDateTimeStamp: String
)