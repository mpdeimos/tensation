package com.mpdeimos.tensor.figure;

import java.awt.BasicStroke;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Shape;
import java.awt.Stroke;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;

import com.mpdeimos.tensor.editpart.EpsilonTensorEditPart;
import com.mpdeimos.tensor.model.EpsilonTensor;

/**
 * Drawing class for an EpsilonTensor
 * 
 * @author mpdeimos
 *
 */
public class EpsilonTensorFigure implements IFigure {

	/** size of the center circle in pixels */
	private static final int CENTER_CIRCLE_SIZE = 6;
	
	private static final Stroke CONNECTOR_STROKE = new BasicStroke(1f);
	
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
		int x = (int)position.getX();
		int y = (int)position.getY();
		
		gfx.setStroke(CONNECTOR_STROKE);
		Ellipse2D circle = new Ellipse2D.Double(x-3, y-3, CENTER_CIRCLE_SIZE, CENTER_CIRCLE_SIZE);
//		Shape line = new Line2D.Double(((double)x+MIDPOINT_X)+0.5, x+0, ((double)x+MIDPOINT_X)+0.5, x+MIDPOINT_Y-6);
//		gfx.draw(line);
//		gfx.drawLine(x+MIDPOINT_X-1, x+0, x+MIDPOINT_X-1, x+MIDPOINT_Y-6);
		
		gfx.fill(circle);
	}
	
}
