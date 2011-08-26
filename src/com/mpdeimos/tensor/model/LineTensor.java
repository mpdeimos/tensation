package com.mpdeimos.tensor.model;

import com.mpdeimos.tensor.model.TensorConnectionAnchor.EDirection;

import java.awt.Point;

/**
 * Tensor representing a simple line.
 * 
 * @author mpdeimos
 * 
 */
public class LineTensor extends TensorBase
{
	/** Constructor. */
	public LineTensor(IModelData parent, Point p)
	{
		super(parent);

		this.anchors.add(new TensorConnectionAnchor(this, 0, EDirection.SINK));

		setPosition(p);
	}

	@Override
	public TensorBase duplicate(IModelData root)
	{
		TensorBase tensor = new LineTensor(root, new Point(this.getPosition()));

		return tensor;
	}
}
