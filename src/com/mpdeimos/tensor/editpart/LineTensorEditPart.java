package com.mpdeimos.tensor.editpart;

import com.mpdeimos.tensor.figure.IFigure;
import com.mpdeimos.tensor.figure.TensorFigure;
import com.mpdeimos.tensor.model.IModelData;

/**
 * EditPart representing a line tensor.
 * 
 * @author mpdeimos
 * 
 */
public class LineTensorEditPart extends TensorEditPartBase
{

	/** Constructor. */
	public LineTensorEditPart(IModelData modelData)
	{
		super(modelData);
	}

	@Override
	protected IFigure createFigure()
	{
		return new TensorFigure(this);
	}

}
