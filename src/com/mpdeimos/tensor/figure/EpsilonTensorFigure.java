package com.mpdeimos.tensor.figure;

import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.geom.Ellipse2D;

import com.mpdeimos.tensor.model.EpsilonTensor;
import com.mpdeimos.tensor.part.EpsilonTensorEditPart;

/**
 * Drawing class for an EpsilonTensor
 * 
 * @author mpdeimos
 *
 */
public class EpsilonTensorFigure implements IFigure {

	/** associated EditPart */
	private final EpsilonTensorEditPart editPart;

	/**
	 * Constructor.
	 */
	public EpsilonTensorFigure(EpsilonTensorEditPart epsilonTensorEditPart) {
		this.editPart = epsilonTensorEditPart;
	}

	@Override
	public void draw(Graphics2D gfx) {
		EpsilonTensor tensor = (EpsilonTensor)editPart.getModelData();
		Point position = tensor.getPosition();
		gfx.draw(new Ellipse2D.Double(position.getX(), position.getY(), 10, 10));		
	}
	
}
