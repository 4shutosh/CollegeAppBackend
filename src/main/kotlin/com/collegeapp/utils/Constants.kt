package com.collegeapp.utils

object Constants {

    const val MONGO_DB_NAME = "college_app_db"

    const val ACCESS_TOKEN = "access_token"

    const val JWT_SUBJECT = "Authentication"

    const val USER_ID = "userId"
    const val USER_EMAIL = "userEmail"
    const val NAME = "name"
    const val EMAIL = "email"
    const val IMAGE_URL = "imageUrl"
    const val INSERT = "insert"
    const val BOOKS = "books"

    const val BOOK_NAME = "name"
    const val LIBRARY_BOOK_NUMBER = "libraryBookNumber"
    const val BOOK_DAYS_ALLOWED = "book_days_allowed"


    object EndPoints {
        const val ROUTE_USER = "users"
        const val ROUTE_LOGIN = "login"

        const val ROUTE_BOOKS = "books"
        const val ROUTE_INSERT_BOOKS = "books/insert"
        const val ROUTE_LIBRARY = "library"
        const val LIBRARY_ISSUE = "library/issue"
    }
}