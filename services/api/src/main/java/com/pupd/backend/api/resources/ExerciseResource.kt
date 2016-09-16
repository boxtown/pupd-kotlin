package com.pupd.backend.api.resources

import com.pupd.backend.data.commands.*
import com.pupd.backend.data.entities.Exercise
import com.pupd.backend.data.queries.GetExercise
import com.pupd.backend.data.queries.ListExercises
import com.pupd.backend.data.queries.QueryHandler
import io.vertx.core.Vertx
import io.vertx.core.json.DecodeException
import io.vertx.core.json.Json
import io.vertx.ext.web.Router
import io.vertx.ext.web.RoutingContext
import io.vertx.ext.web.handler.BodyHandler
import java.util.*
import javax.inject.Inject

/**
 * Exercise REST API resource
 *
 * Created by maxiaojun on 9/2/16.
 */
@JvmSuppressWildcards
class ExerciseResource @Inject constructor(
        private val vertx: Vertx,
        private val commandBus: CommandBus,
        private val getExerciseHandler: QueryHandler<GetExercise, Exercise?>,
        private val listExercisesHandler: QueryHandler<ListExercises, Iterable<Exercise>>) {

    fun createExercise(ctx: RoutingContext) {
        val createCommand = try {
            Json.decodeValue(ctx.bodyAsString, CreateExercise::class.java)
        } catch (e: DecodeException) {
            ctx.response().sendBadRequest(ErrorCode.BAD_BODY)
            return
        }

        if (createCommand.name.length == 0) {
            ctx.response().sendBadRequest(ErrorCode.BAD_BODY)
            return
        }
        createCommand.id = UUID.randomUUID()

        vertx.executeBlocking<Result>(
                { future ->
                    future.complete(commandBus.dispatch(createCommand))
                }, false, { res ->
                    if (res.succeeded()) {
                        val result = res.result()
                        when (result.status) {
                            Status.Ack -> {
                                ctx.response()
                                        .setStatusCode(201)
                                        .putHeader("location", "/exercise/${createCommand.id}")
                                        .end()
                            }
                            Status.Nack -> ctx.response().sendBadRequest(ErrorCode.BAD_BODY, result.message)
                        }
                    } else {
                        // TODO: logging
                        ctx.response().respondTo(res.cause())
                    }
                }
        )
    }

    fun getExercise(ctx: RoutingContext) {
        val id = try {
            UUID.fromString(ctx.request().getParam("id"))
        } catch(e: IllegalArgumentException) {
            ctx.response().sendNotFound()
            return
        }

        vertx.executeBlocking<Exercise?>(
                { future ->
                    future.complete(getExerciseHandler.handle(GetExercise(id)))
                }, false, { res ->
                    if (res.succeeded()) {
                        val exercise = res.result()
                        if (exercise == null) {
                            ctx.response().sendNotFound()
                        } else {
                            ctx.response().end(Json.encode(exercise))
                        }
                    } else {
                        // TODO: logging
                        ctx.response().respondTo(res.cause())
                    }
                })
    }

    fun listExercise(ctx: RoutingContext) {
        val options = ctx.request().params().toListOptions()
        vertx.executeBlocking<Iterable<Exercise>>(
                { future ->
                    future.complete(listExercisesHandler.handle(ListExercises(options)))
                }, false, { res ->
                    if (res.succeeded()) {
                        ctx.response().end(Json.encode(res.result()))
                    } else {
                        // TODO: logging
                        ctx.response().respondTo(res.cause())
                    }
                }
        )
    }

    fun updateExercise(ctx: RoutingContext) {
        val updateCommand = try {
            Json.decodeValue(ctx.bodyAsString, UpdateExercise::class.java)
        } catch(e: DecodeException) {
            ctx.response().sendBadRequest(ErrorCode.BAD_BODY)
            return
        }
        updateCommand.id = try {
            UUID.fromString(ctx.request().getParam("id"))
        } catch (e: IllegalArgumentException) {
            ctx.response().setStatusCode(202).end()
            return
        }

        if (updateCommand.name.length == 0) {
            ctx.response().sendBadRequest(ErrorCode.BAD_BODY)
            return
        }

        vertx.executeBlocking<Result>(
                { future ->
                    future.complete(commandBus.dispatch(updateCommand))
                }, false, { res ->
                    if (res.succeeded()) {
                        val result = res.result()
                        when (result.status) {
                            Status.Ack -> ctx.response().setStatusCode(202).end()
                            Status.Nack -> ctx.response().sendBadRequest(ErrorCode.BAD_BODY, result.message)
                        }
                    } else {
                        // TODO: logging
                        ctx.response().respondTo(res.cause())
                    }
                }
        )
    }

    fun deleteExercise(ctx: RoutingContext) {
        val id = try {
            UUID.fromString(ctx.request().getParam("id"))
        } catch (e: IllegalArgumentException) {
            ctx.response().setStatusCode(202).end()
            return
        }

        vertx.executeBlocking<Result>(
                { future ->
                    future.complete(commandBus.dispatch(DeleteExercise(id)))
                }, false, { res ->
                    if (res.succeeded()) {
                        ctx.response().setStatusCode(202).end()
                    } else {
                        // TODO: logging
                        ctx.response().respondTo(res.cause())
                    }
                }
        )
    }

    /**
     * Applies exercise resource route handlers to a Vertx HTTP Router
     */
    fun applyTo(router: Router) {
        router.route("/exercise*").handler(BodyHandler.create())

        router
                .post("/exercise")
                .consumes("*/json")
                .handler { ctx -> createExercise(ctx) }

        router
                .get("/exercise/:id")
                .produces("application/json")
                .handler { ctx -> getExercise(ctx) }

        router
                .get("/exercises")
                .produces("application/json")
                .handler { ctx -> listExercise(ctx) }

        router
                .put("/exercise/:id")
                .consumes("*/json")
                .handler { ctx -> updateExercise(ctx) }

        router
                .delete("/exercise/:id")
                .handler { ctx -> deleteExercise(ctx) }
    }
}