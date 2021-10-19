package com.collegeapp.utils

import org.slf4j.LoggerFactory

object CollegeLogger {

    private val logger = LoggerFactory.getLogger(this::class.java)

    fun createLogger(type: Class<*>) {
        val newLogger = LoggerFactory.getLogger(type)
    }

    fun info(message: String) {
        logger.info(message)
    }

    fun debug(message: String) {
        logger.debug(message)
    }

    fun error(error: String) {
        logger.error(error)
    }

    fun warn(warning: String) {
        logger.warn(warning)
    }
}