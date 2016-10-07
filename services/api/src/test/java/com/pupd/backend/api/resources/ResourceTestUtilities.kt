package com.pupd.backend.api.resources

import com.google.inject.Guice
import com.google.inject.Injector
import io.vertx.core.AbstractVerticle
import io.vertx.core.Vertx
import io.vertx.ext.unit.TestContext
import org.h2.tools.RunScript
import java.io.FileReader
import java.sql.Connection
import javax.sql.DataSource

/**
 * Collection of resource testing utilities
 *
 * Created by maxiaojun on 9/12/16.
 */

/**
 * Deploys a verticle with the proper test dependencies injected
 * and runs the provided H2 SQL init scripts
 *
 * @param ctx The Vertx runner testing context
 * @param verticleProvider Provides a verticle given a Guice injector for dependencies
 */
internal fun Vertx.deployTestEnvironment(ctx: TestContext, verticleProvider: (Injector) -> AbstractVerticle) {
    val injector = Guice.createInjector(TestResourceModule(this))
    this.deployVerticle(verticleProvider(injector), ctx.asyncAssertSuccess())
}