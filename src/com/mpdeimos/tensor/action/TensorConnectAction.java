package com.mpdeimos.tensor.action;

import java.awt.Graphics2D;

import javax.swing.ImageIcon;

import resources.R;

import com.mpdeimos.tensor.ui.DrawingCanvas;

/**
 * Action for connecting two tensors.
 * 
 * @author mpdeimos
 * 
 */
public class TensorConnectAction extends CanvasActionBase
{
	/**
	 * Constructor.
	 */
	public TensorConnectAction(DrawingCanvas canvas)
	{
		super(
				canvas,
				R.strings.getString("window_action_connect"), //$NON-NLS-1$
				new ImageIcon(R.drawable.getURL("action_connect"))); //$NON-NLS-1$
	}

	@Override
	public boolean drawOverlay(Graphics2D gfx)
	{
		// TODO Auto-generated method stub
		return false;
	}

}
