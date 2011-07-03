/**
 * 
 */
package com.mpdeimos.tensor.editpart;

import com.mpdeimos.tensor.figure.IFigure;
import com.mpdeimos.tensor.figure.TensorConnectionFigure;
import com.mpdeimos.tensor.model.IModelData;
import com.mpdeimos.tensor.model.ModelChangedAdapter;
import com.mpdeimos.tensor.model.TensorConnection;

/**
 * EditPart for Tensor Connections
 * 
 * @author mpdeimos
 * 
 */
public class TensorConnectionEditPart extends EditPartBase
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
}
