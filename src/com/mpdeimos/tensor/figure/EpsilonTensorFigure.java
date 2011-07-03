package com.mpdeimos.tensor.figure;

import java.awt.BasicStroke;
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
		int numConnections = ((EpsilonTensor) this.editPart.getModelData()).getAnchors().size();
		this.connectionPoints = new Point2D[numConnections];
	}

	@Override
	public void updateShapes()
	{
		super.updateShapes();

		int numConnections = this.connectionPoints.length;

		List<Shape> lines = new ArrayList<Shape>(numConnections);
		List<Shape> fills = new ArrayList<Shape>(numConnections + 1);

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

		for (int i = 0; i < numConnections; i++)
		{
			Point2D top = new Point2D.Double();
			Point2D bottom = new Point2D.Double();
			double ang = initAnchorPoints(tensor, i, top, bottom);

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

	/** inits the anchor points for a tensor. */
	public static double initAnchorPoints(
			EpsilonTensor tensor,
			int i,
			Point2D top,
			Point2D bottom)
	{
		double ang = (((double) i) / tensor.getAnchors().size() + tensor.getRotation() / 360)
				* 2
				* Math.PI;
		ang %= 2 * Math.PI;
		double sin = Math.sin(ang);
		double cos = Math.cos(ang);

		if (bottom != null)
		{
			bottom.setLocation(
					tensor.getPosition().x + (CENTER_CIRCLE_RADIUS
							+ CONNECTOR_STROKE_OFFSET)
							* cos,
					tensor.getPosition().y + (CENTER_CIRCLE_RADIUS
							+ CONNECTOR_STROKE_OFFSET)
							* sin);
		}

		if (top != null)
		{
			top.setLocation(
					tensor.getPosition().x + (CENTER_CIRCLE_RADIUS
							+ CONNECTOR_STROKE_OFFSET
							+ CONNECTOR_STROKE_LENGTH)
							* cos,
					tensor.getPosition().y + (CENTER_CIRCLE_RADIUS
							+ CONNECTOR_STROKE_OFFSET
							+ CONNECTOR_STROKE_LENGTH)
							* sin);
		}

		return ang;
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
