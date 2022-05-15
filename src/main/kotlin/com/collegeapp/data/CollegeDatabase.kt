package com.collegeapp.data

import com.collegeapp.auth.JwtService
import com.collegeapp.models.ServerResponse
import com.collegeapp.models.local.*
import com.collegeapp.models.requests.InsertAnnouncementRoute
import com.collegeapp.models.requests.InsertCourseRequest
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
import java.util.concurrent.TimeUnit

class CollegeDatabase {

    private val client = KMongo.createClient(
        ConnectionString(System.getenv("MONGO_URL"))
    ).coroutine
    private val database = client.getDatabase(MONGO_DB_NAME)

    private val userCollection = database.getCollection<CollegeUser>()
    private val libraryCollection = database.getCollection<UserLibraryData>()
    private val booksCollection = database.getCollection<CollegeBook>()
    private val coursesCollection = database.getCollection<CollegeCourse>()
    private val announcementsCollection = database.getCollection<CollegeAnnouncements>()

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
        val user = userCollection.findOne(CollegeUser::userId eq userId)
        if (user == null) {
            return ServerResponse(null, "Invalid User", HttpStatusCode.Unauthorized.value)
        } else {
            var bookToInsert = booksCollection.findOne(CollegeBook::libraryBookNumber eq libraryBookNumber)
            if (bookToInsert == null)
                return ServerResponse(null, "Invalid Book Library Number", HttpStatusCode.NoContent.value)
            else {

                if (!bookToInsert.isAvailableToIssue) {
                    if (userId == bookToInsert.ownerData?.userId) {
                        return ServerResponse(
                            data = userLibrary,
                            message = "You've Already Issued this Book",
                            status = HttpStatusCode.OK.value
                        )
                    }

                    val ownerUser = userCollection.findOne(CollegeUser::userId eq userId)
                    return if (ownerUser != null) {
                        ServerResponse(
                            data = Pair(ownerUser.email, ownerUser.name),
                            message = "Book Unavailable to Issue",
                            status = HttpStatusCode.NoContent.value
                        )
                    } else {
                        ServerResponse(
                            data = null,
                            message = "Something Wrong Happened 420",
                            status = HttpStatusCode.NotAcceptable.value
                        )
                    }
                }


                // this is GMT
                val currentTimeStamp = System.currentTimeMillis()
                val returnTimeStamp = currentTimeStamp + TimeUnit.DAYS.toMillis(bookToInsert.maximumDaysAllowed)

                // updating the bookToInsert in repository as not available to issue
                bookToInsert =
                    bookToInsert.copy(
                        isAvailableToIssue = false, ownerData = CollegeBookOwnerData(
                            userId = user.userId,
                            email = user.email,
                            returnTimeStamp = returnTimeStamp

                        )
                    )
                booksCollection.updateOne(CollegeBook::bookId eq bookToInsert.bookId, bookToInsert)

                val userBookDataToInsert = UserBookData(bookToInsert, System.currentTimeMillis(), returnTimeStamp)

                var responseMessage = "User Library Updated"

                if (userLibrary == null) {
                    val booksListToInsert = listOf(userBookDataToInsert)
                    libraryCollection.insertOne(UserLibraryData(userId, booksListToInsert))

                    responseMessage = "User Library Created"

                } else {
                    val userBooksMutable = userLibrary.userBookDataList.toMutableList()
                    if (userBooksMutable.find { it.book.libraryBookNumber == libraryBookNumber } == null) {
                        userBooksMutable.add(userBookDataToInsert)
                        libraryCollection.updateOne(
                            UserLibraryData::id eq userId,
                            setValue(UserLibraryData::userBookDataList, userBooksMutable)
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
    }

    suspend fun getBookFromBookLibraryNumber(libraryBookNumber: Long): ServerResponse<Any> {
        val book = booksCollection.findOne(CollegeBook::libraryBookNumber eq libraryBookNumber)
        return ServerResponse(
            data = book ?: "",
            message = if (book != null) {
                "Book found"
            } else "No Book Found!",
            status = if (book != null) {
                HttpStatusCode.OK.value
            } else HttpStatusCode.NoContent.value
        )
    }


    suspend fun getAllUserBooks(userID: String): ServerResponse<Any> {
        var userLibraryData = libraryCollection.findOneById(
            userID
        )
        if (userLibraryData == null) {
            libraryCollection.insertOne(UserLibraryData(userID, emptyList()))
        }

        userLibraryData = libraryCollection.findOneById(
            userID
        )
        return ServerResponse(
            data = userLibraryData,
            message = if (userLibraryData?.userBookDataList != null && userLibraryData.userBookDataList.isNotEmpty()) "Books Found" else "No Books Found",
            status = HttpStatusCode.OK.value
        )
    }

    suspend fun insertOrUpdateBook(
        bookName: String, libraryBookNumber: Long, maximumDaysAllowed: Long
    ): ServerResponse<String> {

        val book = booksCollection.findOne(CollegeBook::libraryBookNumber eq libraryBookNumber)
        // todo check valid maximumDaysAllowed here
        if (book != null) {
            val updatedBook = book.copy(
                libraryBookNumber = libraryBookNumber,
                bookName = bookName,
                maximumDaysAllowed = maximumDaysAllowed
            )
            booksCollection.updateOne(CollegeBook::libraryBookNumber eq libraryBookNumber, updatedBook)
            return ServerResponse(
                data = updatedBook.bookId,
                message = "Book Updated ",
                status = HttpStatusCode.OK.value
            )
        } else {
            // always unique
            val newBookId = ObjectId().toString()

            booksCollection.insertOne(
                CollegeBook(
                    bookId = newBookId,
                    libraryBookNumber = libraryBookNumber,
                    bookName = bookName,
                    maximumDaysAllowed = maximumDaysAllowed,
                    isAvailableToIssue = true
                )
            )
            return ServerResponse(
                data = newBookId,
                message = "Book Insert Success",
                status = HttpStatusCode.OK.value
            )
        }
    }

    suspend fun getAllBooks(): ServerResponse<List<CollegeBook>> {
        val allBooks = booksCollection.find().toList()
        return ServerResponse(
            data = allBooks,
            message = if (allBooks.isNotEmpty()) "Books Found ${allBooks.size}" else "No Books Found",
            status = HttpStatusCode.OK.value
        )
    }

    /**
     * adds penalty for each of the user book in [UserBookData::penalty] as well as the totalPenalty field in the [UserLibraryData::totalPenalty]
     */
    suspend fun addPenaltyToUserLibraryForBook(userId: String, bookId: String) {
        val userLibrary = libraryCollection.findOne(UserLibraryData::id eq userId)
        if (userLibrary != null) {
            val userBooks = userLibrary.userBookDataList.toMutableList()
            val userBookToUpdate = userBooks.find { it.book.bookId == bookId }
            val userBookToUpdateIndex = userBooks.indexOf(userBookToUpdate)
            if (userBookToUpdate != null) {
                userBookToUpdate.penalty += 1
                userBooks[userBookToUpdateIndex] = userBookToUpdate
                libraryCollection.updateOneById(
                    userId, setValue(UserLibraryData::userBookDataList, userBooks)
                )

                val updatedTotalPenalty = userLibrary.totalPenalty + 1
                libraryCollection.updateOne(
                    UserLibraryData::id eq userId,
                    setValue(UserLibraryData::totalPenalty, updatedTotalPenalty)
                )
            }
        }

    }

    suspend fun returnBook(userEmail: String, libraryBookNumber: Long): ServerResponse<Any> {
        val user = userCollection.findOne(CollegeUser::email eq userEmail)
        if (user != null) {
            val userLibrary = libraryCollection.findOne(UserLibraryData::id eq user.userId)
            val book = booksCollection.findOne(CollegeBook::libraryBookNumber eq libraryBookNumber)

            if (book != null) {
                if (userLibrary != null) {
                    if (!book.isAvailableToIssue) {
                        val userBooks = userLibrary.userBookDataList.toMutableList()
                        val bookToDelete = userBooks.find { it.book.libraryBookNumber == libraryBookNumber }

                        userBooks.remove(bookToDelete)

                        libraryCollection.updateOne(
                            UserLibraryData::id eq user.userId,
                            setValue(UserLibraryData::userBookDataList, userBooks)
                        )

                        booksCollection.updateOne(
                            CollegeBook::libraryBookNumber eq libraryBookNumber,
                            book.copy(isAvailableToIssue = true, ownerData = null)
                        )

                        return ServerResponse(
                            data = null,
                            message = "Book Return Success!",
                            status = HttpStatusCode.OK.value
                        )
                    } else {
                        return ServerResponse(
                            data = null,
                            message = "Book not issued! Check libraryBookNumber again",
                            status = HttpStatusCode.OK.value
                        )

                    }
                } else {
                    return ServerResponse(
                        data = null,
                        message = "User Library Not Found!",
                        status = HttpStatusCode.OK.value
                    )
                }
            } else {
                return ServerResponse(
                    data = null,
                    message = "No Book Found for this number $libraryBookNumber!",
                    status = HttpStatusCode.OK.value
                )
            }
        }
        return ServerResponse(
            data = null,
            message = "No User Found of this email $userEmail!",
            status = HttpStatusCode.OK.value
        )

    }

    suspend fun updateUserLibraryTotalPenalty(userEmail: String, penalty: Int): ServerResponse<Any> {
        val user = userCollection.findOne(CollegeUser::email eq userEmail)
            ?: return ServerResponse(
                data = null,
                message = "No user Found!",
                status = HttpStatusCode.OK.value
            )

        if (libraryCollection.findOne(UserLibraryData::id eq user.userId) == null)
            return ServerResponse(
                data = null,
                message = "No user library Found! No books issued yet?",
                status = HttpStatusCode.OK.value
            )


        libraryCollection.updateOne(
            UserLibraryData::id eq user.userId,
            setValue(UserLibraryData::totalPenalty, penalty)
        )

        val updatedLibrary = libraryCollection.findOne(UserLibraryData::id eq user.userId)

        return ServerResponse(
            data = updatedLibrary,
            message = "Penalty Updated!",
            status = HttpStatusCode.OK.value
        )
    }

    suspend fun getAllCourses(): ServerResponse<List<CollegeCourse>> {
        val courses = coursesCollection.find().toList()

        return ServerResponse(
            data = courses,
            message = if (courses.isNotEmpty()) "Courses Found" else "No Courses Found",
            status = HttpStatusCode.OK.value
        )
    }

    suspend fun insertOrUpdateCourse(insertCourseRequest: InsertCourseRequest): ServerResponse<Any> {
        val course = coursesCollection.findOne(CollegeCourse::code eq insertCourseRequest.courseCode)
        if (course != null) {
            val updatedCourse = course.copy(
                name = insertCourseRequest.courseName,
                description = insertCourseRequest.courseDescription,
                facultyName = insertCourseRequest.courseFacultyName
            )
            if (course != updatedCourse)
                coursesCollection.updateOne(
                    CollegeCourse::id eq course.id,
                    updatedCourse
                )
            return ServerResponse(
                data = updatedCourse,
                "Course Updated",
                HttpStatusCode.OK.value
            )
        } else {
            val newCourseId = ObjectId().toString()

            val courseToInsert = CollegeCourse(
                id = newCourseId,
                name = insertCourseRequest.courseName,
                code = insertCourseRequest.courseCode,
                description = insertCourseRequest.courseDescription,
                facultyName = insertCourseRequest.courseFacultyName
            )

            coursesCollection.insertOne(courseToInsert)

            return ServerResponse(
                data = courseToInsert,
                message = "Course Added",
                HttpStatusCode.OK.value
            )
        }
    }

    suspend fun getAllAnnouncements(): ServerResponse<Any> {
        val announcements = announcementsCollection.find().toList()

        return ServerResponse(
            data = announcements,
            message = if (announcements.isNotEmpty()) "Announcements Found" else "No Announcements Found",
            status = HttpStatusCode.OK.value
        )
    }

    suspend fun insertAnnouncement(insertAnnouncementRoute: InsertAnnouncementRoute): ServerResponse<String> {

        val newAnnouncementId = ObjectId().toString()

        announcementsCollection.insertOne(
            CollegeAnnouncements(
                id = newAnnouncementId,
                title = insertAnnouncementRoute.title,
                message = insertAnnouncementRoute.message,
            )
        )

        return ServerResponse(
            data = newAnnouncementId,
            message = "Announcement Insert Success",
            status = HttpStatusCode.OK.value
        )

    }

    suspend fun deleteAnnouncement(announcementId: String): ServerResponse<Any> {

        // can use wasAcknowledged() here
        val announcementToDelete = announcementsCollection.findOne(CollegeAnnouncements::id eq announcementId)
        return if (announcementToDelete != null) {
            announcementsCollection.deleteOne(CollegeAnnouncements::id eq announcementId)

            ServerResponse(
                data = "",
                message = "Announcement Delete Success",
                status = HttpStatusCode.OK.value
            )
        } else {
            ServerResponse(
                data = "",
                message = "Something went wrong",
                status = HttpStatusCode.BadRequest.value
            )
        }
    }
}