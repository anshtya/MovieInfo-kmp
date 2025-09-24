package com.anshtya.movieinfo.common.data.network

class HttpException(
    override val message: String,
    override val cause: Throwable? = null
): Exception(message, cause)