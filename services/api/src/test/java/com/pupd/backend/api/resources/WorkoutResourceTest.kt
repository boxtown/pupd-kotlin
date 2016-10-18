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
                    ctx.assertEquals(workout, workouts[0])
                    async.complete()
                }
            }
            errorHandler { t -> ctx.fail(t) }
        }
    }

    @Test
    fun testListWorkoutsWithNoArguments(ctx: TestContext) {
        val async = ctx.async()
        client.doGet<Unit> {
            uri {
                path("workouts")
            }
            handler { resp ->
                ctx.assertEquals(resp.statusCode(), 200)
                resp.bodyHandler { body ->
                    val array = body.toJsonArray()
                    ctx.assertEquals(array.size(), 3)
                    arrayOf("Test Workout A",
                            "Test Workout C",
                            "Test Workout B")
                            .forEachIndexed { i, s ->
                                ctx.assertEquals(array.getJsonObject(i).getString("name"), s)
                            }
                    async.complete()
                }
            }
            errorHandler { t -> ctx.fail(t) }
        }
    }

    @Test
    fun testListWorkoutsWithOffset(ctx: TestContext) {
        val async = ctx.async()
        client.doGet<Unit> {
            uri {
                path("workouts")
                query("offset" to "1")
            }
            handler { resp ->
                ctx.assertEquals(resp.statusCode(), 200)
                resp.bodyHandler { body ->
                    val array = body.toJsonArray()
                    ctx.assertEquals(array.size(), 2)
                    arrayOf("Test Workout C", "Test Workout B").forEachIndexed { i, s ->
                        ctx.assertEquals(array.getJsonObject(i).getString("name"), s)
                    }
                    async.complete()
                }
            }
            errorHandler { t -> ctx.fail(t) }
        }
    }

    @Test
    fun testListWorkoutsWithLimit(ctx: TestContext) {
        val async = ctx.async()
        client.doGet<Unit> {
            uri {
                path("workouts")
                query("limit" to "1")
            }
            handler { resp ->
                ctx.assertEquals(resp.statusCode(), 200)
                resp.bodyHandler { body ->
                    val array = body.toJsonArray()
                    ctx.assertEquals(array.size(), 1)
                    ctx.assertEquals(array.getJsonObject(0).getString("name"), "Test Workout A")
                    async.complete()
                }
            }
            errorHandler { t -> ctx.fail(t) }
        }
    }

    @Test
    fun testListWorkoutsWithSorting(ctx: TestContext) {
        val async = ctx.async()
        client.doGet<Unit> {
            uri {
                path("workouts")
                query("sort" to "name")
            }
            handler { resp ->
                ctx.assertEquals(resp.statusCode(), 200)
                resp.bodyHandler { body ->
                    val array = body.toJsonArray()
                    ctx.assertEquals(array.size(), 3)
                    arrayOf("Test Workout A",
                            "Test Workout B",
                            "Test Workout C")
                            .forEachIndexed { i, s ->
                                ctx.assertEquals(array.getJsonObject(i).getString("name"), s)
                            }
                    async.complete()
                }
            }
            errorHandler { t -> ctx.fail(t) }
        }
    }

    @Test
    fun testListWorkoutsInDescendingOrder(ctx: TestContext) {
        val async = ctx.async()
        client.doGet<Unit> {
            uri {
                path("workouts")
                query("desc" to "true")
            }
            handler { resp ->
                ctx.assertEquals(resp.statusCode(), 200)
                resp.bodyHandler { body ->
                    val array = body.toJsonArray()
                    ctx.assertEquals(array.size(), 3)
                    arrayOf("Test Workout B",
                            "Test Workout C",
                            "Test Workout A")
                            .forEachIndexed { i, s ->
                                ctx.assertEquals(array.getJsonObject(i).getString("name"), s)
                            }
                    async.complete()
                }
            }
            errorHandler { t -> ctx.fail(t) }
        }
    }

    @Test
    fun testListWorkouts(ctx: TestContext) {
        val async = ctx.async()
        client.doGet<Unit> {
            uri {
                path("workouts")
                query("sort" to "name")
                query("desc" to "true")
                query("offset" to 1)
                query("limit" to 1)
            }
            handler { resp ->
                ctx.assertEquals(resp.statusCode(), 200)
                resp.bodyHandler { body ->
                    val array = body.toJsonArray()
                    ctx.assertEquals(array.size(), 1)
                    ctx.assertEquals(array.getJsonObject(0).getString("name"), "Test Workout B")
                    async.complete()
                }
            }
            errorHandler { t -> ctx.fail(t) }
        }
    }
}