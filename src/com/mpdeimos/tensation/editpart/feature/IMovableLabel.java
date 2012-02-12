package com.mpdeimos.tensation.editpart.feature;

import com.mpdeimos.tensation.action.canvas.ICanvasAction;
import com.mpdeimos.tensation.action.canvas.SelectEditPartAction;
import com.mpdeimos.tensation.util.Gfx;
import com.mpdeimos.tensation.util.VecMath;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

/**
 * Interface for Labeled EditParts where the label is movable.
 * 
 * @author mpdeimos
 * 
 */
public interface IMovableLabel extends IFeatureEditPart
{
	/** @return the position of the label. */
	public Point2D getLabelPosition();

	/** set the position of the label. */
	public void setLabelPosition(Point2D position);

	/** @return the label of the editpart. null if not set. */
	public String getLabel();

	/** feature class for EditParts with connection sources. */
	public class Feature extends SelectEditPartAction.IFeature<IMovableLabel>
	{
		/** flag for determining if the overlay is drawn. */
		boolean drawOverlay = false;

		/** the movement start point. */
		private Point2D moveStart;

		/** the initial position upon moving. */
		private Point2D moveInitial;

		/** Constructor. */
		public Feature(IMovableLabel editPart)
		{
			super(editPart);
		}

		@Override
		public boolean doOnMouseMoved(ICanvasAction action, MouseEvent e)
		{
			Rectangle2D labelBounds = getLabelBounds();
			if (labelBounds == null)
				return false;

			this.drawOverlay = false;

			if (labelBounds.contains(e.getPoint()))
			{
				action.getCanvas().setCursor(new Cursor(Cursor.MOVE_CURSOR));
				this.drawOverlay = true;
				action.getCanvas().repaint();
				return true;
			}
			return false;
		}

		@Override
		public boolean doOnMousePressed(ICanvasAction action, MouseEvent e)
		{
			Rectangle2D labelBounds = getLabelBounds();
			if (labelBounds == null)
				return false;

			if (!labelBounds.contains(e.getPoint()))
				return false;

			this.drawOverlay = true;
			this.moveStart = VecMath.fresh(e.getPoint());
			this.moveInitial = VecMath.fresh(this.editPart.getLabelPosition());

			return true;
		}

		@Override
		public boolean doOnMouseDragged(ICanvasAction action, MouseEvent e)
		{
			if (this.moveStart == null)
				return false;

			Point2D p = VecMath.sub(
						VecMath.fresh(e.getPoint()),
						this.moveStart);
			VecMath.add(p, this.moveInitial);
			this.editPart.setLabelPosition(p);
			return true;
		}

		@Override
		public boolean doOnMouseReleased(ICanvasAction action, MouseEvent e)
		{
			this.moveStart = null;
			return false;
		}

		@Override
		public boolean drawOverlay(ICanvasAction action, Graphics2D gfx)
		{
			if (!this.drawOverlay)
				return false;

			Rectangle2D labelBounds = getLabelBounds();
			if (labelBounds == null)
				return false;

			Color c = gfx.getColor();
			gfx.setColor(Color.LIGHT_GRAY);
			Gfx.drawRect(gfx, labelBounds);
			gfx.setColor(c);

			return false;
		}

		/** @return the bounds of the label. */
		private Rectangle2D getLabelBounds()
		{
			if (this.editPart.getLabel() == null)
				return null;

			Point2D labelPosition = this.editPart.getLabelPosition();
			if (labelPosition == null)
				labelPosition = VecMath.fresh();

			Dimension bounds = Gfx.approximateTextWidth(
					Gfx.SANS_SERIF_10,
					this.editPart.getLabel());

			Point2D corner = VecMath.fresh(
					bounds.width / 2 + 2,
					bounds.height / 2 + 2);
			VecMath.add(corner, labelPosition);

			Rectangle2D rect = new Rectangle2D.Double();
			rect.setFrameFromCenter(labelPosition, corner);
			return rect;
		}
	}
}
