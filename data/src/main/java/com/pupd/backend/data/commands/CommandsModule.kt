package com.pupd.backend.data.commands

import com.google.inject.AbstractModule
import com.google.inject.Provides
import com.pupd.backend.data.Database
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Guice DI module for commands
 *
 * Created by maxiaojun on 9/11/16.
 */
class CommandsModule() : AbstractModule() {
    override fun configure() {
        bind(CommandBus::class.java).to(DefaultCommandBus::class.java).`in`(Singleton::class.java)
    }

    @Singleton
    @Provides
    @Inject
    fun createExerciseHandler(db: Database): CommandHandler<CreateExercise> = CreateExerciseHandler(db)

    @Singleton
    @Provides
    @Inject
    fun updateExerciseHandler(db: Database): CommandHandler<UpdateExercise> = UpdateExerciseHandler(db)

    @Singleton
    @Provides
    @Inject
    fun deleteExerciseHandler(db: Database): CommandHandler<DeleteExercise> = DeleteExerciseHandler(db)
}
