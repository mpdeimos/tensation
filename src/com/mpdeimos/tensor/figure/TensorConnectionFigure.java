package com.mpdeimos.tensor.figure;

import java.awt.Point;
import java.awt.Shape;
import java.awt.geom.CubicCurve2D;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Point2D;
import java.awt.geom.Point2D.Double;
import java.util.ArrayList;
import java.util.List;

import com.mpdeimos.tensor.editpart.IEditPart;
import com.mpdeimos.tensor.figure.ShapePack.EDrawingMode;
import com.mpdeimos.tensor.model.TensorConnection;
import com.mpdeimos.tensor.util.PointUtil;

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
	private Double sourceControlPointUnstretched;

	/** the position of the sink anchor (unstretched). */
	private Double sinkControlPointUnstretched;

	/** Constructor. */
	public TensorConnectionFigure(IEditPart editPart)
	{
		super(editPart);
	}

	@Override
	public void updateShapes()
	{
		super.updateShapes();

		TensorConnection connection = (TensorConnection) this.editPart.getModelData();
		List<Shape> shapes = new ArrayList<Shape>(1);
		// List<Shape> cpts = new ArrayList<Shape>(1);

		this.sinkAnchor = new Point2D.Double();
		this.sourceAnchor = new Point2D.Double();

		EpsilonTensorFigure.initAnchorPoints(
				connection.getSource().getTensor(),
				connection.getSource().getId(),
				this.sourceAnchor,
				null);
		EpsilonTensorFigure.initAnchorPoints(
				connection.getSink().getTensor(),
				connection.getSink().getId(),
				this.sinkAnchor,
				null);

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

		// Shape line = new Line2D.Double(source, sink);

		CubicCurve2D bezier = new CubicCurve2D.Double();
		bezier.setCurve(
				this.sourceAnchor,
				this.sourceControlPoint,
				this.sinkControlPoint,
				this.sinkAnchor);

		// Shape cpt1 = new Ellipse2D.Double(
		// this.sinkControlPoint.getX() - 1,
		// this.sinkControlPoint.getY() - 1,
		// 2,
		// 2);
		// Shape cpt2 = new Ellipse2D.Double(
		// this.sourceControlPoint.getX() - 1,
		// this.sourceControlPoint.getY() - 1,
		// 2,
		// 2);

		Shape circle1 = new Ellipse2D.Double(
				this.sourceAnchor.getX() - 1,
				this.sourceAnchor.getY() - 1,
				2,
				2);
		Shape circle2 = new Ellipse2D.Double(
				this.sinkAnchor.getX() - 1,
				this.sinkAnchor.getY() - 1,
				2,
				2);

		// shapes.add(line);
		shapes.add(bezier);
		shapes.add(circle1);
		shapes.add(circle2);

		// cpts.add(cpt1);
		// cpts.add(cpt2);

		ShapePack pack = new ShapePack(EDrawingMode.STROKE, shapes);
		this.shapePacks.add(pack);

		// ShapePack pack2 = new ShapePack(EDrawingMode.STROKE, cpts);
		// this.shapePacks.add(pack2);
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
