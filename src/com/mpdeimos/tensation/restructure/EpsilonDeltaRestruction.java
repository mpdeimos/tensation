package com.mpdeimos.tensation.restructure;

import com.mpdeimos.tensation.editpart.IEditPart;
import com.mpdeimos.tensation.editpart.TensorConnectionEditPart;
import com.mpdeimos.tensation.editpart.TensorEditPartBase;
import com.mpdeimos.tensation.editpart.feature.IDuplicatable;
import com.mpdeimos.tensation.impex.Clipboard;
import com.mpdeimos.tensation.model.EOperation;
import com.mpdeimos.tensation.model.IModelData;
import com.mpdeimos.tensation.model.ModelDataBase;
import com.mpdeimos.tensation.model.Operator;
import com.mpdeimos.tensation.model.TensorBase;
import com.mpdeimos.tensation.model.TensorConnection;
import com.mpdeimos.tensation.model.TensorConnectionAnchor;
import com.mpdeimos.tensation.ui.Application;
import com.mpdeimos.tensation.ui.DrawingCanvas;
import com.mpdeimos.tensation.util.CompoundInfiniteUndoableEdit;
import com.mpdeimos.tensation.util.GraphUtil;
import com.mpdeimos.tensation.util.InfiniteUndoableEdit;
import com.mpdeimos.tensation.util.Log;
import com.mpdeimos.tensation.util.Tupel;
import com.mpdeimos.tensation.util.VecMath;
import com.mpdeimos.tensation.util.Wrapper;

import java.awt.Point;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import resources.R;

/**
 * Performs epsilon-delta transformations.
 * 
 * @author mpdeimos
 * 
 */
