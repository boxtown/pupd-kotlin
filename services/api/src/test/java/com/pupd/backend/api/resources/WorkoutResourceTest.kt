package com.pupd.backend.api.resources

import com.pupd.backend.api.ApiVerticle
import com.pupd.backend.data.entities.Exercise
import com.pupd.backend.data.entities.Workout
import com.pupd.backend.data.entities.WorkoutExercise
import com.pupd.backend.data.entities.WorkoutSet
import com.pupd.backend.shared.vertx.doGet
import io.vertx.core.Vertx
import io.vertx.core.http.HttpClient
import io.vertx.core.json.Json
import io.vertx.ext.unit.TestContext
import io.vertx.ext.unit.junit.VertxUnitRunner
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.util.*

/**
 * Workout resource test class
 *
 * Created by maxiaojun on 9/20/16.
 */
@RunWith(VertxUnitRunner::class)
class WorkoutResourceTest {
    private lateinit var vertx: Vertx
    private lateinit var client: HttpClient

    @Before
    fun setUp(ctx: TestContext) {
        vertx = Vertx.vertx()
        vertx.deployTestEnvironment(ctx) { injector -> ApiVerticle(injector) }
        client = vertx.createHttpClient()
    }

    @After
    fun tearDown(ctx: TestContext) {
        vertx.close(ctx.asyncAssertSuccess())
    }

    @Test
    fun testGetWorkout(ctx: TestContext) {
        val ids = arrayOf(
                UUID.fromString("00000000-0000-4000-8000-000000000001"),
                UUID.fromString("00000000-0000-4000-8000-000000000002")
        )

        val async = ctx.async()
        client.doGet<Unit> {
            uri {
                path("workout")
                path("00000000-0000-4000-8000-000000000001")
            }
            handler { resp ->
                ctx.assertEquals(resp.statusCode(), 200)
                resp.bodyHandler { body ->
                    val check = Workout(
                            ids[0],
                            "Test Workout A",
                            mapOf(
                                    ids[0] to WorkoutExercise(
                                            Exercise(ids[0], "Test Exercise A"),
                                            listOf(
                                                    WorkoutSet(5, 85.0),
                                                    WorkoutSet(5, 95.0),
                                                    WorkoutSet(5, 100.0)
                                            ),
                                            5.0
                                    ),
                                    ids[1] to WorkoutExercise(
                                            Exercise(ids[1], "Test Exercise C"),
                                            listOf(
                                                    WorkoutSet(5, 65.0)
                                            ),
                                            10.0
                                    )
                            ))
                    val workout = Json.decodeValue(body.toString(), Workout::class.java)
                    ctx.assertEquals(workout, check)
                }
            }
            errorHandler { t -> ctx.fail(t) }
        }.thenRun {
            async.complete()
        }
    }
}