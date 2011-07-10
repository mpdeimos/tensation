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

	/** The relative distance of the source anchor. */
	private double sourceDistance = 2;

	/** The relative distance of the sink anchor. */
	private double sinkDistance = 2;

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

	/** sets the control point distance for the source anchor. */
	public void setSourceControlPointDistance(double d)
	{
		this.sourceDistance = d;
		fireOnModelDataChanged(this);
	}

	/** sets the control point distance for the sink anchor. */
	public void setSinkControlPointDistance(double d)
	{
		this.sinkDistance = d;
		fireOnModelDataChanged(this);
	}

	/** @return the relative distance of the source anchor. */
	public double getSourceDistance()
	{
		return this.sourceDistance;
	}

	/** @return the relative distance of the sink anchor. */
	public double getSinkDistance()
	{
		return this.sinkDistance;
	}
}
