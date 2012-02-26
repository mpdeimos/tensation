package com.mpdeimos.tensation.action.canvas;

import com.mpdeimos.tensation.editpart.feature.IConnectable.ConnectionPoint;
import com.mpdeimos.tensation.editpart.feature.IFeatureEditPart;
import com.mpdeimos.tensation.feature.ICanvasFeatureContract;
import com.mpdeimos.tensation.model.ModelRoot;
import com.mpdeimos.tensation.model.TensorConnection;
import com.mpdeimos.tensation.util.InfiniteUndoableEdit;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.geom.Ellipse2D;

import javax.swing.ImageIcon;

import resources.R;

/**
 * Action for connecting two tensors.
 * 
 * @author mpdeimos
 * 
 */
public class TensorConnectAction extends CanvasActionBase
{
	/** The point where to start drawing the connection. */
	private ConnectionPoint startConnectionPoint;

	/** The Dragging position of the mouse. */
	private Point curDraggingPos;

	/** The point where to end drawing the connection. */
	private ConnectionPoint endConnectionPoint;

	/**
	 * Constructor.
	 */
	public TensorConnectAction()
	{
		super(
				R.string.WINDOW_ACTION_CONNECT.string(),
				new ImageIcon(R.drawable.ACTION_CONNECT.url()), KeyEvent.VK_C);
	}

	@Override
	public void actionPerformed(ActionEvent e)
	{
		super.actionPerformed(e);

		this.startConnectionPoint = null;
	}

	@Override
	public boolean drawOverlay(Graphics2D gfx)
	{
		Color oldPaint = gfx.getColor();
		if (getStartPoint() != null)
		{
			if (this.curDraggingPos != null)
			{
				if (this.endConnectionPoint != null)
				{
					// snapping & different color as feedback
					gfx.setColor(Color.BLUE);
					gfx.drawLine(
							(int) this.endConnectionPoint.getPoint().getX(),
							(int) this.endConnectionPoint.getPoint().getY(),
							(int) this.startConnectionPoint.getPoint().getX(),
							(int) this.startConnectionPoint.getPoint().getY());
				}
				else
				{
					gfx.setColor(Color.RED);
					gfx.drawLine(
							this.curDraggingPos.x,
							this.curDraggingPos.y,
							(int) this.startConnectionPoint.getPoint().getX(),
							(int) this.startConnectionPoint.getPoint().getY());
				}
			}

			gfx.setColor(Color.BLUE);
			gfx.fill(new Ellipse2D.Double(
					this.startConnectionPoint.getPoint().getX() - 4,
					this.startConnectionPoint.getPoint().getY() - 4,
					8,
					8));

			gfx.setColor(Color.GREEN);
		}
		else
		{
			gfx.setColor(Color.BLUE);
		}

		super.drawOverlayForFeatures(this.canvas.getEditParts(), gfx);

		if (this.endConnectionPoint != null)
		{
			gfx.setColor(Color.BLUE);
			gfx.fill(new Ellipse2D.Double(
					this.endConnectionPoint.getPoint().getX() - 4,
					this.endConnectionPoint.getPoint().getY() - 4,
					8,
					8));
		}

		gfx.setColor(oldPaint);

		return true;
	}

	@Override
	public boolean doOnMouseMoved(MouseEvent e)
	{
		if (handleMouseEventForFeatures(
				this.canvas.getEditParts(),
				e,
				MouseEvent.MOUSE_MOVED))
			return true;

		this.canvas.setCursor(Cursor.getDefaultCursor());
		this.canvas.repaint();

		return true;
	}

	@Override
	public boolean doOnMousePressed(MouseEvent e)
	{
		if (handleMouseEventForFeatures(
				this.canvas.getEditParts(),
				e,
				MouseEvent.MOUSE_PRESSED))
			return true;

		this.startConnectionPoint = null;
		this.canvas.repaint();

		return true;
	}

	@Override
	public boolean doOnMouseDragged(MouseEvent e)
	{
		if (getStartPoint() == null)
			return false;

		this.endConnectionPoint = null;
		this.curDraggingPos = e.getPoint();

		handleMouseEventForFeatures(
				this.canvas.getEditParts(),
				e,
				MouseEvent.MOUSE_DRAGGED);

		this.canvas.repaint();

		return true;
	}

	@Override
	public boolean doOnMouseReleased(MouseEvent e)
	{
		this.endConnectionPoint = null;

		handleMouseEventForFeatures(
				this.canvas.getEditParts(),
				e,
				MouseEvent.MOUSE_DRAGGED);

		if (this.endConnectionPoint != null)
		{
			final ModelRoot model = this.canvas.getModel();
			final TensorConnection connection = new TensorConnection(
					model,
					this.startConnectionPoint.getAnchor(),
					this.endConnectionPoint.getAnchor());

			this.canvas.getUndoManager().addEdit(
					new InfiniteUndoableEdit()
			{
				@Override
				public String getPresentationName()
				{
					return R.string.WINDOW_ACTION_CONNECT.string();
				}

				@Override
				public void redo()
				{
					model.addChild(connection);
					connection.renew();
				}

				@Override
				public void undo()
				{
					connection.remove();
				}
			}.act());
		}

		this.endConnectionPoint = null;
		this.startConnectionPoint = null;
		this.curDraggingPos = null;

		this.canvas.repaint();

		return true;
	}

	/** Sets the start point for a new connection. */
	public void setStartPoint(ConnectionPoint connection)
	{
		this.startConnectionPoint = connection;

		this.canvas.repaint();
	}

	/** Sets the end point for a new connection. */
	public void setEndPoint(ConnectionPoint connection)
	{
		this.endConnectionPoint = connection;
	}

	/** @return whether a start point is already defined. */
	public ConnectionPoint getStartPoint()
	{
		return this.startConnectionPoint;
	}

	/** Contract for tensor connection features. */
	public static abstract class IFeature<I extends IFeatureEditPart> extends
			CanvasFeatureBase<I, TensorConnectAction.IFeature<?>>
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
