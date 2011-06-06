package com.mpdeimos.tensor.editpart;

import com.mpdeimos.tensor.EditPart;
import com.mpdeimos.tensor.figure.EpsilonTensorFigure;
import com.mpdeimos.tensor.model.EpsilonTensor;
import com.mpdeimos.tensor.model.IModelData;

/**
 * EditPart class for an epsilon tensor.
 * 
 * @author mpdeimos
 *
 */
@EditPart(model=EpsilonTensor.class)
public class EpsilonTensorEditPart extends EditPartBase {

	/** Constructor. */
	public EpsilonTensorEditPart(IModelData modelData) {
		super(modelData);
		setFigure(new EpsilonTensorFigure(this));
	}

}
