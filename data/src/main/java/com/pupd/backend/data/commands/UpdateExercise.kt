package com.pupd.backend.data.commands

import com.pupd.backend.data.Database
import com.pupd.backend.data.generated.Tables
import java.util.*
import javax.inject.Inject

/**
 * Command to update an exercise with the given id
 *
 * Created by maxiaojun on 9/3/16.
 */
data class UpdateExercise(var id: UUID? = null, var name: String = "")

/**
 * Command handler for update exercise command
 *
 * Created by maxiaojun on 9/7/16.
 */
class UpdateExerciseHandler @Inject constructor(private val db: Database) :
        BaseCommandHandler<UpdateExercise>(),
        CommandHandler<UpdateExercise> {

    override fun handle(command: UpdateExercise): Result {
        if (command.id == null || command.name.length == 0) {
            return Result(Status.Nack, "Invalid exercise")
        }

        return super.handle(command) { command ->
            db.exec { ctx ->
                val record = ctx.newRecord(Tables.EXERCISES)
                record.set(Tables.EXERCISES.ID, command.id)
                record.set(Tables.EXERCISES.NAME, command.name)
                record.update()
            }
            Result()
        }
    }
}