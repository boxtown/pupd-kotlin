package com.pupd.backend.api

import io.vertx.core.DeploymentOptions
import io.vertx.core.json.JsonObject

/**
 * Configuration key constants
 *
 * Created by maxiaojun on 9/4/16.
 */
class Config {
    companion object {
        const val HTTP_PORT = "http.port"
    }
}

class Environment {
    companion object {
        const val ENVIRONMENT = "PUPD_ENV"
        const val DB_USER = "PUPD_DB_USER"
        const val DB_PW = "PUPD_DB_PW"
    }
}

/**
 * Kotlin grammatic sugar for building deployment options
 */
internal fun DeploymentOptions.build(builder: DeploymentOptions.() -> Unit): DeploymentOptions {
    this.builder()
    return this
}

/**
 * Kotlin grammatic sugar for building config JSON objects
 */
internal fun JsonObject.build(builder: JsonObject.() -> Unit): JsonObject {
    this.builder()
    return this
}