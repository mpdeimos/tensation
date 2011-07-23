package com.mpdeimos.tensor.editpart;

import com.mpdeimos.tensor.editpart.feature.IConnectable;
import com.mpdeimos.tensor.editpart.feature.IMovable;
import com.mpdeimos.tensor.editpart.feature.IRotatable;
import com.mpdeimos.tensor.figure.TensorFigure;
import com.mpdeimos.tensor.model.IModelData;
import com.mpdeimos.tensor.model.TensorBase;
import com.mpdeimos.tensor.model.TensorConnectionAnchor;
import com.mpdeimos.tensor.model.TensorConnectionAnchor.EDirection;

import java.awt.Dimension;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;

/**
 * Base EditPart implementation for tensors.
 * 
 * @author mpdeimos
 * 
 */
public abstract class TensorEditPartBase extends EditPartBase implements
		IRotatable, IConnectable, IMovable
{

	/** Constructor. */
	public TensorEditPartBase(IModelData modelData)
	{
		super(modelData);
	}

	@Override
	public Point getPosition()
	{
		return ((TensorBase) (this.getModel())).getPosition();
	}

	@Override
	public void setPosition(Point p)
	{
		((TensorBase) (this.getModel())).setPosition(p);
	}

	@Override
	public double getRotation()
	{
		return ((TensorBase) (this.getModel())).getRotation();
	}

	@Override
	public void setRotation(double degrees)
	{
		((TensorBase) (this.getModel())).setRotation(degrees);
	}

	@Override
	public Dimension getRotationIndicatorOffset()
	{
		Rectangle r = this.getBoundingRectangle();
		return new Dimension((int) Math.sqrt(r.getWidth() * r.getWidth() / 4
				+ r.getHeight() * r.getHeight() / 4), 0);
	}

	@Override
	public List<ConnectionPoint> getConnectionSources()
	{
		TensorFigure figure = (TensorFigure) getFigure();
		Point2D[] connectionPoints = figure.getConnectionPoints();
		TensorBase tensor = (TensorBase) getModel();

		List<ConnectionPoint> connections = new ArrayList<ConnectionPoint>();
		for (TensorConnectionAnchor anchor : tensor.getAnchors())
		{
			if (anchor.getDirection() == EDirection.SOURCE)
				connections.add(new ConnectionPoint(
						anchor,
						connectionPoints[anchor.getId()]));
		}
		return connections;
	}

	@Override
	public List<ConnectionPoint> getConnectionSinks()
	{
		TensorFigure figure = (TensorFigure) getFigure();
		Point2D[] connectionPoints = figure.getConnectionPoints();
		TensorBase tensor = (TensorBase) getModel();

		List<ConnectionPoint> connections = new ArrayList<ConnectionPoint>();
		for (TensorConnectionAnchor anchor : tensor.getAnchors())
		{
			if (anchor.getDirection() == EDirection.SINK)
				connections.add(new ConnectionPoint(
						anchor,
						connectionPoints[anchor.getId()]));
		}
		return connections;
	}

}