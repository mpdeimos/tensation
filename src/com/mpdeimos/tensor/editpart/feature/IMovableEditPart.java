/**
 * 
 */
package com.mpdeimos.tensor.editpart.feature;

import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.event.MouseEvent;

import com.mpdeimos.tensor.ui.DrawingCanvas;
import com.mpdeimos.tensor.util.PointUtil;

/**
 * Base interface for movable EditParts.
 * 
 * @author mpdeimos
 * 
 */
public interface IMovableEditPart extends IFeatureEditPart
{
	/** Returns the current position of the EditPart. */
	public Point getPosition();

	/** Sets the position of the current EditPart. */
	public void setPosition(Point p);

	/** feature class for movable EditParts */
	public class Feature extends FeatureBase<IMovableEditPart>
	{
		/** The offset to the EditPart position when in moving mode. */
		private Dimension moveStartPointDelta;

		/** Constructor. */
		public Feature(IMovableEditPart editPart)
		{
			super(editPart);
		}

		@Override
		public boolean doOnMouseMoved(DrawingCanvas canvas, MouseEvent e)
		{
			if (this.editPart.getBoundingRectangle().contains(e.getPoint()))
			{
				canvas.setCursor(new Cursor(Cursor.MOVE_CURSOR));
				canvas.repaint();
				return true;
			}
			return false;
		}

		@Override
		public boolean doOnMousePressed(DrawingCanvas canvas, MouseEvent e)
		{
			if (e.getButton() == MouseEvent.BUTTON1)
			{
				if (this.editPart.getBoundingRectangle().contains(e.getPoint()))
				{
					Point curPos = this.editPart.getPosition();
					this.moveStartPointDelta = PointUtil.getDelta(
							curPos,
							e.getPoint());
					return true;
				}
			}

			return false;
		}

		@Override
		public boolean doOnMouseDragged(DrawingCanvas canvas, MouseEvent e)
		{
			if (this.moveStartPointDelta != null)
			{
				Point curPos = e.getPoint();
				curPos.translate(
						this.moveStartPointDelta.width,
						this.moveStartPointDelta.height);
				this.editPart.setPosition(curPos);
				return true;
			}

			return false;
		}

		@Override
		public boolean doOnMouseReleased(DrawingCanvas canvas, MouseEvent e)
		{
			this.moveStartPointDelta = null;
			return false;
		}
	}
}
