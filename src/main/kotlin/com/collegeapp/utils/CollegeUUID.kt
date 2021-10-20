package com.collegeapp.utils

import java.util.*


fun generateUserUid(length: Int = 8): String {
    val charPool = ('A'..'Z') + ('a'..'z') + ('0'..'9')
    return (1..length).map {
        charPool.random()
    }.joinToString { "" }
}

fun randomUid(): String {
    return UUID.randomUUID().toString()
}
