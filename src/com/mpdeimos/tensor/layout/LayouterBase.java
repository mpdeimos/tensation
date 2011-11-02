package com.mpdeimos.tensor.layout;

import com.mpdeimos.tensor.action.ActionBase;
import com.mpdeimos.tensor.action.canvas.CanvasActionBase;
import com.mpdeimos.tensor.ui.ContextPanelContentBase;
import com.mpdeimos.tensor.ui.DividerLabel;

import java.awt.event.ActionEvent;

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

	/** Layouts the current diagram. */
	abstract void layout();

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
			layout();
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
