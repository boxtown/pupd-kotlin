package com.pupd.backend.data.h2

import org.h2.jdbcx.JdbcDataSource
import javax.inject.Provider
import javax.sql.DataSource

/**
 * H2 in memory data source provider
 *
 * Created by maxiaojun on 9/12/16.
 */
class H2DataSourceProvider : Provider<DataSource> {
    override fun get(): DataSource {
        val ds = JdbcDataSource()
        ds.setURL("jdbc:h2:mem:test;DB_CLOSE_DELAY=-1;INIT=CREATE SCHEMA IF NOT EXISTS \"public\"")
        return ds
    }
}
