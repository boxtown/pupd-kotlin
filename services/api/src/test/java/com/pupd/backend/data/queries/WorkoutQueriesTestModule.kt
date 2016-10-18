package com.pupd.backend.data.queries

import com.google.inject.AbstractModule
import com.google.inject.Provides
import com.pupd.backend.data.entities.Exercise
import com.pupd.backend.data.entities.Workout
import com.pupd.backend.data.entities.WorkoutExercise
import com.pupd.backend.data.entities.WorkoutSet
import java.util.*
import javax.inject.Singleton

/**
 * Guice DI workout queries module for testing
 *
 * Created by maxiaojun on 10/6/16.
 */
class WorkoutQueriesTestModule : AbstractModule() {
    private val ids: Array<UUID> = arrayOf(
            UUID.fromString("00000000-0000-4000-8000-000000000001"),
            UUID.fromString("00000000-0000-4000-8000-000000000002"),
            UUID.fromString("00000000-0000-4000-8000-000000000003")
    )
    private val dataSource: Map<UUID, Workout> = mapOf(
            ids[0] to Workout(ids[0], "Test Workout A", mapOf(
                    ids[0] to WorkoutExercise(Exercise(ids[0], "Test Exercise A"), listOf(
                            WorkoutSet(5, 85.0),
                            WorkoutSet(5, 95.0),
                            WorkoutSet(5, 100.0)
                    ), 5.0),
                    ids[1] to WorkoutExercise(Exercise(ids[1], "Test Exercise C"), listOf(
                            WorkoutSet(5, 65.0)
                    ), 10.0)
            )),
            ids[1] to Workout(ids[1], "Test Workout C"),
            ids[2] to Workout(ids[2], "Test Workout B", mapOf(
                    ids[2] to WorkoutExercise(Exercise(ids[2], "Test Exercise B"), listOf(
                            WorkoutSet(5, 100.0),
                            WorkoutSet(5, 100.0),
                            WorkoutSet(5, 100.0)
                    ), 3.0)
            ))
    )

    override fun configure() {
    }

    @Suppress("UNUSED")
    @Singleton
    @Provides
    fun getWorkoutHandler(): QueryHandler<GetWorkout, Workout?> =
            object : QueryHandler<GetWorkout, Workout?> {
                override fun handle(query: GetWorkout): Workout? = dataSource[query.id]
            }

    @Suppress("UNUSED")
    @Singleton
    @Provides
    fun listWorkoutsHandler(): QueryHandler<ListWorkouts, Iterable<Workout>> =
            object : QueryHandler<ListWorkouts, Iterable<Workout>> {
                override fun handle(query: ListWorkouts): Iterable<Workout> {
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
