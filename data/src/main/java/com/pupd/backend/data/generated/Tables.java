/**
 * This class is generated by jOOQ
 */
package com.pupd.backend.data.generated;


import com.pupd.backend.data.generated.tables.Exercises;
import com.pupd.backend.data.generated.tables.ProgramWorkouts;
import com.pupd.backend.data.generated.tables.Programs;
import com.pupd.backend.data.generated.tables.WorkoutExercises;
import com.pupd.backend.data.generated.tables.Workouts;

import javax.annotation.Generated;


/**
 * Convenience access to all tables in public
 */
@Generated(
    value = {
        "http://www.jooq.org",
        "jOOQ version:3.8.4"
    },
    comments = "This class is generated by jOOQ"
)
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class Tables {

    /**
     * The table <code>public.exercises</code>.
     */
    public static final Exercises EXERCISES = com.pupd.backend.data.generated.tables.Exercises.EXERCISES;

    /**
     * The table <code>public.program_workouts</code>.
     */
    public static final ProgramWorkouts PROGRAM_WORKOUTS = com.pupd.backend.data.generated.tables.ProgramWorkouts.PROGRAM_WORKOUTS;

    /**
     * The table <code>public.programs</code>.
     */
    public static final Programs PROGRAMS = com.pupd.backend.data.generated.tables.Programs.PROGRAMS;

    /**
     * The table <code>public.workout_exercises</code>.
     */
    public static final WorkoutExercises WORKOUT_EXERCISES = com.pupd.backend.data.generated.tables.WorkoutExercises.WORKOUT_EXERCISES;

    /**
     * The table <code>public.workouts</code>.
     */
    public static final Workouts WORKOUTS = com.pupd.backend.data.generated.tables.Workouts.WORKOUTS;
}
