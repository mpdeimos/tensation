package com.mpdeimos.tensor.figure;

import java.awt.Point;
import java.awt.Shape;
import java.awt.geom.CubicCurve2D;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Point2D;
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

		Point2D sink = new Point2D.Double();
		Point2D source = new Point2D.Double();

		EpsilonTensorFigure.initAnchorPoints(
				connection.getSource().getTensor(),
				connection.getSource().getId(),
				source,
				null);
		EpsilonTensorFigure.initAnchorPoints(
				connection.getSink().getTensor(),
				connection.getSink().getId(),
				sink,
				null);

		Point sinkCenter = connection.getSink().getTensor().getPosition();
		Point sourceCenter = connection.getSource().getTensor().getPosition();

		this.sourceControlPoint = new Point2D.Double();
		this.sinkControlPoint = new Point2D.Double();

		PointUtil.sub(sink, sinkCenter, this.sinkControlPoint);
		this.sinkControlPoint.setLocation(
				2 * this.sinkControlPoint.getX(),
				2 * this.sinkControlPoint.getY());
		PointUtil.move(this.sinkControlPoint, sink.getX(), sink.getY());

		PointUtil.sub(source, sourceCenter, this.sourceControlPoint);
		this.sourceControlPoint.setLocation(
				2 * this.sourceControlPoint.getX(),
				2 * this.sourceControlPoint.getY());
		PointUtil.move(this.sourceControlPoint, source.getX(), source.getY());

		// Shape line = new Line2D.Double(source, sink);

		CubicCurve2D bezier = new CubicCurve2D.Double();
		bezier.setCurve(
				source,
				this.sourceControlPoint,
				this.sinkControlPoint,
				sink);

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
				source.getX() - 1,
				source.getY() - 1,
				2,
				2);
		Shape circle2 = new Ellipse2D.Double(
				sink.getX() - 1,
				sink.getY() - 1,
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
}
