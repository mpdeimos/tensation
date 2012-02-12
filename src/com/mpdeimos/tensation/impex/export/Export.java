package com.mpdeimos.tensation.impex.export;

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

	/**
	 * Optional name of a setter method. Used instead of accessing the field
	 * directly.
	 */
	String set() default "";
}
