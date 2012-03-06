package com.mpdeimos.tensation.action;

import com.mpdeimos.tensation.ui.Application;

import java.awt.event.ActionEvent;
import java.util.HashMap;

import javax.swing.ButtonGroup;
import javax.swing.JRadioButtonMenuItem;

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
	private final JRadioButtonMenuItem parentMenu;

	/** Menu radio group. */
	private static ButtonGroup radioGroup = new ButtonGroup();

	/** list for updating the canvas scale */
	private static HashMap<Integer, ScaleCanvasAction> radioList = new HashMap<Integer, ScaleCanvasAction>();

	/**
	 * Constructor.
	 * 
	 * @param menuOptionsScale
	 */
	public ScaleCanvasAction(int percent, JRadioButtonMenuItem parentMenu)
	{
		super(
				String.format(
						R.string.WINDOW_MENU_VIEW_CANVASSCALE_ACTION.string(),
						percent),
				null);
		this.percent = percent;
		this.parentMenu = parentMenu;
		radioList.put(percent, this);
		radioGroup.add(this.parentMenu);
	}

	@Override
	public void actionPerformed(ActionEvent e)
	{
		Application.getApp().getActiveCanvas().setScale(this.percent / 100.0);
	}

	/** updates the scale ticks. */
	public static void updateScaleTicks(int scale)
	{
		ScaleCanvasAction scaleCanvasAction = radioList.get(scale);
		if (scaleCanvasAction != null)
		{
			scaleCanvasAction.parentMenu.setSelected(true);
		}
	}
}
