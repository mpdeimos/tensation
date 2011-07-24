/**
 * 
 */
package com.mpdeimos.tensor.ui;

import com.mpdeimos.tensor.util.LayoutUtil;

import java.awt.Component;

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

	@Override
	public Component add(Component comp)
	{
		LayoutUtil.setWidth(comp, Short.MAX_VALUE);
		return super.add(comp);
	}
}