package com.collegeapp.models.requests

data class InsertAnnouncementRequest(
    val title: String,
    val message: String,
    val link: String? = null,
    val expiryDateTimeStamp: String
)