package com.mpdeimos.tensor.figure;

import com.mpdeimos.tensor.editpart.IEditPart;
import com.mpdeimos.tensor.figure.ShapePack.EDrawingMode;
import com.mpdeimos.tensor.impex.ESvg;
import com.mpdeimos.tensor.model.TensorBase;
import com.mpdeimos.tensor.model.TensorConnectionAnchor;
import com.mpdeimos.tensor.model.TensorConnectionAnchor.EDirection;
import com.mpdeimos.tensor.util.ImmutableList;
import com.mpdeimos.tensor.util.PointUtil;

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

import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * Drawing class for an EpsilonTensor
 * 
 * @author mpdeimos
 * 
 */
public class TensorFigure extends FigureBase
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
	public TensorFigure(IEditPart editPart)
	{
		super(editPart);
	}

	@Override
	protected void initBeforeFirstUpdateShapes()
	{
		super.initBeforeFirstUpdateShapes();
		int numConnections = ((TensorBase) this.editPart.getModel()).getAnchors().size();
		this.connectionPoints = new Point2D[numConnections];
	}

	@Override
	public void updateShapes()
	{
		super.updateShapes();

		int numConnections = this.connectionPoints.length;

		List<Shape> lines = new ArrayList<Shape>(numConnections);
		List<Shape> fills = new ArrayList<Shape>(numConnections + 1);

		TensorBase tensor = (TensorBase) this.editPart.getModel();
		Point position = tensor.getPosition();
		int x = (int) position.getX();
		int y = (int) position.getY();

		Ellipse2D circle = new Ellipse2D.Double(
				x - CENTER_CIRCLE_RADIUS + 0.5,
				y - CENTER_CIRCLE_RADIUS + 0.5,
				2 * CENTER_CIRCLE_RADIUS - 1,
				2 * CENTER_CIRCLE_RADIUS - 1);
		fills.add(circle);

		ImmutableList<TensorConnectionAnchor> anchors = tensor.getAnchors();
		for (int i = 0; i < numConnections; i++)
		{
			Point2D top = new Point2D.Double();
			Point2D bottom = new Point2D.Double();
			Point2D triangleHead = new Point2D.Double();
			double ang = initAnchorPoints(
					tensor,
					i,
					top,
					bottom,
					triangleHead);

			this.connectionPoints[i] = top;

			Point2D triangleL = null;
			if (anchors.get(i).getDirection() == EDirection.SOURCE)
				triangleL = new Point2D.Double(
						CONNECTOR_STROKE_OFFSET + CONNECTOR_STROKE_LENGTH,
						-CENTER_CIRCLE_RADIUS);
			else
				triangleL = new Point2D.Double(
						2 * CENTER_CIRCLE_RADIUS + CONNECTOR_STROKE_OFFSET,
						-CENTER_CIRCLE_RADIUS);

			PointUtil.rotate(triangleL, ang);
			PointUtil.move(triangleL, x, y);

			Point2D triangleR = null;
			if (anchors.get(i).getDirection() == EDirection.SOURCE)
				triangleR = new Point2D.Double(
							CONNECTOR_STROKE_OFFSET + CONNECTOR_STROKE_LENGTH,
							CENTER_CIRCLE_RADIUS);
			else
				triangleR = new Point2D.Double(
						2 * CENTER_CIRCLE_RADIUS + CONNECTOR_STROKE_OFFSET,
						CENTER_CIRCLE_RADIUS);

			PointUtil.rotate(triangleR, ang);
			PointUtil.move(triangleR, x, y);

			GeneralPath triangle = new GeneralPath();
			triangle.moveTo(triangleHead.getX(), triangleHead.getY());
			triangle.lineTo(triangleL.getX(), triangleL.getY());
			triangle.lineTo(triangleR.getX(), triangleR.getY());
			triangle.closePath();
			fills.add(triangle);

			Shape line = new Line2D.Double(bottom, top);
			lines.add(line);
		}

		ShapePack linePack = new ShapePack(EDrawingMode.STROKE, lines);
		linePack.setStroke(CONNECTOR_STROKE);
		this.shapePacks.add(linePack);
		this.shapePacks.add(new ShapePack(EDrawingMode.FILL, fills));
	}

	/** inits the anchor points for a tensor. */
	public static double initAnchorPoints(
			TensorBase tensor,
			int i,
			Point2D top,
			Point2D bottom, Point2D triangleHead)
	{
		return initAnchorPoints(
				tensor,
				i,
				top,
				bottom,
				triangleHead,
				tensor.getRotation());
	}

	/** inits the anchor points for a tensor. */
	public static double initAnchorPoints(
			TensorBase tensor,
			int i,
			Point2D top,
			Point2D bottom, Point2D triangleHead, double ang)
	{
		ang = (((double) i) / tensor.getAnchors().size() + ang / 360)
				* 2
				* Math.PI;
		ang %= 2 * Math.PI;

		double sin = Math.sin(ang);
		double cos = Math.cos(ang);
		EDirection direction = tensor.getAnchors().get(i).getDirection();

		int offset = CENTER_CIRCLE_RADIUS + CONNECTOR_STROKE_OFFSET;
		if (bottom != null)
		{
			bottom.setLocation(
					tensor.getPosition().x + offset * cos,
					tensor.getPosition().y + offset * sin);

			if (triangleHead != null && EDirection.SINK == direction)
			{
				triangleHead.setLocation(bottom);

				bottom.setLocation(
						tensor.getPosition().x
								+ (offset + CONNECTOR_STROKE_OFFSET) * cos,
						tensor.getPosition().y
								+ (offset + CONNECTOR_STROKE_OFFSET) * sin);
			}
		}

		if (top != null)
		{
			top.setLocation(
					tensor.getPosition().x + (offset + CONNECTOR_STROKE_LENGTH)
							* cos,
					tensor.getPosition().y + (offset + CONNECTOR_STROKE_LENGTH)
							* sin);
			if (triangleHead != null && EDirection.SOURCE == direction)
			{
				triangleHead.setLocation(top);
				top.setLocation(
						tensor.getPosition().x
								+ (offset - CONNECTOR_STROKE_OFFSET + CONNECTOR_STROKE_LENGTH)
								* cos,
						tensor.getPosition().y
								+ (offset - CONNECTOR_STROKE_OFFSET + CONNECTOR_STROKE_LENGTH)
								* sin);
			}
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

		TensorBase tensor = (TensorBase) this.editPart.getModel();
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

	@Override
	public Element getSvgNode(Document doc)
	{
		TensorBase tensor = (TensorBase) this.editPart.getModel();
		Point position = tensor.getPosition();

		Element group = doc.createElement(ESvg.ELEMENT_GROUP.$());
		group.setAttribute(
				ESvg.ATTRIB_TRANSFORM.$(),
				String.format(
						ESvg.VALUE_TRANSFORM_FUNC_TRANSLATE.$() +
								ESvg.VALUE_TRANSFORM_FUNC_ROTATE.$(),
						position.getX(),
						position.getY(),
						tensor.getRotation()));
		group.setAttribute(ESvg.ATTRIB_FILL.$(), ESvg.VALUE_COLOR_BLACK.$());
		group.setAttribute(ESvg.ATTRIB_STROKE.$(), ESvg.VALUE_COLOR_BLACK.$());
		group.setAttribute(ESvg.ATTRIB_STROKE_WIDTH.$(), Integer.toString(1));

		ImmutableList<TensorConnectionAnchor> anchors = tensor.getAnchors();
		for (int i = 0; i < anchors.size(); i++)
		{
			Point2D top = new Point2D.Double();
			Point2D bottom = new Point2D.Double();
			initAnchorPoints(
					tensor,
					i,
					top,
					bottom,
					null,
					0);

			PointUtil.move(top, -position.x, -position.y);
			PointUtil.move(bottom, -position.x, -position.y);

			// TensorConnectionAnchor anchor = anchors.get(i);

			Element line = doc.createElement(ESvg.ELEMENT_LINE.$());
			group.appendChild(line);

			line.setAttribute(
					ESvg.ATTRIB_FROM_X.$(),
					Double.toString(bottom.getX()));
			line.setAttribute(
					ESvg.ATTRIB_FROM_Y.$(),
					Double.toString(bottom.getY()));
			line.setAttribute(ESvg.ATTRIB_TO_X.$(), Double.toString(top.getX()));
			line.setAttribute(ESvg.ATTRIB_TO_Y.$(), Double.toString(top.getY()));
		}

		Element circle = doc.createElement(ESvg.ELEMENT_CIRCLE.$());
		group.appendChild(circle);
		circle.setAttribute(
				ESvg.ATTRIB_RADIUS.$(),
				Double.toString(CENTER_CIRCLE_RADIUS - 0.5));

		return group;
	}
}
