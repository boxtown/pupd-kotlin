package com.pupd.backend.data.queries

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.KotlinModule
import com.pupd.backend.data.DefaultDatabase
import com.pupd.backend.data.h2.getDataSource
import com.pupd.backend.data.runSetupScripts
import org.jooq.SQLDialect
import org.junit.Before
import org.junit.Test
import java.util.*
import org.junit.Assert.assertThat
import org.hamcrest.CoreMatchers.`is`

/**
 * Test class for workout queries
 *
 * Created by maxiaojun on 10/7/16.
 */
class WorkoutQueriesTest {
    private val dataSource = getDataSource()
    private val database = DefaultDatabase(dataSource, SQLDialect.H2)
    private val mapper = ObjectMapper()

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
        assertThat(result?.name, `is`("Test Workout A"))
        assertThat(result?.exercises?.size, `is`(2))

        var exercise = result?.exercises?.get(UUID.fromString("00000000-0000-4000-8000-000000000001"))
        assertThat(exercise?.exercise?.name, `is`("Test Exercise A"))
        assertThat(exercise?.sets?.size, `is`(3))
        arrayOf(5 to 85.0, 5 to 95.0, 5 to 100.0).forEachIndexed { i, pair ->
            val set = exercise?.sets?.get(i)
            assertThat(set?.reps, `is`(pair.first))
            assertThat(set?.weight, `is`(pair.second))
        }
        assertThat(exercise?.increment, `is`(5.0))


        exercise = result?.exercises?.get(UUID.fromString("00000000-0000-4000-8000-000000000002"))
        assertThat(exercise?.exercise?.name, `is`("Test Exercise C"))
        assertThat(exercise?.sets?.size, `is`(1))
        val set = exercise?.sets?.first()
        assertThat(set?.reps, `is`(5))
        assertThat(set?.weight, `is`(65.0))
        assertThat(exercise?.increment, `is`(10.0))
    }
}
