/**
 * 
 */
package com.mpdeimos.tensation.ui;

import com.mpdeimos.tensation.util.LayoutUtil;

import java.awt.Component;
import java.awt.Dimension;

import javax.swing.BoxLayout;
import javax.swing.JComponent;

/**
 * Abstract class for content implementation being shown in the context panel.
 * All components added to this layout will be stretched to maximum width.
 * 
 * @author mpdeimos
 * 
 */
public abstract class ContextPanelContentBase extends RefreshablePanel
{
	/** The maximum width of the panel container. */
	public static final int PANEL_WIDTH = ContextPanel.PANEL_WIDTH;

	/** The left margin for insets. */
	public static final int INSET_LEFT_MARGIN = 5;

	/** The right margin for insets. */
	public static final int INSET_RIGHT_MARGIN = 5;

	/** Constructor. */
	public ContextPanelContentBase()
	{
		BoxLayout gridLayout = new BoxLayout(this, BoxLayout.PAGE_AXIS);
		this.setLayout(gridLayout);
	}

	@Override
	public Component add(Component comp)
	{
		return add(comp, Component.CENTER_ALIGNMENT);
	}

	/** add with alignment. */
	public Component add(Component comp, float alignment)
	{
		Dimension preferredSize = comp.getPreferredSize();
		LayoutUtil.setWidth(comp, Short.MAX_VALUE);
		LayoutUtil.setHeight(comp, (int) preferredSize.getHeight());
		if (comp instanceof JComponent)
		{
			((JComponent) comp).setAlignmentX(alignment);
		}
		return super.add(comp);
	}

	/** @return a new labeled component with inset margins. */
	public static LabeledComponent label(String text, Component component)
	{
		return new LabeledComponent(
				text,
				component,
				INSET_LEFT_MARGIN,
				INSET_RIGHT_MARGIN);
	}

	@Override
	public void refresh()
	{
		// nothing
	}
}