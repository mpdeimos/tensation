package com.mpdeimos.tensor.editpart.feature;

import java.awt.Cursor;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Point2D;
import java.util.List;

import com.mpdeimos.tensor.action.ICanvasAction;
import com.mpdeimos.tensor.action.TensorConnectAction;

/**
 * Interface for EditParts with Connection abilities.
 * 
 * @author mpdeimos
 * 
 */
public interface IConnectable extends IFeatureEditPart
{
	/** @return List of connection sources. */
	public List<Connection> getConnectionSources();

	/** @return List of connection sinks. */
	public List<Connection> getConnectionSinks();

	/** feature class for EditParts with connection sources. */
	public class Feature extends
			FeatureBase<IConnectable, TensorConnectAction>
	{
		/** Constructor. */
		public Feature(IConnectable editPart)
		{
			super(editPart);
		}

		@Override
		public boolean doOnMouseMoved(ICanvasAction action, MouseEvent e)
		{
			TensorConnectAction connectAction = (TensorConnectAction) action;

			List<Connection> connections;
			if (connectAction.hasStartPoint())
				connections = this.editPart.getConnectionSinks();
			else
				connections = this.editPart.getConnectionSources();

			for (Connection connection : connections)
			{
				if (e.getPoint().distance(connection.getPoint()) > 5)
					continue;
				action.getCanvas().setCursor(
						new Cursor(Cursor.CROSSHAIR_CURSOR));
				return true;
			}

			return false;
		}

		@Override
		public boolean doOnMousePressed(ICanvasAction action, MouseEvent e)
		{
			TensorConnectAction connectAction = (TensorConnectAction) action;

			List<Connection> connections;
			if (connectAction.hasStartPoint())
				connections = this.editPart.getConnectionSinks();
			else
				connections = this.editPart.getConnectionSources();

			for (Connection connection : connections)
			{
				if (e.getPoint().distance(connection.getPoint()) > 5)
					continue;

				connectAction.setStartPoint(this.editPart, connection.getId());

				return true;
			}

			return false;
		}

		@Override
		public boolean doOnMouseDragged(ICanvasAction action, MouseEvent e)
		{
			TensorConnectAction connectAction = (TensorConnectAction) action;

			if (!connectAction.hasStartPoint())
				return false;

			List<Connection> connections = this.editPart.getConnectionSinks();

			for (Connection connection : connections)
			{
				if (e.getPoint().distance(connection.getPoint()) > 5)
					continue;

				connectAction.setEndPoint(this.editPart, connection.getId());

				return true;
			}

			return false;
		}

		@Override
		public boolean drawOverlay(ICanvasAction action, Graphics2D gfx)
		{
			TensorConnectAction connectAction = (TensorConnectAction) action;

			List<Connection> connections;
			if (connectAction.hasStartPoint())
				connections = this.editPart.getConnectionSinks();
			else
				connections = this.editPart.getConnectionSources();

			if (connections == null)
				return false;

			Ellipse2D anchor = null;
			for (Connection connection : connections)
			{
				anchor = new Ellipse2D.Double(
						connection.getPoint().getX() - 4,
						connection.getPoint().getY() - 4,
						8,
						8);

				gfx.fill(anchor);
			}

			return false;
		}
	}

	/** Connection source helper class */
	public class Connection
	{
		/** The unique connection source ID within this edit part. */
		private final int id;

		/** The 2D point coordinates for the connection source. */
		private final Point2D point;

		/** Constructor. */
		public Connection(int id, Point2D pt)
		{
			this.id = id;
			this.point = pt;
		}

		/** @return the unique id for this connection source. */
		public int getId()
		{
			return this.id;
		}

		/** @return the point for this connection source. */
		public Point2D getPoint()
		{
			return this.point;
		}
	}
}
