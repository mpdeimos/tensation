package com.mpdeimos.tensation.figure;

import com.mpdeimos.tensation.editpart.EditPartBase;
import com.mpdeimos.tensation.editpart.IEditPart;
import com.mpdeimos.tensation.figure.ShapePack.EDrawingMode;
import com.mpdeimos.tensation.impex.svg.ESvg;
import com.mpdeimos.tensation.impex.svg.ESvgDefinitions;
import com.mpdeimos.tensation.model.EDirection;
import com.mpdeimos.tensation.model.TensorBase;
import com.mpdeimos.tensation.model.TensorConnectionAnchor;
import com.mpdeimos.tensation.util.Gfx;
import com.mpdeimos.tensation.util.ImmutableList;
import com.mpdeimos.tensation.util.PointUtil;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.geom.Ellipse2D;
import java.awt.geom.GeneralPath;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.HashMap;
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
	private static final int CONNECTOR_STROKE_LENGTH = 11;

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
	protected void updateShapes()
	{
		super.updateShapes();

		int numConnections = this.connectionPoints.length;

		List<Shape> lines = new ArrayList<Shape>(numConnections);
		List<Shape> fills = new ArrayList<Shape>(numConnections + 1);

		TensorBase tensor = (TensorBase) this.editPart.getModel();
		Point position = tensor.getPosition();
		int x = (int) position.getX();
		int y = (int) position.getY();

		int centerCircleRadius = getCenterCircleRadius(tensor);
		Ellipse2D circle = new Ellipse2D.Double(
				x - centerCircleRadius + 0.5,
				y - centerCircleRadius + 0.5,
				2 * centerCircleRadius - 1,
				2 * centerCircleRadius - 1);
		if (tensor.getLabel() != null)
			lines.add(circle);
		else
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
						CONNECTOR_STROKE_OFFSET + CONNECTOR_STROKE_LENGTH
								+ centerCircleRadius - CENTER_CIRCLE_RADIUS,
						-CENTER_CIRCLE_RADIUS);
			else
				triangleL = new Point2D.Double(
						centerCircleRadius + CENTER_CIRCLE_RADIUS
								+ CONNECTOR_STROKE_OFFSET,
						-CENTER_CIRCLE_RADIUS);

			PointUtil.rotate(triangleL, ang);
			PointUtil.move(triangleL, x, y);

			Point2D triangleR = null;
			if (anchors.get(i).getDirection() == EDirection.SOURCE)
				triangleR = new Point2D.Double(
							CONNECTOR_STROKE_OFFSET + CONNECTOR_STROKE_LENGTH
									+ centerCircleRadius - CENTER_CIRCLE_RADIUS,
							CENTER_CIRCLE_RADIUS);
			else
				triangleR = new Point2D.Double(
						centerCircleRadius + CENTER_CIRCLE_RADIUS
								+ CONNECTOR_STROKE_OFFSET,
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
		this.shapePacks.add(linePack);
		this.shapePacks.add(new ShapePack(EDrawingMode.FILL, fills));
	}

	/** @return the center circle radius of a tensor. */
	private static int getCenterCircleRadius(TensorBase tensor)
	{
		if (tensor.getLabel() == null)
			return CENTER_CIRCLE_RADIUS;

		Dimension bounds = Gfx.approximateTextWidth(
					Gfx.SANS_SERIF_10,
					tensor.getLabel());

		return (int) Math.ceil(Math.sqrt(bounds.getWidth()
					* bounds.getWidth() + bounds.getHeight()
					* bounds.getHeight()) / 2) + CONNECTOR_STROKE_OFFSET;
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
				tensor.getRotation(),
				tensor.getPosition());
	}

	/** inits the anchor points for a tensor. */
	public static double initAnchorPoints(
			TensorBase tensor,
			int i,
			Point2D top,
			Point2D bottom, Point2D triangleHead, double ang, Point2D position)
	{
		ang = (((double) i) / tensor.getAnchors().size() + ang / 360)
				* 2
				* Math.PI;
		ang %= 2 * Math.PI;

		double sin = Math.sin(ang);
		double cos = Math.cos(ang);
		EDirection direction = tensor.getAnchors().get(i).getDirection();

		int centerCircleRadius = getCenterCircleRadius(tensor);

		int offset = centerCircleRadius + CONNECTOR_STROKE_OFFSET;
		if (bottom != null)
		{
			bottom.setLocation(
					position.getX() + (offset - 1) * cos,
					position.getY() + (offset - 1) * sin);

			if (triangleHead != null && EDirection.SINK == direction)
			{
				triangleHead.setLocation(bottom);

				bottom.setLocation(
						position.getX()
								+ (offset + CONNECTOR_STROKE_OFFSET + 1) * cos,
						position.getY()
								+ (offset + CONNECTOR_STROKE_OFFSET + 1) * sin);
			}
		}

		if (top != null)
		{
			top.setLocation(
					position.getX()
							+ (offset + 1 + CONNECTOR_STROKE_LENGTH)
							* cos,
					position.getY()
							+ (offset + 1 + CONNECTOR_STROKE_LENGTH)
							* sin);
			if (triangleHead != null && EDirection.SOURCE == direction)
			{
				triangleHead.setLocation(top);
				top.setLocation(
						position.getX()
								+ (offset - CONNECTOR_STROKE_OFFSET
										+ CONNECTOR_STROKE_LENGTH - 1)
								* cos,
						position.getY()
								+ (offset - CONNECTOR_STROKE_OFFSET
										+ CONNECTOR_STROKE_LENGTH - 1)
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

		int offset = CONNECTOR_STROKE_LENGTH + getCenterCircleRadius(tensor)
				+ CONNECTOR_STROKE_OFFSET;

		return new Rectangle(
				(int) position.getX() - offset - 1,
				(int) position.getY() - offset - 1,
				2 * offset + 2,
				2 * offset + 2);
	}

	/** @return the connection points of this figure. */
	public Point2D[] getConnectionPoints()
	{
		return this.connectionPoints;
	}

	@Override
	public Element getSvgNode(Document doc, HashMap<String, Element> defs)
	{
		TensorBase tensor = (TensorBase) this.editPart.getModel();
		Point position = tensor.getPosition();
		AppearanceContainer appearance = ((EditPartBase) this.editPart).getAppearanceContainer();
		String def = getSvgDefName();

		Element g = doc.createElement(ESvg.ELEMENT_GROUP.$());
		g.setAttribute(
				ESvg.ATTRIB_TRANSFORM.$(),
				ESvg.VALUE_TRANSFORM_FUNC_TRANSLATE.$(
						position.getX(),
						position.getY())
				);

		Element use = doc.createElement(ESvg.ELEMENT_USE.$());
		g.appendChild(use);

		use.setAttribute(ESvg.ATTRIB_XLINK_HREF.$(), ESvg.VALUE_REF.$(def));
		use.setAttribute(
				ESvg.ATTRIB_TRANSFORM.$(),
				ESvg.VALUE_TRANSFORM_FUNC_ROTATE.$(tensor.getRotation())
				);
		g.setAttribute(
				ESvg.ATTRIB_CLASS.$(),
				ESvgDefinitions.CLASS_TENSOR.$());

		if (!defs.containsKey(def))
		{
			Element group = doc.createElement(ESvg.ELEMENT_GROUP.$());
			group.setAttribute(ESvg.ATTRIB_ID.$(), def);

			String markerStartID = null;
			String markerEndID = null;
			ImmutableList<TensorConnectionAnchor> anchors = tensor.getAnchors();
			for (int i = 0; i < anchors.size(); i++)
			{
				Point2D top = new Point2D.Double();
				Point2D bottom = new Point2D.Double();
				Point2D dummy = new Point2D.Double();
				initAnchorPoints(
						tensor,
						i,
						top,
						bottom,
						dummy,
						0,
						tensor.getPosition());

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
				line.setAttribute(
						ESvg.ATTRIB_TO_X.$(),
						Double.toString(top.getX()));
				line.setAttribute(
						ESvg.ATTRIB_TO_Y.$(),
						Double.toString(top.getY()));

				if (anchors.get(i).getDirection() == EDirection.SOURCE)
				{
					String id = ESvgDefinitions.MARKER_TRIANGLE_END.$();
					if (appearance.getColor() != null)
					{
						id = ESvgDefinitions.MARKER_TRIANGLE_END_COLOR.$(appearance.getColor().getRGB());
						markerEndID = id;
					}
					line.setAttribute(
							ESvg.ATTRIB_MARKER_END.$(),
							ESvg.VALUE_REF_URL.$(id));
				}
				else
				{
					String id = ESvgDefinitions.MARKER_TRIANGLE_START.$();
					if (appearance.getColor() != null)
					{
						id = ESvgDefinitions.MARKER_TRIANGLE_START_COLOR.$(appearance.getColor().getRGB());
						markerStartID = id;
					}
					line.setAttribute(
							ESvg.ATTRIB_MARKER_START.$(),
							ESvg.VALUE_REF_URL.$(id));
				}
			}

			if (markerStartID != null && !defs.containsKey(markerStartID))
			{
				Element marker = defs.get(ESvgDefinitions.MARKER_TRIANGLE_START.$());
				marker = (Element) marker.cloneNode(true);
				marker.setAttribute(ESvg.ATTRIB_ID.$(), markerStartID);
				marker.setAttribute(
						ESvg.ATTRIB_FILL.$(),
						ESvg.VALUE_HEX.$(appearance.getColor().getRGB()));
				defs.put(markerStartID, marker);
			}

			if (markerEndID != null && !defs.containsKey(markerEndID))
			{
				Element marker = defs.get(ESvgDefinitions.MARKER_TRIANGLE_END.$());
				marker = (Element) marker.cloneNode(true);
				marker.setAttribute(ESvg.ATTRIB_ID.$(), markerEndID);
				marker.setAttribute(
						ESvg.ATTRIB_FILL.$(),
						ESvg.VALUE_HEX.$(appearance.getColor().getRGB()));
				defs.put(markerEndID, marker);
			}

			Element circle = doc.createElement(ESvg.ELEMENT_CIRCLE.$());
			group.appendChild(circle);

			if (tensor.getLabel() == null)
			{
				circle.setAttribute(
						ESvg.ATTRIB_CLASS.$(),
						ESvgDefinitions.CLASS_TENSOR_CIRCLE.$());
			}
			else
			{
				circle.setAttribute(
						ESvg.ATTRIB_CLASS.$(),
						ESvgDefinitions.CLASS_TENSOR_CIRCLE_UNFILLED.$());
			}
			circle.setAttribute(
					ESvg.ATTRIB_RADIUS.$(),
					Double.toString(getCenterCircleRadius(tensor)));

			defs.put(def, group);
		}

		if (tensor.getLabel() != null)
		{
			Element txt = doc.createElement(ESvg.ELEMENT_TEXT.$());
			txt.setTextContent(tensor.getLabel());
			txt.setAttribute(ESvg.ATTRIB_TEXT_DY.$(), "0.5ex"); //$NON-NLS-1$
			txt.setAttribute(
					ESvg.ATTRIB_TEXT_ANCHOR.$(),
					ESvg.VALUE_TEXT_ANCHOR_MIDDLE.$());
			g.appendChild(txt);
		}

		appearance.applyAppearance(
				g);

		return g;
	}

	/** @return the string used as svg def. */
	private String getSvgDefName()
	{
		TensorBase tensor = (TensorBase) this.editPart.getModel();
		List<TensorConnectionAnchor> anchors = tensor.getAnchors();

		String name = ESvgDefinitions.TENSOR_DEF_PREFIX.$() + anchors.size();

		for (int i = 0; i < anchors.size(); i++)
		{
			name += anchors.get(i).getDirection() == EDirection.SINK ? ESvgDefinitions.TENSOR_DEF_SINK.$()
					: ESvgDefinitions.TENSOR_DEF_SOURCE.$();
		}

		Color color = tensor.getAppearanceContainer().getColor();
		if (color != null)
		{
			name += ESvgDefinitions.TENSOR_DEF_COLOR_POSTFIX.$(color.getRGB());
		}

		if (tensor.getLabel() != null)
		{
			int radius = getCenterCircleRadius(tensor);
			name += ESvgDefinitions.TENSOR_DEF_LABEL_POSTFIX.$() + radius;
		}

		return name;
	}

	@Override
	public void draw(Graphics2D gfx)
	{
		super.draw(gfx);
		Font f = gfx.getFont();
		gfx.setFont(Gfx.SANS_SERIF_10);
		Gfx.drawTextCentered(
				gfx,
				((TensorBase) this.editPart.getModel()).getPosition(),
				((TensorBase) this.editPart.getModel()).getLabel());
		gfx.setFont(f);
	}
}
