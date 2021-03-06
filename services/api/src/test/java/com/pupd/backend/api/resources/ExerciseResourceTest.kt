package com.pupd.backend.api.resources

import com.pupd.backend.api.ApiVerticle
import com.pupd.backend.data.entities.Exercise
import com.pupd.backend.shared.vertx.*
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
 * Exercise Resource Test class
 *
 * Created by maxiaojun on 9/4/16.
 */
@RunWith(VertxUnitRunner::class)
class ExerciseResourceTest {
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
    fun testGetExercise(ctx: TestContext) {
        val async = ctx.async()
        client.doGet<Unit> {
            uri {
                path("exercise")
                path("00000000-0000-4000-8000-000000000001")
            }
            handler { resp ->
                ctx.assertEquals(resp.statusCode(), 200)
                resp.bodyHandler { body ->
                    val exercise = Json.decodeValue(body.toString(), Exercise::class.java)
                    ctx.assertEquals(exercise?.name, "Test Exercise A")
                    async.complete()
                }
            }
            errorHandler { t ->
                ctx.fail(t)
            }
        }
    }

    @Test
    fun testGetMissingExercise(ctx: TestContext) {
        val async = ctx.async()
        client.doGet<Unit> {
            uri {
                path("exercise")
                path("00000000-0000-4000-8000-000000000004")
            }
            handler { resp ->
                ctx.assertEquals(resp.statusCode(), 404)
                async.complete()
            }
        }
    }

    @Test
    fun testListExercisesWithNoArguments(ctx: TestContext) {
        val async = ctx.async()
        client.doGet<Unit> {
            uri {
                path("exercises")
            }
            handler { resp ->
                ctx.assertEquals(resp.statusCode(), 200)
                resp.bodyHandler { body ->
                    val array = body.toJsonArray()
                    ctx.assertEquals(array.size(), 3)
                    arrayOf("Test Exercise A",
                            "Test Exercise C",
                            "Test Exercise B")
                            .forEachIndexed { i, s ->
                                ctx.assertEquals(array.getJsonObject(i).getString("name"), s)
                            }
                    async.complete()
                }
            }
            errorHandler { t ->
                ctx.fail(t)
            }
        }
    }

    @Test
    fun testListExercisesWithOffset(ctx: TestContext) {
        val async = ctx.async()
        client.doGet<Unit> {
            uri {
                path("exercises")
                query("offset" to 1)
            }
            handler { resp ->
                ctx.assertEquals(resp.statusCode(), 200)
                resp.bodyHandler { body ->
                    val array = body.toJsonArray()
                    ctx.assertEquals(array.size(), 2)
                    arrayOf("Test Exercise C", "Test Exercise B").forEachIndexed { i, s ->
                        ctx.assertEquals(array.getJsonObject(i).getString("name"), s)
                    }
                    async.complete()
                }
            }
            errorHandler { t ->
                ctx.fail(t)
            }
        }
    }

    @Test
    fun testListExercisesWithLimit(ctx: TestContext) {
        val async = ctx.async()
        client.doGet<Unit> {
            uri {
                path("exercises")
                query("limit" to 1)
            }
            handler { resp ->
                ctx.assertEquals(resp.statusCode(), 200)
                resp.bodyHandler { body ->
                    val array = body.toJsonArray()
                    ctx.assertEquals(array.size(), 1)
                    ctx.assertEquals(array.getJsonObject(0).getString("name"), "Test Exercise A")
                    async.complete()
                }
            }
            errorHandler { t ->
                ctx.fail(t)
            }
        }
    }

    @Test
    fun testListExercisesWithSorting(ctx: TestContext) {
        val async = ctx.async()
        client.doGet<Unit> {
            uri {
                path("exercises")
                query("sort" to "name")
            }
            handler { resp ->
                ctx.assertEquals(resp.statusCode(), 200)
                resp.bodyHandler { body ->
                    val array = body.toJsonArray()
                    ctx.assertEquals(array.size(), 3)
                    arrayOf("Test Exercise A",
                            "Test Exercise B",
                            "Test Exercise C")
                    .forEachIndexed { i, s ->
                        ctx.assertEquals(array.getJsonObject(i).getString("name"), s)
                    }
                    async.complete()
                }
            }
            errorHandler { t ->
                ctx.fail(t)
            }
        }
    }

    @Test
    fun testListExercisesInDescendingOrder(ctx: TestContext) {
        val async = ctx.async()
        client.doGet<Unit> {
            uri {
                path("exercises")
                query("desc" to "true")
            }
            handler { resp ->
                ctx.assertEquals(resp.statusCode(), 200)
                resp.bodyHandler { body ->
                    val array = body.toJsonArray()
                    ctx.assertEquals(array.size(), 3)
                    arrayOf("Test Exercise B",
                            "Test Exercise C",
                            "Test Exercise A")
                            .forEachIndexed { i, s ->
                                ctx.assertEquals(array.getJsonObject(i).getString("name"), s)
                            }
                    async.complete()
                }
            }
            errorHandler { t ->
                ctx.fail(t)
            }
        }
    }

