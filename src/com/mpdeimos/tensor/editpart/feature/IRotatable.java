package com.mpdeimos.tensor.editpart.feature;

import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.io.IOException;

import javax.imageio.ImageIO;

import resources.R;

import com.mpdeimos.tensor.action.ICanvasAction;
import com.mpdeimos.tensor.action.SelectEditPartAction;
import com.mpdeimos.tensor.util.Log;
import com.mpdeimos.tensor.util.PointUtil;

/**
 * Base interface for rotatable EditParts.
 * 
 * @author mpdeimos
 * 
 */
public interface IRotatable extends IFeatureEditPart
{
	/** Returns the current rotation of the EditPart in degrees. */
	public double getRotation();

	/** Sets the rotation of the current EditPart in degrees. */
	public void setRotation(double degrees);

	/** @return the position of the rotation indicator. */
	public Dimension getRotationIndicatorOffset();

	/** The feature for interacting with this EditPart. */
	class Feature extends FeatureBase<IRotatable, SelectEditPartAction>
	{
		/** The Point where the rotation indicator is shown */
		private Point rotationIndicator;

		/** The offset to the rotation indicator when in rotating mode. */
		private Dimension rotationStartPointDelta;

		/** the rotation indicator offset. */
		private final Dimension indicatorOffset;

		/** the initial rotation of the indicator. */
		private final double indicatorRoatation;

		/** Constructor. */
		public Feature(IRotatable editPart)
		{
			super(editPart);
			this.indicatorOffset = editPart.getRotationIndicatorOffset();
			this.indicatorRoatation = Math.atan(this.indicatorOffset.height
					/ this.indicatorOffset.width);
		}

		@Override
		public boolean doOnMousePressed(ICanvasAction action, MouseEvent e)
		{
			if (this.rotationIndicator != null
					&& e.getButton() == MouseEvent.BUTTON1)
			{
				Rectangle r = this.editPart.getBoundingRectangle();

				if (hasHitRotationIndicator(r, e))
				{
					Point curPos = new Point(e.getPoint());
					PointUtil.move(
							curPos,
							-(int) r.getCenterX(),
							-(int) r.getCenterY());
					this.rotationStartPointDelta = PointUtil.getDelta(
							curPos,
							this.rotationIndicator);
					return true;
				}
			}

			return false;
		}

		@Override
		public boolean doOnMouseDragged(ICanvasAction action, MouseEvent e)
		{
			if (this.rotationStartPointDelta != null)
			{
				Rectangle r = this.editPart.getBoundingRectangle();

				Point curPos = e.getPoint();
				curPos.translate(
						-(int) r.getCenterX()
								+ this.rotationStartPointDelta.width,
						-(int) r.getCenterY()
								+ this.rotationStartPointDelta.height);
				double ang = (Math.atan(curPos.getY() / curPos.getX()) - this.indicatorRoatation)
						/ Math.PI * 180;
				if (curPos.getX() < 0)
					ang += 180;

				if (e.isControlDown())
					ang = (int) (ang / 22.5) * 22.5;

				updateRoatationIndicator(ang);
				this.editPart.setRotation(ang);

				return true;
			}

			return false;
		}

		@Override
		public boolean doOnMouseMoved(ICanvasAction action, MouseEvent e)
		{
			if (this.rotationIndicator == null)
				return false;

			Rectangle r = this.editPart.getBoundingRectangle();
			if (!hasHitRotationIndicator(r, e))
				return false;

			action.getCanvas().setCursor(
					new Cursor(Cursor.CROSSHAIR_CURSOR));
			action.getCanvas().repaint();
			return true;
		}

		@Override
		public boolean doOnMouseReleased(ICanvasAction action, MouseEvent e)
		{
			this.rotationStartPointDelta = null;
			return false;
		}

		@Override
		public void doOnEditPartSelected(boolean selected)
		{
			if (selected)
			{
				updateRoatationIndicator(this.editPart.getRotation());
			}
			else
			{
				this.rotationIndicator = null;
			}
		}

		@Override
		public boolean drawOverlay(ICanvasAction action, Graphics2D gfx)
		{
			try
			{
				Rectangle r = this.editPart.getBoundingRectangle();
				Image img = ImageIO.read(R.drawable.getURL("overlay-rotate")); //$NON-NLS-1$
				gfx.drawImage(img,
						(int) r.getCenterX()
								+ this.rotationIndicator.x - 8,
						(int) r.getCenterY()
								+ this.rotationIndicator.y - 8,
						null);

			}
			catch (IOException e)
			{
				Log.e(this, "Could not load image"); //$NON-NLS-1$
			}

			return false;
		}

		/**
		 * determines whether the rotation indicator has been hit by this mouse
		 * event.
		 */
		private boolean hasHitRotationIndicator(Rectangle r, MouseEvent e)
		{
			return this.rotationIndicator.distance(
					e.getPoint().x - r.getCenterX(),
					e.getPoint().y - r.getCenterY()) < 10;
		}

		/** updates the rotation indicator */
		private void updateRoatationIndicator(double degrees)
		{
			this.rotationIndicator = new Point(
					this.indicatorOffset.width
							+ (int) Math.signum(this.indicatorOffset.width)
							* SelectEditPartAction.EDITPART_SELECTION_STROKE_OFFSET,
					this.indicatorOffset.height
							+ (int) Math.signum(this.indicatorOffset.height)
							* SelectEditPartAction.EDITPART_SELECTION_STROKE_OFFSET);

			PointUtil.rotate(this.rotationIndicator, degrees / 180 * Math.PI);
		}
	}
}