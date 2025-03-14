package com.sample.walmart_countries.exception

// CustomExceptions.kt

sealed class CustomException(message: String? = null, cause: Throwable? = null) :
    Exception(message, cause) {

    class ApiException(message: String? = null, cause: Throwable? = null) :
        CustomException(message, cause)

    class NetworkException(message: String? = null, cause: Throwable? = null) :
        CustomException(message, cause)

    class DataParsingException(message: String? = null, cause: Throwable? = null) :
        CustomException(message, cause)

    class UnknownException(message: String? = null, cause: Throwable? = null) :
        CustomException(message, cause)
}
