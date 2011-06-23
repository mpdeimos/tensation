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

import com.mpdeimos.tensor.action.SelectEditPartAction;
import com.mpdeimos.tensor.model.IModelData;
import com.mpdeimos.tensor.model.ModelChangedAdapter;
import com.mpdeimos.tensor.ui.DrawingCanvas;
import com.mpdeimos.tensor.util.Log;
import com.mpdeimos.tensor.util.PointUtil;

/**
 * Base interface for rotatable EditParts.
 * 
 * @author mpdeimos
 *
 */
public interface IRotatableEditPart extends IFeatureEditPart {
	/** Returns the current rotation of the EditPart in degrees. */
	public double getRotation();
	
	/** Sets the rotation of the current EditPart in degrees. */
	public void setRotation(double degrees);

	/** The feature for interacting with this EditPart. */
	class Feature extends FeatureBase<IRotatableEditPart>
	{
		/** The Point where the rotation indicator is shown */
		private Point rotationIndicator;
		
		/** The offset to the rotation indicator when in rotating mode. */
		private Dimension rotationStartPointDelta;

		/** The bounding rectangle of this EditPart. */
		private Rectangle r;
		
		/** Constructor. */
		public Feature(IRotatableEditPart editPart) {
			super(editPart);
			
			r = this.editPart.getBoundingRectangle();
			this.editPart.getModelData().addModelChangedListener(new ModelChangedAdapter() {
				@Override
				public void onModelChanged(IModelData model) {
					r = Feature.this.editPart.getBoundingRectangle();
				}
			});
		}
		
		@Override
		public boolean doOnMousePressed(DrawingCanvas canvas, MouseEvent e) {
			if (rotationIndicator != null && e.getButton() == MouseEvent.BUTTON1)
			{
				Rectangle r = this.editPart.getBoundingRectangle();
				
				if (hasHitRotationIndicator(r, e))
				{
					Point curPos = new Point(e.getPoint());
					PointUtil.move(curPos, -(int)r.getCenterX(), -(int)r.getCenterY());
					rotationStartPointDelta = PointUtil.getDelta(curPos, rotationIndicator);
					return true;
				}
			}
			
			return false;
		}
		
		@Override
		public boolean doOnMouseDragged(DrawingCanvas canvas, MouseEvent e) {
			if (rotationStartPointDelta != null)
			{
				Point curPos = e.getPoint();
				curPos.translate(-(int)r.getCenterX()+rotationStartPointDelta.width, -(int)r.getCenterY()+rotationStartPointDelta.height);
				double ang = Math.atan(curPos.getY()/curPos.getX())/Math.PI*180 + 45;
				if (curPos.getX() < 0)
					ang += 180;
				
				updateRoatationIndicator(r, ang);
				editPart.setRotation(ang);
				
				return true;
			}
			
			return false;
		}
		
		@Override
		public boolean doOnMouseMoved(DrawingCanvas canvas, MouseEvent e) {
			if (rotationIndicator != null)
			{
				if (hasHitRotationIndicator(r, e))
				{
					canvas.setCursor(new Cursor(Cursor.CROSSHAIR_CURSOR));
					canvas.repaint();
					return true;
				}
			}
			return false;
		}
		
		@Override
		public boolean doOnMouseReleased(DrawingCanvas canvas, MouseEvent e)
		{
			this.rotationStartPointDelta = null;
			return false;
		}
		
		@Override
		public void doOnEditPartSelected(boolean selected)
		{
			if (selected)
				updateRoatationIndicator(r, editPart.getRotation());
			else
				rotationIndicator = null;
		}
		
		@Override
		public boolean drawOverlay(DrawingCanvas canvas, Graphics2D gfx)
		{
			try {
				Image img = ImageIO.read(R.drawable.getURL("circle-green")); //$NON-NLS-1$
				gfx.drawImage(img, (int)r.getCenterX() + rotationIndicator.x - 8, (int)r.getCenterY() + rotationIndicator.y - 8, null);
				
				return true;
			} catch (IOException e) {
				Log.e(this, "Could not load image"); //$NON-NLS-1$
			}
			
			return true;
		}
		
		/** determines whether the rotation indicator has been hit by this mouse event. */
		private boolean hasHitRotationIndicator(Rectangle r, MouseEvent e) {
			return rotationIndicator.distance(e.getPoint().x - r.getCenterX(), e.getPoint().y - r.getCenterY()) < 10;
		}

		/** updates the rotation indicator */
		private void updateRoatationIndicator(Rectangle r, double degrees) {
			rotationIndicator = new Point(((int)r.getWidth()/2)+SelectEditPartAction.EDITPART_SELECTION_STROKE_OFFSET,
					(-(int)r.getHeight()/2)-SelectEditPartAction.EDITPART_SELECTION_STROKE_OFFSET);

			PointUtil.rotate(rotationIndicator, degrees/180*Math.PI);
		}
	}
}