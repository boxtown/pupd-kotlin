package com.pupd.backend.data.queries

import com.pupd.backend.data.DataException

/**
 * Interface for query handlers
 *
 * Created by maxiaojun on 9/2/16.
 */
interface QueryHandler<in TIn, out TOut> {
    fun handle(query: TIn): TOut
}

/**
 * Base class for command handlers that wraps execution exceptions
 * into data exceptions
 *
 * Created by maxiaojun on 9/7/2016.
 */
open class BaseQueryHandler<TIn, TOut> {
    fun handle(query: TIn, handler: (TIn) -> TOut): TOut {
        try {
            return handler(query)
        } catch (e: Exception) {
            when (e) {
                is DataException -> throw e
                else -> throw DataException(e.message, e)
            }
        }
    }
}