    @Test
    fun testListExercises(ctx: TestContext) {
        val async = ctx.async()
        client.doGet<Unit> {
            uri {
                path("exercises")
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
                    ctx.assertEquals(array.getJsonObject(0).getString("name"), "Test Exercise B")
                    async.complete()
                }
            }
            errorHandler { t ->
                ctx.fail(t)
            }
        }
    }

    @Test
    fun testCreateExercise(ctx: TestContext) {
        val async = ctx.async()
        client.doPost<Unit> {
            uri {
                path("exercise")
            }
            headers {
                put("content-type", "application/json")
            }
            body {
                appendString(Json.encode(Exercise(name = "New Exercise")))
            }
            handler { resp ->
                ctx.assertEquals(resp.statusCode(), 201)
                ctx.assertTrue(resp.headers()["location"].length > 0)
                async.complete()
            }
            errorHandler { t ->
                ctx.fail(t)
            }
        }
    }

    @Test
    fun testCreateExerciseWithNoBody(ctx: TestContext) {
        val async = ctx.async()
        client.doPost<Unit> {
            uri {
                path("exercise")
            }
            headers {
                put("content-type", "application/json")
            }
            handler { resp ->
                ctx.assertEquals(resp.statusCode(), 400)
                // TODO: check error code body
                async.complete()
            }
            errorHandler { t ->
                ctx.fail(t)
            }
        }
    }

    @Test
    fun testCreateExerciseWithBadName(ctx: TestContext) {
        val async = ctx.async()
        client.doPost<Unit> {
            uri {
                path("exercise")
            }
            headers {
                put("content-type", "application/json")
            }
            body {
                appendString(Json.encode(Exercise()))
            }
            handler { resp ->
                ctx.assertEquals(resp.statusCode(), 400)
                // TODO: check error code body
                async.complete()
            }
            errorHandler { t ->
                ctx.fail(t)
            }
        }
    }

    @Test
    fun testUpdateExercise(ctx: TestContext) {
        val async = ctx.async()
        client.doPut<Unit> {
            uri {
                path("exercise")
                path("00000000-0000-4000-8000-000000000001")
            }
            headers {
                put("content-type", "application/json")
            }
            body {
                appendString(Json.encode(Exercise(name = "Updated Exercise")))
            }
            handler { resp ->
                ctx.assertEquals(resp.statusCode(), 202)
                async.complete()
            }
            errorHandler { t ->
                ctx.fail(t)
            }
        }
    }

    @Test
    fun testUpdateExerciseIsIdempotent(ctx: TestContext) {
        val async = ctx.async()
        client.doPut<Unit> {
            uri {
                path("exercise")
                path("null")
            }
            headers {
                put("content-type", "application/json")
            }
            body {
                appendString(Json.encode(Exercise(name = "Updated Exercise")))
            }
            handler { resp ->
                ctx.assertEquals(resp.statusCode(), 202)
                async.complete()
            }
            errorHandler { t ->
                ctx.fail(t)
            }
        }
    }

    @Test
    fun testUpdateExerciseWithBadName(ctx: TestContext) {
        val async = ctx.async()
        client.doPut<Unit> {
            uri {
                path("exercise")
                path("00000000-0000-4000-8000-000000000001")
            }
            headers {
                put("content-type", "application/json")
            }
            body {
                appendString(Json.encode(Exercise()))
            }
            handler { resp ->
                ctx.assertEquals(resp.statusCode(), 400)
                async.complete()
            }
            errorHandler { t ->
                ctx.fail(t)
            }
        }
    }

    @Test
    fun testDeleteExercise(ctx: TestContext) {
        val async = ctx.async()
        client.doDelete<Unit> {
            uri {
                path("exercise")
                path("00000000-0000-4000-8000-000000000001")
            }
            handler { resp ->
                ctx.assertEquals(resp.statusCode(), 202)
                async.complete()
            }
            errorHandler { t ->
                ctx.fail(t)
            }
        }
    }

    @Test
    fun testDeleteExerciseIsIdempotent(ctx: TestContext) {
        val async = ctx.async()
        client.doDelete<Unit> {
            uri {
                path("exercise")
                path("00000000-0000-4000-8000-000000000001")
            }
            errorHandler { t ->
                ctx.fail(t)
            }
        }.thenCompose {
            client.doDelete<Unit> {
                uri {
                    path("exercise")
                    path("00000000-0000-4000-8000-000000000001")
                }
                handler { resp ->
                    ctx.assertEquals(resp.statusCode(), 202)
                    async.complete()
                }
                errorHandler { t ->
                    ctx.fail(t)
                }
            }
        }
    }
}