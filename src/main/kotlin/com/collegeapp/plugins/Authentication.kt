package com.collegeapp.plugins

import com.collegeapp.auth.JwtService
import com.collegeapp.auth.getJwtData
import com.collegeapp.utils.Constants.JWT_SUBJECT
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*

fun Application.configureAuthentication() {

    val jwtData = getJwtData()

    install(Authentication) {
        jwt(JWT_SUBJECT) {
            JwtService(jwtData).configureKtorFeature(this)
        }
    }
}