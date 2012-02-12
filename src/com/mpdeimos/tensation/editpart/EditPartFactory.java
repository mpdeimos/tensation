package com.mpdeimos.tensation.editpart;

import com.mpdeimos.tensation.model.IModelData;
import com.mpdeimos.tensation.util.Log;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

/**
 * The EditPartFactory instantiates EditParts for model data.
 * 
 * @author mpdeimos
 * 
 */
@SuppressWarnings("unchecked")
public class EditPartFactory
{

	/**
	 * @return an EditPart for the given data model object
	 * @throws Throwable
	 */
	public IEditPart createEditPart(IModelData modelData)
	{
		boolean error = false;
		try
		{
			Class<? extends IEditPart> clazz = (Class<? extends IEditPart>) Class.forName(this.getClass().getPackage().getName()
					+ "." + modelData.getClass().getSimpleName() //$NON-NLS-1$
					+ "EditPart"); //$NON-NLS-1$
			Constructor<? extends IEditPart> constructor = clazz.getConstructor(IModelData.class);
			return constructor.newInstance(modelData);
		}
		catch (InvocationTargetException e)
		{
			if (e.getCause() instanceof RuntimeException)
				throw (RuntimeException) e.getCause();
			error = true;
		}
		catch (Exception e)
		{
			error = true;
		}
		if (error)
			Log.e(
					this,
					"An error occured instantiating the EditPart for %s", modelData.getClass().getSimpleName()); //$NON-NLS-1$

		return null;
	}

}
