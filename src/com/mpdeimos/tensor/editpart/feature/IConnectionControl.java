package com.mpdeimos.tensor.editpart.feature;

import com.mpdeimos.tensor.action.canvas.ICanvasAction;
import com.mpdeimos.tensor.action.canvas.SelectEditPartAction;
import com.mpdeimos.tensor.model.TensorBase;
import com.mpdeimos.tensor.model.TensorConnection;
import com.mpdeimos.tensor.ui.Application;
import com.mpdeimos.tensor.util.InfiniteUndoableEdit;
import com.mpdeimos.tensor.util.Log;
import com.mpdeimos.tensor.util.PointUtil;
import com.mpdeimos.tensor.util.Tupel;

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

	/** @return the source control point distance. */
	public double getSourceControlPointDistance();

	/** @return the sink control point distance. */
	public double getSinkControlPointDistance();

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

		/** Initial Control Point distances. */
		private final Tupel<Double, Double> initialControlDistances;

		/** Constructor. */
		public Feature(IConnectionControl editPart)
		{
			super(editPart);

			this.initialControlDistances = new Tupel<Double, Double>();
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

			this.initialControlDistances.$1 = this.editPart.getSinkControlPointDistance();
			this.initialControlDistances.$2 = this.editPart.getSourceControlPointDistance();

			return true;
		}

		@Override
		public boolean doOnMouseDragged(ICanvasAction action, MouseEvent e)
		{
			if (this.movementIndex == 0 || this.movementDelta == null)
				return false;

			Point2D orgControlPoint = null;
			TensorBase tensor = null;

			if (this.movementIndex < 0)
			{
				orgControlPoint = this.editPart.getSinkControlPoint(true);
				tensor = ((TensorConnection) this.editPart.getModel())
						.getSink().getTensor();
			}
			else
			{
				orgControlPoint = this.editPart.getSourceControlPoint(true);
				tensor = ((TensorConnection) this.editPart.getModel())
						.getSource().getTensor();
			}

			Point curPos = new Point(e.getPoint());
			PointUtil.move(
					curPos,
					-this.movementDelta.width,
					-this.movementDelta.height);

			double d = tensor.getPosition().distance(curPos);

			if (e.isControlDown())
				d = (int) (d / 10) * 10;

			double dNorm = tensor.getPosition().distance(orgControlPoint);

			d = Math.max(1, 2 * d / dNorm) - 1;

			if (this.movementIndex < 0 || e.isShiftDown())
				this.editPart.setSinkControlPointDistance(d);
			if (this.movementIndex > 0 || e.isShiftDown())
				this.editPart.setSourceControlPointDistance(d);

			return true;
		}

		@Override
		public boolean doOnMouseReleased(ICanvasAction action, MouseEvent e)
		{
			if (this.movementDelta != null)
			{
				if (this.initialControlDistances.$1 == Feature.this.editPart.getSinkControlPointDistance()
						&& this.initialControlDistances.$2 == Feature.this.editPart.getSourceControlPointDistance())
					return false;

				Application.getApp().getUndoManager().addEdit(
						new InfiniteUndoableEdit()
				{
					private Tupel<Double, Double> before;
					private Tupel<Double, Double> after;

					@Override
					protected void init()
					{
						this.before = new Tupel<Double, Double>(
								Feature.this.initialControlDistances);
						this.after = new Tupel<Double, Double>();
						this.after.$1 = Feature.this.editPart.getSinkControlPointDistance();
						this.after.$2 = Feature.this.editPart.getSourceControlPointDistance();
					}

					@Override
					public void undo()
					{
						Feature.this.editPart.setSinkControlPointDistance(this.before.$1);
						Feature.this.editPart.setSourceControlPointDistance(this.before.$2);
					}

					@Override
					public void redo()
					{
						Feature.this.editPart.setSinkControlPointDistance(this.after.$1);
						Feature.this.editPart.setSourceControlPointDistance(this.after.$2);
					}
				});
			}

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
				Image img = ImageIO.read(R.drawable.CIRCLE_GREEN.url());
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
