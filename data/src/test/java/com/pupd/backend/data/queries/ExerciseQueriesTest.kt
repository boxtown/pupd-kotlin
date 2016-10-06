package com.pupd.backend.data.queries

import com.pupd.backend.data.DefaultDatabase
import com.pupd.backend.data.h2.getDataSource
import com.pupd.backend.data.runSetupScripts
import org.hamcrest.CoreMatchers.`is`
import org.jooq.SQLDialect
import org.junit.Assert.assertNull
import org.junit.Assert.assertThat
import org.junit.Before
import org.junit.Test
import java.util.*

/**
 * Test class for exercise queries
 *
 * Created by maxiaojun on 10/5/16
 */
class ExerciseQueriesTest {
    private val dataSource = getDataSource()
    private val database = DefaultDatabase(dataSource, SQLDialect.H2)

    @Before
    fun setUp() {
        dataSource.runSetupScripts("src/test/resources/sql/setup_exercise_test.sql")
    }

    @Test
    fun testGetExercise() {
        val query = GetExercise(UUID.fromString("00000000-0000-4000-8000-000000000001"))
        val result = GetExerciseHandler(database).handle(query)
        assertThat(result?.name, `is`("Test Exercise A"))
    }

    @Test
    fun testGetMissingExercise() {
        val query = GetExercise(UUID.fromString("00000000-0000-4000-8000-000000000004"))
        val result = GetExerciseHandler(database).handle(query)
        assertNull(result)
    }

    @Test
    fun testListExercisesWithNoArguments() {
        val query = ListExercises(ListOptions())
        val result = ListExercisesHandler(database).handle(query).toList()
        assertThat(result.size, `is`(3))
        arrayOf("Test Exercise A",
                "Test Exercise C",
                "Test Exercise B")
                .forEachIndexed { i, s ->
                    assertThat(result[i].name, `is`(s))
                }
    }

    @Test
    fun testListExercisesWithOffset() {
        val query = ListExercises(ListOptions(offset = 1))
        val result = ListExercisesHandler(database).handle(query).toList()
        assertThat(result.size, `is`(2))
        arrayOf("Test Exercise C",
                "Test Exercise B")
                .forEachIndexed { i, s ->
                    assertThat(result[i].name, `is`(s))
                }
    }

    @Test
    fun testListExercisesWithLimit() {
        val query = ListExercises(ListOptions(limit = 1))
        val result = ListExercisesHandler(database).handle(query).toList()
        assertThat(result.size, `is`(1))
        assertThat(result[0].name, `is`("Test Exercise A"))
    }

    @Test
    fun testListExercisesWithSorting() {
        val query = ListExercises(ListOptions(sort = "name"))
        val result = ListExercisesHandler(database).handle(query).toList()
        assertThat(result.size, `is`(3))
        arrayOf("Test Exercise A",
                "Test Exercise B",
                "Test Exercise C")
                .forEachIndexed { i, s ->
                    assertThat(result[i].name, `is`(s))
                }
    }

    @Test
    fun testListExercisesInDescendingOrder() {
        val query = ListExercises(ListOptions(desc = true))
        val result = ListExercisesHandler(database).handle(query).toList()
        assertThat(result.size, `is`(3))
        arrayOf("Test Exercise C",
                "Test Exercise B",
                "Test Exercise A")
                .forEachIndexed { i, s ->
                    assertThat(result[i].name, `is`(s))
                }
    }

    @Test
    fun testListExercises() {
        val query = ListExercises(ListOptions(offset = 1, limit = 1, sort = "name", desc = true))
        val result = ListExercisesHandler(database).handle(query).toList()
        assertThat(result.size, `is`(1))
        assertThat(result[0].name, `is`("Test Exercise B"))
    }
}
