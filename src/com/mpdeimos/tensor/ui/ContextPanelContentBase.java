/**
 * 
 */
package com.mpdeimos.tensor.ui;

import java.awt.Component;
import java.awt.Dimension;

import javax.swing.BoxLayout;
import javax.swing.JPanel;

/**
 * Abstract class for content implementation being shown in the context panel.
 * All components added to this layout will be stretched to maximum width.
 * 
 * @author mpdeimos
 * 
 */
public abstract class ContextPanelContentBase extends JPanel
{
	/** The maximum width of the panel container. */
	public static final int PANEL_WIDTH = ContextPanel.PANEL_WIDTH;

	/** Constructor. */
	public ContextPanelContentBase()
	{
		BoxLayout gridLayout = new BoxLayout(this, BoxLayout.PAGE_AXIS);
		this.setLayout(gridLayout);
	}

	/** Stretches the component ro full width by specifying its height. */
	protected static void stretchToFullWidth(Component comp, int height)
	{
		comp.setMaximumSize(new Dimension(Short.MAX_VALUE, height));
		comp.setPreferredSize(new Dimension(Short.MAX_VALUE, height));
	}

	/** Stretches the component to full width. */
	protected static void stretchToFullWidth(Component comp)
	{
		Dimension maximumSize = comp.getMaximumSize();
		maximumSize.width = Short.MAX_VALUE;
		comp.setMaximumSize(maximumSize);

		Dimension preferredSize = comp.getPreferredSize();
		preferredSize.width = Short.MAX_VALUE;
		comp.setPreferredSize(preferredSize);
	}
}