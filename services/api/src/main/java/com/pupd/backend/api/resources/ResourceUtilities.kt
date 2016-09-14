package com.pupd.backend.api.resources

import com.fasterxml.jackson.core.JsonGenerator
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializerProvider
import com.fasterxml.jackson.databind.module.SimpleModule
import com.fasterxml.jackson.databind.ser.std.StdSerializer
import com.pupd.backend.data.UniquenessConstraintViolationException
import com.pupd.backend.data.queries.ListOptions
import io.vertx.core.MultiMap
import io.vertx.core.http.HttpServerResponse
import io.vertx.core.json.Json

/**
 * Registers a JSON module on a Jackson ObjectMapper for resources
 */
fun ObjectMapper.registerResourceModule() {
    val module = SimpleModule()
    module.addSerializer(ErrorCodeSerializer())
    this.registerModule(module)
}

/**
 * Maps query parameters to a ListOptions object
 */
fun MultiMap.toListOptions(): ListOptions {
    val options = ListOptions()
    try {
        options.offset = this["offset"]?.toInt() ?: options.offset
    } catch (e: NumberFormatException) { /* don't care if it doesn't parse */
    }
    try {
        options.limit = this["limit"]?.toInt() ?: options.limit
    } catch (e: NumberFormatException) { /* don't care if it doesn't parse */
    }
    options.sort = this["sort"] ?: options.sort
    options.desc = this["desc"]?.toBoolean() ?: options.desc
    return options
}

/**
 * Enumeration of possible API error codes
 *
 * Created by maxiaojun on 9/2/16
 */
internal enum class ErrorCode(val code: Int) {
    BAD_BODY(400) {
        override fun toHttpCode(): Int = 400
        override fun message(): String = "Bad request body"
    },
    NOT_FOUND(404) {
        override fun toHttpCode(): Int = 404
        override fun message(): String = "Entity not found"
    },
    SERVICE_UNAVAILABLE(503) {
        override fun toHttpCode(): Int = 503
        override fun message(): String = "Service unavailable"
    };

    /**
     * Returns HTTP error code associated with API error code
     */
    abstract fun toHttpCode(): Int

    /**
     * Returns message associated with API errorcode
     */
    abstract fun message(): String
}

/**
 * Custom serializer for ErrorCode enum that serializes using integer code
 *
 * Created by maxiaojun on 9/3/16
 */
internal class ErrorCodeSerializer : StdSerializer<ErrorCode>(ErrorCode::class.java) {
    override fun serialize(value: ErrorCode?, gen: JsonGenerator?, provider: SerializerProvider?) {
        gen?.writeNumber(value!!.code)
    }
}

/**
 * API error response data class. Used to send a JSON error response
 *
 * Created by maxiaojun on 9/2/16.
 */
internal data class ErrorResponse(
        val code: ErrorCode = ErrorCode.SERVICE_UNAVAILABLE,
        val message: String = ErrorCode.SERVICE_UNAVAILABLE.message())

/**
 * Sends an appropriate response given the exception.
 */
internal fun HttpServerResponse.respondTo(t: Throwable) {
    when (t) {
        is UniquenessConstraintViolationException -> {
            this
                    .setStatusCode(ErrorCode.BAD_BODY.toHttpCode())
                    .end(Json.encode(ErrorResponse(ErrorCode.BAD_BODY, ErrorCode.BAD_BODY.message())))
        }
        else -> {
            this
                    .setStatusCode(ErrorCode.SERVICE_UNAVAILABLE.toHttpCode())
                    .end(Json.encode(ErrorResponse(ErrorCode.SERVICE_UNAVAILABLE, ErrorCode.SERVICE_UNAVAILABLE.message())))
        }
    }
}

/**
 * Sends a 400 HTTP Bad Request response with an ErrorResponse containing the given
 * ErrorCode and message. Message defaults to the ErrorCode's message
 */
internal fun HttpServerResponse.sendBadRequest(code: ErrorCode, message: String? = null) {
    this.setStatusCode(400).end(Json.encode(ErrorResponse(code, message ?: code.message())))
}

/**
 * Sends a 404 HTTP Bad Request response with an ErrorResponse containing the given
 * ErrorCode and message. ErrorCode defaults to NOT_FOUND. Message defaults to the ErrorCode's message
 */
internal fun HttpServerResponse.sendNotFound(code: ErrorCode = ErrorCode.NOT_FOUND, message: String? = null) {
    this.setStatusCode(404).end(Json.encode(ErrorResponse(code, message ?: code.message())))
}