package com.mpdeimos.tensor.part;

import com.mpdeimos.tensor.figure.EpsilonTensorFigure;
import com.mpdeimos.tensor.model.IModelData;

/**
 * EditPart class for an epsilon tensor.
 * 
 * @author mpdeimos
 *
 */
public class EpsilonTensorEditPart extends EditPartBase {

	/** Constructor. */
	public EpsilonTensorEditPart(IModelData modelData) {
		super(modelData);
		setFigure(new EpsilonTensorFigure(this));
	}

}
