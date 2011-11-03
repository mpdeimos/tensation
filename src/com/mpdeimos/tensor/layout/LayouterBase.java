package com.mpdeimos.tensor.layout;

import com.mpdeimos.tensor.action.ActionBase;
import com.mpdeimos.tensor.action.canvas.CanvasActionBase;
import com.mpdeimos.tensor.model.IModelData;
import com.mpdeimos.tensor.model.ModelRoot;
import com.mpdeimos.tensor.model.TensorBase;
import com.mpdeimos.tensor.ui.Application;
import com.mpdeimos.tensor.ui.ContextPanelContentBase;
import com.mpdeimos.tensor.ui.DividerLabel;
import com.mpdeimos.tensor.util.InfiniteUndoableEdit;

import java.awt.Point;
import java.awt.event.ActionEvent;
import java.util.HashMap;

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
	 * in the newPos array.
	 */
	abstract void layout(HashMap<TensorBase, Point> newPos);

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
			// save original position
			ModelRoot model = Application.getApp().getModel();
			final HashMap<TensorBase, Point> oldPos = new HashMap<TensorBase, Point>();
			final HashMap<TensorBase, Point> newPos = new HashMap<TensorBase, Point>();
			for (IModelData child : model.getChildren())
			{
				if (child instanceof TensorBase)
				{
					TensorBase tensor = (TensorBase) child;
					oldPos.put(tensor, new Point(tensor.getPosition()));
					newPos.put(tensor, new Point(tensor.getPosition()));
				}
			}

			// layout
			layout(newPos);

			// build undo stack
			Application.getApp().getUndoManager().addEdit(
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
						tensor.setPosition(oldPos.get(tensor));
					}
				}

				@Override
				public void redo()
				{
					for (TensorBase tensor : newPos.keySet())
					{
						tensor.setPosition(newPos.get(tensor));
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
