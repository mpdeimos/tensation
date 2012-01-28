package com.mpdeimos.tensation.action.canvas;

import com.mpdeimos.tensation.editpart.IEditPart;
import com.mpdeimos.tensation.editpart.feature.IFeatureEditPart;
import com.mpdeimos.tensation.feature.contract.ICanvasFeatureContract;
import com.mpdeimos.tensation.util.Gfx;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Stroke;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.util.LinkedList;

import javax.swing.ImageIcon;

import resources.R;

/**
 * Action for drawing tensors
 * 
 * @author mpdeimos
 * 
 */
public class SelectEditPartAction extends CanvasActionBase
{
	/** The currently highlighted EditPart. */
	private IEditPart highlightedEditPart;

	/** newly selected editpart. */
	private IEditPart newlySelectedEditPart = null;

	/** the bounding rect for manual selection */
	private Rectangle selectionRect;

	/** the selection start point. */
	private Point selectionStartPoint;

	/** The stroke of selection rectangle. */
	private static BasicStroke EDITPART_SELECTION_STROKE = new BasicStroke(
			1.0f,
			BasicStroke.CAP_SQUARE,
			BasicStroke.JOIN_MITER,
			1,
			new float[] { 3f, 3f },
			0);

	/** the offset of the selection rectangle. */
	public static int EDITPART_SELECTION_STROKE_OFFSET = 3;

	/**
	 * Constructor.
	 */
	public SelectEditPartAction()
	{
		super(
				R.string.WINDOW_ACTION_SELECT.string(),
				new ImageIcon(R.drawable.SELECT.url()), KeyEvent.VK_S);
	}

	@Override
	public void stopAction()
	{
		super.stopAction();

		// reset some stuff
		this.highlightedEditPart = null;

		if (this.canvas != null)
		{
			this.canvas.setCursor(Cursor.getDefaultCursor());
		}
	}

	@Override
	public boolean doOnMouseMoved(MouseEvent e)
	{
		super.doOnMouseMoved(e);

		if (this.highlightedEditPart != null)
			this.highlightedEditPart.setHighlighted(false);

		if (handleMouseEventForFeatures(
				this.canvas.getSelectedEditParts(),
				e,
				MouseEvent.MOUSE_MOVED))
			return true;

		this.highlightedEditPart = null;
		for (IEditPart editPart : this.canvas.getEditParts())
		{
			if (editPart.isSelected()) // TODO check
				continue;

			boolean tmpOver = isMouseOver(editPart, e.getPoint());

			if (tmpOver && this.highlightedEditPart == null)
				this.highlightedEditPart = editPart;

			editPart.setHighlighted(false);
		}

		if (this.highlightedEditPart != null)
		{
			this.highlightedEditPart.setHighlighted(true);
			this.canvas.setCursor(new Cursor(Cursor.HAND_CURSOR));
		}
		else
		{
			this.canvas.setCursor(new Cursor(Cursor.CROSSHAIR_CURSOR));
		}

		this.canvas.repaint();
		return true;
	}

	@Override
	public boolean doOnMousePressed(MouseEvent e)
	{
		super.doOnMousePressed(e);

		for (IEditPart ep : this.canvas.getSelectedEditParts())
			ep.setHighlighted(false);

		if (handleMouseEventForFeatures(
				this.canvas.getSelectedEditParts(),
				e,
				MouseEvent.MOUSE_PRESSED))
			return true;

		if (e.getButton() == MouseEvent.BUTTON1)
		{
			if (!e.isShiftDown())
			{
				this.canvas.clearSelectedEditParts();
			}

			for (IEditPart editPart : this.canvas.getEditParts())
			{
				if (isMouseOver(editPart, e.getPoint()))
				{
					this.canvas.addSelectedEditPart(editPart);

					this.newlySelectedEditPart = editPart;

					this.canvas.repaint();

					handleMouseEventForFeatures(
							this.canvas.getSelectedEditParts(),
							e,
							MouseEvent.MOUSE_PRESSED);

					return true;
				}
			}

			this.selectionStartPoint = new Point(e.getPoint());

			return false;
		}

		return false;
	}

