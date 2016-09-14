package com.pupd.backend.data.postgres

import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import javax.inject.Inject
import javax.inject.Named
import javax.inject.Provider
import javax.sql.DataSource

/**
 * Guice DI provider for SQL DataSource. Uses HikariCP
 * as connection pool
 *
 * Created by maxiaojun on 9/1/16.
 */
class PostgresDataSourceProvider @Inject constructor(
        private @Named(DB_USER_INJECTION) val username: String,
        private @Named(DB_PW_INJECTION) val password: String) : Provider<DataSource> {

    companion object {
        const val DB_USER_INJECTION = "DbUser"
        const val DB_PW_INJECTION = "DbPw"
    }

    override fun get(): DataSource {
        val config = HikariConfig()
        config.dataSourceClassName = "org.postgresql.ds.PGSimpleDataSource"
        config.dataSourceProperties.put("databaseName", "pupddb")
        config.username = username
        config.password = password
        // TODO: adjust config settings on DB pool
        return HikariDataSource(config)
    }
}