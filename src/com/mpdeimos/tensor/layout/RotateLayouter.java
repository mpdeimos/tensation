package com.mpdeimos.tensor.layout;

import com.mpdeimos.tensor.editpart.IEditPart;
import com.mpdeimos.tensor.editpart.feature.IConnectable;
import com.mpdeimos.tensor.editpart.feature.IConnectable.ConnectionPoint;
import com.mpdeimos.tensor.model.TensorBase;
import com.mpdeimos.tensor.model.TensorConnection;
import com.mpdeimos.tensor.model.TensorConnectionAnchor;
import com.mpdeimos.tensor.util.VecMath;

import java.awt.geom.Point2D;
import java.util.HashMap;
import java.util.List;

import resources.R;

/**
 * Randomized Graph Layouting
 * 
 * @author mpdeimos
 */
public class RotateLayouter extends LayouterBase
{
	/** Constructor. */
	public RotateLayouter()
	{
		super(R.string.LAYOUT_ROTATE.string());
	}

	@Override
	public boolean layout(
			HashMap<TensorBase, Point2D> positions,
			HashMap<TensorBase, Double> rotations,
			List<TensorConnection> connections)
	{
		HashMap<TensorBase, IConnectable> tensorsToEditParts = new HashMap<TensorBase, IConnectable>();
		for (TensorBase tensor : positions.keySet())
		{
			inner: for (IEditPart part : this.canvas.getEditParts())
			{
				if (part.getModel() == tensor && part instanceof IConnectable)
				{
					tensorsToEditParts.put(tensor, (IConnectable) part);
					break inner;
				}
			}
		}

		for (TensorConnection connection : connections)
		{
			TensorConnectionAnchor sinkAnchor = connection.getSink();
			TensorConnectionAnchor sourceAnchor = connection.getSource();
			TensorBase sink = sinkAnchor.getTensor();
			TensorBase source = sourceAnchor.getTensor();

			ConnectionPoint sinkPoint = getConnectionPoint(
					sinkAnchor,
					tensorsToEditParts.get(sink));
			ConnectionPoint sourcePoint = getConnectionPoint(
					sourceAnchor,
					tensorsToEditParts.get(source));

			calculateRotation(rotations, sink, source, sourcePoint);
			calculateRotation(rotations, source, sink, sinkPoint);

		}
		return true;
	}

	/**
	 * Calculates the rotation between a tensor connection point and another
	 * tensor.
	 * 
	 * The result is stored within the rotation hash map.
	 */
	private void calculateRotation(
			HashMap<TensorBase, Double> rotations,
			TensorBase otherTensor,
			TensorBase tensor,
			ConnectionPoint connectionPoint)
	{
		Point2D anchorPoint = VecMath.sub(
				VecMath.fresh(connectionPoint.getPoint()),
				tensor.getPosition());
		Point2D otherPoint = VecMath.sub(
				VecMath.fresh(otherTensor.getPosition()),
				tensor.getPosition());

		double rotation = rotations.get(tensor);

		rotation += 180 * halfAng(VecMath.ang(anchorPoint, otherPoint))
				/ (Math.PI * tensor.getOccupiedAnchors().size());
		// Division by # connected anchors is needed to normalize/weight the
		// rotation.

		rotations.put(tensor, rotation);
	}

	/** Converts a 0..2PI angle to -PI..PI */
	private double halfAng(double ang)
	{
		if (ang < Math.PI)
			return ang;

		return ang - 2 * Math.PI;
	}

	/** @return the connection point for a given anchor. */
	private ConnectionPoint getConnectionPoint(
			TensorConnectionAnchor anchor,
			IConnectable editPart)
	{
		ConnectionPoint p2 = null;
		for (ConnectionPoint pp : editPart.getConnectionSinks())
		{
			if (pp.getAnchor() == anchor)
			{
				p2 = pp;
				break;
			}
		}
		if (p2 == null)
		{
			for (ConnectionPoint pp : editPart.getConnectionSources())
			{
				if (pp.getAnchor() == anchor)
				{
					p2 = pp;
					break;
				}
			}
		}
		return p2;
	}
}
