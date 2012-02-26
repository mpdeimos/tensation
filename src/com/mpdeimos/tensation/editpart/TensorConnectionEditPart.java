/**
 * 
 */
package com.mpdeimos.tensation.editpart;

import com.mpdeimos.tensation.editpart.feature.IConnectionControl;
import com.mpdeimos.tensation.editpart.feature.IDuplicatable;
import com.mpdeimos.tensation.editpart.feature.ILabeled;
import com.mpdeimos.tensation.editpart.feature.IMovableLabel;
import com.mpdeimos.tensation.editpart.feature.IRestructurable;
import com.mpdeimos.tensation.figure.IFigure;
import com.mpdeimos.tensation.figure.TensorConnectionFigure;
import com.mpdeimos.tensation.impex.export.ExportHandler;
import com.mpdeimos.tensation.model.EDirection;
import com.mpdeimos.tensation.model.IModelData;
import com.mpdeimos.tensation.model.ModelChangedAdapter;
import com.mpdeimos.tensation.model.ModelRoot;
import com.mpdeimos.tensation.model.TensorBase;
import com.mpdeimos.tensation.model.TensorConnection;
import com.mpdeimos.tensation.ui.Application;
import com.mpdeimos.tensation.ui.DrawingCanvas;
import com.mpdeimos.tensation.util.VecMath;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * EditPart for Tensor Connections
 * 
 * @author mpdeimos
 * 
 */
public class TensorConnectionEditPart extends EditPartBase implements
		IMovableLabel, IConnectionControl, IDuplicatable, ILabeled,
		IRestructurable
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

		TensorConnection tc = new TensorConnection(
				canvas.getModel(),
				source.getAnchors().get(connection.getSource().getId()),
				sink.getAnchors().get(connection.getSink().getId()));

		tc.getAppearanceContainer().setValues(
				connection.getAppearanceContainer().getValues());

		new ExportHandler(tc).setValues(new ExportHandler(connection).getValues());

		return tc;
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

	@Override
	public Point2D getLabelPosition()
	{
		Point2D labelPosition = ((TensorConnection) this.getModel()).getLabelPosition();

		Point2D attachment = getSinkAnchor();

		if (EDirection.SOURCE == ((TensorConnection) this.getModel()).getLabelAttachment())
			attachment = getSourceAnchor();

		if (labelPosition == null)
			return attachment;

		return VecMath.add(labelPosition, attachment);
	}

	@Override
	public void setLabelPosition(Point2D position)
	{
		TensorConnection tensorConnection = (TensorConnection) this.getModel();

		if (position != null)
		{
			Point2D attachment = getSinkAnchor();
			tensorConnection.setLabelAttachment(EDirection.SINK);

			if (VecMath.distance(position, getSourceAnchor()) < VecMath.distance(
					position,
					getSinkAnchor()))
			{
				attachment = getSourceAnchor();
				tensorConnection.setLabelAttachment(EDirection.SOURCE);
			}
			VecMath.sub(position, attachment);
		}

		tensorConnection.setLabelPosition(position);
	}

	@Override
	public Set<Point2D> getRestructionLabelPositions()
	{
		TensorConnectionFigure figure = ((TensorConnectionFigure) this.getFigure());
		Set<Point2D> points = new HashSet<Point2D>();
		points.add(figure.getSinkAnchor());
		points.add(figure.getSourceAnchor());

		return points;
	}
}
