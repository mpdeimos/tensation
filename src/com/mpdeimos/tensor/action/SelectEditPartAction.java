package com.mpdeimos.tensor.action;

import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;

import javax.swing.ImageIcon;

import resources.R;

import com.mpdeimos.tensor.editpart.IEditPart;
import com.mpdeimos.tensor.ui.DrawingCanvas;

/**
 * Action for drawing tensors
 * 
 * @author mpdeimos
 *
 */
public class SelectEditPartAction extends CanvasActionBase {
	
	/**
	 * Constructor.
	 */
	public SelectEditPartAction(DrawingCanvas drawingPanel)
	{
		super(drawingPanel, R.strings.getString("window_action_select"), new ImageIcon(R.drawable.getURL("select"))); //$NON-NLS-1$ //$NON-NLS-2$
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		super.actionPerformed(e);
		drawingPanel.startCanvasAction(this);
	}

	@Override
	public boolean doOnMouseMove(MouseEvent e) {
		for (IEditPart editPart : drawingPanel.getEditParts())
		{
			editPart.isMouseOver(e.getPoint());
		}
		drawingPanel.repaint();
		return true;
	}

	@Override
	public boolean doOnMouseClicked(MouseEvent e) {
		
		if (e.getButton() == MouseEvent.BUTTON1)
		{
			
			return true;
		}
		
		return false;
	}

	@Override
	public boolean drawOverlay(Graphics2D gfx) {
		return false;
	}
	
}
