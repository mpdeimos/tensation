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
	public List<Connection> getConnectionSources()
	{
		List<Connection> connections = new ArrayList<Connection>();
		Point2D[] connectionPoints = ((EpsilonTensorFigure) getFigure()).getConnectionPoints();
		for (int i = 0; i < connectionPoints.length; i++)
		{
			if (i % 2 == 0)
				connections.add(new Connection(i, connectionPoints[i]));
		}
		return connections;
	}

	@Override
	public List<Connection> getConnectionSinks()
	{
		List<Connection> connections = new ArrayList<Connection>();
		Point2D[] connectionPoints = ((EpsilonTensorFigure) getFigure()).getConnectionPoints();
		for (int i = 0; i < connectionPoints.length; i++)
		{
			if (i % 2 == 1)
				connections.add(new Connection(i, connectionPoints[i]));
		}
		return connections;
	}
}
