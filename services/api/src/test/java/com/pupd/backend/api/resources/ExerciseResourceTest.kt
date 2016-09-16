package com.pupd.backend.api.resources

import com.pupd.backend.api.ApiVerticle
import com.pupd.backend.data.entities.Exercise
import com.pupd.backend.shared.vertx.doRequest
import com.pupd.backend.shared.vertx.put
import io.vertx.core.Vertx
import io.vertx.core.http.HttpClient
import io.vertx.core.http.HttpMethod
import io.vertx.core.json.Json
import io.vertx.ext.unit.TestContext
import io.vertx.ext.unit.junit.VertxUnitRunner
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.util.*

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
        vertx.deployTestEnvironment(ctx, "src/test/resources/sql/setup_exercise_test.sql") {
            injector ->
            ApiVerticle(injector)
        }
        client = vertx.createHttpClient()
    }

    @After
    fun tearDown(ctx: TestContext) {
        vertx.close(ctx.asyncAssertSuccess())
    }

    @Test
    fun testGetExercise(ctx: TestContext) {
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
                    } catch (e: Exception) {
                        ctx.fail(e)
                    }
                }
            }
        }
    }

    @Test
    fun testGetMissingExercise(ctx: TestContext) {
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
        // test default
        val async1 = ctx.async()
        client.doRequest(HttpMethod.GET) {
            uri {
                path("exercises")
            }
            handler { resp ->
                ctx.assertEquals(resp.statusCode(), 200)
                resp.bodyHandler { body ->
                    try {
                        val array = body.toJsonArray()
                        ctx.assertEquals(array.size(), 3)
                        ctx.assertEquals(array.getJsonObject(0).getString("name"), "Test Exercise A")
                        ctx.assertEquals(array.getJsonObject(2).getString("name"), "Test Exercise B")
                        async1.complete()
                    } catch (e: Exception) {
                        ctx.fail(e)
                    }
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
                    try {
                        val array = body.toJsonArray()
                        ctx.assertEquals(array.size(), 2)
                        ctx.assertEquals(array.getJsonObject(0).getString("name"), "Test Exercise C")
                        async2.complete()
                    } catch (e: Exception) {
                        ctx.fail(e)
                    }
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
                    try {
                        val array = body.toJsonArray()
                        ctx.assertEquals(array.size(), 1)
                        ctx.assertEquals(array.getJsonObject(0).getString("name"), "Test Exercise A")
                        async3.complete()
                    } catch (e: Exception) {
                        ctx.fail(e)
                    }
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
                    try {
                        val array = body.toJsonArray()
                        ctx.assertEquals(array.size(), 3)
                        ctx.assertEquals(array.getJsonObject(1).getString("name"), "Test Exercise B")
                        async4.complete()
                    } catch (e: Exception) {
                        ctx.fail(e)
                    }
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
                    try {
                        val array = body.toJsonArray()
                        ctx.assertEquals(array.size(), 3)
                        ctx.assertEquals(array.getJsonObject(0).getString("name"), "Test Exercise B")
                        async5.complete()
                    } catch (e: Exception) {
                        ctx.fail(e)
                    }
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
                    try {
                        val array = body.toJsonArray()
                        ctx.assertEquals(array.size(), 1)
                        ctx.assertEquals(array.getJsonObject(0).getString("name"), "Test Exercise B")
                        async6.complete()
                    } catch (e: Exception) {
                        ctx.fail(e)
                    }
                }
            }
        }
    }

    @Test
    fun testCreateExercise(ctx: TestContext) {
        val async = ctx.async()
        client.doRequest(HttpMethod.POST) {
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

                val id = resp.headers()["location"].substringAfterLast('/')
                val async2 = ctx.async()
                client.doRequest(HttpMethod.GET) {
                    uri {
                        path("exercise")
                        path(id)
                    }
                    handler { resp ->
                        ctx.assertEquals(resp.statusCode(), 200)
                        resp.bodyHandler { body ->
                            try {
                                val exercise = Json.decodeValue(body.toString(), Exercise::class.java)
                                ctx.assertEquals(exercise?.name, "New Exercise")
                                async2.complete()
                            } catch (e: Exception) {
                                ctx.fail(e)
                            }
                        }
                    }
                }
                async.complete()
            }
        }
    }

    @Test
    fun testCreateBadExercise(ctx: TestContext) {
        val client = vertx.createHttpClient()
    }

    @Test
    fun testUpdateExercise(ctx: TestContext) {

    }

    @Test
    fun testDeleteExercise(ctx: TestContext) {

    }
}