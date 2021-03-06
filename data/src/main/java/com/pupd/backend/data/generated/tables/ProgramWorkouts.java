/**
 * This class is generated by jOOQ
 */
package com.pupd.backend.data.generated.tables;


import com.pupd.backend.data.generated.Keys;
import com.pupd.backend.data.generated.Public;
import com.pupd.backend.data.generated.tables.records.ProgramWorkoutsRecord;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import javax.annotation.Generated;

import org.jooq.Field;
import org.jooq.ForeignKey;
import org.jooq.Schema;
import org.jooq.Table;
import org.jooq.TableField;
import org.jooq.UniqueKey;
import org.jooq.impl.TableImpl;


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
public class ProgramWorkouts extends TableImpl<ProgramWorkoutsRecord> {

    private static final long serialVersionUID = -160580641;

    /**
     * The reference instance of <code>public.program_workouts</code>
     */
    public static final ProgramWorkouts PROGRAM_WORKOUTS = new ProgramWorkouts();

    /**
     * The class holding records for this type
     */
    @Override
    public Class<ProgramWorkoutsRecord> getRecordType() {
        return ProgramWorkoutsRecord.class;
    }

    /**
     * The column <code>public.program_workouts.program_id</code>.
     */
    public final TableField<ProgramWorkoutsRecord, UUID> PROGRAM_ID = createField("program_id", org.jooq.impl.SQLDataType.UUID.nullable(false), this, "");

    /**
     * The column <code>public.program_workouts.workout_id</code>.
     */
    public final TableField<ProgramWorkoutsRecord, UUID> WORKOUT_ID = createField("workout_id", org.jooq.impl.SQLDataType.UUID.nullable(false), this, "");

    /**
     * The column <code>public.program_workouts.reps</code>.
     */
    public final TableField<ProgramWorkoutsRecord, Integer> REPS = createField("reps", org.jooq.impl.SQLDataType.INTEGER.nullable(false), this, "");

    /**
     * The column <code>public.program_workouts.start_offset</code>.
     */
    public final TableField<ProgramWorkoutsRecord, Integer> START_OFFSET = createField("start_offset", org.jooq.impl.SQLDataType.INTEGER.nullable(false), this, "");

    /**
     * The column <code>public.program_workouts.length_in_days</code>.
     */
    public final TableField<ProgramWorkoutsRecord, Integer> LENGTH_IN_DAYS = createField("length_in_days", org.jooq.impl.SQLDataType.INTEGER.nullable(false), this, "");

    /**
     * Create a <code>public.program_workouts</code> table reference
     */
    public ProgramWorkouts() {
        this("program_workouts", null);
    }

    /**
     * Create an aliased <code>public.program_workouts</code> table reference
     */
    public ProgramWorkouts(String alias) {
        this(alias, PROGRAM_WORKOUTS);
    }

    private ProgramWorkouts(String alias, Table<ProgramWorkoutsRecord> aliased) {
        this(alias, aliased, null);
    }

    private ProgramWorkouts(String alias, Table<ProgramWorkoutsRecord> aliased, Field<?>[] parameters) {
        super(alias, null, aliased, parameters, "");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Schema getSchema() {
        return Public.PUBLIC;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public UniqueKey<ProgramWorkoutsRecord> getPrimaryKey() {
        return Keys.PROGRAM_WORKOUTS_PKEY;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<UniqueKey<ProgramWorkoutsRecord>> getKeys() {
        return Arrays.<UniqueKey<ProgramWorkoutsRecord>>asList(Keys.PROGRAM_WORKOUTS_PKEY);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<ForeignKey<ProgramWorkoutsRecord, ?>> getReferences() {
        return Arrays.<ForeignKey<ProgramWorkoutsRecord, ?>>asList(Keys.PROGRAM_WORKOUTS__PROGRAM_WORKOUTS_PROGRAM_ID_FKEY);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ProgramWorkouts as(String alias) {
        return new ProgramWorkouts(alias, this);
    }

    /**
     * Rename this table
     */
    public ProgramWorkouts rename(String name) {
        return new ProgramWorkouts(name, null);
    }
}
