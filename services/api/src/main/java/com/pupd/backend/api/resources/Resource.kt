package com.pupd.backend.api.resources

import io.vertx.ext.web.Router

/**
 * An interface for Vertx REST resources
 *
 * Created by maxiaojun on 9/20/16.
 */
interface Resource {
    fun applyTo(router: Router)
}

/**
 * Registers a resource to the router by calling
 * the resource's applyTo function
 *
 * @param resource The resource to register
 */
fun Router.register(resource: Resource) {
    resource.applyTo(this)
}
