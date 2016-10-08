package com.pupd.backend.data.queries

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.KotlinModule
import com.pupd.backend.data.DefaultDatabase
import com.pupd.backend.data.entities.Exercise
import com.pupd.backend.data.entities.Workout
import com.pupd.backend.data.entities.WorkoutExercise
import com.pupd.backend.data.entities.WorkoutSet
import com.pupd.backend.data.h2.getDataSource
import com.pupd.backend.data.runSetupScripts
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.CoreMatchers.equalTo
import org.jooq.SQLDialect
import org.junit.Assert.assertThat
import org.junit.Before
import org.junit.Test
import java.util.*

/**
 * Test class for workout queries
 *
 * Created by maxiaojun on 10/7/16.
 */
class WorkoutQueriesTest {
    private val dataSource = getDataSource()
    private val database = DefaultDatabase(dataSource, SQLDialect.H2)
    private val mapper = ObjectMapper()

    /* Data to check against */
    private val ids: Array<UUID> = arrayOf(
            UUID.fromString("00000000-0000-4000-8000-000000000001"),
            UUID.fromString("00000000-0000-4000-8000-000000000002"),
            UUID.fromString("00000000-0000-4000-8000-000000000003")
    )
    private val workouts: Array<Workout> = arrayOf(
            Workout(ids[0], "Test Workout A", mapOf(
                    ids[0] to WorkoutExercise(Exercise(ids[0], "Test Exercise A"), listOf(
                            WorkoutSet(5, 85.0),
                            WorkoutSet(5, 95.0),
                            WorkoutSet(5, 100.0)
                    ), 5.0),
                    ids[1] to WorkoutExercise(Exercise(ids[1], "Test Exercise C"), listOf(
                            WorkoutSet(5, 65.0)
                    ), 10.0)
            )),
            Workout(ids[1], "Test Workout C"),
            Workout(ids[2], "Test Workout B", mapOf(
                    ids[2] to WorkoutExercise(Exercise(ids[2], "Test Exercise B"), listOf(
                            WorkoutSet(5, 100.0),
                            WorkoutSet(5, 100.0),
                            WorkoutSet(5, 100.0)
                    ), 3.0)
            ))
    )

    init {
        mapper.registerModule(KotlinModule())
    }

    @Before
    fun setUp() {
        dataSource.runSetupScripts(
                "src/test/resources/sql/setup_exercise_test.sql",
                "src/test/resources/sql/setup_workout_test.sql")
    }

    @Test
    fun testGetWorkout() {
        val query = GetWorkout(UUID.fromString("00000000-0000-4000-8000-000000000001"))
        val result = GetWorkoutHandler(database, mapper).handle(query)
        assertThat(result, equalTo(workouts[0]))
    }

    @Test
    fun testListWorkoutsWithNoArguments() {
        val query = ListWorkouts(ListOptions())
        val result = ListWorkoutsHandler(database, mapper).handle(query).toList()
        assertThat(result.size, `is`(3))
        workouts.forEachIndexed { i, workout ->
            assertThat(workout, equalTo(result[i]))
        }
    }
}
