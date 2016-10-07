package com.pupd.backend.api.resources

import com.google.inject.AbstractModule
import com.pupd.backend.data.DataTestModule
import io.vertx.core.Vertx
import io.vertx.core.json.Json
import javax.inject.Singleton

/**
 * Test Guice DI module for resources
 *
 * Created by maxiaojun on 9/12/16.
 */
class ResourceTestModule(private val vertx: Vertx) : AbstractModule() {
    override fun configure() {
        install(DataTestModule(Json.mapper))

        bind(Vertx::class.java).toInstance(vertx)
        bind(ExerciseResource::class.java).`in`(Singleton::class.java)
        bind(WorkoutResource::class.java).`in`(Singleton::class.java)
    }
}
