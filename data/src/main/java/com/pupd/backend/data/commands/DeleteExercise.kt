package com.pupd.backend.data.commands

import com.pupd.backend.data.Database
import com.pupd.backend.data.generated.Tables
import java.util.*
import javax.inject.Inject

/**
 * Command to delete an exercise by id
 *
 * Created by maxiaojun on 9/3/16.
 */
data class DeleteExercise(var id: UUID? = null)

/**
 * Command handler for delete exercise command
 *
 * Created by maxiaojun on 9/7/16.
 */
class DeleteExerciseHandler @Inject constructor(private val db: Database) : CommandHandler<DeleteExercise> {
    override fun handle(command: DeleteExercise): Result {
        db.exec { ctx ->
            ctx.deleteFrom(Tables.EXERCISES).where(Tables.EXERCISES.ID.equal(command.id)).execute()
        }
        return Result()
    }
}