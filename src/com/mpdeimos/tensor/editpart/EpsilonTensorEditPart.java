package com.mpdeimos.tensor.editpart;

import java.awt.Dimension;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;

import com.mpdeimos.tensor.editpart.feature.IConnectable;
import com.mpdeimos.tensor.editpart.feature.IMovable;
import com.mpdeimos.tensor.editpart.feature.IRotatable;
import com.mpdeimos.tensor.figure.EpsilonTensorFigure;
import com.mpdeimos.tensor.figure.IFigure;
import com.mpdeimos.tensor.model.EpsilonTensor;
import com.mpdeimos.tensor.model.IModelData;
import com.mpdeimos.tensor.model.TensorConnectionAnchor;
import com.mpdeimos.tensor.model.TensorConnectionAnchor.EDirection;

/**
 * EditPart class for an epsilon tensor.
 * 
 * @author mpdeimos
 * 
 */
public class EpsilonTensorEditPart extends EditPartBase implements
		IRotatable, IConnectable, IMovable
{
	/** Constructor. */
	public EpsilonTensorEditPart(IModelData modelData)
	{
		super(modelData);
	}

	@Override
	protected IFigure createFigure()
	{
		return new EpsilonTensorFigure(this);
	}

	@Override
	public Point getPosition()
	{
		return ((EpsilonTensor) (this.getModelData())).getPosition();
	}

	@Override
	public void setPosition(Point p)
	{
		((EpsilonTensor) (this.getModelData())).setPosition(p);
	}

	@Override
	public double getRotation()
	{
		return ((EpsilonTensor) (this.getModelData())).getRotation();
	}

	@Override
	public void setRotation(double degrees)
	{
		((EpsilonTensor) (this.getModelData())).setRotation(degrees);
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
		EpsilonTensorFigure figure = (EpsilonTensorFigure) getFigure();
		Point2D[] connectionPoints = figure.getConnectionPoints();
		EpsilonTensor tensor = (EpsilonTensor) getModelData();

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
		EpsilonTensorFigure figure = (EpsilonTensorFigure) getFigure();
		Point2D[] connectionPoints = figure.getConnectionPoints();
		EpsilonTensor tensor = (EpsilonTensor) getModelData();

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
