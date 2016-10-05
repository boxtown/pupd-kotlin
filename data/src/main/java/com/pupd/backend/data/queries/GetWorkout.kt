package com.pupd.backend.data.queries

import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.ObjectMapper
import com.pupd.backend.data.Database
import com.pupd.backend.data.entities.Exercise
import com.pupd.backend.data.entities.Workout
import com.pupd.backend.data.entities.WorkoutExercise
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

            val exercises: MutableMap<UUID, WorkoutExercise> = mutableMapOf()
            ctx.select()
                    .from(Tables.WORKOUT_EXERCISES)
                    .join(Tables.EXERCISES).onKey()
                    .where(Tables.WORKOUT_EXERCISES.WORKOUT_ID.equal(query.id))
                    .fetch()
                    .map { record ->
                        record.into(WorkoutExercisesRecord::class.java) to record.into(ExercisesRecord::class.java)
                    }
                    .forEach { pair ->
                        val typeRef = object : TypeReference<List<WorkoutSet>>() {}
                        exercises.put(pair.first.exerciseId, WorkoutExercise(
                                pair.second.into(Exercise::class.java),
                                mapper.readValue(pair.first.sets, typeRef),
                                pair.first.incr
                        ))
                    }

            workout.exercises = exercises
            workout
        }
    }
}