package com.mpdeimos.tensor.model;

import com.mpdeimos.tensor.model.TensorConnectionAnchor.EDirection;

import java.awt.Point;

/**
 * Represents an Epsilon Tensor (ok, will do so in the future, now we're just
 * testing stuff)
 * 
 * @author mpdeimos
 * 
 */
public class EpsilonTensor extends TensorBase
{
	/**
	 * Constructor.
	 */
	public EpsilonTensor(IModelData parent, EDirection direction)
	{
		this(parent, new Point(0, 0), direction);
	}

	/**
	 * Constructor w/ initial coordinates.
	 */
	public EpsilonTensor(
			IModelData parent,
			Point position,
			EDirection direction)
	{
		super(parent);

		int id = 0;
		this.anchors.add(new TensorConnectionAnchor(this, id++, direction));
		this.anchors.add(new TensorConnectionAnchor(this, id++, direction));
		this.anchors.add(new TensorConnectionAnchor(this, id++, direction));

		setPosition(position);
	}

	@Override
	public EpsilonTensor duplicate(IModelData root)
	{
		EDirection direction = this.anchors.get(0).getDirection();
		EpsilonTensor tensor = new EpsilonTensor(
				root,
				new Point(this.position),
				direction);

		return tensor;
	}
}
