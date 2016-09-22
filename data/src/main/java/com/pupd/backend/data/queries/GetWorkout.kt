package com.pupd.backend.data.queries

import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.ObjectMapper
import com.pupd.backend.data.Database
import com.pupd.backend.data.entities.Exercise
import com.pupd.backend.data.entities.Workout
import com.pupd.backend.data.entities.WorkoutSet
import com.pupd.backend.data.generated.Tables
import com.pupd.backend.data.generated.tables.records.ExercisesRecord
import com.pupd.backend.data.generated.tables.records.WorkoutExercisesRecord
import java.util.*
import javax.inject.Inject

/**
 * Query to retrieve a workout by id
 * Created by maxiaojun on 9/8/16.
 */
data class GetWorkout(val id: UUID)

class GetWorkoutHandler @Inject constructor(private val db: Database, private val mapper: ObjectMapper) :
        QueryHandler<GetWorkout, Workout?> {

    override fun handle(query: GetWorkout): Workout? {
        return db.query { ctx ->
            val workout = ctx.select()
                    .from(Tables.WORKOUTS)
                    .where(Tables.WORKOUTS.ID.equal(query.id))
                    .fetchOne()?.into(Workout::class.java) ?: return@query null

            val exercises: MutableMap<UUID, Exercise> = mutableMapOf()
            val sets: MutableMap<UUID, Array<WorkoutSet>> = mutableMapOf()
            val increments: MutableMap<UUID, Double> = mutableMapOf()
            ctx.select().from(Tables.WORKOUT_EXERCISES).join(Tables.EXERCISES).onKey().fetch()
                    .map { record ->
                        record.into(WorkoutExercisesRecord::class.java) to record.into(ExercisesRecord::class.java)
                    }
                    .forEach { pair ->
                        exercises.put(pair.second.id, Exercise(pair.second.id, pair.second.name))
                        sets.put(pair.second.id, mapper.readValue(pair.first.sets,
                                                                  object : TypeReference<Array<WorkoutSet>>() {}))
                        increments.put(pair.second.id, pair.first.incr)
                    }

            workout.exercises = exercises
            workout.sets = sets
            workout.increments = increments
            workout
        }
    }
}