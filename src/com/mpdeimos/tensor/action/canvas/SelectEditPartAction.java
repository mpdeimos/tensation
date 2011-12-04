package com.mpdeimos.tensor.action.canvas;

import com.mpdeimos.tensor.editpart.IEditPart;
import com.mpdeimos.tensor.editpart.feature.IDuplicatable;
import com.mpdeimos.tensor.util.Gfx;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Stroke;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

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
	/** The currently selected EditParts. */
	private final List<IEditPart> selectedEditParts = new ArrayList<IEditPart>();

	/** The currently highlighted EditPart. */
	private IEditPart highlightedEditPart;

	/** newly selected editpart. */
	private IEditPart newlySelectedEditPart = null;

	/** The currently copied EditParts. */
	private final List<IDuplicatable> copiedEditParts = new ArrayList<IDuplicatable>();

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
				new ImageIcon(R.drawable.SELECT.url()));
	}

	@Override
	public void stopAction()
	{
		super.stopAction();

		// reset some stuff
		for (IEditPart e : this.selectedEditParts)
			e.setSelected(false);

		this.selectedEditParts.clear();
		this.canvas.setCursor(Cursor.getDefaultCursor());
	}

	@Override
	public boolean doOnMouseMoved(MouseEvent e)
	{
		super.doOnMouseMoved(e);

		if (this.highlightedEditPart != null)
			this.highlightedEditPart.setHighlighted(false);

		if (handleMouseEventForFeatures(
				this.selectedEditParts,
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

		for (IEditPart ep : this.selectedEditParts)
			ep.setHighlighted(false);

		if (handleMouseEventForFeatures(
				this.selectedEditParts,
				e,
				MouseEvent.MOUSE_PRESSED))
			return true;

		if (e.getButton() == MouseEvent.BUTTON1)
		{
			if (!e.isShiftDown())
			{
				for (IEditPart ep : this.selectedEditParts)
					ep.setSelected(false);

				this.selectedEditParts.clear();
			}

			for (IEditPart editPart : this.canvas.getEditParts())
			{
				if (isMouseOver(editPart, e.getPoint()))
				{
					this.selectedEditParts.add(editPart);
					editPart.setSelected(true);

					this.newlySelectedEditPart = editPart;

					this.canvas.repaint();

					handleMouseEventForFeatures(
							this.selectedEditParts,
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

			this.selectedEditParts.clear();
			for (IEditPart part : this.canvas.getEditParts())
			{
				if (part.intersects(this.selectionRect))
				{
					part.setSelected(true);
					this.selectedEditParts.add(part);
				}
				else
				{
					part.setSelected(false);
				}
			}

			this.canvas.repaint();

			return true;
		}

		if (handleMouseEventForFeatures(
				this.selectedEditParts,
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
				this.selectedEditParts,
				e,
				MouseEvent.MOUSE_RELEASED))
			return true;

		if (e.getButton() == MouseEvent.BUTTON1 && e.isShiftDown()
				&& this.newlySelectedEditPart == null)
		{
			for (IEditPart editPart : new LinkedList<IEditPart>(
					this.selectedEditParts))
			{
				if (isMouseOver(editPart, e.getPoint()))
				{
					this.selectedEditParts.remove(editPart);
					editPart.setSelected(false);

					this.canvas.repaint();
				}
			}
		}

		this.newlySelectedEditPart = null;

		return false;
	}

	@Override
	public boolean doOnKeyPressed(KeyEvent e)
	{
		boolean handled = false;
		if (e.getKeyCode() == KeyEvent.VK_DELETE)
		{
			if (!this.selectedEditParts.isEmpty())
			{
				// CompoundInfiniteUndoableEdit edit = new
				// CompoundInfiniteUndoableEdit();
				// final ModelRoot root = Application.getApp().getModel();
				// for (final IEditPart part : this.selectedEditParts)
				// {
				// edit.add(new CompoundInfiniteUndoableEdit()
				// {
				// @Override
				// public void redo()
				// {
				// root.removeChild(part.getModel());
				// }
				//
				// @Override
				// public void undo()
				// {
				// root.addChild(part.getModel());
				// }
				//
				// @Override
				// public String getPresentationName()
				// {
				// return R.string.WINDOW_MENU_EDIT_DELETE.string();
				// }
				// });
				// handled = true;
				// }
				for (final IEditPart part : new LinkedList<IEditPart>(
						this.selectedEditParts))
				{
					// TODO make undoable
					part.getModel().remove();
					handled = true;
				}
				this.selectedEditParts.clear();
			}
		}

		if (e.getKeyCode() == KeyEvent.VK_C)
		{
			this.copiedEditParts.clear();
			for (IEditPart part : this.selectedEditParts)
			{
				if (part instanceof IDuplicatable)
				{
					this.copiedEditParts.add((IDuplicatable) part);
					handled = true;
				}
			}
		}

		if (handled)
			return true;

		return this.handleKeyEventForFeatures(this.canvas.getEditParts(), e);
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

		for (IEditPart part : this.selectedEditParts)
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
		if (!this.selectedEditParts.isEmpty())
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

	/** @return the currently copied edit part. */
	public List<IDuplicatable> getCopiedEditParts()
	{
		return this.copiedEditParts;
	}
}
