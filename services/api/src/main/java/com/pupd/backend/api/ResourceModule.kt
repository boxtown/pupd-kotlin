package com.pupd.backend.api

import com.google.inject.AbstractModule
import com.google.inject.name.Names
import com.pupd.backend.api.resources.ExerciseResource
import com.pupd.backend.api.resources.WorkoutResource
import com.pupd.backend.data.DataModule
import com.pupd.backend.data.postgres.PostgresDataSourceProvider
import com.pupd.backend.shared.EnvironmentType
import io.vertx.core.Vertx
import javax.inject.Singleton

/**
 * Guice DI module for API resources
 *
 * Created by maxiaojun on 9/3/16.
 */
class ResourceModule(private val vertx: Vertx) : AbstractModule() {
    private val env = System.getenv()

    override fun configure() {
        install(DataModule())

        val environmentType = EnvironmentType.fromString(env[Environment.ENVIRONMENT] ?: "")
        when (environmentType) {
            EnvironmentType.Production -> initializeProductionBindings()
            else -> initializeDevBindings()
        }
        initializeSharedBindings()
    }

    private fun initializeDevBindings() {
        bind(String::class.java)
                .annotatedWith(Names.named(PostgresDataSourceProvider.DB_USER_INJECTION))
                .toInstance("tester")
        bind(String::class.java)
                .annotatedWith(Names.named(PostgresDataSourceProvider.DB_PW_INJECTION))
                .toInstance("test")
    }

    private fun initializeProductionBindings() {
        bind(String::class.java)
                .annotatedWith(Names.named(PostgresDataSourceProvider.DB_USER_INJECTION))
                .toInstance(env[Environment.DB_USER])
        bind(String::class.java)
                .annotatedWith(Names.named(PostgresDataSourceProvider.DB_PW_INJECTION))
                .toInstance(env[Environment.DB_PW])
    }

    private fun initializeSharedBindings() {
        bind(Vertx::class.java).toInstance(vertx)
        bind(ExerciseResource::class.java).`in`(Singleton::class.java)
        bind(WorkoutResource::class.java).`in`(Singleton::class.java)
    }
}