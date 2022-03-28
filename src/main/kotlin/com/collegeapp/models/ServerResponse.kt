package com.collegeapp.models

data class ServerResponse<T>(
    var data: T? = null,
    var message: String = "",
    var status: Int
)

data class ErrorResponse(
    var message: String = "",
    var status: Int
)