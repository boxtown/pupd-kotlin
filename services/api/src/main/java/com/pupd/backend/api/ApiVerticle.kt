package com.pupd.backend.api

import com.google.inject.Guice
import com.google.inject.Injector
import com.pupd.backend.api.resources.ExerciseResource
import com.pupd.backend.api.resources.WorkoutResource
import com.pupd.backend.api.resources.register
import com.pupd.backend.api.resources.registerResourceModule
import io.vertx.core.AbstractVerticle
import io.vertx.core.Future
import io.vertx.core.json.Json
import io.vertx.ext.web.Router

/**
 * Verticle class to run the backend API
 *
 * Created by maxiaojun on 9/3/16.
 */
class ApiVerticle(private var injector: Injector? = null) : AbstractVerticle() {
    override fun start(startFuture: Future<Void>?) {
        if (injector == null) {
            injector = Guice.createInjector(ResourceModule(vertx))
        }
        Json.mapper.registerResourceModule()

        val router = Router.router(vertx)
        router.register(injector!!.getInstance(ExerciseResource::class.java))
        router.register(injector!!.getInstance(WorkoutResource::class.java))

        vertx
                .createHttpServer()
                .requestHandler { req -> router.accept(req) }
                .listen(config().getInteger(Config.HTTP_PORT, 8080)) {
                    res ->
                    when {
                        res.succeeded() -> startFuture?.complete()
                        else -> startFuture?.fail(res.cause())
                    }
                }
    }
}