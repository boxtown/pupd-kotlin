package com.pupd.backend.data

import org.jooq.DSLContext
import org.jooq.SQLDialect
import org.jooq.exception.DataAccessException
import org.jooq.impl.DSL
import javax.inject.Inject
import javax.sql.DataSource

/**
 * Default PostgreSQL database implementation. Is stateless so is thread-safe.
 *
 * Created by maxiaojun on 9/1/16.
 */
class DefaultDatabase @Inject constructor(
        private val ds: DataSource,
        private val dialect: SQLDialect) : Database {

    override fun exec(fn: (DSLContext) -> Unit) {
        val conn = ds.connection
        try {
            fn(DSL.using(conn, dialect))
        } catch (e: DataAccessException) {
            // throw custom exception for PG error code 23505 unique constraint violation
            // per https://www.postgresql.org/docs/9.3/static/errcodes-appendix.html
            when (e.sqlState()) {
                "23505" -> throw UniquenessConstraintViolationException(e)
                else -> throw e
            }
        } finally {
            conn.close()
        }
    }

    override fun <T> query(fn: (DSLContext) -> T): T {
        val conn = ds.connection
        try {
            return fn(DSL.using(conn, dialect))
        } catch(e: DataAccessException) {
            // throw custom exception for PG error code 23505 unique constraint violation
            // per https://www.postgresql.org/docs/9.3/static/errcodes-appendix.html
            when (e.sqlState()) {
                "23505" -> throw UniquenessConstraintViolationException(e)
                else -> throw e
            }
        } finally {
            conn.close()
        }
    }
}