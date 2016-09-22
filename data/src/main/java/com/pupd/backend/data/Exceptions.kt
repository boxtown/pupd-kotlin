package com.pupd.backend.data

/**
 * Exception class for uniqueness constraint violations in the data layer
 *
 * Created by maxiaojun on 9/1/16.
 */
class UniquenessConstraintViolationException(cause: Throwable? = null) :
        Exception("Uniqueness constraint violated", cause)