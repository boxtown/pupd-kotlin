/**
 * This class is generated by jOOQ
 */
package com.pupd.backend.data.generated.routines;


import com.pupd.backend.data.generated.Public;

import javax.annotation.Generated;

import org.jooq.Field;
import org.jooq.Parameter;
import org.jooq.impl.AbstractRoutine;


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
public class Digest2 extends AbstractRoutine<byte[]> {

    private static final long serialVersionUID = -88376054;

    /**
     * The parameter <code>public.digest.RETURN_VALUE</code>.
     */
    public static final Parameter<byte[]> RETURN_VALUE = createParameter("RETURN_VALUE", org.jooq.impl.SQLDataType.BLOB, false, false);

    /**
     * The parameter <code>public.digest._1</code>.
     */
    public static final Parameter<byte[]> _1 = createParameter("_1", org.jooq.impl.SQLDataType.BLOB, false, true);

    /**
     * The parameter <code>public.digest._2</code>.
     */
    public static final Parameter<String> _2 = createParameter("_2", org.jooq.impl.SQLDataType.CLOB, false, true);

    /**
     * Create a new routine call instance
     */
    public Digest2() {
        super("digest", Public.PUBLIC, org.jooq.impl.SQLDataType.BLOB);

        setReturnParameter(RETURN_VALUE);
        addInParameter(_1);
        addInParameter(_2);
        setOverloaded(true);
    }

    /**
     * Set the <code>_1</code> parameter IN value to the routine
     */
    public void set__1(byte[] value) {
        setValue(_1, value);
    }

    /**
     * Set the <code>_1</code> parameter to the function to be used with a {@link org.jooq.Select} statement
     */
    public void set__1(Field<byte[]> field) {
        setField(_1, field);
    }

    /**
     * Set the <code>_2</code> parameter IN value to the routine
     */
    public void set__2(String value) {
        setValue(_2, value);
    }

    /**
     * Set the <code>_2</code> parameter to the function to be used with a {@link org.jooq.Select} statement
     */
    public void set__2(Field<String> field) {
        setField(_2, field);
    }
}
