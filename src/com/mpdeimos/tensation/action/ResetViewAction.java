package com.mpdeimos.tensation.action;

import com.mpdeimos.tensation.ui.Application;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import javax.swing.KeyStroke;

import resources.R;

/**
 * Resets the canvas view.
 * 
 * @author mpdeimos
 * 
 */
public class ResetViewAction extends ActionBase
{
	/**
	 * Constructor.
	 * 
	 * @param menuOptionsScale
	 */
	public ResetViewAction()
	{
		super(R.string.WINDOW_MENU_VIEW_RESET.string(), null,
				KeyStroke.getKeyStroke(
						KeyEvent.VK_NUMPAD0, ActionEvent.CTRL_MASK));
	}

	@Override
	public void actionPerformed(ActionEvent e)
	{
		Application.getApp().getActiveCanvas().setScale(1);
		Application.getApp().getActiveCanvas().setScroll(0, 0);
	}
}