	@Override
	public boolean doOnMouseDragged(MouseEvent e)
	{
		super.doOnMouseDragged(e);

		this.newlySelectedEditPart = null;

		if (this.selectionStartPoint != null)
		{
			this.selectionRect = new Rectangle(
					this.selectionStartPoint.x,
					this.selectionStartPoint.y,
					0,
					0);
			this.selectionRect.add(e.getPoint());

			this.canvas.clearSelectedEditParts();
			for (IEditPart part : this.canvas.getEditParts())
			{
				if (part.intersects(this.selectionRect))
				{
					this.canvas.addSelectedEditPart(part);
				}
			}

			this.canvas.repaint();

			return true;
		}

		if (handleMouseEventForFeatures(
				this.canvas.getSelectedEditParts(),
				e,
				MouseEvent.MOUSE_DRAGGED))
			return true;

		return false;
	}

	@Override
	public boolean doOnMouseReleased(MouseEvent e)
	{
		super.doOnMousePressed(e);

		this.selectionRect = null;
		this.selectionStartPoint = null;

		if (handleMouseEventForFeatures(
				this.canvas.getSelectedEditParts(),
				e,
				MouseEvent.MOUSE_RELEASED))
			return true;

		if (e.getButton() == MouseEvent.BUTTON1 && e.isShiftDown()
				&& this.newlySelectedEditPart == null)
		{
			for (IEditPart editPart : new LinkedList<IEditPart>(
					this.canvas.getSelectedEditParts()))
			{
				if (isMouseOver(editPart, e.getPoint()))
				{
					this.canvas.removeSelectedEditPart(editPart);
				}
			}
			this.canvas.repaint();
		}

		this.newlySelectedEditPart = null;

		return false;
	}

	@Override
	public boolean doOnKeyPressed(KeyEvent e)
	{
		return this.handleKeyEventForFeatures(
				this.canvas.getEditParts(),
				e,
				false);
	}

	@Override
	public boolean doOnKeyReleased(KeyEvent e)
	{
		boolean handled = this.handleKeyEventForFeatures(
				this.canvas.getEditParts(),
				e,
				true);

		return handled;
	}

	@Override
	public boolean drawOverlay(Graphics2D gfx)
	{
		if (this.selectionRect != null)
		{
			Color c = gfx.getColor();
			gfx.setColor(Color.MAGENTA);
			Stroke s = gfx.getStroke();
			gfx.setStroke(EDITPART_SELECTION_STROKE);

			if (this.selectionRect != null)
				Gfx.drawRect(gfx, this.selectionRect);

			gfx.setStroke(s);
			gfx.setColor(c);

			return true;
		}

		for (IEditPart part : this.canvas.getSelectedEditParts())
		{
			// old bounding box code
			//
			// Stroke s = gfx.getStroke();
			// gfx.setStroke(EDITPART_SELECTION_STROKE);
			// Rectangle r = part.getBoundingRectangle();
			// Rectangle2D rect = new Rectangle2D.Double(
			// r.getX()
			// - EDITPART_SELECTION_STROKE_OFFSET + 0.5,
			// r.getY()
			// - EDITPART_SELECTION_STROKE_OFFSET + 0.5,
			// r.getWidth() + 2
			// * EDITPART_SELECTION_STROKE_OFFSET,
			// r.getHeight() + 2
			// * EDITPART_SELECTION_STROKE_OFFSET);
			// gfx.draw(rect);
			//
			// gfx.setStroke(s);

			drawOverlayForFeatures(part, gfx);
		}
		if (!this.canvas.getSelectedEditParts().isEmpty())
			return true;

		return false;
	}

	/** Tests whether the mouse is over a given EditPart. */
	private boolean isMouseOver(IEditPart editPart, Point point)
	{
		int offset = 2;
		Rectangle rect = new Rectangle(point.x - offset, point.y - offset, 4, 4);
		return editPart.intersects(rect);
	}

	/** Contract for tensor connection features. */
	public static abstract class IFeature<I extends IFeatureEditPart> extends
			CanvasFeatureBase<I, SelectEditPartAction.IFeature<?>>
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
