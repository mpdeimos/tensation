package com.mpdeimos.tensor.action;

import com.mpdeimos.tensor.ui.Application;

import java.awt.event.ActionEvent;

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
		super(R.string.WINDOW_MENU_VIEW_RESET.string(), null);
	}

	@Override
	public void actionPerformed(ActionEvent e)
	{
		Application.getApp().getDrawingCanvas().setScale(1);
		Application.getApp().getDrawingCanvas().setScroll(0, 0);
	}
}
