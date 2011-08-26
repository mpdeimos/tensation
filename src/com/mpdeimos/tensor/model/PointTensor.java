package com.mpdeimos.tensor.model;

import com.mpdeimos.tensor.model.TensorConnectionAnchor.EDirection;

import java.awt.Point;

/**
 * Tensor representing a point.
 * 
 * @author mpdeimos
 * 
 */
public class PointTensor extends TensorBase
{
	/** Constructor. */
	public PointTensor(IModelData parent, Point p)
	{
		super(parent);

		this.anchors.add(new TensorConnectionAnchor(this, 0, EDirection.SOURCE));

		setPosition(p);
	}

	@Override
	public TensorBase duplicate(IModelData root)
	{
		TensorBase tensor = new PointTensor(root, new Point(this.getPosition()));

		return tensor;
	}
}
