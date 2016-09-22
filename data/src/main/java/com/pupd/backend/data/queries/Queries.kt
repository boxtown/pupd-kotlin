package com.pupd.backend.data.queries

/**
 * Interface for query handlers
 *
 * Created by maxiaojun on 9/2/16.
 */
interface QueryHandler<in TIn, out TOut> {
    fun handle(query: TIn): TOut
}