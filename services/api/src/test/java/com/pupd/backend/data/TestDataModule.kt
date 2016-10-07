package com.pupd.backend.data

import com.fasterxml.jackson.databind.ObjectMapper
import com.pupd.backend.data.commands.CommandBus
import com.pupd.backend.data.commands.DefaultCommandBus
import com.pupd.backend.data.commands.TestExerciseCommandsModule
import com.pupd.backend.data.queries.TestExerciseQueriesModule
import com.pupd.backend.data.queries.TestWorkoutQueriesModule
import javax.inject.Singleton

/**
 * Test Guice DI module for data for testing resources
 *
 * Created by maxiaojun on 9/12/16.
 */
class TestDataModule(mapper: ObjectMapper) : DataModule(mapper) {
    override fun configure() {
        install(TestExerciseCommandsModule())
        install(TestExerciseQueriesModule())
        install(TestWorkoutQueriesModule())

        bind(CommandBus::class.java).to(DefaultCommandBus::class.java).`in`(Singleton::class.java)
    }
}
