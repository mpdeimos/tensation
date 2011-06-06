package com.mpdeimos.tensor.figure;

import java.awt.BasicStroke;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Shape;
import java.awt.Stroke;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;

import com.mpdeimos.tensor.editpart.EpsilonTensorEditPart;
import com.mpdeimos.tensor.editpart.IEditPart;
import com.mpdeimos.tensor.model.EpsilonTensor;

/**
 * Drawing class for an EpsilonTensor
 * 
 * @author mpdeimos
 *
 */
public class EpsilonTensorFigure extends FigureBase {

	/** size of the center circle in pixels */
	private static final int CENTER_CIRCLE_RADIUS = 4;
	
	/** offset of the connector stroke to the circle */
	private static final int CONNECTOR_STROKE_OFFSET = 2;
	
	/** length of the connector strokes */
	private static final int CONNECTOR_STROKE_LENGTH = 8;
	
	/** the style of the connector stroke */
	private static final Stroke CONNECTOR_STROKE = new BasicStroke(1.1f);

	/**
	 * Constructor.
	 */
	public EpsilonTensorFigure(IEditPart editPart) {
		super(editPart);
	}

	@Override
	public void draw(Graphics2D gfx) {
		EpsilonTensor tensor = (EpsilonTensor)editPart.getModelData();
		Point position = tensor.getPosition();
		int x = (int)position.getX();
		int y = (int)position.getY();
		
		gfx.setStroke(CONNECTOR_STROKE);
		Ellipse2D circle = new Ellipse2D.Double(x-CENTER_CIRCLE_RADIUS+0.5, y-CENTER_CIRCLE_RADIUS+0.5, 2*CENTER_CIRCLE_RADIUS-0.5, 2*CENTER_CIRCLE_RADIUS-0.5);
		gfx.fill(circle);
		
		int max = 300;
		for (int i = 0; i < max; i++)
		{
			double ang = (((double)i)/max+tensor.getRotation()/360)*2*Math.PI;
			ang %= 2*Math.PI;
			Shape line = new Line2D.Double(
					x+(CENTER_CIRCLE_RADIUS+CONNECTOR_STROKE_OFFSET)*Math.sin(ang),
					y+(CENTER_CIRCLE_RADIUS+CONNECTOR_STROKE_OFFSET)*Math.cos(ang),
					x+(CENTER_CIRCLE_RADIUS+CONNECTOR_STROKE_OFFSET+CONNECTOR_STROKE_LENGTH)*Math.sin(ang),
					y+(CENTER_CIRCLE_RADIUS+CONNECTOR_STROKE_OFFSET+CONNECTOR_STROKE_LENGTH)*Math.cos(ang));
			gfx.draw(line);
		}
		
	}
	
}
