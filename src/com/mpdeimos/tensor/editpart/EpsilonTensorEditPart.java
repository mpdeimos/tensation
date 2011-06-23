package com.mpdeimos.tensor.editpart;

import java.awt.Point;
import java.util.ArrayList;

import com.mpdeimos.tensor.editpart.feature.IFeature;
import com.mpdeimos.tensor.editpart.feature.IRotatableEditPart;
import com.mpdeimos.tensor.figure.EpsilonTensorFigure;
import com.mpdeimos.tensor.model.EpsilonTensor;
import com.mpdeimos.tensor.model.IModelData;

/**
 * EditPart class for an epsilon tensor.
 * 
 * @author mpdeimos
 *
 */
public class EpsilonTensorEditPart extends EditPartBase implements IMovableEditPart, IRotatableEditPart {
	
	/** Constructor. */
	public EpsilonTensorEditPart(IModelData modelData) {
		super(modelData);
		setFigure(new EpsilonTensorFigure(this));
		
		features = new ArrayList<IFeature>(1);
		features.add(new IRotatableEditPart.Feature(this));
	}

	@Override
	public Point getPosition() {
		return ((EpsilonTensor)(this.getModelData())).getPosition();
	}

	@Override
	public void setPosition(Point p) {
		((EpsilonTensor)(this.getModelData())).setPosition(p);
	}

	@Override
	public double getRotation() {
		return ((EpsilonTensor)(this.getModelData())).getRotation();
	}

	@Override
	public void setRotation(double degrees) {
		((EpsilonTensor)(this.getModelData())).setRotation(degrees);
	}
}
