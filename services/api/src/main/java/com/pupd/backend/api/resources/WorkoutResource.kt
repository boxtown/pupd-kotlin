package com.pupd.backend.api.resources

import com.pupd.backend.data.commands.CommandBus
import com.pupd.backend.data.entities.Workout
import com.pupd.backend.data.queries.GetWorkout
import com.pupd.backend.data.queries.QueryHandler
import io.vertx.core.Vertx
import io.vertx.core.json.Json
import io.vertx.ext.web.Router
import io.vertx.ext.web.RoutingContext
import io.vertx.ext.web.handler.BodyHandler
import java.util.*
import javax.inject.Inject

/**
 * Workout REST API resource
 *
 * Created by maxiaojun on 9/12/16.
 */
@JvmSuppressWildcards
class WorkoutResource @Inject constructor(
        private val vertx: Vertx,
        private val commandBus: CommandBus,
        private val getWorkoutHandler: QueryHandler<GetWorkout, Workout?>) {

    fun getWorkout(ctx: RoutingContext) {
        val id = try {
            UUID.fromString(ctx.request().getParam("id"))
        } catch(e: IllegalArgumentException) {
            ctx.response().sendNotFound()
            return
        }

        vertx.executeBlocking<Workout?>(
                { future ->
                    future.complete(getWorkoutHandler.handle(GetWorkout(id)))
                }, false, { res ->
                    if (res.succeeded()) {
                        val workout = res.result()
                        if (workout == null) {
                            ctx.response().sendNotFound()
                        } else {
                            ctx.response().end(Json.encode(workout))
                        }
                    } else {
                        // TODO: logging
                        ctx.response().respondTo(res.cause())
                    }
                })
    }

    /**
     * Applies exercise resource route handlers to a Vertx HTTP Router
     */
    fun applyTo(router: Router) {
        router.route("/workout*").handler(BodyHandler.create())

        router
                .get("/workout/:id")
                .produces("application/json")
                .handler { ctx -> getWorkout(ctx) }
    }
}