package com.pupd.backend.data.entities

import java.util.*

/**
 * Exercise entity data class
 *
 * Created by maxiaojun on 8/31/16.
 */
data class Exercise(var id: UUID? = null, var name: String = "")

/**
 * RepScheme entity class
 *
 * Created by maxiaojun on 8/31/16.
 */
class RepScheme(var reps: Array<Int> = arrayOf(), val weights: Array<Double> = arrayOf()) {
    val isConsistent: Boolean
        get() = reps.size == weights.size
}

/**
 * Workout entity class
 *
 * Created by maxiaojun on 8/31/16.
 */
class Workout(
        var id: UUID? = null,
        var name: String = "",
        var exercises: Map<UUID, Exercise> = mapOf(),
        var sets: Map<UUID, RepScheme> = mapOf(),
        var increments: Map<UUID, Double> = mapOf()) {

    val isConsistent: Boolean
        get() {
            if (!exercises.all { e -> sets.containsKey(e.key) && increments.containsKey(e.key) }) {
                return false
            }
            if (!sets.all { s -> s.value.isConsistent }) {
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
class Cycle(var id: UUID? = null, var workouts: Array<Workout> = arrayOf())

/**
 * Workout program entity class
 *
 * Created by maxiaojun on 8/31/16.
 */
class Program(var id: UUID? = null, var name: String = "", var cycles: Array<Cycle> = arrayOf())