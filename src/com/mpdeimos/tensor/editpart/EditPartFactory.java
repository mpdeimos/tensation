package com.mpdeimos.tensor.editpart;

import com.mpdeimos.tensor.model.EpsilonTensor;
import com.mpdeimos.tensor.model.IModelData;

/**
 * The EditPartFactory instantiates EditParts for model data.
 * 
 * @author mpdeimos
 *
 */
public class EditPartFactory {
	
	/**
	 * @return an EditPart for the given data model object
	 */
	public IEditPart createEditPart(IModelData modelData)
	{
		if (modelData instanceof EpsilonTensor)
			return new EpsilonTensorEditPart(modelData);
		
		return null;
	}

}
