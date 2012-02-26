package com.mpdeimos.tensation.action.canvas;

import com.mpdeimos.tensation.editpart.feature.IFeatureEditPart;
import com.mpdeimos.tensation.feature.contract.ICanvasFeatureContract;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;

import javax.swing.ImageIcon;

import resources.R;

/**
 * Action for applying the epsilon delta rule.
 * 
 * @author mpdeimos
 * 
 */
public class GraphRestructureAction extends CanvasActionBase
{

	/**
	 * Constructor.
	 */
	public GraphRestructureAction()
	{
		super(
				R.string.WINDOW_ACTION_RESTRUCTURE.string(),
				new ImageIcon(R.drawable.ACTION_RESTRUCTURE.url()),
				KeyEvent.VK_R);
	}

	@Override
	public boolean drawOverlay(Graphics2D gfx)
	{
		Color oldPaint = gfx.getColor();

		super.drawOverlayForFeatures(this.canvas.getEditParts(), gfx);

		gfx.setColor(oldPaint);

		return true;
	}

	@Override
	public boolean doOnMouseMoved(MouseEvent e)
	{
		return super.handleMouseEventForFeatures(
				this.canvas.getEditParts(),
				e,
				MouseEvent.MOUSE_MOVED);
	}

	@Override
	public boolean doOnMouseReleased(MouseEvent e)
	{
		return super.handleMouseEventForFeatures(
				this.canvas.getEditParts(),
				e,
				MouseEvent.MOUSE_RELEASED);
	}

	/** Contract for tensor simplification features. */
	public static abstract class IFeature<I extends IFeatureEditPart> extends
			CanvasFeatureBase<I, GraphRestructureAction.IFeature<?>>
	{
		/** Constructor. */
		public IFeature(I editPart)
		{
			super(editPart);
		}
	}

	@Override
	protected Class<? extends ICanvasFeatureContract> getCanvasContract()
	{
		return IFeature.class;
	}

}
