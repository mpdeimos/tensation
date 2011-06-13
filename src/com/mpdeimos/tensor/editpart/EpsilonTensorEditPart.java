package com.mpdeimos.tensor.editpart;

import java.awt.Point;

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
public class EpsilonTensorEditPart extends EditPartBase implements IMovableEditPart {

	/** Constructor. */
	public EpsilonTensorEditPart(IModelData modelData) {
		super(modelData);
		setFigure(new EpsilonTensorFigure(this));
	}

	@Override
	public Point getPosition() {
		return ((EpsilonTensor)(this.getModelData())).getPosition();
	}

	@Override
	public void setPosition(Point p) {
		((EpsilonTensor)(this.getModelData())).setPosition(p);
	}
	
}
