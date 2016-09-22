package com.pupd.backend.data.entities

import java.util.*

/**
 * Exercise entity data class
 *
 * Created by maxiaojun on 8/31/16.
 */
data class Exercise(var id: UUID? = null, var name: String = "")

/**
 * Workout set entity data class
 *
 * Created by maxiaojun on 9/21/16.
 */
data class WorkoutSet(var reps: Int = 0, var weight: Double = 0.0)

/**
 * Workout entity class
 *
 * Created by maxiaojun on 8/31/16.
 */
class Workout(
        var id: UUID? = null,
        var name: String = "",
        var exercises: Map<UUID, Exercise> = mapOf(),
        var sets: Map<UUID, Array<WorkoutSet>> = mapOf(),
        var increments: Map<UUID, Double> = mapOf()) {

    val isConsistent: Boolean
        get() {
            if (!exercises.all { e -> sets.containsKey(e.key) && increments.containsKey(e.key) }) {
                return false
            }
            return true
        }
}

/**
 * Workout cycle entity class
 *
 * Created by maxiaojun on 8/31/16.
 */
class Cycle(var workout: Workout? = null, var reps: Int = 0, var offset: Int = 0, var lengthInDays: Int = 0)

/**
 * Workout program entity class
 *
 * Created by maxiaojun on 8/31/16.
 */
class Program(var id: UUID? = null, var name: String = "", var cycles: Array<Cycle> = arrayOf())