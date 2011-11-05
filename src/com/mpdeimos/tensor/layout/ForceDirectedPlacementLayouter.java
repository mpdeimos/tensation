package com.mpdeimos.tensor.layout;

import com.mpdeimos.tensor.model.TensorBase;
import com.mpdeimos.tensor.model.TensorConnection;
import com.mpdeimos.tensor.ui.DividerLabel;
import com.mpdeimos.tensor.util.Gfx;
import com.mpdeimos.tensor.util.VecMath;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.HashMap;
import java.util.List;

import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;

import resources.R;

/**
 * Force directed graph drawing algorithm, as proposed by THOMAS M. J.
 * FRUCHTERMAN and EDWARD M. REINGOLD
 * 
 * @author mpdeimos
 */
public class ForceDirectedPlacementLayouter extends LayouterBase
{
	/** algorithm iterations. */
	private SpinnerNumberModel uiIterations;

	/** reference boundings. */
	private ComboBoxModel uiBounds;

	private Rectangle2D selectedRect;

	private Point startPoint;

	/** Constructor. */
	public ForceDirectedPlacementLayouter()
	{
		super(R.string.LAYOUT_FDP.string());
	}

	@Override
	public boolean layout(
			HashMap<TensorBase, Point2D> tensors,
			List<TensorConnection> connections)
	{
		if (tensors.size() < 1)
			return false;

		Rectangle2D imageRectangle = null;

		Object boundMode = this.uiBounds.getSelectedItem();
		if (R.string.LAYOUT_FDP_BOUNDS_SCREEN == boundMode)
		{
			imageRectangle = new Rectangle2D.Double(
					0,
					0,
					this.canvas.getBounds().getWidth(),
					this.canvas.getBounds().getHeight());
		}
		else if (R.string.LAYOUT_FDP_BOUNDS_SELECT == boundMode)
		{
			if (this.selectedRect == null)
				return false;

			imageRectangle = this.selectedRect;
		}

		HashMap<TensorBase, Point2D> displacements = new HashMap<TensorBase, Point2D>();
		for (TensorBase u : tensors.keySet())
		{
			displacements.put(u, VecMath.fresh());
			if (R.string.LAYOUT_FDP_BOUNDS_TENSORS == boundMode)
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

		@SuppressWarnings("null")
		double area = imageRectangle.getWidth() * imageRectangle.getHeight();
		double k = Math.sqrt(area / tensors.size());
		Point2D temperature = VecMath.fresh(
				imageRectangle.getWidth(),
				imageRectangle.getHeight());
		VecMath.div(temperature, 10);

		for (int i = 1; i < this.uiIterations.getNumber().intValue(); i++)
		{
			// calculate repulsive forces
			for (TensorBase u : tensors.keySet())
			{
				for (TensorBase v : tensors.keySet())
				{
					if (u != v) // refcomp ok
					{
						Point2D disp = displacements.get(v);
						Point2D delta = VecMath.sub(
								tensors.get(v),
								tensors.get(u),
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
						tensors.get(v),
						tensors.get(u),
						VecMath.fresh());
				double norm = VecMath.norm(delta);
				VecMath.div(delta, norm);
				VecMath.mul(delta, attractiveForce(k, norm));
				VecMath.add(uDisp, delta);
				VecMath.sub(vDisp, delta);
			}

			// update tensor position map
			for (TensorBase u : tensors.keySet())
			{
				Point2D disp = displacements.get(u);
				Point2D pos = tensors.get(u);

				Point2D min = VecMath.min(VecMath.fresh(disp), temperature);
				VecMath.mul(min, temperature);
				VecMath.normalize(disp);
				VecMath.add(pos, disp);
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

			temperature = coolDown(temperature);
		}

		return true;
	}

	/** temperature cooling */
	private Point2D coolDown(Point2D temperature)
	{
		return VecMath.mul(temperature, .95);
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
			this.selectedRect = new Rectangle2D.Double(
					this.startPoint.x,
					this.startPoint.y,
					0,
					0);
			this.selectedRect.add(e.getPoint());

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

			if (this.selectedRect != null)
				Gfx.drawRect(gfx, this.selectedRect);

			return true;
		}
		return false;
	}

	@Override
	public void onContextPanelInit(ContextPanel panel)
	{
		panel.add(new DividerLabel(R.string.LAYOUT_FDP_ITERATIONS.string()));

		this.uiIterations = new SpinnerNumberModel(500, 0, 10000, 100);
		JSpinner spinner = new JSpinner(this.uiIterations);

		panel.add(spinner);

		panel.add(new DividerLabel(R.string.LAYOUT_FDP_BOUNDS.string()));

		this.uiBounds = new DefaultComboBoxModel(new R.string[] {
				R.string.LAYOUT_FDP_BOUNDS_TENSORS,
				R.string.LAYOUT_FDP_BOUNDS_SCREEN,
				R.string.LAYOUT_FDP_BOUNDS_SELECT });
		JComboBox bounds = new JComboBox(this.uiBounds);
		panel.add(bounds);
	}

}
