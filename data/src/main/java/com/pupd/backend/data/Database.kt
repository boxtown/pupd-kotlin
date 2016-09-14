package com.pupd.backend.data

import org.jooq.DSLContext

/**
 * Interface for a data context
 *
 * Created by maxiaojun on 9/1/16.
 */
interface Database {
    fun exec(fn: (DSLContext) -> Unit)
    fun <T> query(fn: (DSLContext) -> T): T
}