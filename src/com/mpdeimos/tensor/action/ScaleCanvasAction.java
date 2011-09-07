package com.mpdeimos.tensor.action;

import com.mpdeimos.tensor.ui.Application;

import java.awt.Component;
import java.awt.Container;
import java.awt.event.ActionEvent;

import javax.swing.JCheckBoxMenuItem;
import javax.swing.JPopupMenu;

import resources.R;

/**
 * Creates a new tensor model.
 * 
 * @author mpdeimos
 * 
 */
public class ScaleCanvasAction extends ActionBase
{
	/** the scale percentage for this action. */
	private final int percent;

	/** the parent options menu. */
	private final JCheckBoxMenuItem parentMenu;

	/**
	 * Constructor.
	 * 
	 * @param menuOptionsScale
	 */
	public ScaleCanvasAction(int percent, JCheckBoxMenuItem parentMenu)
	{
		super(
				String.format(
						R.string.WINDOW_MENU_VIEW_CANVASSCALE_ACTION.string(),
						percent),
				null);
		this.percent = percent;
		this.parentMenu = parentMenu;
	}

	@Override
	public void actionPerformed(ActionEvent e)
	{
		Container parent = this.parentMenu.getParent();
		if (parent instanceof JPopupMenu)
		{
			for (Component comp : ((JPopupMenu) parent).getComponents())
			{
				if (comp instanceof JCheckBoxMenuItem)
					((JCheckBoxMenuItem) comp).setSelected(false);
			}
		}
		Application.getApp().getDrawingCanvas().setScale(this.percent / 100.0);
		this.parentMenu.setSelected(true);
	}
}
