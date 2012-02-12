package com.mpdeimos.tensation.model;

import com.mpdeimos.tensation.figure.AppearanceContainer;
import com.mpdeimos.tensation.impex.export.Export;
import com.mpdeimos.tensation.util.StringUtil;
import com.mpdeimos.tensation.util.VecMath;

import java.awt.geom.Point2D;

import resources.R;

/**
 * Represents a connection between two Tensors
 * 
 * @author mpdeimos
 * 
 */
public class TensorConnection extends ModelDataBase implements
		AppearanceContainer.IAppearanceHolder
{
	/** Start tensor anchor of the connection. */
	private TensorConnectionAnchor source;

	/** End tensor anchor of the connection. */
	private TensorConnectionAnchor sink;

	/** The relative distance of the source anchor. */
	@Export(name = "source.distance")
	private double sourceDistance = 2;

	/** The relative distance of the sink anchor. */
	@Export(name = "sink.distance")
	private double sinkDistance = 2;

	/** The appearance container of this object. */
	private final AppearanceContainer appearanceContainer = new AppearanceContainer();

	/** The label of the connection. */
	@Export
	private String label;

	/** The label position (relative to an anchor). */
	@Export(name = "label.position", set = "setPosition")
	private Point2D labelPosition;

	/** the label attachment anchor. */
	@Export(name = "label.attachment")
	private EDirection labelAttachment;

	/**
	 * Constructor.
	 */
	public TensorConnection(
			ModelDataBase parent,
			TensorConnectionAnchor source,
			TensorConnectionAnchor sink)
	{
		super(parent);

		if (source.getDirection() == EDirection.SOURCE)
			this.source = source;
		else
			this.sink = source;

		if (sink.getDirection() == EDirection.SINK)
			this.sink = sink;
		else
			this.source = sink;

		if (this.source == null || this.sink == null)
			throw new IllegalArgumentException(
					R.string.EXCEPTION_MODEL_CONNECTION_MISSMATCH.string());

		this.renew();
	}

	@Override
	public boolean remove()
	{
		this.sink.releaseAnchor(this);
		this.source.releaseAnchor(this);

		return super.remove();
	}

	/** Sets the anchors of the connected tensors occupied. */
	public void renew()
	{
		this.source.occupyAnchor(this);
		this.sink.occupyAnchor(this);
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

	@Override
	public AppearanceContainer getAppearanceContainer()
	{
		return this.appearanceContainer;
	}

	/** @return the label of this tensor. */
	public String getLabel()
	{
		return this.label;
	}

	/** sets the label of this tensor. */
	public void setLabel(String label)
	{
		if (StringUtil.isNullOrEmpty(label))
		{
			label = null;
			this.labelPosition = null;
			this.labelAttachment = null;
		}

		this.label = label;
		this.fireOnModelDataChanged(this);
	}

	/** @return sets position of the label, if a label is set. */
	public void setLabelPosition(Point2D position)
	{
		if (this.label == null)
			return;

		if (this.labelPosition == null)
			this.labelPosition = VecMath.fresh();

		this.labelPosition.setLocation(position);
		this.fireOnModelDataChanged(this);
	}

	/**
	 * @return the position of the label (a copy of), null if no label is set or
	 *         position was not set.
	 */
	public Point2D getLabelPosition()
	{
		if (this.labelPosition == null)
			return null;

		return VecMath.fresh(this.labelPosition);
	}

	/**
	 * @return the label attachment anchor. null if no label, or not previously
	 *         set.
	 */
	public EDirection getLabelAttachment()
	{
		return this.labelAttachment;
	}

	/**
	 * sets the label attachment to the given anchor.
	 */
	public void setLabelAttachment(EDirection anchor)
	{
		this.labelAttachment = anchor;
	}
}
