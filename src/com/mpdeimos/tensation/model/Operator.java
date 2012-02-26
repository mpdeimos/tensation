package com.mpdeimos.tensation.model;

import com.mpdeimos.tensation.figure.AppearanceContainer;
import com.mpdeimos.tensation.figure.AppearanceContainer.IAppearanceHolder;
import com.mpdeimos.tensation.impex.export.Export;

import java.awt.Point;

/**
 * model class for operators.
 * 
 * @author mpdeimos
 * 
 */
public class Operator extends ModelDataBase implements
		IAppearanceHolder
{
	/** Position of the object in screen coordinates. */
	@Export
	protected Point position;

	/** Operation type. */
	@Export
	private EOperation operation = EOperation.PLUS;

	/** The appearance container of this object. */
	private final AppearanceContainer appearanceContainer = new AppearanceContainer();

	/** Constructor. */
	public Operator(IModelData parent)
	{
		super(parent);

		this.position = new Point(0, 0);
	}

	/**
	 * @return the position of this object.
	 */
	public Point getPosition()
	{
		return this.position;
	}

	/** Sets the position of the current point. */
	public void setPosition(Point p)
	{
		this.position.setLocation(p);
		fireOnModelDataChanged(this);
	}

	/** Duplicates this operator with the current model as parent. */
	public Operator duplicate()
	{
		return duplicate(this.parent);
	}

	/** Duplicates this operator with the given model as parent. */
	public Operator duplicate(IModelData root)
	{
		Operator operator = new Operator(root);
		operator.setPosition(getPosition());
		return operator;
	}

	@Override
	public AppearanceContainer getAppearanceContainer()
	{
		return this.appearanceContainer;
	}

	/** sets the operation type of this operator. */
	public void setOperation(EOperation operation)
	{
		this.operation = operation;
	}

	/** @return the operation type of this operator. */
	public EOperation getOperation()
	{
		return this.operation;
	}

}