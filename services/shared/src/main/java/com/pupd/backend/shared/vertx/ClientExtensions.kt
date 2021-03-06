package com.pupd.backend.shared.vertx

import io.vertx.core.buffer.Buffer
import io.vertx.core.http.HttpClient
import io.vertx.core.http.HttpClientResponse
import io.vertx.core.http.HttpMethod
import java.util.concurrent.CompletableFuture

/**
 * Wrapper function for HttpClient.doRequest(HttpMethod.GET, ...)
 *
 * @param cfg Request building logic
 */
fun <T> HttpClient.doGet(cfg: RequestBuilder<T>.() -> Unit): CompletableFuture<T>
        = this.doRequest(HttpMethod.GET, cfg)

/**
 * Wrapper function for HttpClient.doRequest(HttpMethod.POST, ...)
 *
 * @param cfg Request building logic
 */
fun <T> HttpClient.doPost(cfg: RequestBuilder<T>.() -> Unit): CompletableFuture<T>
        = this.doRequest(HttpMethod.POST, cfg)

/**
 * Wrapper function for HttpClient.doRequest(HttpMethod.PUT, ...)
 *
 * @param cfg Request building logic
 */
fun <T> HttpClient.doPut(cfg: RequestBuilder<T>.() -> Unit): CompletableFuture<T>
        = this.doRequest(HttpMethod.PUT, cfg)

/**
 * Wrapper function for HttpClient.doRequest(HttpMethod.DELETE, ...)
 *
 * @param cfg Request building logic
 */
fun <T> HttpClient.doDelete(cfg: RequestBuilder<T>.() -> Unit): CompletableFuture<T>
        = this.doRequest(HttpMethod.DELETE, cfg)

/**
 * Syntactic sugar function for performing requests on Vertx HttpClients
 * in a more Kotlin-builder-esque fashion
 *
 * @param method HTTP method for request
 * @param cfg Request building logic
 */
fun <T> HttpClient.doRequest(method: HttpMethod, cfg: RequestBuilder<T>.() -> Unit): CompletableFuture<T> {
    val builder = RequestBuilder<T>()
    builder.cfg()

    val promise = CompletableFuture<T>()
    val handler: (HttpClientResponse) -> Unit = { resp ->
        resp.exceptionHandler(builder.errorHandler)
        promise.complete(builder.handler(resp))
    }

    val req = when (method) {
        HttpMethod.GET -> {
            this.get(builder.port, builder.host, builder.uri, handler)
        }
        HttpMethod.POST -> {
            this.post(builder.port, builder.host, builder.uri, handler)
        }
        HttpMethod.PUT -> {
            this.put(builder.port, builder.host, builder.uri, handler)
        }
        HttpMethod.DELETE -> {
            this.delete(builder.port, builder.host, builder.uri, handler)
        }
        else -> throw UnsupportedOperationException()
    }
    req.exceptionHandler(builder.errorHandler)

    for ((k, v) in builder.headers) {
        req.putHeader(k, v)
    }
    if (method == HttpMethod.POST || method == HttpMethod.PUT) {
        if (builder.body.length() > 0) {
            req.putHeader("content-length", "${builder.body.length()}")
            req.write(builder.body)
        }
    }
    req.end()
    return promise
}

/**
 * Extension function for headers (Mutable map of strings to mutable list of strings)
 * that allows insertion of single value without wrapping in a mutable list
 *
 * @param key Header key
 * @param value Header value
 */
fun MutableMap<String, MutableList<String>>.put(key: String, value: String) {
    this[key]?.add(value) ?: this.put(key, mutableListOf(value))
}

/**
 * Class for building an HTTP client request
 *
 * Created by maxiaojun on 9/13/2016.
 */
class RequestBuilder<T> {
    /**
     * The target request port. Defaults to 8080
     */
    var port = 8080

    /**
     * The target host. Defaults to localhost
     */
    var host = "localhost"

    /**
     * The request body
     */
    internal var body: Buffer = Buffer.buffer()
        get
        private set

    /**
     * The relative URI. Built using the uri() method
     */
    internal var uri = ""
        get
        private set

    /**
     * Map of headers. Can be built in a Kotlin-esque builder fashion
     * using the headers() method
     */
    val headers: MutableMap<String, MutableList<String>> = mutableMapOf()

    /**
     * The response handler for the request
     */
    internal var handler: (HttpClientResponse) -> T? = { null }
        get
        private set

    /**
     * The error handler for both the request and the response
     */
    internal var errorHandler: (Throwable) -> Unit = { }
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
    fun headers(cfg: MutableMap<String, MutableList<String>>.() -> Unit) {
        headers.cfg()
    }

    /**
     * Builder method for request body. The builder works on a Vertx Buffer
     *
     * @param cfg Body building logic
     */
    fun body(cfg: Buffer.() -> Unit) {
        body.cfg()
    }

    /**
     * Setter method for handler. Allows setting of handler without resorting to
     * the object.property = syntax
     *
     * @param h Response handling function
     */
    fun handler(h: (HttpClientResponse) -> T) {
        handler = h
    }

    /**
     * Setter method for errorHandler. Allows setting of error handler
     * without resorting to the object.property = syntax
     *
     * @param h Error handling function
     */
    fun errorHandler(h: (Throwable) -> Unit) {
        errorHandler = h
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