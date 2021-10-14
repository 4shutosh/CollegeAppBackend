package com.collegeapp.auth

import com.auth0.jwt.JWT
import com.auth0.jwt.JWTVerifier
import com.auth0.jwt.algorithms.Algorithm
import com.collegeapp.utils.Constants.JWT_SUBJECT
import com.collegeapp.utils.Constants.USER_EMAIL
import com.collegeapp.utils.Constants.USER_ID
import io.ktor.application.*
import io.ktor.auth.*
import io.ktor.auth.jwt.*
import java.util.*

class JwtService constructor(
    private val jwtData: JwtData
) {

    private val algorithm = Algorithm.HMAC256(jwtData.secret)

    private
    val jwtVerifier: JWTVerifier = JWT.require(algorithm).withIssuer(jwtData.jwtIssuer).build()

    fun generateToken(userId: String, userEmail: String): String =
        JWT.create()
            .withSubject(JWT_SUBJECT)
            .withIssuer(jwtData.jwtIssuer)
            .withClaim(USER_ID, userId) // can add another claim of userEmail
            .withClaim(USER_EMAIL, userEmail)
            .sign(algorithm)


    private fun expiresAt() =
        Date(System.currentTimeMillis() + 3_600_000 * 24) // todo create expiry here .withExpires()


    fun configureKtorFeature(config: JWTAuthenticationProvider.Configuration) = with(config) {
        verifier(jwtVerifier)
        realm = jwtData.jwtRealm

        validate {
            val userId = it.payload.getClaim(USER_ID).asInt()
            val userEmail = it.payload.getClaim(USER_EMAIL).asString()

            if (userId != null && userEmail != null) {
                JwtUser(userId, userEmail)
            } else {
                null
            }
        }
    }

    data class JwtUser(val userId: Int, val userEmail: String) : Principal

    data class JwtData(val secret: String, val jwtIssuer: String, val jwtRealm: String)
}

fun Application.getJwtData(): JwtService.JwtData {

    // todo throw error if something is wrong here

    val jwtConfig = environment.config.propertyOrNull("jwt.secret")?.getString() ?: "ABCDabcd"
    val jwtIssuer = environment.config.propertyOrNull("jwt.issuer")?.getString() ?: "http://0.0.0.0:8080/"
    val jwtRealm = environment.config.propertyOrNull("jwt.realm")?.getString() ?: "Jwt Realm"

    return JwtService.JwtData(
        secret = jwtConfig,
        jwtIssuer = jwtIssuer,
        jwtRealm = jwtRealm
    )
}