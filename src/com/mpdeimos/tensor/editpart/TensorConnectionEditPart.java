/**
 * 
 */
package com.mpdeimos.tensor.editpart;

import com.mpdeimos.tensor.editpart.feature.IConnectionControl;
import com.mpdeimos.tensor.editpart.feature.IDuplicatable;
import com.mpdeimos.tensor.figure.IFigure;
import com.mpdeimos.tensor.figure.TensorConnectionFigure;
import com.mpdeimos.tensor.model.IModelData;
import com.mpdeimos.tensor.model.ModelChangedAdapter;
import com.mpdeimos.tensor.model.ModelRoot;
import com.mpdeimos.tensor.model.TensorBase;
import com.mpdeimos.tensor.model.TensorConnection;
import com.mpdeimos.tensor.ui.Application;
import com.mpdeimos.tensor.ui.DrawingCanvas;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * EditPart for Tensor Connections
 * 
 * @author mpdeimos
 * 
 */
public class TensorConnectionEditPart extends EditPartBase implements
		IConnectionControl, IDuplicatable
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

	@Override
	public IModelData duplicateModel(
			HashMap<IDuplicatable, IModelData> duplicates)
	{
		DrawingCanvas canvas = Application.getApp().getActiveCanvas();
		TensorConnection connection = (TensorConnection) this.getModel();
		DrawingCanvas canvasOrig = ((ModelRoot) connection.getParent()).getDrawingCanvas();

		TensorEditPartBase sourceOrig = (TensorEditPartBase) canvasOrig.getEditPartForModelData(connection.getSource().getTensor());
		TensorEditPartBase sinkOrig = (TensorEditPartBase) canvasOrig.getEditPartForModelData(connection.getSink().getTensor());

		TensorBase source = (TensorBase) duplicates.get(sourceOrig);
		TensorBase sink = (TensorBase) duplicates.get(sinkOrig);

		return new TensorConnection(
				canvas.getModel(),
				source.getAnchors().get(connection.getSource().getId()),
				sink.getAnchors().get(connection.getSink().getId()));
	}

	@Override
	public int getDuplicatePriority()
	{
		return 100;
	}

	@Override
	public List<IDuplicatable> getLinkedEditParts()
	{
		List<IDuplicatable> tensors = new ArrayList<IDuplicatable>();

		TensorConnection connection = (TensorConnection) this.getModel();
		DrawingCanvas canvas = Application.getApp().getActiveCanvas();

		tensors.add((IDuplicatable) canvas.getEditPartForModelData(connection.getSource().getTensor()));
		tensors.add((IDuplicatable) canvas.getEditPartForModelData(connection.getSink().getTensor()));

		return tensors;
	}
}
