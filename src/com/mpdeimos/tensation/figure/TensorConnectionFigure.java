package com.mpdeimos.tensation.figure;

import com.mpdeimos.tensation.editpart.IEditPart;
import com.mpdeimos.tensation.figure.ShapePack.EDrawingMode;
import com.mpdeimos.tensation.impex.svg.ESvg;
import com.mpdeimos.tensation.impex.svg.ESvgDefinitions;
import com.mpdeimos.tensation.model.TensorConnection;
import com.mpdeimos.tensation.util.PointUtil;

import java.awt.Point;
import java.awt.Shape;
import java.awt.geom.CubicCurve2D;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * Figure class for Tensor connections.
 * 
 * @author mpdeimos
 * 
 */
public class TensorConnectionFigure extends FigureBase
{
	/** bezier control point for the source anchor. */
	private Point2D sourceControlPoint;

	/** bezier control point for the sink anchor. */
	private Point2D sinkControlPoint;

	/** the position of the sink anchor. */
	private Point2D sinkAnchor;

	/** the position of the source anchor. */
	private Point2D sourceAnchor;

	/** the position of the source anchor (unstretched). */
	private Point2D.Double sourceControlPointUnstretched;

	/** the position of the sink anchor (unstretched). */
	private Point2D.Double sinkControlPointUnstretched;

	/** Constructor. */
	public TensorConnectionFigure(IEditPart editPart)
	{
		super(editPart);
	}

	@Override
	public void updateShapes()
	{
		super.updateShapes();

		TensorConnection connection = (TensorConnection) this.editPart.getModel();
		List<Shape> shapes = new ArrayList<Shape>(1);
		// List<Shape> fills = new ArrayList<Shape>(1);

		this.sinkAnchor = new Point2D.Double();
		this.sourceAnchor = new Point2D.Double();

		TensorFigure.initAnchorPoints(
				connection.getSource().getTensor(),
				connection.getSource().getId(),
				this.sourceAnchor,
				null, null);
		TensorFigure.initAnchorPoints(
				connection.getSink().getTensor(),
				connection.getSink().getId(),
				this.sinkAnchor,
				null, null);

		Point sourceCenter = connection.getSource().getTensor().getPosition();
		this.sourceControlPoint = new Point2D.Double();

		PointUtil.sub(this.sourceAnchor, sourceCenter, this.sourceControlPoint);
		this.sourceControlPointUnstretched = new Point2D.Double(
				this.sourceControlPoint.getX(), this.sourceControlPoint.getY());
		PointUtil.move(
				this.sourceControlPoint,
				connection.getSourceDistance() * this.sourceControlPoint.getX()
						+ sourceCenter.getX(),
				connection.getSourceDistance() * this.sourceControlPoint.getY()
						+ sourceCenter.getY());
		PointUtil.move(
				this.sourceControlPointUnstretched,
				this.sourceControlPointUnstretched.getX() + sourceCenter.getX(),
				this.sourceControlPointUnstretched.getY() + sourceCenter.getY());

		Point sinkCenter = connection.getSink().getTensor().getPosition();
		this.sinkControlPoint = new Point2D.Double();

		PointUtil.sub(this.sinkAnchor, sinkCenter, this.sinkControlPoint);
		this.sinkControlPointUnstretched = new Point2D.Double(
				this.sinkControlPoint.getX(), this.sinkControlPoint.getY());
		PointUtil.move(
				this.sinkControlPoint,
				connection.getSinkDistance() * this.sinkControlPoint.getX()
						+ sinkCenter.getX(),
				connection.getSinkDistance() * this.sinkControlPoint.getY()
						+ sinkCenter.getY());
		PointUtil.move(
				this.sinkControlPointUnstretched,
				this.sinkControlPointUnstretched.getX() + sinkCenter.getX(),
				this.sinkControlPointUnstretched.getY() + sinkCenter.getY());

		CubicCurve2D bezier = new CubicCurve2D.Double();
		bezier.setCurve(
				this.sourceAnchor,
				this.sourceControlPoint,
				this.sinkControlPoint,
				this.sinkAnchor);

		// Shape circle1 = new Ellipse2D.Double(
		// this.sourceAnchor.getX() - 1,
		// this.sourceAnchor.getY() - 1,
		// 2,
		// 2);

		// Shape circle2 = new Ellipse2D.Double(
		// this.sinkAnchor.getX() - 2,
		// this.sinkAnchor.getY() - 2,
		// 4,
		// 4);

		shapes.add(bezier);

		// fills.add(circle2);

		ShapePack pack = new ShapePack(EDrawingMode.STROKE, shapes);
		this.shapePacks.add(pack);
		// pack = new ShapePack(EDrawingMode.FILL, fills);
		// this.shapePacks.add(pack);
	}

	@Override
	public Element getSvgNode(Document doc, HashMap<String, Element> defs)
	{
		this.updateShapes();

		Element path = doc.createElement(ESvg.ELEMENT_PATH.$());

		path.setAttribute(ESvg.ATTRIB_PATH_DATA.$(),
				String.format(Locale.ENGLISH, "M %f %f C %f %f %f %f %f %f", //$NON-NLS-1$
						this.sourceAnchor.getX(),
						this.sourceAnchor.getY(),
						this.sourceControlPoint.getX(),
						this.sourceControlPoint.getY(),
						this.sinkControlPoint.getX(),
						this.sinkControlPoint.getY(),
						this.sinkAnchor.getX(),
						this.sinkAnchor.getY()
						));

		path.setAttribute(
				ESvg.ATTRIB_MARKER_END.$(),
				ESvg.VALUE_REF_URL.$(ESvgDefinitions.MARKER_CRICLE.$()));

		return path;
	}

	/** @return the control point for the sink anchor. */
	public Point2D getSinkControlPoint(boolean unstretched)
	{
		if (unstretched)
			return this.sinkControlPointUnstretched;
		return this.sinkControlPoint;
	}

	/** @return the control point for the source anchor. */
	public Point2D getSourceControlPoint(boolean unstretched)
	{
		if (unstretched)
			return this.sourceControlPointUnstretched;
		return this.sourceControlPoint;
	}

	/** @return the source anchor. */
	public Point2D getSourceAnchor()
	{
		return this.sourceAnchor;
	}

	/** @return the sink anchor. */
	public Point2D getSinkAnchor()
	{
		return this.sinkAnchor;
	}
}
