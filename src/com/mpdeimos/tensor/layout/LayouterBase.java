package com.mpdeimos.tensor.layout;

import com.mpdeimos.tensor.action.ActionBase;
import com.mpdeimos.tensor.action.canvas.CanvasActionBase;
import com.mpdeimos.tensor.editpart.IEditPart;
import com.mpdeimos.tensor.editpart.TensorConnectionEditPart;
import com.mpdeimos.tensor.editpart.TensorEditPartBase;
import com.mpdeimos.tensor.editpart.feature.IDuplicatable;
import com.mpdeimos.tensor.model.IModelData;
import com.mpdeimos.tensor.model.ModelRoot;
import com.mpdeimos.tensor.model.TensorBase;
import com.mpdeimos.tensor.model.TensorConnection;
import com.mpdeimos.tensor.ui.Application;
import com.mpdeimos.tensor.ui.ContextPanelContentBase;
import com.mpdeimos.tensor.ui.DividerLabel;
import com.mpdeimos.tensor.ui.DrawingCanvas;
import com.mpdeimos.tensor.util.InfiniteUndoableEdit;
import com.mpdeimos.tensor.util.VecMath;

import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.geom.Point2D;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import javax.swing.JButton;

import resources.R;

/**
 * Layouting action base
 * 
 * @author mpdeimos
 */
public abstract class LayouterBase extends CanvasActionBase
{
	/** the context panel. */
	private final ContextPanel contextPanel;

	/** Constructor. */
	public LayouterBase(String name)
	{
		super(name, null);

		this.contextPanel = this.new ContextPanel();
	}

	@Override
	protected final ContextPanelContentBase getContextPanel()
	{
		return this.contextPanel;
	}

	/**
	 * Layouts the current diagram. The new tensor positions need to be stored
	 * in the newPos map.
	 * 
	 * @return true on success
	 */
	abstract boolean layout(
			HashMap<TensorBase, Point2D> positions,
			HashMap<TensorBase, Double> rotations,
			Set<TensorConnection> connections);

	/** Callback for creating a context panel. */
	public void onContextPanelInit(ContextPanel panel)
	{
		// nothing
	}

	/** @ignore */
	private class LayoutAction extends ActionBase
	{
		/** Constructor. */
		public LayoutAction()
		{
			super(R.string.LAYOUT_BUTTON.string(), null);
		}

		@Override
		public void actionPerformed(ActionEvent e)
		{
			DrawingCanvas activeCanvas = Application.getApp().getActiveCanvas();

			// save original position
			ModelRoot model = activeCanvas.getModel();
			final HashMap<TensorBase, Point2D> oldPos = new HashMap<TensorBase, Point2D>();
			final HashMap<TensorBase, Point2D> newPos = new HashMap<TensorBase, Point2D>();
			final HashMap<TensorBase, Double> oldRot = new HashMap<TensorBase, Double>();
			final HashMap<TensorBase, Double> newRot = new HashMap<TensorBase, Double>();
			HashSet<TensorConnection> connections = new HashSet<TensorConnection>();

			Set<TensorBase> tensors = new HashSet<TensorBase>();

			for (IEditPart part : Application.getApp().getActiveCanvas().getSelectedEditParts())
			{
				if (part instanceof TensorEditPartBase)
				{
					tensors.add((TensorBase) part.getModel());
				}
				if (part instanceof TensorConnectionEditPart)
				{
					connections.add((TensorConnection) part.getModel());
					for (IDuplicatable linkedPart : ((TensorConnectionEditPart) part).getLinkedEditParts())
					{
						if (linkedPart instanceof TensorEditPartBase)
						{
							tensors.add((TensorBase) ((TensorEditPartBase) linkedPart).getModel());
						}
					}
				}
			}

			// grab tensors if none are selected
			if (tensors.size() == 0)
			{
				for (IModelData child : model.getChildren())
				{
					if (child instanceof TensorBase)
					{
						tensors.add((TensorBase) child);
					}
				}
			}

			for (TensorBase tensor : tensors)
			{
				oldPos.put(tensor, VecMath.fresh((tensor.getPosition())));
				newPos.put(tensor, VecMath.fresh((tensor.getPosition())));
				oldRot.put(tensor, tensor.getRotation());
				newRot.put(tensor, tensor.getRotation());
			}

			for (IModelData child : model.getChildren())
			{
				if (child instanceof TensorConnection)
				{
					TensorConnection connection = (TensorConnection) child;
					if (!tensors.contains(connection.getSource().getTensor()))
						continue;
					if (!tensors.contains(connection.getSink().getTensor()))
						continue;

					connections.add(connection);
				}
			}

			// layout
			if (!layout(newPos, newRot, connections))
				return;

			// build undo stack
			activeCanvas.getUndoManager().addEdit(
					new InfiniteUndoableEdit()
			{
				@Override
				public String getPresentationName()
				{
					return LayouterBase.this.getName();
				}

				@Override
				public void undo()
				{
					for (TensorBase tensor : oldPos.keySet())
					{
						Point2D point = oldPos.get(tensor);
						tensor.setPosition(new Point(
								(int) point.getX(),
								(int) point.getY()));
						tensor.setRotation(oldRot.get(tensor));
					}
				}

				@Override
				public void redo()
				{
					for (TensorBase tensor : newPos.keySet())
					{
						Point2D point = newPos.get(tensor);
						tensor.setPosition(new Point(
								(int) point.getX(),
								(int) point.getY()));
						tensor.setRotation(newRot.get(tensor));
					}
				}
			}.act());
		}
	}

	/** @ignore */
	protected class ContextPanel extends ContextPanelContentBase
	{
		/** Constructor. */
		public ContextPanel()
		{
			this.add(new DividerLabel(LayouterBase.this.getName()));

			JButton layout = new JButton(new LayoutAction());
			this.add(layout);

			onContextPanelInit(this);

		}
	}
}
