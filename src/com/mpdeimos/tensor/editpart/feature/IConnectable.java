package com.mpdeimos.tensor.editpart.feature;

import java.awt.Cursor;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Point2D;
import java.util.LinkedList;
import java.util.List;

import com.mpdeimos.tensor.action.ICanvasAction;
import com.mpdeimos.tensor.action.TensorConnectAction;
import com.mpdeimos.tensor.model.TensorConnectionAnchor;
import com.mpdeimos.tensor.model.TensorConnectionAnchor.EDirection;

/**
 * Interface for EditParts with Connection abilities.
 * 
 * @author mpdeimos
 * 
 */
public interface IConnectable extends IFeatureEditPart
{
	/** @return List of connection sources. */
	public List<ConnectionPoint> getConnectionSources();

	/** @return List of connection sinks. */
	public List<ConnectionPoint> getConnectionSinks();

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

			List<ConnectionPoint> connections;
			ConnectionPoint startPoint = connectAction.getStartPoint();
			if (startPoint != null)
			{
				if (startPoint.getAnchor().getDirection() == EDirection.SOURCE)
					connections = this.editPart.getConnectionSinks();
				else
					connections = this.editPart.getConnectionSources();
			}
			else
			{
				connections = this.editPart.getConnectionSources();
				connections.addAll(this.editPart.getConnectionSinks());
			}

			if (connections == null)
				return false;

			removeOccupiedConnections(connections);

			for (ConnectionPoint connection : connections)
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

			List<ConnectionPoint> connections;
			ConnectionPoint startPoint = connectAction.getStartPoint();
			if (startPoint != null)
			{
				if (startPoint.getAnchor().getDirection() == EDirection.SOURCE)
					connections = this.editPart.getConnectionSinks();
				else
					connections = this.editPart.getConnectionSources();
			}
			else
			{
				connections = this.editPart.getConnectionSources();
				connections.addAll(this.editPart.getConnectionSinks());
			}

			if (connections == null)
				return false;

			removeOccupiedConnections(connections);

			for (ConnectionPoint connection : connections)
			{
				if (e.getPoint().distance(connection.getPoint()) > 5)
					continue;

				connectAction.setStartPoint(connection);

				return true;
			}

			return false;
		}

		@Override
		public boolean doOnMouseDragged(ICanvasAction action, MouseEvent e)
		{
			TensorConnectAction connectAction = (TensorConnectAction) action;

			ConnectionPoint startPoint = connectAction.getStartPoint();
			if (startPoint == null)
				return false;

			List<ConnectionPoint> connections;
			if (startPoint.getAnchor().getDirection() == EDirection.SOURCE)
				connections = this.editPart.getConnectionSinks();
			else
				connections = this.editPart.getConnectionSources();

			if (connections == null)
				return false;

			removeOccupiedConnections(connections);

			for (ConnectionPoint connection : connections)
			{
				if (e.getPoint().distance(connection.getPoint()) > 5)
					continue;

				connectAction.setEndPoint(connection);

				return true;
			}

			return false;
		}

		@Override
		public boolean doOnMouseReleased(ICanvasAction action, MouseEvent e)
		{
			return doOnMouseDragged(action, e);
		}

		@Override
		public boolean drawOverlay(ICanvasAction action, Graphics2D gfx)
		{
			TensorConnectAction connectAction = (TensorConnectAction) action;

			List<ConnectionPoint> connections;
			ConnectionPoint startPoint = connectAction.getStartPoint();
			if (startPoint != null)
			{
				if (startPoint.getAnchor().getDirection() == EDirection.SOURCE)
					connections = this.editPart.getConnectionSinks();
				else
					connections = this.editPart.getConnectionSources();
			}
			else
			{
				connections = this.editPart.getConnectionSources();
				connections.addAll(this.editPart.getConnectionSinks());
			}

			if (connections == null)
				return false;

			removeOccupiedConnections(connections);

			Ellipse2D anchor = null;
			for (ConnectionPoint connection : connections)
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

		/** removes occupied connections from a given set of connections. */
		public void removeOccupiedConnections(List<ConnectionPoint> connections)
		{
			List<ConnectionPoint> toBeRemoved = new LinkedList<ConnectionPoint>();

			for (ConnectionPoint conn : connections)
			{
				if (conn.getAnchor().isOccopied())
					toBeRemoved.add(conn);
			}

			connections.removeAll(toBeRemoved);
		}
	}

	/** Connection source helper class */
	public class ConnectionPoint
	{
		/** The connection anchor. */
		private final TensorConnectionAnchor anchor;

		/** The 2D point coordinates for the connection source. */
		private final Point2D point;

		/** Constructor. */
		public ConnectionPoint(TensorConnectionAnchor anchor, Point2D pt)
		{
			this.anchor = anchor;
			this.point = pt;
		}

		/** @return the anchor of this connection. */
		public TensorConnectionAnchor getAnchor()
		{
			return this.anchor;
		}

		/** @return the point for this connection source. */
		public Point2D getPoint()
		{
			return this.point;
		}
	}
}
