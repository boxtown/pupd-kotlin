package com.pupd.backend.data.h2

import org.h2.jdbcx.JdbcDataSource
import javax.sql.DataSource

/**
 * Returns an H2 in-memory data source
 *
 * @return H2 in-memory DataSource
 */
fun getDataSource(): DataSource {
    val ds = JdbcDataSource()
    ds.setURL("jdbc:h2:mem:test;DB_CLOSE_DELAY=-1;INIT=CREATE SCHEMA IF NOT EXISTS \"public\"")
    return ds
}
