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
        val async = ctx.async()
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
                    ctx.assertEquals(workout.exercises.size, 2)

                    var exercise = workout.exercises[UUID.fromString("00000000-0000-4000-8000-000000000001")]
                    ctx.assertEquals(exercise?.exercise?.name, "Test Exercise A")
                    ctx.assertEquals(exercise?.sets?.size, 3)
                    arrayOf(5 to 85.0, 5 to 95.0, 5 to 100.0).forEachIndexed { i, pair ->
                        val set = exercise?.sets?.get(i)
                        ctx.assertEquals(set?.reps, pair.first)
                        ctx.assertEquals(set?.weight, pair.second)
                    }
                    ctx.assertEquals(exercise?.increment, 5.0)


                    exercise = workout.exercises[UUID.fromString("00000000-0000-4000-8000-000000000002")]
                    ctx.assertEquals(exercise?.exercise?.name, "Test Exercise C")
                    ctx.assertEquals(exercise?.sets?.size, 1)
                    val set = exercise?.sets?.first()
                    ctx.assertEquals(set?.reps, 5)
                    ctx.assertEquals(set?.weight, 65.0)
                    ctx.assertEquals(exercise?.increment, 10.0)
                }
            }
            errorHandler { t -> ctx.fail(t) }
        }.thenRun {
            async.complete()
        }
    }
}