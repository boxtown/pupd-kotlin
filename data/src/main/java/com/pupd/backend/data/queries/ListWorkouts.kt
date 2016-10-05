package com.pupd.backend.data.queries

/**
 * Query to list workouts
 *
 * Created by maxiaojun on 10/4/16.
 */
data class ListWorkouts(val options: ListOptions)

/*
class ListWorkoutsHandler @Inject constructor(private val db: Database) :
        QueryHandler<ListWorkouts, Iterable<Workout>> {

    override fun handle(query: ListWorkouts): Iterable<Workout> {
        db.query { ctx ->
            val field = Tables.WORKOUTS.field(query.options.sort)
            val sqlQuery = ctx.selectFrom(Tables.WORKOUTS).query

            when {
                query.options.desc -> sqlQuery.addOrderBy(field?.desc() ?: Tables.WORKOUTS.ID.desc())
                else -> sqlQuery.addOrderBy(field?.asc() ?: Tables.WORKOUTS.ID.asc())
            }

            val offset = if (query.options.offset < 0) 0 else query.options.offset
            when {
                query.options.limit > 0 -> sqlQuery.addLimit(offset, query.options.limit)
                else -> sqlQuery.addOffset(offset)
            }
            val workouts = sqlQuery.fetch().into(Workout::class.java)
            val workoutIds = workouts.map { w -> w.id }
            ctx.selectFrom(Tables.WORKOUT_EXERCISES)
                    .where(Tables.WORKOUT_EXERCISES.WORKOUT_ID.`in`(workoutIds))
                    .fetch()
                    .groupBy { record -> record.workoutId }
        }
    }
} */
