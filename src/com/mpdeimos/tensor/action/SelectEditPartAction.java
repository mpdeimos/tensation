package com.mpdeimos.tensor.action;

import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;

import javax.swing.AbstractAction;
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
public class SelectEditPartAction extends AbstractAction implements ICanvasAction {

	/** back reference to the drawing panel. */
	private final DrawingCanvas drawingPanel;
	
	/**
	 * Constructor.
	 * @param drawingPanel 
	 */
	public SelectEditPartAction(DrawingCanvas drawingPanel)
	{
		super(R.strings.getString("window_action_select"), new ImageIcon(R.drawable.getURL("select"))); //$NON-NLS-1$ //$NON-NLS-2$
		this.drawingPanel = drawingPanel;
	}
	
	@Override
	public void actionPerformed(ActionEvent arg0) {
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
