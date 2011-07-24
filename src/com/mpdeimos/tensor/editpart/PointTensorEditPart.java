package com.mpdeimos.tensor.editpart;

import com.mpdeimos.tensor.figure.IFigure;
import com.mpdeimos.tensor.figure.TensorFigure;
import com.mpdeimos.tensor.model.IModelData;

/**
 * EditPart representing a point tensor.
 * 
 * @author mpdeimos
 * 
 */
public class PointTensorEditPart extends TensorEditPartBase
{

	/** Constructor. */
	public PointTensorEditPart(IModelData modelData)
	{
		super(modelData);
	}

	@Override
	protected IFigure createFigure()
	{
		return new TensorFigure(this);
	}

}
