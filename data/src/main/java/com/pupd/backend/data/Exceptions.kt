package com.pupd.backend.data

/**
 * Exception class for exceptions occuring in the data layer
 *
 * Created by maxiaojun on 9/1/16.
 */
open class DataException(
        message: String? = null,
        cause: Throwable? = null) : Exception(message, cause)

/**
 * Exception class for uniqueness constraint violations in the data layer
 *
 * Created by maxiaojun on 9/1/16.
 */
class UniquenessConstraintViolationException(cause: Throwable? = null) :
        DataException("Uniqueness constraint violated", cause)