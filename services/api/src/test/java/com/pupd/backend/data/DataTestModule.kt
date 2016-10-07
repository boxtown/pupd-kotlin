package com.pupd.backend.data

import com.fasterxml.jackson.databind.ObjectMapper
import com.pupd.backend.data.commands.CommandBus
import com.pupd.backend.data.commands.DefaultCommandBus
import com.pupd.backend.data.commands.ExerciseCommandsTestModule
import com.pupd.backend.data.queries.ExerciseQueriesTestModule
import com.pupd.backend.data.queries.WorkoutQueriesTestModule
import javax.inject.Singleton

/**
 * Test Guice DI module for data for testing resources
 *
 * Created by maxiaojun on 9/12/16.
 */
class DataTestModule(mapper: ObjectMapper) : DataModule(mapper) {
    override fun configure() {
        install(ExerciseCommandsTestModule())
        install(ExerciseQueriesTestModule())
        install(WorkoutQueriesTestModule())

        bind(CommandBus::class.java).to(DefaultCommandBus::class.java).`in`(Singleton::class.java)
    }
}
