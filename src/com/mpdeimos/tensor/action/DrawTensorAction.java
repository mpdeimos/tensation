package com.mpdeimos.tensor.action;

import java.awt.Component;
import java.awt.Graphics;
import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.Icon;
import javax.swing.ImageIcon;

import com.mpdeimos.tensor.Main;
import com.mpdeimos.tensor.ui.ApplicationWindow;
import com.mpdeimos.tensor.ui.DrawingCanvas;

import resources.R;

/**
 * Action for drawing tensors
 * 
 * @author mpdeimos
 *
 */
public class DrawTensorAction extends AbstractAction {

	/** back reference to the drawing panel. */
	private final DrawingCanvas drawingPanel;

	/**
	 * Constructor.
	 * @param drawingPanel 
	 */
	public DrawTensorAction(DrawingCanvas drawingPanel)
	{
		super(R.strings.getString("window_action_draw"), new ImageIcon(R.drawable.getURL("draw"))); //$NON-NLS-1$ //$NON-NLS-2$
		this.drawingPanel = drawingPanel;
	}
	
	@Override
	public void actionPerformed(ActionEvent arg0) {
		drawingPanel.performAction(this);
	}


}
