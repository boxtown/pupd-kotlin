package com.pupd.backend.data.queries

import com.pupd.backend.data.Database
import com.pupd.backend.data.entities.Exercise
import com.pupd.backend.data.generated.Tables
import javax.inject.Inject

/**
 * Query to list exercises
 *
 * Created by maxiaojun on 9/3/16.
 */
data class ListExercises(val options: ListOptions)

/**
 * Query handler for list exercises query
 *
 * Created by maxiaojun on 9/7/16.
 */
class ListExercisesHandler @Inject constructor(private val db: Database) :
        QueryHandler<ListExercises, Iterable<Exercise>> {

    override fun handle(query: ListExercises): Iterable<Exercise> =
            db.query { ctx ->
                val field = Tables.EXERCISES.field(query.options.sort)
                val sqlQuery = ctx.selectFrom(Tables.EXERCISES).query

                when {
                    query.options.desc -> sqlQuery.addOrderBy(field?.desc() ?: Tables.EXERCISES.ID.desc())
                    else -> sqlQuery.addOrderBy(field?.asc() ?: Tables.EXERCISES.ID.asc())
                }

                val offset = if (query.options.offset < 0) 0 else query.options.offset
                when {
                    query.options.limit > 0 -> sqlQuery.addLimit(offset, query.options.limit)
                    else -> sqlQuery.addOffset(offset)
                }
                sqlQuery.fetch().into(Exercise::class.java)
            }
}