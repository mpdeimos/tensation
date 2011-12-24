package com.mpdeimos.tensor.layout;

import com.mpdeimos.tensor.editpart.IEditPart;
import com.mpdeimos.tensor.editpart.feature.IConnectable;
import com.mpdeimos.tensor.figure.TensorFigure;
import com.mpdeimos.tensor.model.TensorBase;
import com.mpdeimos.tensor.model.TensorConnection;
import com.mpdeimos.tensor.model.TensorConnectionAnchor;
import com.mpdeimos.tensor.ui.DrawingCanvas;
import com.mpdeimos.tensor.util.MapUtil;
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
		HashMap<TensorBase, IConnectable> tensorsToEditParts = extractConnectables(
				this.canvas,
				positions);

		return optimizeRotation(rotations, connections, tensorsToEditParts);
	}

	/**
	 * Optimizes the tensor rotation.
	 * 
	 * @param rotations
	 *            HashMap for storing the new tensor rotations (= result) and
	 *            original rotations are read from.
	 * @param connections
	 *            Tensor connection list.
	 * @param tensorsToEditParts
	 *            a HashMap mapping from tensors to EditParts, use
	 *            extractConnectables for gathering this mapping.
	 * @return true if rotations were adjusted.
	 */
	public static boolean optimizeRotation(
			HashMap<TensorBase, Double> rotations,
			List<TensorConnection> connections,
			HashMap<TensorBase, IConnectable> tensorsToEditParts)
	{
		HashMap<TensorBase, Double> rotationsClone = MapUtil.clone(rotations);

		for (TensorConnection connection : connections)
		{
			TensorConnectionAnchor sinkAnchor = connection.getSink();
			TensorConnectionAnchor sourceAnchor = connection.getSource();
			TensorBase sink = sinkAnchor.getTensor();
			TensorBase source = sourceAnchor.getTensor();

			Point2D sinkPoint = getConnectionPoint(rotations, sinkAnchor);
			Point2D sourcePoint = getConnectionPoint(rotations, sourceAnchor);

			calculateRotation(rotationsClone, sink, source, sourcePoint);
			calculateRotation(rotationsClone, source, sink, sinkPoint);

		}

		rotations.putAll(rotationsClone);
		return true;
	}

	/** @return a HashMap containing the connectable editparts. */
	public static HashMap<TensorBase, IConnectable> extractConnectables(
			DrawingCanvas canvas,
			HashMap<TensorBase, Point2D> positions)
	{
		HashMap<TensorBase, IConnectable> tensorsToEditParts = new HashMap<TensorBase, IConnectable>();
		for (TensorBase tensor : positions.keySet())
		{
			inner: for (IEditPart part : canvas.getEditParts())
			{
				if (part.getModel() == tensor && part instanceof IConnectable)
				{
					tensorsToEditParts.put(tensor, (IConnectable) part);
					break inner;
				}
			}
		}
		return tensorsToEditParts;
	}

	/**
	 * Calculates the rotation between a tensor connection point and another
	 * tensor.
	 * 
	 * The result is stored within the rotation hash map.
	 */
	private static void calculateRotation(
			HashMap<TensorBase, Double> rotations,
			TensorBase otherTensor,
			TensorBase tensor,
			Point2D connectionPoint)
	{
		Point2D anchorPoint = VecMath.sub(
				VecMath.fresh(connectionPoint),
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
	private static double halfAng(double ang)
	{
		if (ang < Math.PI)
			return ang;

		return ang - 2 * Math.PI;
	}

	/** @return the connection point for a given anchor. */
	private static Point2D getConnectionPoint(
			HashMap<TensorBase, Double> rotations,
			TensorConnectionAnchor anchor)
	{
		double rot = rotations.get(anchor.getTensor());
		Point2D connectionPoint = VecMath.fresh();
		TensorFigure.initAnchorPoints(
				anchor.getTensor(),
				anchor.getId(),
				connectionPoint,
				null,
				null,
				rot);

		return connectionPoint;
	}
}
