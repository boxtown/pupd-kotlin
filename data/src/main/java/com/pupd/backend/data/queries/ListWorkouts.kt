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
import javax.inject.Inject

/**
 * Query to list workouts
 *
 * Created by maxiaojun on 10/4/16.
 */
data class ListWorkouts(val options: ListOptions)

/**
 * Query handler for list workouts query
 *
 * Created by maxiaojun on 10/7/16
 */
class ListWorkoutsHandler @Inject constructor(private val db: Database, private val mapper: ObjectMapper) :
        QueryHandler<ListWorkouts, Iterable<Workout>> {

    override fun handle(query: ListWorkouts): Iterable<Workout> =
            db.query { ctx ->
                val setsRef = object : TypeReference<List<WorkoutSet>>() {}
                val workouts = ctx.selectFrom(Tables.WORKOUTS).fetch().into(Workout::class.java)
                val ids = workouts.map { workout -> workout.id }
                val exercises = ctx.select()
                        .from(Tables.WORKOUT_EXERCISES)
                        .join(Tables.EXERCISES).onKey()
                        .where(Tables.WORKOUT_EXERCISES.WORKOUT_ID.`in`(ids))
                        .fetch()
                        .groupBy { record -> record.get(Tables.WORKOUT_EXERCISES.WORKOUT_ID) }

                workouts.forEach { workout ->
                    val records = exercises[workout.id]
                    workout.exercises = records?.map { r ->
                        r.into(ExercisesRecord::class.java) to r.into(WorkoutExercisesRecord::class.java)
                    }?.associateBy({ pair -> pair.first.id }, { pair ->
                        WorkoutExercise(
                                pair.first.into(Exercise::class.java),
                                mapper.readValue(pair.second.sets, setsRef),
                                pair.second.incr
                        )
                    }) ?: workout.exercises
                }
                workouts
            }
}