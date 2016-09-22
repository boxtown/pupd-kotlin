package com.pupd.backend.data

import com.fasterxml.jackson.databind.ObjectMapper
import com.google.inject.AbstractModule
import com.google.inject.Provides
import com.pupd.backend.data.commands.CommandsModule
import com.pupd.backend.data.postgres.PostgresDataSourceProvider
import com.pupd.backend.data.queries.QueriesModule
import org.jooq.SQLDialect
import javax.inject.Singleton
import javax.sql.DataSource

/**
 * Guice DI module for data dependencies
 *
 * Created by maxiaojun on 9/1/16.
 */
class DataModule() : AbstractModule() {
    override fun configure() {
        install(CommandsModule())
        install(QueriesModule())

        bind(SQLDialect::class.java).toInstance(SQLDialect.POSTGRES)
        bind(DataSource::class.java).toProvider(PostgresDataSourceProvider::class.java).`in`(Singleton::class.java)
        bind(Database::class.java).to(DefaultDatabase::class.java).`in`(Singleton::class.java)
    }

    @Singleton
    @Provides
    fun objectMapper(): ObjectMapper {
        return ObjectMapper()
    }
}