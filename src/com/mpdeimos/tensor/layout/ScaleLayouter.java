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
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.geom.Point2D;
import java.util.HashMap;
import java.util.List;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;

import resources.R;

/**
 * Scaling the whole graph. No auto-layouting, but may be useful in some
 * situations.
 * 
 * @author mpdeimos
 */
public class ScaleLayouter extends LayouterBase
{
	/** The selected centroid. */
	private Point selectedCentroid;

	/** Last mouse position. */
	private final Point lastMousePos = new Point();

	/** Scaling factor. */
	private SpinnerNumberModel factor;

	/** the centroid selection combobox. */
	private DefaultComboBoxModel uiCentroid;

	private DefaultComboBoxModel uiMethod;

	/** Constructor. */
	public ScaleLayouter()
	{
		super(R.string.LAYOUT_SCALE.string());
	}

	@Override
	public boolean layout(
			HashMap<TensorBase, Point2D> tensors,
			List<TensorConnection> connections)
	{

		Point2D centroid = VecMath.fresh();

		if (R.string.LAYOUT_SCALE_CENTROID_MASS == this.uiCentroid.getSelectedItem())
		{
			for (TensorBase tensor : tensors.keySet())
			{
				VecMath.add(centroid, tensor.getPosition());
			}
			VecMath.div(centroid, tensors.size());
		}
		else if (R.string.LAYOUT_SCALE_CENTROID_SELECT == this.uiCentroid.getSelectedItem())
		{
			if (this.selectedCentroid == null)
				return false;

			VecMath.set(centroid, this.selectedCentroid);
		}
		else
		{
			Rectangle imageRectangle = this.canvas.getImageRectangle();
			VecMath.set(
					centroid,
					imageRectangle.getCenterX(),
					imageRectangle.getCenterY());
		}

		Point2D centroidInv = VecMath.mul(VecMath.fresh(centroid), -1.0);

		for (TensorBase tensor : tensors.keySet())
		{
			Point2D p = VecMath.fresh(tensor.getPosition());
			VecMath.add(p, centroidInv);

			VecMath.mul(p, this.factor.getNumber().doubleValue());
			// Object method = this.uiMethod.getSelectedItem();
			// if (R.string.LAYOUT_SCALE_METHOD_LOG == method)
			// {
			// VecMath.set(p, logG(p.getX()), logG(p.getY()));
			// }

			VecMath.add(p, centroid);

			tensors.put(tensor, new Point((int) p.getX(), (int) p.getY()));
		}

		return true;
	}

	// /** Graceful, mirrored log. */
	// private double logG(double x)
	// {
	// // double m = 1;
	// // if (x < 0)
	// // {
	// // m = -1;
	// // x = -x;
	// // }
	// // if (x < 1)
	// // {
	// // return 0;
	// // }
	// //
	// // return m * Math.log(x);
	//
	// // if (x == 0)
	// // return 0;
	// //
	// // return 1 / x;
	//
	// }

	@Override
	public boolean doOnMouseMoved(MouseEvent e)
	{
		if (R.string.LAYOUT_SCALE_CENTROID_SELECT != this.uiCentroid.getSelectedItem())
		{
			this.canvas.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
			return false;
		}

		this.lastMousePos.setLocation(e.getPoint());

		this.canvas.repaint();
		return true;
	}

	@Override
	public boolean doOnMouseClicked(MouseEvent e)
	{
		if (R.string.LAYOUT_SCALE_CENTROID_SELECT != this.uiCentroid.getSelectedItem())
		{
			return false;
		}

		this.selectedCentroid = new Point(e.getPoint());

		this.canvas.repaint();
		return true;
	}

	@Override
	public boolean drawOverlay(Graphics2D gfx)
	{
		if (R.string.LAYOUT_SCALE_CENTROID_SELECT != this.uiCentroid.getSelectedItem())
		{
			return false;
		}

		if (this.selectedCentroid != null)
		{
			gfx.setColor(Color.RED);
			Gfx.drawCrosshair(gfx, this.selectedCentroid);
		}

		this.canvas.setCursor(new Cursor(Cursor.CROSSHAIR_CURSOR));

		return false;
	}

	@Override
	public void onContextPanelInit(ContextPanel panel)
	{
		panel.add(new DividerLabel(R.string.LAYOUT_SCALE_FACTOR.string()));

		this.factor = new SpinnerNumberModel(1.5, -10, 10, 0.1);
		JSpinner spinner = new JSpinner(this.factor);

		panel.add(spinner);

		panel.add(new DividerLabel(R.string.LAYOUT_SCALE_CENTROID.string()));

		this.uiCentroid = new DefaultComboBoxModel(new R.string[] {
				R.string.LAYOUT_SCALE_CENTROID_MASS,
				R.string.LAYOUT_SCALE_CENTROID_AREA,
				R.string.LAYOUT_SCALE_CENTROID_SELECT });
		JComboBox bounds = new JComboBox(this.uiCentroid);
		panel.add(bounds);

		panel.add(new DividerLabel(R.string.LAYOUT_SCALE_METHOD.string()));

		// this.uiMethod = new DefaultComboBoxModel(new R.string[] {
		// R.string.LAYOUT_SCALE_METHOD_LINEAR,
		// R.string.LAYOUT_SCALE_METHOD_LOG });
		// JComboBox method = new JComboBox(this.uiMethod);
		// panel.add(method);
	}
}
