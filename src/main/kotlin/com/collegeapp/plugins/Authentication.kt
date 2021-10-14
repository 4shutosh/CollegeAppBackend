package com.collegeapp.plugins

import com.collegeapp.auth.JwtService
import com.collegeapp.auth.getJwtData
import com.collegeapp.utils.Constants.JWT_SUBJECT
import io.ktor.application.*
import io.ktor.auth.*
import io.ktor.auth.jwt.*

fun Application.configureAuthentication() {

    val jwtData = getJwtData()

    install(Authentication) {
        jwt(JWT_SUBJECT) {
            JwtService(jwtData).configureKtorFeature(this)
        }
    }
}