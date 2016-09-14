package com.pupd.backend.data.queries

import com.pupd.backend.data.Database
import com.pupd.backend.data.entities.Exercise
import com.pupd.backend.data.entities.RepScheme
import com.pupd.backend.data.entities.Workout
import com.pupd.backend.data.generated.Tables
import com.pupd.backend.data.generated.tables.records.WorkoutExercisesRecord
import java.util.*
import javax.inject.Inject

/**
 * Query to retrieve a workout by id
 * Created by maxiaojun on 9/8/16.
 */
data class GetWorkout(val id: UUID)

class GetWorkoutHandler @Inject constructor(private val db: Database) :
        BaseQueryHandler<GetWorkout, Workout?>(),
        QueryHandler<GetWorkout, Workout?> {

    override fun handle(query: GetWorkout): Workout? {
        return db.query { ctx ->
            val workout = ctx.select()
                    .from(Tables.WORKOUTS)
                    .where(Tables.WORKOUTS.ID.equal(query.id))
                    .fetchOne()?.into(Workout::class.java) ?: return@query null

            val exercises: MutableMap<UUID, Exercise> = mutableMapOf()
            val sets: MutableMap<UUID, RepScheme> = mutableMapOf()
            val increments: MutableMap<UUID, Double> = mutableMapOf()
            ctx.select()
                    .from(Tables.WORKOUT_EXERCISES)
                    .innerJoin(Tables.EXERCISES)
                    .onKey()
                    .where(Tables.WORKOUT_EXERCISES.WORKOUT_ID.equal(query.id))
                    .map { record ->
                        record.into(WorkoutExercisesRecord::class.java) to record.into(Exercise::class.java)
                    }
                    .forEach { tuple ->
                        exercises.put(tuple.second.id!!, tuple.second)
                        sets.put(tuple.second.id!!, RepScheme(tuple.first.reps, tuple.first.weights))
                        increments.put(tuple.second.id!!, tuple.first.incr)
                    }

            workout.exercises = exercises
            workout.sets = sets
            workout.increments = increments
            workout
        }
    }
}