package com.mpdeimos.tensor.editpart;

import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.List;

import com.mpdeimos.tensor.EditPart;
import com.mpdeimos.tensor.model.IModelData;
import com.mpdeimos.tensor.util.Log;
import com.mpdeimos.tensor.util.PackageUtils;

/**
 * The EditPartFactory instantiates EditParts for model data.
 * 
 * @author mpdeimos
 *
 */
@SuppressWarnings("unchecked")
public class EditPartFactory {
	
	/** static map for the edit part factory */
	private static HashMap<Class<? extends IModelData>, Class<? extends IEditPart>> map = new HashMap<Class<? extends IModelData>, Class<? extends IEditPart>>();

	/**
	 * Build the factory map based upon annotations.
	 */
	static
	{
		List<Class<?>> clazzes = PackageUtils.getClassesForPackage(EditPartFactory.class.getPackage());
		for (Class<?> clazz : clazzes)
		{
			if (clazz.isAnnotationPresent(EditPart.class))
			{
				
				if (IEditPart.class.isAssignableFrom(clazz))
				{
					EditPart epa = clazz.getAnnotation(EditPart.class);
					map.put(epa.model(), (Class<? extends IEditPart>) clazz);
				}
			}
		}
	}
	
	/**
	 * @return an EditPart for the given data model object
	 */
	public IEditPart createEditPart(IModelData modelData)
	{
		Class<? extends IEditPart> clazz = map.get(modelData.getClass());
		
		if (clazz == null)
		{
			Log.e(this, "Could not create EditPart for %s due to a missing EditPart annotation", modelData.getClass().getSimpleName()); //$NON-NLS-1$
			return null;
		}
		
		try {
			Constructor<? extends IEditPart> constructor = clazz.getConstructor(IModelData.class);
			return constructor.newInstance(modelData);
		} catch (Exception e) {
			Log.e(this, e);
		}
		
		return null;
	}

}
