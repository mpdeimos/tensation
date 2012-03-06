package com.mpdeimos.tensation.layout;

import com.mpdeimos.tensation.model.TensorBase;
import com.mpdeimos.tensation.model.TensorConnection;
import com.mpdeimos.tensation.ui.DividerLabel;
import com.mpdeimos.tensation.util.Gfx;
import com.mpdeimos.tensation.util.GraphUtil;
import com.mpdeimos.tensation.util.Tupel;
import com.mpdeimos.tensation.util.VecMath;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;

import resources.R;

/**
 * Force directed graph drawing algorithm, as proposed by THOMAS M. J.
 * FRUCHTERMAN and EDWARD M. REINGOLD
 * 
 * @author mpdeimos
 */
public class ForceDirectedPlacementLayouter extends LayouterBase
{
	/** Offset for screen bounding mode. */
	static final double SCREEN_MARGIN = 35;

	/** reference boundings. */
	private ComboBoxModel uiBounds;

	/** the bounding rect for manual selection */
	private Rectangle2D selectRect;

	/** the start point for manual bounding rect selection. */
	private Point startPoint;

	/** checkbox for toggling separate component layouting. */
	private JCheckBox uiComponents;

	/** checkbox for toggling rotation optimization. */
	private JCheckBox uiRotation;

	/** Constructor. */
	public ForceDirectedPlacementLayouter()
	{
		super(R.string.LAYOUT_FDP.string());
	}

	@Override
	public boolean layout(
			HashMap<TensorBase, Point2D> positions,
			HashMap<TensorBase, Double> rotations,
			Set<TensorConnection> connections)
	{
		if (positions.size() < 1)
			return false;

		Set<Tupel<Set<TensorBase>, Set<TensorConnection>>> connectedSubgraphs = null;
		if (this.uiComponents.isSelected())
		{
			connectedSubgraphs = GraphUtil.getConnectedSubgraphs(
					positions.keySet(),
					connections);
		}
		else
		{
			connectedSubgraphs = new HashSet<Tupel<Set<TensorBase>, Set<TensorConnection>>>();
			connectedSubgraphs.add(new Tupel<Set<TensorBase>, Set<TensorConnection>>(
					positions.keySet(),
					new HashSet<TensorConnection>(connections)));
		}

		boolean updates = false;
		for (Tupel<Set<TensorBase>, Set<TensorConnection>> connectedSubgraph : connectedSubgraphs)
		{
			updates |= layoutSubgraphs(
					connectedSubgraph.$1,
					connectedSubgraph.$2,
					positions,
					rotations);
		}

		return updates;
	}

	/** Layouts a given subgraph */
	private boolean layoutSubgraphs(
			Set<TensorBase> tensors,
			Set<TensorConnection> connections,
			HashMap<TensorBase, Point2D> positions,
			HashMap<TensorBase, Double> rotations)

