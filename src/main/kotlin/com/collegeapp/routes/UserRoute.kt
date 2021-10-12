package com.collegeapp.routes

import com.collegeapp.models.User
import com.collegeapp.utils.Constants.EMAIL
import com.collegeapp.utils.Constants.IMAGE_URL
import com.collegeapp.utils.Constants.MONGO_DB_NAME
import com.collegeapp.utils.Constants.NAME
import com.collegeapp.utils.Constants.ROUTE_USER
import com.collegeapp.utils.Constants.USER_ID
import io.ktor.application.*
import io.ktor.http.*
import io.ktor.response.*
import io.ktor.routing.*
import org.litote.kmongo.coroutine.coroutine
import org.litote.kmongo.eq
import org.litote.kmongo.reactivestreams.KMongo

object UserRoute {
    private val client = KMongo.createClient().coroutine
    private val database = client.getDatabase(MONGO_DB_NAME)
    private val userCollection = database.getCollection<User>()

    fun Route.loginOrCreateUser() {
        get("/$ROUTE_USER") {
            val userId = call.parameters[USER_ID]?.toLong()
            val userEmail = call.parameters[EMAIL].toString()
            val userName = call.parameters[NAME].toString()
            val userImageUrl = call.parameters[IMAGE_URL].toString()

            if (userId == 69L) {
                call.respondText(
                    status = HttpStatusCode.OK,
                    text = "Welcome GOD $userName"
                )
                return@get
            }

            // todo check if imageUrl is necessary
            if (userId == null || userEmail.isEmpty() || userName.isEmpty() || userImageUrl.isEmpty()) {
                call.respond(HttpStatusCode.BadRequest)
                return@get
            }

            userId?.let { id ->
                val foundUser = userCollection.findOne(User::userId eq id) // todo user constants here

                if (foundUser != null) {
                    call.respond(
                        HttpStatusCode.OK,
                        foundUser
                    )
                } else {

                    val createUser = User(
                        userId = userId,
                        name = userName,
                        email = userEmail,
                        imageUrl = userImageUrl
                    )

                    userCollection.insertOne(createUser)

                    call.respond(
                        HttpStatusCode.OK,
                        createUser
                    )
                }
            }
        }
    }

}