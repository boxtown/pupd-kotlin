package com.pupd.backend.api.resources

import com.pupd.backend.api.ApiVerticle
import com.pupd.backend.data.entities.Workout
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
        vertx.deployTestEnvironment(
                ctx,
                "src/test/resources/sql/setup_exercise_test.sql",
                "src/test/resources/sql/setup_workout_test.sql")
        { injector ->
            ApiVerticle(injector)
        }
        client = vertx.createHttpClient()
    }

    @After
    fun tearDown(ctx: TestContext) {
        vertx.close(ctx.asyncAssertSuccess())
    }

    @Test
    fun testGetWorkout(ctx: TestContext) {
        val async1 = ctx.async()
        client.doGet<Unit> {
            uri {
                path("workout")
                path("00000000-0000-4000-8000-000000000001")
            }
            handler { resp ->
                ctx.assertEquals(resp.statusCode(), 200)
                resp.bodyHandler { body ->
                    val workout = Json.decodeValue(body.toString(), Workout::class.java)
                    ctx.assertEquals(workout.name, "Test Workout A")
                    ctx.assertTrue(workout.exercises.size == 3)
                    ctx.assertTrue(workout.sets.size == 3)
                    ctx.assertTrue(workout.increments.size == 3)
                    async1.complete()
                }
            }
        }.exceptionally { e -> ctx.fail(e) }

        val async2 = ctx.async()
        client.doGet<Unit> {
            uri {
                path("workout")
                path("00000000-0000-4000-8000-000000000003")
            }
            handler { resp ->
                ctx.assertEquals(resp.statusCode(), 200)
                resp.bodyHandler { body ->
                    async2.complete()
                }
            }
        }.exceptionally { e -> ctx.fail(e) }
    }
}