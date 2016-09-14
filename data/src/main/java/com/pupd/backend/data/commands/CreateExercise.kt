package com.pupd.backend.data.commands

import com.pupd.backend.data.Database
import com.pupd.backend.data.generated.Tables
import java.util.*
import javax.inject.Inject

/**
 * Command for creating an exercise with a given id and name
 *
 * Created by maxiaojun on 9/1/16.
 */
data class CreateExercise(var id: UUID? = null, var name: String = "") {
}

/**
 * Command handler for create exercise command
 *
 * Created by maxiaojun on 9/7/2016.
 */
class CreateExerciseHandler @Inject constructor(private val db: Database) :
        BaseCommandHandler<CreateExercise>(),
        CommandHandler<CreateExercise> {

    override fun handle(command: CreateExercise): Result {
        if (command.id == null || command.name.length == 0) {
            return Result(Status.Nack, "Invalid command")
        }

        return super.handle(command) { command ->
            db.exec { ctx ->
                ctx.insertInto(Tables.EXERCISES).values(command.id, command.name).execute()
            }
            Result()
        }
    }
}