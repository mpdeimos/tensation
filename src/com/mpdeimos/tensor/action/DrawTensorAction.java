package com.mpdeimos.tensor.action;

import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.MouseEvent;

import javax.swing.ImageIcon;

import resources.R;

import com.mpdeimos.tensor.editpart.EpsilonTensorEditPart;
import com.mpdeimos.tensor.model.EpsilonTensor;
import com.mpdeimos.tensor.model.ModelRoot;
import com.mpdeimos.tensor.ui.ApplicationWindow;
import com.mpdeimos.tensor.ui.DrawingCanvas;

/**
 * Action for drawing tensors
 * 
 * @author mpdeimos
 * 
 */
public class DrawTensorAction extends CanvasActionBase
{

	/** position of the current drawing figure */
	private final Point position;

	/** the editpart being drawn ontop of the canvas */
	private final EpsilonTensorEditPart editPart;

	/**
	 * Constructor.
	 * 
	 * @param drawingPanel
	 */
	public DrawTensorAction(
			ApplicationWindow applicationWindow,
			DrawingCanvas drawingPanel)
	{
		super(
				applicationWindow,
				drawingPanel,
				R.strings.getString("window_action_draw"), //$NON-NLS-1$
				new ImageIcon(R.drawable.getURL("draw"))); //$NON-NLS-1$

		this.position = new Point(0, 0);
		this.editPart = new EpsilonTensorEditPart(new EpsilonTensor(
				null,
				this.position));
	}

	@Override
	public boolean doOnMouseMoved(MouseEvent e)
	{
		this.editPart.setPosition(e.getPoint());
		this.canvas.repaint();
		return true;
	}

	@Override
	public boolean doOnMouseClicked(MouseEvent e)
	{

		if (e.getButton() == MouseEvent.BUTTON1)
		{
			ModelRoot root = this.canvas.getModel();
			root.addChild(new EpsilonTensor(root, e.getPoint()));

			return true;
		}

		return false;
	}

	@Override
	public boolean drawOverlay(Graphics2D gfx)
	{
		this.editPart.draw(gfx);
		return true;
	}

}
