package com.pupd.backend.data

import com.fasterxml.jackson.databind.ObjectMapper
import com.google.inject.Provides
import com.pupd.backend.data.commands.CommandsModule
import com.pupd.backend.data.entities.Workout
import com.pupd.backend.data.h2.H2DataSourceProvider
import com.pupd.backend.data.queries.GetWorkout
import com.pupd.backend.data.queries.GetWorkoutHandler
import com.pupd.backend.data.queries.QueryHandler
import com.pupd.backend.data.queries.TestExerciseQueriesModule
import org.jooq.SQLDialect
import javax.inject.Inject
import javax.inject.Singleton
import javax.sql.DataSource

/**
 * Test Guice DI module for data for testing resources
 *
 * Created by maxiaojun on 9/12/16.
 */
class TestDataModule(mapper: ObjectMapper) : DataModule(mapper) {
    override fun configure() {
        install(CommandsModule())
        install(TestExerciseQueriesModule())

        bind(SQLDialect::class.java).toInstance(SQLDialect.H2)
        bind(DataSource::class.java).toProvider(H2DataSourceProvider::class.java).`in`(Singleton::class.java)
        bind(Database::class.java).to(DefaultDatabase::class.java).`in`(Singleton::class.java)
    }

    // temporary
    @Singleton
    @Provides
    @Inject
    fun getWorkoutHandler(db: Database, mapper: ObjectMapper): QueryHandler<GetWorkout, Workout?>
            = GetWorkoutHandler(db, mapper)
}
