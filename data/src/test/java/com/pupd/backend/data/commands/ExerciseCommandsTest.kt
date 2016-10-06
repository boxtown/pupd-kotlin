package com.pupd.backend.data.commands

import com.pupd.backend.data.DefaultDatabase
import com.pupd.backend.data.h2.getDataSource
import com.pupd.backend.data.queries.GetExercise
import com.pupd.backend.data.queries.GetExerciseHandler
import com.pupd.backend.data.runSetupScripts
import org.hamcrest.CoreMatchers.`is`
import org.jooq.SQLDialect
import org.junit.Assert.assertNull
import org.junit.Assert.assertThat
import org.junit.Before
import org.junit.Test
import java.util.*

/**
 * Test class of exercise commands
 *
 * Created by maxiaojun on 10/5/16.
 */
class ExerciseCommandsTest {
    private val dataSource = getDataSource()
    private val database = DefaultDatabase(dataSource, SQLDialect.H2)

    @Before
    fun setUp() {
        dataSource.runSetupScripts("src/test/resources/sql/setup_exercise_test.sql")
    }

    @Test
    fun testCreateExercise() {
        val id = UUID.randomUUID()
        val command = CreateExercise(id, "New Exercise")
        val result = CreateExerciseHandler(database).handle(command)
        assertThat(result.status, `is`(Status.Ack))

        val check = GetExerciseHandler(database).handle(GetExercise(id))
        assertThat(check?.name, `is`("New Exercise"))
    }

    @Test
    fun testCreateExerciseWithBadName() {
        val id = UUID.randomUUID()
        val command = CreateExercise(id)
        val result = CreateExerciseHandler(database).handle(command)
        assertThat(result.status, `is`(Status.Nack))

        val check = GetExerciseHandler(database).handle(GetExercise(id))
        assertNull(check)
    }

    @Test
    fun testUpdateExercise() {
        val id = UUID.fromString("00000000-0000-4000-8000-000000000001")
        val command = UpdateExercise(id, "Updated Exercise")
        val result = UpdateExerciseHandler(database).handle(command)
        assertThat(result.status, `is`(Status.Ack))

        val check = GetExerciseHandler(database).handle(GetExercise(id))
        assertThat(check?.name, `is`("Updated Exercise"))
    }

    @Test
    fun testUpdateExerciseWithBadName() {
        val id = UUID.fromString("00000000-0000-4000-8000-000000000001")
        val command = UpdateExercise(id, "")
        val result = UpdateExerciseHandler(database).handle(command)
        assertThat(result.status, `is`(Status.Nack))

        val check = GetExerciseHandler(database).handle(GetExercise(id))
        assertThat(check?.name, `is`("Test Exercise A"))
    }

    @Test
    fun testUpdateExerciseIsIdempotent() {
        val id = UUID.fromString("00000000-0000-4000-8000-000000000004")
        val command = UpdateExercise(id, "Updated Exercise")
        val result = UpdateExerciseHandler(database).handle(command)
        assertThat(result.status, `is`(Status.Ack))

        val check = GetExerciseHandler(database).handle(GetExercise(id))
        assertNull(check)
    }

    @Test
    fun testDeleteExercise() {
        val id = UUID.fromString("00000000-0000-4000-8000-000000000001")
        val command = DeleteExercise(id)
        val result = DeleteExerciseHandler(database).handle(command)
        assertThat(result.status, `is`(Status.Ack))

        val check = GetExerciseHandler(database).handle(GetExercise(id))
        assertNull(check)
    }

    @Test
    fun testDeleteExerciseIsIdempotent() {
        val id = UUID.fromString("00000000-0000-4000-8000-000000000001")
        val command = DeleteExercise(id)

        // run once to delete
        DeleteExerciseHandler(database).handle(command)

        // now check idempotency
        val result = DeleteExerciseHandler(database).handle(command)
        assertThat(result.status, `is`(Status.Ack))
    }
}
