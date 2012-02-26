package com.mpdeimos.tensation.editpart;

import com.mpdeimos.tensation.editpart.feature.IDuplicatable;
import com.mpdeimos.tensation.editpart.feature.IMovable;
import com.mpdeimos.tensation.editpart.feature.IOperatorChangable;
import com.mpdeimos.tensation.figure.IFigure;
import com.mpdeimos.tensation.figure.OperatorFigure;
import com.mpdeimos.tensation.model.EOperation;
import com.mpdeimos.tensation.model.IModelData;
import com.mpdeimos.tensation.model.Operator;
import com.mpdeimos.tensation.ui.Application;

import java.awt.Point;
import java.util.Collection;
import java.util.HashMap;

/**
 * EditPart implementation for operators.
 * 
 * @author mpdeimos
 * 
 */
public class OperatorEditPart extends EditPartBase implements
		IMovable, IDuplicatable, IOperatorChangable
{

	/** Constructor. */
	public OperatorEditPart(IModelData modelData)
	{
		super(modelData);
	}

	@Override
	public Point getPosition()
	{
		return ((Operator) (this.getModel())).getPosition();
	}

	@Override
	public void setPosition(Point p)
	{
		((Operator) (this.getModel())).setPosition(p);
	}

	@Override
	public IModelData duplicateModel(
			HashMap<IDuplicatable, IModelData> duplicates)
	{
		Operator model = (Operator) getModel();
		model = model.duplicate(Application.getApp().getActiveCanvas().getModel());
		return model;
	}

	@Override
	public int getDuplicatePriority()
	{
		return 10;
	}

	@Override
	public Collection<? extends IDuplicatable> getLinkedEditParts()
	{
		return null;
	}

	@Override
	protected IFigure createFigure()
	{
		return new OperatorFigure(this);
	}

	@Override
	public void setOperation(EOperation operation)
	{
		((Operator) (this.getModel())).setOperation(operation);

	}

	@Override
	public EOperation getOperator()
	{
		return ((Operator) (this.getModel())).getOperation();
	}
}