package com.mpdeimos.tensation.model;

import java.awt.Point;
import java.util.ArrayList;
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
		this(
				parent,
				anchorDirections.toArray(new EDirection[anchorDirections.size()]));
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
	public GenericTensor(
			IModelData parent,
			Point position,
			EDirection[] anchorDirections)
	{
		this(parent, anchorDirections);
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
		List<EDirection> directions = new ArrayList<EDirection>(
				this.anchors.size());

		for (TensorConnectionAnchor anchor : this.anchors)
		{
			directions.add(anchor.getDirection());
		}

		GenericTensor tensor = new GenericTensor(
				root,
				directions);

		tensor.setRotation(getRotation());
		tensor.setPosition(getPosition());
		tensor.setLabel(getLabel());

		tensor.getAppearanceContainer().setValues(
				getAppearanceContainer().getValues());

		return tensor;
	}
}