public class EpsilonDeltaRestruction implements IRestruction
{
	@Override
	public void restruct(IEditPart restructable)
	{
		Log.v(this, "restructure called"); //$NON-NLS-1$

		if (restructable.getModel() instanceof TensorConnection)
		{
			TensorConnection connection = (TensorConnection) restructable.getModel();
			final Wrapper<TensorConnection> connectionRef = new Wrapper<TensorConnection>(
					connection);

			final IModelData parent = connection.getParent();

			final TensorConnectionAnchor sinkAnchor = connection.getSink();
			final TensorBase sink = sinkAnchor.getTensor();
			final TensorConnectionAnchor sourceAnchor = connection.getSource();
			final TensorBase source = sourceAnchor.getTensor();

			final TensorConnectionAnchor sinkNext = sink.getAnchors().get(
					(sinkAnchor.getId() + 1) % 3).getConnection().getSource();
			final TensorConnectionAnchor sinkPrev = sink.getAnchors().get(
					(sinkAnchor.getId() - 1) % 3).getConnection().getSource();

			final TensorConnectionAnchor sourceNext = source.getAnchors().get(
					(sourceAnchor.getId() + 1) % 3).getConnection().getSink();
			final TensorConnectionAnchor sourcePrev = source.getAnchors().get(
					(sourceAnchor.getId() - 1) % 3).getConnection().getSink();

			Application.getApp().getActiveCanvas().getUndoManager().addEdit(
					new InfiniteUndoableEdit()
			{
				/** cache for new editparts. */
				private final List<IModelData> newOnes = new ArrayList<IModelData>();
				private CompoundInfiniteUndoableEdit cie;

				@Override
				public String getPresentationName()
				{
					return R.string.WINDOW_ACTION_RESTRUCTURE_EPSILONDELTA.string();
				}

				@Override
				public void redo()
				{
					DrawingCanvas canvas = Application.getApp().getActiveCanvas();
					canvas.setByPassModelEvents(true);

					TensorConnection connection = connectionRef.get();
					Tupel<Set<TensorBase>, Set<TensorConnection>> subgraph = GraphUtil.getCompleteSubgraph(connection);
					List<IDuplicatable> toBeDuplicated = new ArrayList<IDuplicatable>(
							subgraph.$1.size() + subgraph.$2.size());

					Rectangle2D rect = null;
					for (TensorBase t : subgraph.$1)
					{
						if (rect == null)
						{
							rect = new Rectangle2D.Double(
									t.getPosition().getX(),
									t.getPosition().getY(), 0, 0);
						}
						else
						{
							rect.add(t.getPosition());
						}
						toBeDuplicated.add((TensorEditPartBase) Application.getApp().getActiveCanvas().getEditPartForModelData(
								t));
					}
					for (TensorConnection t : subgraph.$2)
					{
						toBeDuplicated.add((TensorConnectionEditPart) Application.getApp().getActiveCanvas().getEditPartForModelData(
								t));
					}

					assert (rect != null);

					Clipboard.get().clip(toBeDuplicated);
					this.cie = new CompoundInfiniteUndoableEdit();
					Map<IDuplicatable, IModelData> copied = Clipboard.get().copyToCanvas(
							this.cie, false);

					for (IDuplicatable m : copied.keySet())
					{
						IModelData d = copied.get(m);
						if (d instanceof TensorBase)
						{
							TensorBase tensor = (TensorBase) d;
							Point2D p = VecMath.add(
									VecMath.fresh(tensor.getPosition()),
									rect.getWidth() + 20,
									0);
							tensor.setPosition(new Point(
									(int) p.getX(),
									(int) p.getY()));
						}
					}

					Operator minus = new Operator(connection.getParent());
					minus.setOperation(EOperation.MINUS);
					minus.setPosition(new Point(
							(int) (rect.getMaxX() + 10),
							(int) rect.getCenterY()));
					parent.addChild(minus);
					this.newOnes.add(minus);

					TensorConnection connection2 = (TensorConnection)
							copied.get(canvas.getEditPartForModelData(connection));

					TensorConnectionAnchor sinkAnchor2 =
							connection2.getSink();
					TensorBase sink2 = sinkAnchor2.getTensor();
					TensorConnectionAnchor sourceAnchor2 =
							connection2.getSource();
					TensorBase source2 = sourceAnchor2.getTensor();

					TensorConnectionAnchor sinkNext2 =
							sink2.getAnchors().get(
									(sinkAnchor2.getId() + 1) %
									3).getConnection().getSource();
					TensorConnectionAnchor sinkPrev2 =
							sink2.getAnchors().get(
									(sinkAnchor2.getId() - 1) %
									3).getConnection().getSource();

					TensorConnectionAnchor sourceNext2 =
							source2.getAnchors().get(
									(sourceAnchor2.getId() + 1) %
									3).getConnection().getSink();
					TensorConnectionAnchor sourcePrev2 =
							source2.getAnchors().get(
									(sourceAnchor2.getId() - 1) %
									3).getConnection().getSink();

					sink.remove();
					source.remove();
					sink2.remove();
					source2.remove();

					TensorConnection c = new TensorConnection(
							(ModelDataBase) parent,
							sourceNext,
							sinkNext);
					parent.addChild(c);
					this.newOnes.add(c);
					c = new TensorConnection(
							(ModelDataBase) parent,
							sourceNext2,
							sinkPrev2);
					parent.addChild(c);
					this.newOnes.add(c);

					c = new TensorConnection(
							(ModelDataBase) parent,
							sourcePrev,
							sinkPrev);
					parent.addChild(c);
					this.newOnes.add(c);
					c = new TensorConnection(
							(ModelDataBase) parent,
							sourcePrev2,
							sinkNext2);
					parent.addChild(c);
					this.newOnes.add(c);

					canvas.setByPassModelEvents(false);

				}

				@Override
				public void undo()
				{
					DrawingCanvas canvas = Application.getApp().getActiveCanvas();
					canvas.setByPassModelEvents(true);

					this.cie.undo();

					for (IModelData m : this.newOnes)
					{
						if (m != null)
							m.remove();
					}
					this.newOnes.clear();

					parent.addChild(sink);
					parent.addChild(source);

					connectionRef.set(new TensorConnection(
							(ModelDataBase) parent,
							sourceAnchor,
							sinkAnchor));
					parent.addChild(connectionRef.get());

					parent.addChild(new TensorConnection(
							(ModelDataBase) parent,
							sinkNext,
							sink.getAnchors().get(
									(sinkAnchor.getId() + 1) % 3)));

					parent.addChild(new TensorConnection(
							(ModelDataBase) parent,
							sinkPrev,
							sink.getAnchors().get(
									(sinkAnchor.getId() - 1) % 3)));

					parent.addChild(new TensorConnection(
							(ModelDataBase) parent,
							sourceNext,
							source.getAnchors().get(
									(sourceAnchor.getId() + 1) % 3)));

					parent.addChild(new TensorConnection(
							(ModelDataBase) parent,
							sourcePrev,
							source.getAnchors().get(
									(sourceAnchor.getId() - 1) % 3)));

					canvas.setByPassModelEvents(false);
				}
			}.act());
		}
		else
		{
			throw new IllegalArgumentException();
		}
	}

	@Override
	public String getDisplayName()
	{
		return R.string.WINDOW_ACTION_RESTRUCTURE_EPSILONDELTA_LABEL.string();
	}

	@Override
	public boolean isRestructable(IEditPart restructable)
	{
		if (restructable.getModel() instanceof TensorConnection)
		{
			TensorConnection connection = (TensorConnection) restructable.getModel();

			TensorBase sink = connection.getSink().getTensor();
			TensorBase source = connection.getSource().getTensor();

			return checkTensor(source) && checkTensor(sink);
		}
		return false;
	}

	/** Checks the tensor for being simplifyable w/ epsilon-delta rule. */
	private boolean checkTensor(TensorBase tensor)
	{
		return tensor.getAnchors().size() == 3
				&& tensor.getOccupiedAnchors().size() == 3;
	}
}