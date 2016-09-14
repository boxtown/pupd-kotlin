package com.pupd.backend.data.commands

import com.google.inject.ConfigurationException
import com.google.inject.Injector
import com.google.inject.Key
import com.google.inject.util.Types
import com.pupd.backend.data.DataException
import javax.inject.Inject

/**
 * Default command bus implementation
 *
 * Created by maxiaojun on 9/7/16.
 */
class DefaultCommandBus @Inject constructor(private val injector: Injector): CommandBus {

    override fun <T: Any> dispatch(command: T): Result {
        val commandClass = command.javaClass
        val handler = try {
            injector.getInstance(Key.get(Types.newParameterizedType(CommandHandler::class.java, commandClass)))
        } catch (e: ConfigurationException) {
            throw DataException("No defined handler for command")
        }

        @Suppress("UNCHECKED_CAST")
        return (handler as CommandHandler<T>).handle(command)
    }
}