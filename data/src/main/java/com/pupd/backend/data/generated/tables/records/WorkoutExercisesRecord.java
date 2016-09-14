/**
 * This class is generated by jOOQ
 */
package com.pupd.backend.data.generated.tables.records;


import com.pupd.backend.data.generated.tables.WorkoutExercises;

import java.util.UUID;

import javax.annotation.Generated;

import org.jooq.Field;
import org.jooq.Record1;
import org.jooq.Record6;
import org.jooq.Row6;
import org.jooq.impl.UpdatableRecordImpl;


/**
 * This class is generated by jOOQ.
 */
@Generated(
    value = {
        "http://www.jooq.org",
        "jOOQ version:3.8.4"
    },
    comments = "This class is generated by jOOQ"
)
@SuppressWarnings({ "all", "unchecked", "rawtypes" })
public class WorkoutExercisesRecord extends UpdatableRecordImpl<WorkoutExercisesRecord> implements Record6<UUID, UUID, UUID, Integer[], Double[], Double> {

    private static final long serialVersionUID = 1610937757;

    /**
     * Setter for <code>public.workout_exercises.id</code>.
     */
    public void setId(UUID value) {
        set(0, value);
    }

    /**
     * Getter for <code>public.workout_exercises.id</code>.
     */
    public UUID getId() {
        return (UUID) get(0);
    }

    /**
     * Setter for <code>public.workout_exercises.workout_id</code>.
     */
    public void setWorkoutId(UUID value) {
        set(1, value);
    }

    /**
     * Getter for <code>public.workout_exercises.workout_id</code>.
     */
    public UUID getWorkoutId() {
        return (UUID) get(1);
    }

    /**
     * Setter for <code>public.workout_exercises.exercise_id</code>.
     */
    public void setExerciseId(UUID value) {
        set(2, value);
    }

    /**
     * Getter for <code>public.workout_exercises.exercise_id</code>.
     */
    public UUID getExerciseId() {
        return (UUID) get(2);
    }

    /**
     * Setter for <code>public.workout_exercises.reps</code>.
     */
    public void setReps(Integer[] value) {
        set(3, value);
    }

    /**
     * Getter for <code>public.workout_exercises.reps</code>.
     */
    public Integer[] getReps() {
        return (Integer[]) get(3);
    }

    /**
     * Setter for <code>public.workout_exercises.weights</code>.
     */
    public void setWeights(Double[] value) {
        set(4, value);
    }

    /**
     * Getter for <code>public.workout_exercises.weights</code>.
     */
    public Double[] getWeights() {
        return (Double[]) get(4);
    }

    /**
     * Setter for <code>public.workout_exercises.incr</code>.
     */
    public void setIncr(Double value) {
        set(5, value);
    }

    /**
     * Getter for <code>public.workout_exercises.incr</code>.
     */
    public Double getIncr() {
        return (Double) get(5);
    }

    // -------------------------------------------------------------------------
    // Primary key information
    // -------------------------------------------------------------------------

    /**
     * {@inheritDoc}
     */
    @Override
    public Record1<UUID> key() {
        return (Record1) super.key();
    }

    // -------------------------------------------------------------------------
    // Record6 type implementation
    // -------------------------------------------------------------------------

    /**
     * {@inheritDoc}
     */
    @Override
    public Row6<UUID, UUID, UUID, Integer[], Double[], Double> fieldsRow() {
        return (Row6) super.fieldsRow();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Row6<UUID, UUID, UUID, Integer[], Double[], Double> valuesRow() {
        return (Row6) super.valuesRow();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<UUID> field1() {
        return WorkoutExercises.WORKOUT_EXERCISES.ID;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<UUID> field2() {
        return WorkoutExercises.WORKOUT_EXERCISES.WORKOUT_ID;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<UUID> field3() {
        return WorkoutExercises.WORKOUT_EXERCISES.EXERCISE_ID;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<Integer[]> field4() {
        return WorkoutExercises.WORKOUT_EXERCISES.REPS;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<Double[]> field5() {
        return WorkoutExercises.WORKOUT_EXERCISES.WEIGHTS;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Field<Double> field6() {
        return WorkoutExercises.WORKOUT_EXERCISES.INCR;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public UUID value1() {
        return getId();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public UUID value2() {
        return getWorkoutId();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public UUID value3() {
        return getExerciseId();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Integer[] value4() {
        return getReps();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Double[] value5() {
        return getWeights();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Double value6() {
        return getIncr();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public WorkoutExercisesRecord value1(UUID value) {
        setId(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public WorkoutExercisesRecord value2(UUID value) {
        setWorkoutId(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public WorkoutExercisesRecord value3(UUID value) {
        setExerciseId(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public WorkoutExercisesRecord value4(Integer[] value) {
        setReps(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public WorkoutExercisesRecord value5(Double[] value) {
        setWeights(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public WorkoutExercisesRecord value6(Double value) {
        setIncr(value);
        return this;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public WorkoutExercisesRecord values(UUID value1, UUID value2, UUID value3, Integer[] value4, Double[] value5, Double value6) {
        value1(value1);
        value2(value2);
        value3(value3);
        value4(value4);
        value5(value5);
        value6(value6);
        return this;
    }

    // -------------------------------------------------------------------------
    // Constructors
    // -------------------------------------------------------------------------

    /**
     * Create a detached WorkoutExercisesRecord
     */
    public WorkoutExercisesRecord() {
        super(WorkoutExercises.WORKOUT_EXERCISES);
    }

    /**
     * Create a detached, initialised WorkoutExercisesRecord
     */
    public WorkoutExercisesRecord(UUID id, UUID workoutId, UUID exerciseId, Integer[] reps, Double[] weights, Double incr) {
        super(WorkoutExercises.WORKOUT_EXERCISES);

        set(0, id);
        set(1, workoutId);
        set(2, exerciseId);
        set(3, reps);
        set(4, weights);
        set(5, incr);
    }
}
