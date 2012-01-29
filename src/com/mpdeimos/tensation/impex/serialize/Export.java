package com.mpdeimos.tensation.impex.serialize;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author mpdeimos
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.FIELD })
public @interface Export
{
	/** Optional name of the exported field. */
	String name() default "";

	/** True if null values should be exported. Default false. */
	boolean nulls() default false;
}
