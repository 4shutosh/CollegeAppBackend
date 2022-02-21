package com.collegeapp.data

import com.collegeapp.auth.JwtService
import com.collegeapp.models.responses.CollegeUser
import com.collegeapp.models.local.UserBookData
import com.collegeapp.models.local.UserLibraryData
import com.collegeapp.utils.CollegeLogger
import com.collegeapp.utils.Constants.MONGO_DB_NAME
import com.collegeapp.utils.generateUserUid
import com.mongodb.ConnectionString
import org.litote.kmongo.coroutine.coroutine
import org.litote.kmongo.eq
import org.litote.kmongo.reactivestreams.KMongo

class CollegeDatabase {

    // todo set env variable here
    private val client = KMongo.createClient(
        ConnectionString("mongodb://127.0.0.1:27017")
    ).coroutine
    private val database = client.getDatabase(MONGO_DB_NAME)

    private val userCollection = database.getCollection<CollegeUser>()
    private val userBooksCollection = database.getCollection<UserLibraryData>()

    suspend fun checkForUser(
        jwtToken: JwtService.JwtData,
        userEmail: String,
        userName: String,
        userImageUrl: String
    ): CollegeUser {
        val foundUser = userCollection.findOne(CollegeUser::email eq userEmail)

        if (foundUser != null) {
            if (foundUser.imageUrl == userImageUrl ||
                foundUser.name == userName
            ) {
                return foundUser
            } else {
                val updatedUser = CollegeUser(
                    accessToken = foundUser.accessToken,
                    name = userName,
                    email = foundUser.email,
                    imageUrl = userImageUrl,
                    userId = foundUser.userId
                )

                userCollection.updateOne(
                    CollegeUser::userId eq foundUser.userId,
                    updatedUser
                )

                return updatedUser
            }
        } else {
            val newUserId = generateUserUid()

            val accessToken = JwtService(jwtToken).generateToken(newUserId, userEmail)

            CollegeLogger.info("New user created with userId : $newUserId")

            val createUser = CollegeUser(
                userId = newUserId,
                name = userName,
                email = userEmail,
                imageUrl = userImageUrl,
                accessToken = accessToken
            )

            userCollection.insertOne(createUser).wasAcknowledged()

            CollegeLogger.info("New user inserted into the db : $createUser")

            return createUser
        }
    }

    suspend fun checkForBookAndIssue(){

    }


    suspend fun getAllUserBooks(userID: String) : List<UserBookData>? {
        val userLibraryData = userBooksCollection.findOneById(
            userID
        )
        return userLibraryData?.books
    }


}