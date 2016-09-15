package com.pupd.backend.api.resources

import com.pupd.backend.api.ApiVerticle
import com.pupd.backend.data.entities.Exercise
import com.pupd.backend.api.resources.deployTestEnvironment
import com.pupd.backend.shared.vertx.doRequest
import io.vertx.core.Vertx
import io.vertx.core.http.HttpMethod
import io.vertx.core.json.DecodeException
import io.vertx.core.json.Json
import io.vertx.core.json.JsonArray
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

    @Before
    fun setUp(ctx: TestContext) {
        vertx = Vertx.vertx()
        vertx.deployTestEnvironment(ctx, "src/test/resources/sql/setup_exercise_test.sql") {
            injector -> ApiVerticle(injector)
        }
    }

    @After
    fun tearDown(ctx: TestContext) {
        vertx.close(ctx.asyncAssertSuccess())
    }

    @Test
    fun testGetExercise(ctx: TestContext) {
        val client = vertx.createHttpClient()
        val async = ctx.async()
        client.doRequest(HttpMethod.GET) {
            uri {
                path("exercise")
                path("00000000-0000-4000-8000-000000000001")
            }
            handler { resp ->
                ctx.assertEquals(resp.statusCode(), 200)
                resp.bodyHandler { body ->
                    try {
                        val exercise = Json.decodeValue(body.toString(), Exercise::class.java)
                        ctx.assertEquals(exercise?.name, "Test Exercise A")
                        async.complete()
                    } catch (e: DecodeException) {
                        ctx.fail(e)
                    }
                }
            }
        }
    }

    @Test
    fun testGetMissingExercise(ctx: TestContext) {
        val client = vertx.createHttpClient()
        val async = ctx.async()
        client.doRequest(HttpMethod.GET) {
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
    fun testListExercises(ctx: TestContext) {
        val client = vertx.createHttpClient()

        // test default
        val async1 = ctx.async()
        client.doRequest(HttpMethod.GET) {
            uri {
                path("exercises")
            }
            handler { resp ->
                ctx.assertEquals(resp.statusCode(), 200)
                resp.bodyHandler { body ->
                    val array = body.toJsonArray()
                    ctx.assertEquals(array.size(), 3)
                    ctx.assertEquals(array.getJsonObject(0).getString("name"), "Test Exercise A")
                    ctx.assertEquals(array.getJsonObject(2).getString("name"), "Test Exercise B")
                    async1.complete()
                }
            }
        }

        // test offset
        val async2 = ctx.async()
        client.doRequest(HttpMethod.GET) {
            uri {
                path("exercises")
                query("offset" to 1)
            }
            handler { resp ->
                ctx.assertEquals(resp.statusCode(), 200)
                resp.bodyHandler { body ->
                    val array = body.toJsonArray()
                    ctx.assertEquals(array.size(), 2)
                    ctx.assertEquals(array.getJsonObject(0).getString("name"), "Test Exercise C")
                    async2.complete()
                }
            }
        }

        // test limit
        val async3 = ctx.async()
        client.doRequest(HttpMethod.GET) {
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
                    async3.complete()
                }
            }
        }

        // test sort by
        val async4 = ctx.async()
        client.doRequest(HttpMethod.GET) {
            uri {
                path("exercises")
                query("sort" to "name")
            }
            handler { resp ->
                ctx.assertEquals(resp.statusCode(), 200)
                resp.bodyHandler { body ->
                    val array = body.toJsonArray()
                    ctx.assertEquals(array.size(), 3)
                    ctx.assertEquals(array.getJsonObject(1).getString("name"), "Test Exercise B")
                    async4.complete()
                }
            }
        }

        // test desc
        val async5 = ctx.async()
        client.doRequest(HttpMethod.GET) {
            uri {
                path("exercises")
                query("desc" to "true")
            }
            handler { resp ->
                ctx.assertEquals(resp.statusCode(), 200)
                resp.bodyHandler { body ->
                    val array = body.toJsonArray()
                    ctx.assertEquals(array.size(), 3)
                    ctx.assertEquals(array.getJsonObject(0).getString("name"), "Test Exercise B")
                    async5.complete()
                }
            }
        }

        // test combo
        val async6 = ctx.async()
        client.doRequest(HttpMethod.GET) {
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
                    async6.complete()
                }
            }
        }
    }
}