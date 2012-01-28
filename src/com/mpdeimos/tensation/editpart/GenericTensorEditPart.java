package com.mpdeimos.tensation.editpart;

import com.mpdeimos.tensation.figure.IFigure;
import com.mpdeimos.tensation.figure.TensorFigure;
import com.mpdeimos.tensation.model.IModelData;

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
