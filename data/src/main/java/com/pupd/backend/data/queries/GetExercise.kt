package com.pupd.backend.data.queries

import com.pupd.backend.data.Database
import com.pupd.backend.data.entities.Exercise
import com.pupd.backend.data.generated.Tables
import java.util.*
import javax.inject.Inject

/**
 * Query to retrieve an exercise by its id
 *
 * Created by maxiaojun on 9/2/16.
 */
data class GetExercise(val id: UUID)

/**
 * Query handler for get exercise query
 */
class GetExerciseHandler @Inject constructor(private val db: Database) :
        BaseQueryHandler<GetExercise, Exercise?>(),
        QueryHandler<GetExercise, Exercise?> {

    override fun handle(query: GetExercise): Exercise? = super.handle(query) { query ->
        db.query { ctx ->
            ctx.selectFrom(Tables.EXERCISES)
                    .where(Tables.EXERCISES.ID.equal(query.id))
                    .fetchOne()?.into(Exercise::class.java)
        }
    }
}