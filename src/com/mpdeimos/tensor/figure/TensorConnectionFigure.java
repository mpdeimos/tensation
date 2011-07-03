package com.mpdeimos.tensor.figure;

import java.awt.Shape;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;

import com.mpdeimos.tensor.editpart.EditPartFactory;
import com.mpdeimos.tensor.editpart.IEditPart;
import com.mpdeimos.tensor.figure.ShapePack.EDrawingMode;
import com.mpdeimos.tensor.model.TensorConnection;

/**
 * Figure class for Tensor connections.
 * 
 * @author mpdeimos
 * 
 */
public class TensorConnectionFigure extends FigureBase
{
	/** EditPartFactory for this figure. TODO workaround */
	static EditPartFactory editPartFactory = new EditPartFactory();

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

		Shape line = new Line2D.Double(source, sink);
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

		shapes.add(line);
		shapes.add(circle1);
		shapes.add(circle2);

		ShapePack pack = new ShapePack(EDrawingMode.STROKE, shapes);
		this.shapePacks.add(pack);
	}
}
