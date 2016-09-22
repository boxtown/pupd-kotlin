package com.pupd.backend.data.commands

/**
 * Enumeration of possible command handler statuses
 *
 * Created by maxiaojun on 9/1/16.
 */
enum class Status {
    Ack, Nack
}

/**
 * Data class for command handler results
 *
 * Created by maxiaojun on 9/1/16.
 */
data class Result(val status: Status = Status.Ack, val message: String = "")

/**
 * Interface for command bus dispatcher
 *
 * Created by maxiaojun on 9/1/16.
 */
interface CommandBus {
    fun <T: Any> dispatch(command: T): Result
}

/**
 * Interface for command handler
 *
 * Created by maxiaojun on 9/7/2016.
 */
interface CommandHandler<in T> {
    fun handle(command: T): Result
}