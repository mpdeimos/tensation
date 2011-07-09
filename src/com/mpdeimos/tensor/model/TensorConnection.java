package com.mpdeimos.tensor.model;

/**
 * Represents a connection between two Tensors
 * 
 * @author mpdeimos
 * 
 */
public class TensorConnection extends ModelDataBase
{
	/** Start tensor anchor of the connection. */
	private final TensorConnectionAnchor source;

	/** End tensor anchor of the connection. */
	private final TensorConnectionAnchor sink;

	/**
	 * Constructor.
	 */
	public TensorConnection(
			ModelDataBase parent,
			TensorConnectionAnchor source,
			TensorConnectionAnchor sink)
	{
		super(parent);

		this.source = source;
		source.occupyAnchor(this);

		this.sink = sink;
		sink.occupyAnchor(this);
	}

	@Override
	public boolean remove()
	{
		this.sink.releaseAnchor(this);
		this.source.releaseAnchor(this);

		return super.remove();
	}

	/**
	 * @return the sink of the connection
	 */
	public TensorConnectionAnchor getSink()
	{
		return this.sink;
	}

	/**
	 * @return the source of the connection
	 */
	public TensorConnectionAnchor getSource()
	{
		return this.source;
	}
}
