package com.mpdeimos.tensor.layout;

import com.mpdeimos.tensor.model.TensorBase;
import com.mpdeimos.tensor.model.TensorConnection;
import com.mpdeimos.tensor.util.VecMath;

import java.awt.Graphics2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.HashMap;
import java.util.List;

import resources.R;

/**
 * Force directed graph drawing algorithm, as proposed by THOMAS M. J.
 * FRUCHTERMAN and EDWARD M. REINGOLD
 * 
 * @author mpdeimos
 */
public class ForceDirectedPlacementLayouter extends LayouterBase
{

	private static final int ITERATIONS = 1000;

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

		HashMap<TensorBase, Point2D> displacements = new HashMap<TensorBase, Point2D>();
		for (TensorBase u : tensors.keySet())
		{
			displacements.put(u, VecMath.fresh());
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

			this.canvas.repaint();
		}

		@SuppressWarnings("null")
		double area = imageRectangle.getWidth() * imageRectangle.getHeight();
		double k = Math.sqrt(area / tensors.size());
		Point2D temperature = VecMath.fresh(
				imageRectangle.getWidth(),
				imageRectangle.getHeight());
		VecMath.div(temperature, 10);

		for (int i = 1; i < ITERATIONS; i++)
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
	public boolean drawOverlay(Graphics2D gfx)
	{
		return false;
	}

	@Override
	public void onContextPanelInit(ContextPanel panel)
	{
	}

}
