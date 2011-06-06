/**
 * 
 */
package com.mpdeimos.tensor;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import com.mpdeimos.tensor.model.IModelData;


/**
 * Annotation for linking IEditPart classes to IModelData classes.
 * 
 * @author mpdeimos
 *
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface EditPart {

	/** The model linked to this EditPart */
	Class<? extends IModelData> model();

}
