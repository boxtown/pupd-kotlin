package com.pupd.backend.api.resources

import com.pupd.backend.api.ApiVerticle
import com.pupd.backend.data.entities.Exercise
import com.pupd.backend.api.resources.deployTestEnvironment
import com.pupd.backend.shared.vertx.doRequest
import io.vertx.core.Vertx
import io.vertx.core.http.HttpMethod
import io.vertx.core.json.DecodeException
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
        val async = ctx.async()
        val client = vertx.createHttpClient()
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
        val async = ctx.async()
        val client = vertx.createHttpClient()
        client.doRequest(HttpMethod.GET) {
            uri {
                path("exercise")
                path("00000000-0000-4000-8000-000000000002")
            }
            handler { resp ->
                ctx.assertEquals(resp.statusCode(), 404)
                async.complete()
            }
        }
    }
}