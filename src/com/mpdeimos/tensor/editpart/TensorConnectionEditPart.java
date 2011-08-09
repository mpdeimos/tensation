/**
 * 
 */
package com.mpdeimos.tensor.editpart;

import com.mpdeimos.tensor.editpart.feature.IConnectionControl;
import com.mpdeimos.tensor.figure.IFigure;
import com.mpdeimos.tensor.figure.TensorConnectionFigure;
import com.mpdeimos.tensor.model.IModelData;
import com.mpdeimos.tensor.model.ModelChangedAdapter;
import com.mpdeimos.tensor.model.TensorConnection;

import java.awt.geom.Point2D;

/**
 * EditPart for Tensor Connections
 * 
 * @author mpdeimos
 * 
 */
public class TensorConnectionEditPart extends EditPartBase implements
		IConnectionControl
{
	/** Constructor. */
	public TensorConnectionEditPart(IModelData modelData)
	{
		super(modelData);

		ModelChangedListener listener = new ModelChangedListener();

		TensorConnection connection = (TensorConnection) modelData;

		connection.getSink().getTensor().addModelChangedListener(listener);
		connection.getSource().getTensor().addModelChangedListener(listener);
	}

	@Override
	protected IFigure createFigure()
	{
		return new TensorConnectionFigure(this);
	}

	/** Private model change listener. */
	private class ModelChangedListener extends ModelChangedAdapter
	{
		@Override
		public void onModelChanged(IModelData model)
		{
			getFigure().redraw();
		}
	}

	@Override
	public Point2D getSourceControlPoint(boolean unstretched)
	{
		return ((TensorConnectionFigure) this.getFigure()).getSourceControlPoint(unstretched);
	}

	@Override
	public Point2D getSinkControlPoint(boolean unstretched)
	{
		return ((TensorConnectionFigure) this.getFigure()).getSinkControlPoint(unstretched);
	}

	@Override
	public Point2D getSourceAnchor()
	{
		return ((TensorConnectionFigure) this.getFigure()).getSourceAnchor();
	}

	@Override
	public Point2D getSinkAnchor()
	{
		return ((TensorConnectionFigure) this.getFigure()).getSinkAnchor();
	}

	@Override
	public void setSourceControlPointDistance(double d)
	{
		((TensorConnection) this.getModel()).setSourceControlPointDistance(d);
	}

	@Override
	public void setSinkControlPointDistance(double d)
	{
		((TensorConnection) this.getModel()).setSinkControlPointDistance(d);
	}

	@Override
	public double getSourceControlPointDistance()
	{
		return ((TensorConnection) this.getModel()).getSourceDistance();
	}

	@Override
	public double getSinkControlPointDistance()
	{
		return ((TensorConnection) this.getModel()).getSinkDistance();
	}
}
