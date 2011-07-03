/**
 * 
 */
package com.mpdeimos.tensor.model;

/**
 * Represents a connection anchor for a EpsilonTensor.
 * 
 * @author mpdeimos
 * 
 */
public class TensorConnectionAnchor
{
	/** the direction of this anchor. */
	private final EDirection direction;

	/** the unique id of this anchor. */
	private final int id;

	/** the tensor of this anchor. */
	private final EpsilonTensor tensor;

	/** Direction of this connection. */
	public enum EDirection
	{
		/** outgoing connection. */
		SOURCE,
		/** ingoing connection. */
		SINK
	}

	/** Constructor. */
	public TensorConnectionAnchor(EpsilonTensor tensor, EDirection direction)
	{
		this.tensor = tensor;
		this.direction = direction;
		this.id = tensor.getAnchors().size();
	}

	/** @return the direction of this anchor. */
	public EDirection getDirection()
	{
		return this.direction;
	}

	/** @return the id of this anchor. */
	public int getId()
	{
		return this.id;
	}

	/** @return the tensor of this anchor. */
	public EpsilonTensor getTensor()
	{
		return this.tensor;
	}
}
