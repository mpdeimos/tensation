package com.mpdeimos.tensor.editpart;

import com.mpdeimos.tensor.figure.TensorFigure;
import com.mpdeimos.tensor.figure.IFigure;
import com.mpdeimos.tensor.model.IModelData;

/**
 * EditPart class for an epsilon tensor.
 * 
 * @author mpdeimos
 * 
 */
public class EpsilonTensorEditPart extends TensorEditPartBase
{
	/** Constructor. */
	public EpsilonTensorEditPart(IModelData modelData)
	{
		super(modelData);
	}

	@Override
	protected IFigure createFigure()
	{
		return new TensorFigure(this);
	}
}
