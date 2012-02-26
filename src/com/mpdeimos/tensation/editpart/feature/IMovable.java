/**
 * 
 */
package com.mpdeimos.tensation.editpart.feature;

import com.mpdeimos.tensation.action.canvas.ICanvasAction;
import com.mpdeimos.tensation.action.canvas.SelectEditPartAction;
import com.mpdeimos.tensation.ui.Application;
import com.mpdeimos.tensation.util.CompoundInfiniteUndoableEdit;
import com.mpdeimos.tensation.util.InfiniteUndoableEdit;
import com.mpdeimos.tensation.util.VecMath;

import java.awt.Cursor;
import java.awt.Point;
import java.awt.event.MouseEvent;

import javax.swing.undo.UndoManager;

import resources.R;

/**
 * Base interface for movable EditParts.
 * 
 * @author mpdeimos
 * 
 */
public interface IMovable extends IFeatureEditPart
{
	/** Returns the current position of the EditPart. */
	public Point getPosition();

	/** Sets the position of the current EditPart. */
	public void setPosition(Point p);

	/** feature class for movable EditParts */
	public class Feature extends SelectEditPartAction.IFeature<IMovable>
	{
		/** The offset to the EditPart position when in moving mode. */
		private static Point moveStartPoint;

		/** Amount of tensors to move along. */
		private static int moveAlongCount = 0;

		/** The EditPart position when starting movement. */
		private Point initialPosition;

		/** The compound edit */
		private static CompoundInfiniteUndoableEdit compoundEdit;

		/** Constructor. */
		public Feature(IMovable editPart)
		{
			super(editPart);
		}

		@Override
		public boolean doOnMouseMoved(ICanvasAction action, MouseEvent e)
		{
			if (this.editPart.getBoundingRectangle().contains(e.getPoint()))
			{
				action.getCanvas().setCursor(new Cursor(Cursor.MOVE_CURSOR));
				action.getCanvas().repaint();
				return true;
			}
			return false;
		}

		@Override
		public boolean doOnMousePressed(ICanvasAction action, MouseEvent e)
		{
			if (e.getButton() == MouseEvent.BUTTON1)
			{
				if (this.editPart.getBoundingRectangle().contains(e.getPoint()))
				{
					Feature.moveStartPoint = new Point(e.getPoint());
					this.initialPosition = new Point(
							this.editPart.getPosition());
					moveAlongCount = 1;
					return true;
				}
			}

			return false;
		}

		@Override
		public boolean doOnMouseDragged(ICanvasAction action, MouseEvent e)
		{
			if (Feature.moveStartPoint != null)
			{
				if (this.initialPosition == null)
				{
					this.initialPosition = new Point(
							this.editPart.getPosition());
					moveAlongCount++;
				}

				Point p = this.editPart.getPosition();
				VecMath.sub(
						VecMath.fresh(e.getPoint()),
						Feature.moveStartPoint,
						p);
				VecMath.add(p, this.initialPosition);

				if (e.isControlDown())
					VecMath.mul(VecMath.div(p, 10), 10);

				this.editPart.setPosition(p);
				return false;
			}

			return false;
		}

		@Override
		public boolean doOnMouseReleased(ICanvasAction action, MouseEvent e)
		{
			if (Feature.moveStartPoint != null)
			{
				moveAlongCount--;
				if (moveAlongCount == 0)
					Feature.moveStartPoint = null;

				if (Feature.this.initialPosition == null
						|| Feature.this.editPart.getPosition().equals(
								Feature.this.initialPosition))
					return false;

				InfiniteUndoableEdit edit = new InfiniteUndoableEdit()
				{
					private Point before;
					private Point after;

					@Override
					protected void init()
					{
						this.before = new Point(Feature.this.initialPosition);
						this.after = new Point(
								Feature.this.editPart.getPosition());
					}

					@Override
					public String getPresentationName()
					{
						return R.string.WINDOW_ACTION_MOVE.string();
					}

					@Override
					public void undo()
					{
						Feature.this.editPart.setPosition(this.before);
					}

					@Override
					public void redo()
					{
						Feature.this.editPart.setPosition(this.after);
					}
				};

				if (moveAlongCount > 0)
				{
					if (compoundEdit == null)
						compoundEdit = new CompoundInfiniteUndoableEdit();

					compoundEdit.add(edit);
				}
				else
				{
					UndoManager undoManager = Application.getApp().getActiveCanvas().getUndoManager();
					if (compoundEdit == null)
					{
						undoManager.addEdit(edit);
					}
					else
					{
						compoundEdit.add(edit);
						undoManager.addEdit(compoundEdit);
						compoundEdit = null;
					}
				}
			}
			this.initialPosition = null;
			return false;
		}
	}
}
