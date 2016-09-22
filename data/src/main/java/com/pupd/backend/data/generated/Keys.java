/**
 * This class is generated by jOOQ
 */
package com.pupd.backend.data.generated;


import com.pupd.backend.data.generated.tables.Exercises;
import com.pupd.backend.data.generated.tables.ProgramWorkouts;
import com.pupd.backend.data.generated.tables.Programs;
import com.pupd.backend.data.generated.tables.WorkoutExercises;
import com.pupd.backend.data.generated.tables.Workouts;
import com.pupd.backend.data.generated.tables.records.ExercisesRecord;
import com.pupd.backend.data.generated.tables.records.ProgramWorkoutsRecord;
import com.pupd.backend.data.generated.tables.records.ProgramsRecord;
import com.pupd.backend.data.generated.tables.records.WorkoutExercisesRecord;
import com.pupd.backend.data.generated.tables.records.WorkoutsRecord;

import javax.annotation.Generated;

import org.jooq.ForeignKey;
import org.jooq.UniqueKey;
import org.jooq.impl.AbstractKeys;


/**
 * A class modelling foreign key relationships between tables of the <code>public</code> 
 * schema
 */
@Generated(
    value = {
        "http://www.jooq.org",
        "jOOQ version:3.8.4"
    },
    comments = "This class is generated by jOOQ"
)
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class Keys {

    // -------------------------------------------------------------------------
    // IDENTITY definitions
    // -------------------------------------------------------------------------


    // -------------------------------------------------------------------------
    // UNIQUE and PRIMARY KEY definitions
    // -------------------------------------------------------------------------

    public static final UniqueKey<ExercisesRecord> EXERCISES_PKEY = UniqueKeys0.EXERCISES_PKEY;
    public static final UniqueKey<ExercisesRecord> EXERCISES_NAME_KEY = UniqueKeys0.EXERCISES_NAME_KEY;
    public static final UniqueKey<ProgramWorkoutsRecord> PROGRAM_WORKOUTS_PKEY = UniqueKeys0.PROGRAM_WORKOUTS_PKEY;
    public static final UniqueKey<ProgramsRecord> PROGRAMS_PKEY = UniqueKeys0.PROGRAMS_PKEY;
    public static final UniqueKey<ProgramsRecord> PROGRAMS_NAME_KEY = UniqueKeys0.PROGRAMS_NAME_KEY;
    public static final UniqueKey<WorkoutExercisesRecord> WORKOUT_EXERCISES_PKEY = UniqueKeys0.WORKOUT_EXERCISES_PKEY;
    public static final UniqueKey<WorkoutsRecord> WORKOUTS_PKEY = UniqueKeys0.WORKOUTS_PKEY;
    public static final UniqueKey<WorkoutsRecord> WORKOUTS_NAME_KEY = UniqueKeys0.WORKOUTS_NAME_KEY;

    // -------------------------------------------------------------------------
    // FOREIGN KEY definitions
    // -------------------------------------------------------------------------

    public static final ForeignKey<ProgramWorkoutsRecord, ProgramsRecord> PROGRAM_WORKOUTS__PROGRAM_WORKOUTS_PROGRAM_ID_FKEY = ForeignKeys0.PROGRAM_WORKOUTS__PROGRAM_WORKOUTS_PROGRAM_ID_FKEY;
    public static final ForeignKey<WorkoutExercisesRecord, WorkoutsRecord> WORKOUT_EXERCISES__WORKOUT_EXERCISES_WORKOUT_ID_FKEY = ForeignKeys0.WORKOUT_EXERCISES__WORKOUT_EXERCISES_WORKOUT_ID_FKEY;
    public static final ForeignKey<WorkoutExercisesRecord, ExercisesRecord> WORKOUT_EXERCISES__WORKOUT_EXERCISES_EXERCISE_ID_FKEY = ForeignKeys0.WORKOUT_EXERCISES__WORKOUT_EXERCISES_EXERCISE_ID_FKEY;

    // -------------------------------------------------------------------------
    // [#1459] distribute members to avoid static initialisers > 64kb
    // -------------------------------------------------------------------------

    private static class UniqueKeys0 extends AbstractKeys {
        public static final UniqueKey<ExercisesRecord> EXERCISES_PKEY = createUniqueKey(Exercises.EXERCISES, "exercises_pkey", Exercises.EXERCISES.ID);
        public static final UniqueKey<ExercisesRecord> EXERCISES_NAME_KEY = createUniqueKey(Exercises.EXERCISES, "exercises_name_key", Exercises.EXERCISES.NAME);
        public static final UniqueKey<ProgramWorkoutsRecord> PROGRAM_WORKOUTS_PKEY = createUniqueKey(ProgramWorkouts.PROGRAM_WORKOUTS, "program_workouts_pkey", ProgramWorkouts.PROGRAM_WORKOUTS.PROGRAM_ID, ProgramWorkouts.PROGRAM_WORKOUTS.WORKOUT_ID);
        public static final UniqueKey<ProgramsRecord> PROGRAMS_PKEY = createUniqueKey(Programs.PROGRAMS, "programs_pkey", Programs.PROGRAMS.ID);
        public static final UniqueKey<ProgramsRecord> PROGRAMS_NAME_KEY = createUniqueKey(Programs.PROGRAMS, "programs_name_key", Programs.PROGRAMS.NAME);
        public static final UniqueKey<WorkoutExercisesRecord> WORKOUT_EXERCISES_PKEY = createUniqueKey(WorkoutExercises.WORKOUT_EXERCISES, "workout_exercises_pkey", WorkoutExercises.WORKOUT_EXERCISES.ID);
        public static final UniqueKey<WorkoutsRecord> WORKOUTS_PKEY = createUniqueKey(Workouts.WORKOUTS, "workouts_pkey", Workouts.WORKOUTS.ID);
        public static final UniqueKey<WorkoutsRecord> WORKOUTS_NAME_KEY = createUniqueKey(Workouts.WORKOUTS, "workouts_name_key", Workouts.WORKOUTS.NAME);
    }

    private static class ForeignKeys0 extends AbstractKeys {
        public static final ForeignKey<ProgramWorkoutsRecord, ProgramsRecord> PROGRAM_WORKOUTS__PROGRAM_WORKOUTS_PROGRAM_ID_FKEY = createForeignKey(com.pupd.backend.data.generated.Keys.PROGRAMS_PKEY, ProgramWorkouts.PROGRAM_WORKOUTS, "program_workouts__program_workouts_program_id_fkey", ProgramWorkouts.PROGRAM_WORKOUTS.PROGRAM_ID);
        public static final ForeignKey<WorkoutExercisesRecord, WorkoutsRecord> WORKOUT_EXERCISES__WORKOUT_EXERCISES_WORKOUT_ID_FKEY = createForeignKey(com.pupd.backend.data.generated.Keys.WORKOUTS_PKEY, WorkoutExercises.WORKOUT_EXERCISES, "workout_exercises__workout_exercises_workout_id_fkey", WorkoutExercises.WORKOUT_EXERCISES.WORKOUT_ID);
        public static final ForeignKey<WorkoutExercisesRecord, ExercisesRecord> WORKOUT_EXERCISES__WORKOUT_EXERCISES_EXERCISE_ID_FKEY = createForeignKey(com.pupd.backend.data.generated.Keys.EXERCISES_PKEY, WorkoutExercises.WORKOUT_EXERCISES, "workout_exercises__workout_exercises_exercise_id_fkey", WorkoutExercises.WORKOUT_EXERCISES.EXERCISE_ID);
    }
}