	{
		if (tensors.size() <= 1)
			return false;

		Rectangle2D imageRectangle = null;
		Ellipse2D tensorRadius = null;
		double radius = 0;

		Object boundMode = this.uiBounds.getSelectedItem();
		if (R.string.LAYOUT_FDP_BOUNDS_SCREEN == boundMode)
		{
			imageRectangle = new Rectangle2D.Double(
					this.canvas.getScroll().getX()
							+ ForceDirectedPlacementLayouter.SCREEN_MARGIN,
					this.canvas.getScroll().getY()
							+ ForceDirectedPlacementLayouter.SCREEN_MARGIN,
					this.canvas.getBounds().getWidth() - 2 * SCREEN_MARGIN,
					this.canvas.getBounds().getHeight() - 2 * SCREEN_MARGIN);
		}
		else if (R.string.LAYOUT_FDP_BOUNDS_SELECT == boundMode)
		{
			if (this.selectRect == null)
				return false;

			imageRectangle = this.selectRect;
		}
		else if (R.string.LAYOUT_FDP_BOUNDS_TENSORS_CIRCLE == boundMode)
		{
			Tupel<Point2D, Point2D> maxConnection = null;

			for (TensorBase t1 : tensors)
			{
				for (TensorBase t2 : tensors)
				{
					Point2D p1 = positions.get(t1);
					Point2D p2 = positions.get(t2);

					double d = VecMath.distance(p1, p2) / 2;
					if (d > radius)
					{
						maxConnection = new Tupel<Point2D, Point2D>(p1, p2);
						radius = d;
					}
				}
			}

			if (maxConnection == null)
				return false;

			// tensorRadius = new Ellipse2D.Double(
			// maxConnection.$1.getX(),
			// maxConnection.$1.getY(),
			// maxConnection.$2.getX() - maxConnection.$1.getX(),
			// maxConnection.$2.getY() - maxConnection.$1.getY());

			Point2D p = VecMath.center(
					VecMath.fresh(maxConnection.$1),
					maxConnection.$2);
			tensorRadius = new Ellipse2D.Double();
			tensorRadius.setFrameFromCenter(
					p.getX(),
					p.getY(),
					p.getX() + radius,
					p.getY() + radius);
		}

		HashMap<TensorBase, Point2D> displacements = new HashMap<TensorBase, Point2D>();
		for (TensorBase u : tensors)
		{
			displacements.put(u, VecMath.fresh());
			if (R.string.LAYOUT_FDP_BOUNDS_TENSORS_RECT == boundMode)
			{
				if (imageRectangle == null)
				{
					imageRectangle = new Rectangle2D.Double(
							u.getPosition().getX(),
							u.getPosition().getY(), 0, 0);
				}
				else
				{
					imageRectangle.add(u.getPosition());
				}
			}
		}

		double area = 0;
		Point2D temperature = null;
		if (imageRectangle != null)
		{
			area = imageRectangle.getWidth() * imageRectangle.getHeight();
			temperature = VecMath.fresh(
					imageRectangle.getWidth(),
					imageRectangle.getHeight());

		}
		else if (tensorRadius != null)
		{
			area = 2 * Math.PI * radius * radius;
			temperature = VecMath.fresh(radius * 2);
		}
		else
		{
			throw new IllegalStateException();
		}
		double k = Math.sqrt(area / tensors.size());

		double change = 0;
		// this.uiIterations.getNumber().intValue();
		for (int i = 0; i < Integer.MAX_VALUE; i++)
		{
			change = 0;

			// calculate repulsive forces
			for (TensorBase u : tensors)
			{
				for (TensorBase v : tensors)
				{
					if (u != v) // refcomp ok
					{
						Point2D disp = displacements.get(v);
						Point2D delta = VecMath.sub(
								positions.get(v),
								positions.get(u),
								VecMath.fresh());

						double norm = VecMath.norm(delta);
						VecMath.div(delta, norm);
						VecMath.mul(delta, repulsiveForce(k, norm));
						VecMath.add(disp, delta);
					}
				}
			}

			// calculate attractive forces
			for (TensorConnection e : connections)
			{
				TensorBase u = e.getSource().getTensor();
				TensorBase v = e.getSink().getTensor();
				Point2D uDisp = displacements.get(u);
				Point2D vDisp = displacements.get(v);

				Point2D delta = VecMath.sub(
						positions.get(v),
						positions.get(u),
						VecMath.fresh());
				double norm = VecMath.norm(delta);
				VecMath.div(delta, norm);
				VecMath.mul(delta, attractiveForce(k, norm));
				VecMath.add(uDisp, delta);
				VecMath.sub(vDisp, delta);
			}

			// update tensor position map
			for (TensorBase u : tensors)
			{
				Point2D disp = displacements.get(u);
				Point2D pos = positions.get(u);

				Point2D min = VecMath.min(
						VecMath.abs(VecMath.fresh(disp)),
						temperature);
				VecMath.normalize(disp);
				VecMath.mul(disp, min);
				VecMath.add(pos, disp);

				change += Math.abs(disp.getX()) + Math.abs(disp.getY());

				if (tensorRadius != null)
				{
					pos = VecMath.round(pos);
					if (!tensorRadius.contains(pos))
					{
						Point2D center = VecMath.fresh(
								tensorRadius.getCenterX(),
								tensorRadius.getCenterY());
						VecMath.sub(pos, center);
						VecMath.normalize(pos);
						VecMath.mul(pos, Math.floor(radius));
						Point2D sig = VecMath.signum(pos, VecMath.fresh());
						VecMath.mul(pos, sig);
						VecMath.floor(pos);
						VecMath.mul(pos, sig);
						VecMath.add(pos, center);
					}
				}
				else if (imageRectangle != null)
				{
					VecMath.min(
							pos,
							VecMath.fresh(
									imageRectangle.getMaxX(),
									imageRectangle.getMaxY()));
					VecMath.max(
							pos,
							VecMath.fresh(
									imageRectangle.getMinX(),
									imageRectangle.getMinY()));
				}
			}

			if (this.uiRotation.isSelected())
			{
				RotateLayouter.optimizeRotation(
						positions,
						rotations,
						connections);
			}

			temperature = coolDown(temperature);

			if (change < 0.5 || Double.isNaN(change))
				break;
		}

		return true;
	}

