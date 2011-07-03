package com.mpdeimos.tensor.action;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Point2D;

import javax.swing.ImageIcon;

import resources.R;

import com.mpdeimos.tensor.editpart.feature.IConnectable;
import com.mpdeimos.tensor.editpart.feature.IConnectable.Connection;
import com.mpdeimos.tensor.ui.DrawingCanvas;

/**
 * Action for connecting two tensors.
 * 
 * @author mpdeimos
 * 
 */
public class TensorConnectAction extends CanvasActionBase
{
	/** The EditPart we start creating a connection. */
	private IConnectable startConnectable;

	/** The connection ID of the start point. */
	private int startConnectionID;

	/** The point where to start drawing the connection. */
	private Point2D startConnectionPoint;

	/** The Dragging position of the mouse. */
	private Point curDraggingPos;

	/** The EditPart we end creating a connection. */
	private IConnectable endConnectable;

	/** The connection ID of the end point. */
	private int endConnectionID;

	/** The point where to end drawing the connection. */
	private Point2D endConnectionPoint;

	/**
	 * Constructor.
	 */
	public TensorConnectAction(DrawingCanvas canvas)
	{
		super(
				canvas,
				R.strings.getString("window_action_connect"), //$NON-NLS-1$
				new ImageIcon(R.drawable.getURL("action_connect"))); //$NON-NLS-1$
	}

	@Override
	public void actionPerformed(ActionEvent e)
	{
		super.actionPerformed(e);

		this.startConnectable = null;
	}

	@Override
	public boolean drawOverlay(Graphics2D gfx)
	{
		Color oldPaint = gfx.getColor();
		if (hasStartPoint())
		{
			if (this.curDraggingPos != null)
			{
				if (this.endConnectionPoint == null)
					gfx.setColor(Color.RED);
				else
					gfx.setColor(Color.BLUE);

				gfx.drawLine(
						this.curDraggingPos.x,
						this.curDraggingPos.y,
						(int) this.startConnectionPoint.getX(),
						(int) this.startConnectionPoint.getY());
			}

			gfx.setColor(Color.BLUE);
			gfx.fill(new Ellipse2D.Double(
					this.startConnectionPoint.getX() - 4,
					this.startConnectionPoint.getY() - 4,
					8,
					8));

			gfx.setColor(Color.GREEN);
		}
		else
		{
			gfx.setColor(Color.RED);
		}

		super.drawOverlayForFeatures(this.canvas.getEditParts(), gfx);

		if (this.endConnectionPoint != null)
		{
			gfx.setColor(Color.BLUE);
			gfx.fill(new Ellipse2D.Double(
					this.endConnectionPoint.getX() - 4,
					this.endConnectionPoint.getY() - 4,
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

		this.startConnectable = null;
		this.canvas.repaint();

		return true;
	}

	@Override
	public boolean doOnMouseDragged(MouseEvent e)
	{
		if (!hasStartPoint())
			return false;

		this.endConnectable = null;
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
		this.curDraggingPos = null;
		this.startConnectable = null;
		this.endConnectable = null;
		this.endConnectionPoint = null;

		this.canvas.repaint();

		return true;
	}

	/** Sets the start point for a new connection. */
	public void setStartPoint(IConnectable connectable, int id)
	{
		this.startConnectable = connectable;
		this.startConnectionID = id;
		for (Connection connection : connectable.getConnectionSources())
		{
			if (this.startConnectionID == connection.getId())
				this.startConnectionPoint = connection.getPoint();
		}

		this.canvas.repaint();
	}

	/** Sets the end point for a new connection. */
	public void setEndPoint(IConnectable editPart, int id)
	{
		this.endConnectable = editPart;
		this.endConnectionID = id;
		for (Connection connection : editPart.getConnectionSinks())
		{
			if (this.endConnectionID == connection.getId())
				this.endConnectionPoint = connection.getPoint();
		}
	}

	/** @return whether a start point is already defined. */
	public boolean hasStartPoint()
	{
		return this.startConnectable != null;
	}
}
