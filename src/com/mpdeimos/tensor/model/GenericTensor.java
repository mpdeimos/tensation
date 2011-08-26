package com.mpdeimos.tensor.model;

import com.mpdeimos.tensor.model.TensorConnectionAnchor.EDirection;

import java.awt.Point;
import java.util.List;

/**
 * Generic Tensor implementation (used just for loading atm).
 * 
 * @author mpdeimos
 * 
 */
public class GenericTensor extends TensorBase
{
	/** Constructor. */
	public GenericTensor(
			IModelData parent,
			List<EDirection> anchorDirections)
	{
		this(parent, (EDirection[]) anchorDirections.toArray());
	}

	/** Constructor. */
	public GenericTensor(IModelData parent, EDirection[] anchorDirections)
	{
		this(parent);
		List<TensorConnectionAnchor> anchors = TensorConnectionAnchor.getAnchorListFromDirections(
				this,
				anchorDirections);
		this.anchors.addAll(anchors);
	}

	/** Constructor. */
	public GenericTensor(
			IModelData parent,
			Point position,
			List<TensorConnectionAnchor> anchors)
	{
		this(parent);
		this.anchors.addAll(anchors);
		setPosition(position);
	}

	/** Constructor. */
	public GenericTensor(IModelData parent)
	{
		super(parent);
	}

	@Override
	public TensorBase duplicate(IModelData root)
	{
		GenericTensor tensor = new GenericTensor(
				root,
				getPosition(),
				this.anchors);

		tensor.setRotation(getRotation());

		return tensor;
	}
}
