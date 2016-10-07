package com.pupd.backend.data.queries

import com.google.inject.AbstractModule
import com.google.inject.Provides
import com.pupd.backend.data.entities.Exercise
import java.util.*
import javax.inject.Singleton

/**
 * Guice DI queries module for testing
 *
 * Created by maxiaojun on 10/6/16.
 */
class TestExerciseQueriesModule : AbstractModule() {
    val ids = arrayOf(
            UUID.fromString("00000000-0000-4000-8000-000000000001"),
            UUID.fromString("00000000-0000-4000-8000-000000000002"),
            UUID.fromString("00000000-0000-4000-8000-000000000003")
    )
    val dataSource = mapOf(
            ids[0] to Exercise(ids[0], "Test Exercise A"),
            ids[1] to Exercise(ids[1], "Test Exercise C"),
            ids[2] to Exercise(ids[2], "Test Exercose B")
    )

    override fun configure() {
    }

    @Singleton
    @Provides
    fun mockGetExerciseHandler(): QueryHandler<GetExercise, Exercise?> =
            object : QueryHandler<GetExercise, Exercise?> {
                override fun handle(query: GetExercise): Exercise? = dataSource[query.id]
            }

    @Singleton
    @Provides
    fun mockGetListExercisesHandler(): QueryHandler<ListExercises, Iterable<Exercise>> =
            object : QueryHandler<ListExercises, Iterable<Exercise>> {
                override fun handle(query: ListExercises): Iterable<Exercise> {
                    var it = dataSource.values.asIterable()
                    when (query.options.sort) {
                        "name" -> {
                            it = if (query.options.desc) {
                                it.sortedByDescending { e -> e.name }
                            } else {
                                it.sortedBy { e -> e.name }
                            }
                        }
                        else -> {
                            it = if (query.options.desc) {
                                it.reversed()
                            } else {
                                it
                            }
                        }
                    }
                    if (query.options.offset > 0) {
                        it = it.drop(query.options.offset)
                    }
                    if (query.options.limit > 0) {
                        it = it.take(query.options.limit)
                    }
                    return it
                }
            }
}
