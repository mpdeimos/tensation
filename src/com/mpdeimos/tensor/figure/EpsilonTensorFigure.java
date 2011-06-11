package com.mpdeimos.tensor.figure;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.Stroke;
import java.awt.geom.Ellipse2D;
import java.awt.geom.GeneralPath;
import java.awt.geom.Line2D;
import java.awt.geom.PathIterator;
import java.awt.geom.Point2D;

import com.mpdeimos.tensor.editpart.EpsilonTensorEditPart;
import com.mpdeimos.tensor.editpart.IEditPart;
import com.mpdeimos.tensor.model.EpsilonTensor;
import com.mpdeimos.tensor.util.PointUtil;

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
	private static final int CONNECTOR_STROKE_LENGTH = 10;
	
	/** the style of the connector stroke */
	private static final Stroke CONNECTOR_STROKE = new BasicStroke(1.1f);

	private boolean selected = false;

	/**
	 * Constructor.
	 */
	public EpsilonTensorFigure(IEditPart editPart) {
		super(editPart);
	}

	@Override
	public void draw(Graphics2D gfx) {
		
		Color oldPaint = gfx.getColor();
		if (selected)
			gfx.setColor(Color.BLUE);
		
		EpsilonTensor tensor = (EpsilonTensor)editPart.getModelData();
		Point position = tensor.getPosition();
		int x = (int)position.getX();
		int y = (int)position.getY();
		
		gfx.setStroke(CONNECTOR_STROKE);
		Ellipse2D circle = new Ellipse2D.Double(x-CENTER_CIRCLE_RADIUS+0.5, y-CENTER_CIRCLE_RADIUS+0.5, 2*CENTER_CIRCLE_RADIUS-1, 2*CENTER_CIRCLE_RADIUS-1);
		gfx.fill(circle);
		
		int max = 3;
		for (int i = 0; i < max; i++)
		{
			double ang = (((double)i)/max+tensor.getRotation()/360)*2*Math.PI;
			ang %= 2*Math.PI;
			double sin = Math.sin(ang);
			double cos = Math.cos(ang);
			Point2D bottom = new Point2D.Double(
					x+(CENTER_CIRCLE_RADIUS+CONNECTOR_STROKE_OFFSET)*cos,
					y+(CENTER_CIRCLE_RADIUS+CONNECTOR_STROKE_OFFSET)*sin);
			Point2D top = new Point2D.Double(
					x+(CENTER_CIRCLE_RADIUS+CONNECTOR_STROKE_OFFSET+CONNECTOR_STROKE_LENGTH)*cos,
					y+(CENTER_CIRCLE_RADIUS+CONNECTOR_STROKE_OFFSET+CONNECTOR_STROKE_LENGTH)*sin);
			Point2D triangleL = new Point2D.Double(CONNECTOR_STROKE_OFFSET+CONNECTOR_STROKE_LENGTH,-CENTER_CIRCLE_RADIUS);
			PointUtil.rotate(triangleL, ang);
			PointUtil.move(triangleL, x, y);
			
			Point2D triangleR = new Point2D.Double(CONNECTOR_STROKE_OFFSET+CONNECTOR_STROKE_LENGTH,CENTER_CIRCLE_RADIUS);
			PointUtil.rotate(triangleR, ang);
			PointUtil.move(triangleR, x, y);
			
			Shape line = new Line2D.Double(bottom, top);
			
			gfx.draw(line);
			
			GeneralPath triangle = new GeneralPath();
			triangle.moveTo(top.getX(), top.getY());
			triangle.lineTo(triangleL.getX(), triangleL.getY());
			triangle.lineTo(triangleR.getX(), triangleR.getY());
			triangle.closePath();
			gfx.fill(triangle);
		}
		
		gfx.setColor(oldPaint);
	}
	
	@Override
	public void isMouseOver(Point point) {
		Rectangle r = new Rectangle((int)point.getX() - CENTER_CIRCLE_RADIUS,
					(int)point.getY() - CENTER_CIRCLE_RADIUS,
					(int)point.getX() + CENTER_CIRCLE_RADIUS,
					(int)point.getY() + CENTER_CIRCLE_RADIUS);
		
		selected = r.contains(point);
			
	}
	
}
