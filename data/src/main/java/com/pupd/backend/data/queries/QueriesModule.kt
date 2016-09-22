package com.pupd.backend.data.queries

import com.fasterxml.jackson.databind.ObjectMapper
import com.google.inject.AbstractModule
import com.google.inject.Provides
import com.pupd.backend.data.Database
import com.pupd.backend.data.entities.Exercise
import com.pupd.backend.data.entities.Workout
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Guice DI module for queries
 *
 * Created by maxiaojun on 9/11/16.
 */
class QueriesModule() : AbstractModule() {
    override fun configure() {
    }

    @Singleton
    @Provides
    @Inject
    fun getExerciseHandler(db: Database): QueryHandler<GetExercise, Exercise?> = GetExerciseHandler(db)

    @Singleton
    @Provides
    @Inject
    fun listExercisesHandler(db: Database): QueryHandler<ListExercises, Iterable<Exercise>> = ListExercisesHandler(db)

    @Singleton
    @Provides
    @Inject
    fun getWorkoutHandler(db: Database, mapper: ObjectMapper): QueryHandler<GetWorkout, Workout?>
            = GetWorkoutHandler(db, mapper)
}
