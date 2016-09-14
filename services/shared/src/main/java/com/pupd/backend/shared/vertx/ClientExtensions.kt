package com.pupd.backend.shared.vertx

import io.vertx.core.http.HttpClient
import io.vertx.core.http.HttpClientResponse
import io.vertx.core.http.HttpMethod

/**
 * Extension syntatic sugar function for performing requests on Vertx HttpClients
 * in a more Kotlin-builder-esque fashion
 *
 * @param method HTTP method for request
 * @param cfg Request building logic
 */
fun HttpClient.doRequest(method: HttpMethod, cfg: RequestBuilder.() -> Unit) {
    val builder = RequestBuilder()
    builder.cfg()
    when (method) {
        HttpMethod.GET -> {
            val req = this.get(builder.port, builder.host, builder.uri, builder.handler)
            for ((k, v) in builder.headers) {
                req.putHeader(k, v)
            }
            req.end()
        }
        HttpMethod.PUT -> {}
        HttpMethod.POST -> {}
        HttpMethod.DELETE -> {}
        else -> {}
    }
}

/**
 * Class for building an HTTP client request
 *
 * Created by maxiaojun on 9/13/2016.
 */
class RequestBuilder {
    /**
     * The target request port. Defaults to 8080
     */
    var port = 8080

    /**
     * The target host. Defaults to localhost
     */
    var host = "localhost"

    /**
     * The relative URI. Built using the uri() method
     */
    var uri = ""
        get
        private set

    /**
     * Map of headers. Can be built in a Kotlin-esque builder fashion
     * using the headers() method
     */
    val headers: MutableMap<String, Iterable<String>> = mutableMapOf()

    /**
     * The response handler for the request
     */
    var handler: (HttpClientResponse) -> Unit = { }
        get
        private set

    /**
     * Builder method for the relative URI. The builder works on the
     * RelativeUriBuilder class
     *
     * @param cfg Uri building logic
     */
    fun uri(cfg: RelativeUriBuilder.() -> Unit) {
        val builder = RelativeUriBuilder()
        builder.cfg()
        uri = builder.build()
    }

    /**
     * Builder method for request headers. The builder works on the headers MutableMap.
     *
     * @param cfg Headers building logic
     */
    fun headers(cfg: MutableMap<String, Iterable<String>>.() -> Unit) {
        headers.cfg()
    }

    /**
     * Setter method for handler. Allows setting of handler without resorting to
     * the object.property = syntax
     *
     * @param h Response handling function
     */
    fun handler(h: (HttpClientResponse) -> Unit) {
        handler = h
    }
}

/**
 * Class for building an HTTP request relative URI
 *
 * Created by maxiaojun on 9/13/2016.
 */
class RelativeUriBuilder {
    private val params: MutableMap<String, Any> = mutableMapOf()
    private val builder: StringBuilder = StringBuilder()

    /**
     * Appends the path segment on to the builder. Segments are appended
     * in order of calls and are separated with a '/'. E.g. path('a') path('b')
     * results in '/a/b'.
     *
     * @param path Path segment to append
     */
    fun path(path: String) {
        builder.append('/').append(path)
    }

    /**
     * Query registers a key value pair in the query string.
     * For example, query('a', 'b') results in 'a=b' being appended to the query string.
     *
     * @param key The query key
     * @param value The query value
     */
    fun query(key: String, value: Any) {
        params.put(key, value)
    }

    /**
     * Query registers a key value pair in the query string.
     * For example, query('a' to 'b') results in 'a=b' being appended to the query string.
     *
     * @param pair Key value pair where the first item is the key and the second is the value
     */
    fun query(pair: Pair<String, Any>) {
        params.put(pair.first, pair.second)
    }

    /**
     * Builds a relative URI string from the current state of the builder
     *
     * @return Relative URI as string
     */
    fun build(): String {
        if (params.size > 0) {
            builder.append('?')
            builder.append(params.map { entry ->
                entry.key + '=' + entry.value.toString()
            }.joinToString("&"))
        }
        return builder.toString()
    }
}