package com.collegeapp.di

import com.mongodb.ConnectionString
import org.koin.dsl.module
import org.litote.kmongo.coroutine.coroutine
import org.litote.kmongo.reactivestreams.KMongo


val modules = module {
    single {
        KMongo.createClient(
            ConnectionString("mongodb://127.0.0.1:27017")
        ).coroutine
    }
}