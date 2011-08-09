/**
 * 
 */
package com.mpdeimos.tensor.editpart.feature;

import com.mpdeimos.tensor.action.canvas.ICanvasAction;
import com.mpdeimos.tensor.action.canvas.SelectEditPartAction;
import com.mpdeimos.tensor.ui.Application;
import com.mpdeimos.tensor.util.InfiniteUndoableEdit;
import com.mpdeimos.tensor.util.PointUtil;

import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.event.MouseEvent;

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
	public class Feature extends FeatureBase<IMovable, SelectEditPartAction>
	{
		/** The offset to the EditPart position when in moving mode. */
		private Dimension moveStartPointDelta;

		/** The EditPart position when starting movement. */
		private Point initialPosition;

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
					Point curPos = this.editPart.getPosition();
					this.moveStartPointDelta = PointUtil.getDelta(
							curPos,
							e.getPoint());
					this.initialPosition = new Point(curPos);
					return true;
				}
			}

			return false;
		}

		@Override
		public boolean doOnMouseDragged(ICanvasAction action, MouseEvent e)
		{
			if (this.moveStartPointDelta != null)
			{
				Point curPos = e.getPoint();
				curPos.translate(
						this.moveStartPointDelta.width,
						this.moveStartPointDelta.height);

				if (e.isControlDown())
					curPos.setLocation(
							(curPos.x / 10) * 10,
							(curPos.y / 10) * 10);

				this.editPart.setPosition(curPos);
				return true;
			}

			return false;
		}

		@Override
		public boolean doOnMouseReleased(ICanvasAction action, MouseEvent e)
		{
			if (this.moveStartPointDelta != null)
			{
				if (Feature.this.initialPosition.equals(Feature.this.editPart.getPosition()))
					return false;

				Application.getApp().getUndoManager().addEdit(
						new InfiniteUndoableEdit()
				{
					private Point before;
					private Point after;

					@Override
					protected void init()
					{
						this.before = Feature.this.initialPosition;
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
				});
			}
			this.moveStartPointDelta = null;
			return false;
		}
	}
}
