package com.pupd.backend.data.commands

import com.google.inject.AbstractModule
import com.google.inject.Provides
import javax.inject.Singleton

/**
 * Guice DI exercise command module for testing
 *
 * Created by maxiaojun on 10/6/16.
 */
class ExerciseCommandsTestModule : AbstractModule() {
    override fun configure() {
    }

    @Suppress("UNUSED")
    @Singleton
    @Provides
    fun createExerciseHandler(): CommandHandler<CreateExercise> =
            object : CommandHandler<CreateExercise> {
                override fun handle(command: CreateExercise): Result =
                        when {
                            command.id == null || command.name.length == 0 -> Result(Status.Nack)
                            else -> Result()
                        }
            }

    @Suppress("UNUSED")
    @Singleton
    @Provides
    fun updateExerciseHandler(): CommandHandler<UpdateExercise> =
            object : CommandHandler<UpdateExercise> {
                override fun handle(command: UpdateExercise): Result =
                        when {
                            command.name.length == 0 -> Result(Status.Nack)
                            else -> Result()
                        }
            }

    @Suppress("UNUSED")
    @Singleton
    @Provides
    fun deleteExerciseHandler(): CommandHandler<DeleteExercise> =
            object : CommandHandler<DeleteExercise> {
                override fun handle(command: DeleteExercise): Result = Result()
            }
}