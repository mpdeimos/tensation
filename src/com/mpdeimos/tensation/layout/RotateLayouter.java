package com.mpdeimos.tensation.layout;

import com.mpdeimos.tensation.figure.TensorFigure;
import com.mpdeimos.tensation.model.TensorBase;
import com.mpdeimos.tensation.model.TensorConnection;
import com.mpdeimos.tensation.model.TensorConnectionAnchor;
import com.mpdeimos.tensation.util.MapUtil;
import com.mpdeimos.tensation.util.VecMath;

import java.awt.geom.Point2D;
import java.util.HashMap;
import java.util.Set;

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
			Set<TensorConnection> connections)
	{
		return optimizeRotation(positions, rotations, connections) != 0;
	}

	/**
	 * Optimizes the tensor rotation.
	 * 
	 * @param positions
	 *            HashMap for Tensor positions.
	 * @param rotations
	 *            HashMap for storing the new tensor rotations (= result) and
	 *            original rotations are read from.
	 * @param connections
	 *            Tensor connection list.
	 */
	public static double optimizeRotation(
			HashMap<TensorBase, Point2D> positions,
			HashMap<TensorBase, Double> rotations,
			Set<TensorConnection> connections)
	{
		HashMap<TensorBase, Double> rotationsClone = MapUtil.clone(rotations);
		double rotation = 0;

		for (TensorConnection connection : connections)
		{
			TensorConnectionAnchor sinkAnchor = connection.getSink();
			TensorConnectionAnchor sourceAnchor = connection.getSource();
			Point2D sink = positions.get(sinkAnchor.getTensor());
			Point2D source = positions.get(sourceAnchor.getTensor());

			Point2D sinkPoint = getConnectionPoint(
					positions,
					rotations,
					sinkAnchor);
			Point2D sourcePoint = getConnectionPoint(
					positions,
					rotations,
					sourceAnchor);

			rotation += updateRotation(
					rotationsClone,
					sink,
					source,
					sourcePoint,
					sourceAnchor.getTensor());
			rotation += updateRotation(
					rotationsClone,
					source,
					sink,
					sinkPoint,
					sinkAnchor.getTensor());
		}

		rotations.putAll(rotationsClone);
		return Math.abs(rotation);
	}

	/**
	 * Calculates the rotation between a tensor connection point and another
	 * tensor.
	 * 
	 * The result is stored within the rotation hash map.
	 */
	private static double updateRotation(
			HashMap<TensorBase, Double> rotations,
			Point2D otherTensor,
			Point2D tensor,
			Point2D connectionPoint,
			TensorBase otherTensorObj)
	{
		Point2D anchorPoint = VecMath.sub(
				VecMath.fresh(connectionPoint),
				tensor);
		Point2D otherPoint = VecMath.sub(
				VecMath.fresh(otherTensor),
				tensor);

		double rotation = rotations.get(otherTensorObj);
		double angle = 180 * halfAng(VecMath.ang(anchorPoint, otherPoint))
				/ (Math.PI * otherTensorObj.getOccupiedAnchors().size());
		// Division by # connected anchors is needed to normalize/weight the
		// rotation.

		rotation += angle;
		rotation %= 360;

		rotations.put(otherTensorObj, rotation);

		return angle;
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
			HashMap<TensorBase, Point2D> positions,
			HashMap<TensorBase, Double> rotations,
			TensorConnectionAnchor anchor)
	{
		double rot = rotations.get(anchor.getTensor());
		Point2D pos = positions.get(anchor.getTensor());
		Point2D connectionPoint = VecMath.fresh();
		TensorFigure.initAnchorPoints(
				anchor.getTensor(),
				anchor.getId(),
				connectionPoint,
				null,
				null,
				rot,
				pos);

		return connectionPoint;
	}
}
