package com.mpdeimos.tensor.figure;

import java.awt.BasicStroke;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.Stroke;
import java.awt.geom.Ellipse2D;
import java.awt.geom.GeneralPath;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;

import com.mpdeimos.tensor.editpart.IEditPart;
import com.mpdeimos.tensor.figure.ShapePack.EDrawingMode;
import com.mpdeimos.tensor.model.EpsilonTensor;
import com.mpdeimos.tensor.util.PointUtil;

/**
 * Drawing class for an EpsilonTensor
 * 
 * @author mpdeimos
 * 
 */
public class EpsilonTensorFigure extends FigureBase
{

	/** size of the center circle in pixels */
	private static final int CENTER_CIRCLE_RADIUS = 4;

	/** offset of the connector stroke to the circle */
	private static final int CONNECTOR_STROKE_OFFSET = 2;

	/** length of the connector strokes */
	private static final int CONNECTOR_STROKE_LENGTH = 10;

	/** the style of the connector stroke */
	private static final Stroke CONNECTOR_STROKE = new BasicStroke(1.1f);

	/** amount of connections. TODO move to model. */
	private static final int NUM_CONNECTIONS = 3;

	/** buffer for connection points. */
	private Point2D[] connectionPoints;

	/**
	 * Constructor.
	 */
	public EpsilonTensorFigure(IEditPart editPart)
	{
		super(editPart);
	}

	@Override
	protected void initBeforeFirstUpdateShapes()
	{
		super.initBeforeFirstUpdateShapes();
		this.connectionPoints = new Point2D[NUM_CONNECTIONS];
	}

	@Override
	public void updateShapes()
	{
		super.updateShapes();

		List<Shape> lines = new ArrayList<Shape>(NUM_CONNECTIONS);
		List<Shape> fills = new ArrayList<Shape>(NUM_CONNECTIONS + 1);

		EpsilonTensor tensor = (EpsilonTensor) this.editPart.getModelData();
		Point position = tensor.getPosition();
		int x = (int) position.getX();
		int y = (int) position.getY();

		Ellipse2D circle = new Ellipse2D.Double(
				x - CENTER_CIRCLE_RADIUS + 0.5,
				y - CENTER_CIRCLE_RADIUS + 0.5,
				2 * CENTER_CIRCLE_RADIUS - 1,
				2 * CENTER_CIRCLE_RADIUS - 1);
		fills.add(circle);

		for (int i = 0; i < NUM_CONNECTIONS; i++)
		{
			double ang = (((double) i) / NUM_CONNECTIONS + tensor.getRotation() / 360)
					* 2
					* Math.PI;
			ang %= 2 * Math.PI;
			double sin = Math.sin(ang);
			double cos = Math.cos(ang);

			Point2D bottom = new Point2D.Double(
					x + (CENTER_CIRCLE_RADIUS
							+ CONNECTOR_STROKE_OFFSET)
							* cos,
					y + (CENTER_CIRCLE_RADIUS
							+ CONNECTOR_STROKE_OFFSET)
							* sin);

			Point2D top = new Point2D.Double(
					x + (CENTER_CIRCLE_RADIUS
							+ CONNECTOR_STROKE_OFFSET
							+ CONNECTOR_STROKE_LENGTH)
							* cos,
					y + (CENTER_CIRCLE_RADIUS
							+ CONNECTOR_STROKE_OFFSET
							+ CONNECTOR_STROKE_LENGTH)
							* sin);

			this.connectionPoints[i] = top;

			Point2D triangleL = new Point2D.Double(
					CONNECTOR_STROKE_OFFSET + CONNECTOR_STROKE_LENGTH,
					-CENTER_CIRCLE_RADIUS);
			PointUtil.rotate(triangleL, ang);
			PointUtil.move(triangleL, x, y);

			Point2D triangleR = new Point2D.Double(
					CONNECTOR_STROKE_OFFSET + CONNECTOR_STROKE_LENGTH,
					CENTER_CIRCLE_RADIUS);
			PointUtil.rotate(triangleR, ang);
			PointUtil.move(triangleR, x, y);

			Shape line = new Line2D.Double(bottom, top);

			lines.add(line);

			GeneralPath triangle = new GeneralPath();
			triangle.moveTo(top.getX(), top.getY());
			triangle.lineTo(triangleL.getX(), triangleL.getY());
			triangle.lineTo(triangleR.getX(), triangleR.getY());
			triangle.closePath();
			fills.add(triangle);
		}

		ShapePack linePack = new ShapePack(EDrawingMode.STROKE, lines);
		linePack.setStroke(CONNECTOR_STROKE);
		this.shapePacks.add(linePack);
		this.shapePacks.add(new ShapePack(EDrawingMode.FILL, fills));
	}

	@Override
	public void draw(Graphics2D gfx)
	{
		super.draw(gfx);
	}

	//
	// @Override
	// public boolean containsPoints(Point point) {
	// EpsilonTensor tensor = (EpsilonTensor)editPart.getModelData();
	// Point position = tensor.getPosition();
	// Rectangle r = new Rectangle((int)position.getX() - CENTER_CIRCLE_RADIUS,
	// (int)position.getY() - CENTER_CIRCLE_RADIUS,
	// 2*CENTER_CIRCLE_RADIUS,
	// 2*CENTER_CIRCLE_RADIUS);
	//
	// return r.contains(point);
	// }

	@Override
	public Rectangle getBoundingRectangle()
	{

		EpsilonTensor tensor = (EpsilonTensor) this.editPart.getModelData();
		Point position = tensor.getPosition();

		int offset = CONNECTOR_STROKE_LENGTH + CENTER_CIRCLE_RADIUS
				+ CONNECTOR_STROKE_OFFSET;

		return new Rectangle(
				(int) position.getX() - offset,
				(int) position.getY() - offset,
				2 * offset,
				2 * offset);
	}

	/** @return the connections points of this figure. */
	public Point2D[] getConnectionPoints()
	{
		return this.connectionPoints;
	}

}
