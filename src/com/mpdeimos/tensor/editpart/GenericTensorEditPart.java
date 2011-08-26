package com.mpdeimos.tensor.editpart;

import com.mpdeimos.tensor.figure.IFigure;
import com.mpdeimos.tensor.figure.TensorFigure;
import com.mpdeimos.tensor.model.IModelData;

/**
 * EditPart representing a generic tensor.
 * 
 * @author mpdeimos
 * 
 */
public class GenericTensorEditPart extends TensorEditPartBase
{

	/** Constructor. */
	public GenericTensorEditPart(IModelData modelData)
	{
		super(modelData);
	}

	@Override
	protected IFigure createFigure()
	{
		return new TensorFigure(this);
	}

}
