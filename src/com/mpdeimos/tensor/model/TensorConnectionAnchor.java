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
	private final TensorBase tensor;

	/** The connection of this anchor. */
	TensorConnection connection = null;

	/** Direction of this connection. */
	public enum EDirection
	{
		/** outgoing connection. */
		SOURCE,
		/** ingoing connection. */
		SINK
	}

	/** Constructor. */
	public TensorConnectionAnchor(TensorBase tensor, EDirection direction)
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
	public TensorBase getTensor()
	{
		return this.tensor;
	}

	/** @return if the connection could be made. */
	public boolean occupyAnchor(TensorConnection connection)
	{
		if (this.connection != null)
			return false;

		this.connection = connection;

		return true;
	}

	/** @return if the connection could be made. */
	public boolean releaseAnchor(TensorConnection connection)
	{
		if (this.connection != connection)
			return false;

		this.connection = null;

		return true;
	}

	/** @return true if this anchor has an connection. */
	public boolean isOccopied()
	{
		return this.connection != null;
	}

	/** Do stuff upon deleting the parent tensor. */
	/* package */void doOnTensorRemoved()
	{
		if (this.connection != null)
			this.connection.remove();
	}
}