	/** temperature cooling */
	private Point2D coolDown(Point2D temperature)
	{
		return VecMath.mul(temperature, .99);
	}

	/** @return the attractive force wrt z and k */
	private double attractiveForce(double k, double z)
	{
		return z * z / k;
	}

	/** @return the repulsive force wrt z and k */
	private double repulsiveForce(double k, double z)
	{
		return k * k / z;
	}

	@Override
	public boolean doOnMousePressed(MouseEvent e)
	{
		if (this.uiBounds.getSelectedItem() == R.string.LAYOUT_FDP_BOUNDS_SELECT)
		{
			this.startPoint = new Point(e.getPoint());

			return true;
		}
		return false;
	}

	@Override
	public boolean doOnMouseDragged(MouseEvent e)
	{
		if (this.uiBounds.getSelectedItem() == R.string.LAYOUT_FDP_BOUNDS_SELECT)
		{
			this.selectRect = new Rectangle2D.Double(
					this.startPoint.x,
					this.startPoint.y,
					0,
					0);
			this.selectRect.add(e.getPoint());

			this.canvas.repaint();

			return true;
		}
		return false;
	}

	@Override
	public boolean doOnMouseMoved(MouseEvent e)
	{
		if (this.uiBounds.getSelectedItem() == R.string.LAYOUT_FDP_BOUNDS_SELECT)
		{
			this.canvas.setCursor(new Cursor(Cursor.CROSSHAIR_CURSOR));
			return true;
		}
		this.canvas.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
		return false;
	}

	@Override
	public boolean drawOverlay(Graphics2D gfx)
	{
		if (this.uiBounds.getSelectedItem() == R.string.LAYOUT_FDP_BOUNDS_SELECT)
		{
			gfx.setColor(Color.BLUE);

			if (this.selectRect != null)
				Gfx.drawRect(gfx, this.selectRect);

			return true;
		}
		return false;
	}

	@Override
	public void onContextPanelInit(ContextPanel panel)
	{
		panel.add(new DividerLabel(R.string.LAYOUT_FDP_BOUNDS.string()));

		this.uiBounds = new DefaultComboBoxModel(new R.string[] {
				R.string.LAYOUT_FDP_BOUNDS_SCREEN,
				R.string.LAYOUT_FDP_BOUNDS_TENSORS_RECT,
				R.string.LAYOUT_FDP_BOUNDS_TENSORS_CIRCLE,
				R.string.LAYOUT_FDP_BOUNDS_SELECT });
		JComboBox bounds = new JComboBox(this.uiBounds);
		panel.add(bounds);

		panel.add(new DividerLabel(R.string.LAYOUT_FDP_COMPONENTS.string()));
		this.uiComponents = new JCheckBox(
				R.string.LAYOUT_FDP_COMPONENTS_SEPERATE.string(), true);
		panel.add(this.uiComponents);

		panel.add(new DividerLabel(R.string.LAYOUT_FDP_ROTATION.string()));
		this.uiRotation = new JCheckBox(
				R.string.LAYOUT_FDP_ROTATION_OPTIMIZE.string(), true);
		panel.add(this.uiRotation);
	}
}
