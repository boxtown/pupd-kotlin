package com.pupd.backend.data.entities

import java.util.*

/**
 * Exercise entity data class
 *
 * Created by maxiaojun on 8/31/16.
 */
data class Exercise(var id: UUID = EmptyUUID.INSTANCE, var name: String = "")

/**
 * Workout set entity data class
 *
 * Created by maxiaojun on 9/21/16.
 */
data class WorkoutSet(var reps: Int, var weight: Double)

/**
 * Workout exercise entity data class
 *
 * Created by maxiaojun on 10/4/16.
 */
data class WorkoutExercise(
        var exercise: Exercise,
        var sets: List<WorkoutSet> = listOf(),
        var increment: Double = 0.0)
/**
 * Workout entity class
 *
 * Created by maxiaojun on 8/31/16.
 */
data class Workout(
        var id: UUID = EmptyUUID.INSTANCE,
        var name: String = "",
        var exercises: Map<UUID, WorkoutExercise> = mapOf())

/**
 * Workout cycle entity class
 *
 * Created by maxiaojun on 8/31/16.
 */
data class Cycle(
        var workout: Workout,
        var reps: Int,
        var offset: Int,
        var lengthInDays: Int)

/**
 * Workout program entity class
 *
 * Created by maxiaojun on 8/31/16.
 */
data class Program(
        var id: UUID = EmptyUUID.INSTANCE,
        var name: String = "",
        var cycles: List<Cycle> = listOf())