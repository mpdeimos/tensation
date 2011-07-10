package com.mpdeimos.tensor.editpart.feature;

import java.awt.BasicStroke;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.Stroke;
import java.awt.event.MouseEvent;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.io.IOException;

import javax.imageio.ImageIO;

import resources.R;

import com.mpdeimos.tensor.action.ICanvasAction;
import com.mpdeimos.tensor.action.SelectEditPartAction;
import com.mpdeimos.tensor.model.EpsilonTensor;
import com.mpdeimos.tensor.model.TensorConnection;
import com.mpdeimos.tensor.util.Log;
import com.mpdeimos.tensor.util.PointUtil;

/**
 * Feature for controlling bezier points for connections.
 * 
 * @author mpdeimos
 * 
 */
public interface IConnectionControl extends IFeatureEditPart
{
	/** @return the source anchor. */
	public Point2D getSourceAnchor();

	/** @return the sink anchor. */
	public Point2D getSinkAnchor();

	/** @return the control point for the source anchor. */
	public Point2D getSourceControlPoint(boolean unstretched);

	/** @return the control point for the sink anchor. */
	public Point2D getSinkControlPoint(boolean unstretched);

	/** sets the control point distance for the source anchor. */
	public void setSourceControlPointDistance(double d);

	/** sets the control point distance for the sink anchor. */
	public void setSinkControlPointDistance(double d);

	/** the control feature. */
	public class Feature extends
			FeatureBase<IConnectionControl, SelectEditPartAction>
	{
		/** The stroke of the anchor line. */
		private static BasicStroke ANCHOR_CONTROL_STROKE = new BasicStroke(
				1.0f,
				BasicStroke.CAP_SQUARE,
				BasicStroke.JOIN_MITER,
				1,
				new float[] { 3f, 3f },
				0);

		/** the movement delta. */
		private Dimension movementDelta;

		/** the flag of what anchor we're actually moving. */
		private short movementIndex;

		/** Constructor. */
		public Feature(IConnectionControl editPart)
		{
			super(editPart);
		}

		@Override
		public boolean doOnMousePressed(ICanvasAction action, MouseEvent e)
		{
			this.movementIndex = hasHitControlPoint(e);
			if (this.movementIndex == 0)
				return false;

			Point2D controlPoint = null;

			if (this.movementIndex < 0)
				controlPoint = this.editPart.getSinkControlPoint(false);
			else
				controlPoint = this.editPart.getSourceControlPoint(false);

			this.movementDelta = PointUtil.getDelta(e.getPoint(), controlPoint);

			return true;
		}

		@Override
		public boolean doOnMouseDragged(ICanvasAction action, MouseEvent e)
		{
			if (this.movementIndex == 0 || this.movementDelta == null)
				return false;

			Point2D orgControlPoint = null;
			EpsilonTensor tensor = null;

			if (this.movementIndex < 0)
			{
				orgControlPoint = this.editPart.getSinkControlPoint(true);
				tensor = ((TensorConnection) this.editPart.getModelData())
						.getSink().getTensor();
			}
			else
			{
				orgControlPoint = this.editPart.getSourceControlPoint(true);
				tensor = ((TensorConnection) this.editPart.getModelData())
						.getSource().getTensor();
			}

			Point curPos = new Point(e.getPoint());
			PointUtil.move(
					curPos,
					-this.movementDelta.width,
					-this.movementDelta.height);

			double d = tensor.getPosition().distance(curPos);
			double dNorm = tensor.getPosition().distance(orgControlPoint);

			d = Math.max(1, 2 * d / dNorm) - 1;

			if (this.movementIndex < 0)
				this.editPart.setSinkControlPointDistance(d);
			else
				this.editPart.setSourceControlPointDistance(d);

			return true;
		}

		@Override
		public boolean doOnMouseReleased(ICanvasAction action, MouseEvent e)
		{
			this.movementDelta = null;
			return false;
		}

		@Override
		public boolean doOnMouseMoved(ICanvasAction action, MouseEvent e)
		{
			if (hasHitControlPoint(e) == 0)
				return false;

			action.getCanvas().setCursor(
					new Cursor(Cursor.CROSSHAIR_CURSOR));
			action.getCanvas().repaint();

			return true;
		}

		@Override
		public boolean drawOverlay(ICanvasAction action, Graphics2D gfx)
		{
			Point2D sinkControlPoint = this.editPart.getSinkControlPoint(false);
			Point2D sourceControlPoint = this.editPart.getSourceControlPoint(false);

			Stroke oldStroke = gfx.getStroke();
			gfx.setStroke(ANCHOR_CONTROL_STROKE);

			Line2D line = new Line2D.Double(
					sinkControlPoint,
					this.editPart.getSinkAnchor());
			gfx.draw(line);

			Line2D line2 = new Line2D.Double(
					sourceControlPoint,
					this.editPart.getSourceAnchor());
			gfx.draw(line2);

			gfx.setStroke(oldStroke);

			try
			{
				Image img = ImageIO.read(R.drawable.getURL("circle-green")); //$NON-NLS-1$
				gfx.drawImage(img,
						(int) sourceControlPoint.getX() - 8,
						(int) sourceControlPoint.getY() - 8,
						null);
				gfx.drawImage(img,
						(int) sinkControlPoint.getX() - 8,
						(int) sinkControlPoint.getY() - 8,
						null);

			}
			catch (IOException e)
			{
				Log.e(this, "Could not load image"); //$NON-NLS-1$
			}

			return false;
		}

		/**
		 * determines whether one of the indicators has been hit by the mouse.
		 */
		private short hasHitControlPoint(MouseEvent e)
		{
			Point2D sourceControlPoint = this.editPart.getSourceControlPoint(false);
			Point2D sinkControlPoint = this.editPart.getSinkControlPoint(false);

			if (sourceControlPoint.distance(e.getPoint().x, e.getPoint().y) < 10)
				return 1;
			if (sinkControlPoint.distance(e.getPoint().x, e.getPoint().y) < 10)
				return -1;

			return 0;
		}
	}
}
