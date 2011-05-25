package com.mpdeimos.tensor.action;

import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;

import javax.swing.AbstractAction;
import javax.swing.ImageIcon;

import resources.R;

import com.mpdeimos.tensor.editpart.EpsilonTensorEditPart;
import com.mpdeimos.tensor.model.EpsilonTensor;
import com.mpdeimos.tensor.model.ModelRoot;
import com.mpdeimos.tensor.ui.DrawingCanvas;

/**
 * Action for drawing tensors
 * 
 * @author mpdeimos
 *
 */
public class DrawTensorAction extends AbstractAction implements ICanvasAction {

	/** back reference to the drawing panel. */
	private final DrawingCanvas drawingPanel;
	
	/** position of the current drawing figure */
	private final Point position;

	/** the editpart being drawn ontop of the canvas */
	private EpsilonTensorEditPart editPart;

	/**
	 * Constructor.
	 * @param drawingPanel 
	 */
	public DrawTensorAction(DrawingCanvas drawingPanel)
	{
		super(R.strings.getString("window_action_draw"), new ImageIcon(R.drawable.getURL("draw"))); //$NON-NLS-1$ //$NON-NLS-2$
		this.drawingPanel = drawingPanel;
		
		this.position = new Point(0,0);
		this.editPart = new EpsilonTensorEditPart(new EpsilonTensor(position));
	}
	
	@Override
	public void actionPerformed(ActionEvent arg0) {
		drawingPanel.startCanvasAction(this);
	}

	@Override
	public boolean doOnMouseMove(MouseEvent e) {
		position.setLocation(e.getPoint());
		drawingPanel.repaint();
		return true;
	}

	@Override
	public boolean doOnMouseClicked(MouseEvent e) {
		
		if (e.getButton() == MouseEvent.BUTTON1)
		{
//		drawingPanel.stopCanvasAction();
			ModelRoot root = drawingPanel.getModel();
			root.addChild(new EpsilonTensor(e.getPoint()));
			
			return true;
		}
		
		return false;
	}

	@Override
	public boolean drawOverlay(Graphics2D gfx) {
		editPart.draw(gfx);
		return true;
	}
}
