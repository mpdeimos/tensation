/**
 * 
 */
package com.mpdeimos.tensation.ui;

import java.awt.Dimension;
import java.awt.GridLayout;

import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import resources.R;

/**
 * The context panel.
 * 
 * @author mpdeimos
 * 
 */
public class ContextPanel extends JPanel
{
	/** The fixed width of this panel. */
	/* package */static final int PANEL_WIDTH = 180;

	/** the content to display on this JPanel. */
	private ContextPanelContentBase content = null;

	/** The default content panel. */
	private final ContextPanelContentBase defaultContent = new DefaultContent();

	/**
	 * Constructor.
	 * 
	 * @param applicationWindow
	 */
	public ContextPanel()
	{
		this.setLayout(new GridLayout());
		this.setPreferredSize(new Dimension(PANEL_WIDTH, 0));
		this.setBorder(new EmptyBorder(0, 1, 0, 1));
		update();
	}

	@Override
	public int getWidth()
	{
		return PANEL_WIDTH;
	}

	/** Sets the content for the panel. */
	public void setContent(ContextPanelContentBase content)
	{
		this.content = content;
		update();
	}

	/** Updates the content panel. */
	private void update()
	{
		this.removeAll();

		if (this.content != null)
			this.add(this.content);
		else
		{
			this.add(this.defaultContent);
		}

		validate();
		repaint();
	}

	/** default context panel class. */
	private class DefaultContent extends ContextPanelContentBase
	{
		/** Constructor. */
		public DefaultContent()
		{
			this.add(new DividerLabel(
					R.string.WINDOW_CONTEXTPANEL_DISABLED.string()));
		}
	}
}
