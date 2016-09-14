package com.pupd.backend.data

import com.google.inject.AbstractModule
import com.pupd.backend.data.commands.CommandsModule
import com.pupd.backend.data.h2.H2DataSourceProvider
import com.pupd.backend.data.queries.QueriesModule
import org.jooq.SQLDialect
import javax.inject.Singleton
import javax.sql.DataSource

/**
 * Test Guice DI module for data for testing resources
 *
 * Created by maxiaojun on 9/12/16.
 */
class TestDataModule : AbstractModule() {
    override fun configure() {
        install(CommandsModule())
        install(QueriesModule())

        bind(SQLDialect::class.java).toInstance(SQLDialect.H2)
        bind(DataSource::class.java).toProvider(H2DataSourceProvider::class.java).`in`(Singleton::class.java)
        bind(Database::class.java).to(DefaultDatabase::class.java).`in`(Singleton::class.java)
    }
}
