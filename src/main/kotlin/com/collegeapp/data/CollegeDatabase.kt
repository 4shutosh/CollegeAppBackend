package com.collegeapp.data

import com.collegeapp.auth.JwtService
import com.collegeapp.models.ServerResponse
import com.collegeapp.models.local.CollegeBook
import com.collegeapp.models.local.UserBookData
import com.collegeapp.models.local.UserLibraryData
import com.collegeapp.models.responses.CollegeUser
import com.collegeapp.utils.CollegeLogger
import com.collegeapp.utils.Constants.MONGO_DB_NAME
import com.collegeapp.utils.generateUserUid
import com.mongodb.ConnectionString
import io.ktor.http.*
import org.bson.types.ObjectId
import org.litote.kmongo.coroutine.coroutine
import org.litote.kmongo.eq
import org.litote.kmongo.reactivestreams.KMongo
import org.litote.kmongo.setValue

class CollegeDatabase {

    // todo set env variable here
    private val client = KMongo.createClient(
        ConnectionString("mongodb://127.0.0.1:27017")
    ).coroutine
    private val database = client.getDatabase(MONGO_DB_NAME)

    private val userCollection = database.getCollection<CollegeUser>()
    private val libraryCollection = database.getCollection<UserLibraryData>()
    private val booksCollection = database.getCollection<CollegeBook>()

    suspend fun checkForUser(
        jwtToken: JwtService.JwtData, userEmail: String, userName: String, userImageUrl: String
    ): CollegeUser {
        val foundUser = userCollection.findOne(CollegeUser::email eq userEmail)

        if (foundUser != null) {
            if (foundUser.imageUrl == userImageUrl || foundUser.name == userName) {
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
                    CollegeUser::userId eq foundUser.userId, updatedUser
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

    suspend fun checkForBookAndIssue(userId: String, libraryBookNumber: Long): ServerResponse<Any> {
        val userLibrary = libraryCollection.findOne(UserLibraryData::id eq userId)

        val bookToInsert = booksCollection.findOne(CollegeBook::libraryBookNumber eq libraryBookNumber)
        if (bookToInsert == null)
            return ServerResponse(null, "Invalid Book Library Number", HttpStatusCode.OK.value)
        else {
            val userBookDataToInsert = UserBookData(bookToInsert, 0, 0)

            var responseMessage = "User Library Updated"

            if (userLibrary == null) {
                val booksListToInsert = listOf(userBookDataToInsert)
                libraryCollection.insertOne(UserLibraryData(userId, booksListToInsert))

                responseMessage = "User Library Created"

            } else {
                val userBooksMutable = userLibrary.books.toMutableList()
                if (userBooksMutable.find { it.book.libraryBookNumber == libraryBookNumber } == null) {
                    userBooksMutable.add(userBookDataToInsert)
                    libraryCollection.updateOne(
                        UserLibraryData::id eq userId,
                        setValue(UserLibraryData::books, userBooksMutable)
                    )
                } else responseMessage = "Book Already In the User Library"
            }
            return ServerResponse(
                libraryCollection.findOne(UserLibraryData::id eq userId),
                responseMessage,
                HttpStatusCode.OK.value
            )
        }
    }

    suspend fun getBookFromBookLibraryNumber(libraryBookNumber: Long): ServerResponse<CollegeBook?> {
        val book = booksCollection.findOne(CollegeBook::libraryBookNumber eq libraryBookNumber)
        return ServerResponse(
            data = book,
            message = if (book != null) {
                "Book found"
            } else "No Book Found!",
            status = HttpStatusCode.OK.value
        )
    }


    suspend fun getAllUserBooks(userID: String): List<UserBookData>? {
        val userLibraryData = libraryCollection.findOneById(
            userID
        )
        return userLibraryData?.books
    }

    suspend fun insertBook(
        bookName: String, libraryBookNumber: Long, maximumDaysAllowed: Int
    ): String {

        val book = booksCollection.findOne(CollegeBook::libraryBookNumber eq libraryBookNumber)
        if (book != null) {
            val updatedBook = CollegeBook(
                bookId = book.bookId,
                libraryBookNumber,
                bookName = bookName,
                maximumDaysAllowed = maximumDaysAllowed
            )
            booksCollection.updateOne(CollegeBook::libraryBookNumber eq libraryBookNumber, updatedBook)
            return updatedBook.bookId
        } else {
            val newBookId = ObjectId().toString()

            booksCollection.insertOne(
                CollegeBook(
                    bookId = newBookId,
                    libraryBookNumber = libraryBookNumber,
                    bookName = bookName,
                    maximumDaysAllowed = maximumDaysAllowed
                )
            )
            return newBookId
        }
    }

}