/**
 * 
 */
package com.mpdeimos.tensation.editpart;

import com.mpdeimos.tensation.editpart.feature.IConnectionControl;
import com.mpdeimos.tensation.editpart.feature.IDuplicatable;
import com.mpdeimos.tensation.editpart.feature.ILabelable;
import com.mpdeimos.tensation.figure.IFigure;
import com.mpdeimos.tensation.figure.TensorConnectionFigure;
import com.mpdeimos.tensation.model.IModelData;
import com.mpdeimos.tensation.model.ModelChangedAdapter;
import com.mpdeimos.tensation.model.ModelRoot;
import com.mpdeimos.tensation.model.TensorBase;
import com.mpdeimos.tensation.model.TensorConnection;
import com.mpdeimos.tensation.ui.Application;
import com.mpdeimos.tensation.ui.DrawingCanvas;

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
		IConnectionControl, IDuplicatable, ILabelable
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

	@Override
	public void setLabel(String label)
	{
		((TensorConnection) this.getModel()).setLabel(label);
	}

	@Override
	public String getLabel()
	{
		return ((TensorConnection) this.getModel()).getLabel();
	}
}
