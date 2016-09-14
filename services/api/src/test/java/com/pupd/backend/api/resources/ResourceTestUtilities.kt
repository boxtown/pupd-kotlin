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
 * @param scriptPaths Relative paths to H2 SQL Scripts to run on init
 * @param verticleProvider Provides a verticle given a Guice injector for dependencies
 */
internal fun Vertx.deployTestEnvironment(
        ctx: TestContext,
        vararg scriptPaths: String,
        verticleProvider: (Injector) -> AbstractVerticle) {

    val injector = Guice.createInjector(TestResourceModule(this))
    this.deployVerticle(
            verticleProvider(injector),
            ctx.asyncAssertSuccess())

    val ds = injector.getInstance(DataSource::class.java)
    ds.runSetupScripts(*scriptPaths)
}

/**
 * Runs a series of scripts identified by scriptPaths on the DataSource.
 * Scripts must be SQL scripts with valid H2 syntax.
 *
 * @param scriptPaths Relative paths to SQL scripts
 */
internal fun DataSource.runSetupScripts(vararg scriptPaths: String) {
    var conn: Connection? = null
    try {
        conn = this.connection
        scriptPaths.forEach { path ->
            FileReader(path).use { reader ->
                RunScript.execute(conn, reader)
            }
        }
    } finally {
        conn?.close()
    }
}