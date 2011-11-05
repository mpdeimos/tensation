package com.mpdeimos.tensor.layout;

import com.mpdeimos.tensor.model.TensorBase;
import com.mpdeimos.tensor.model.TensorConnection;
import com.mpdeimos.tensor.ui.DividerLabel;
import com.mpdeimos.tensor.util.VecMath;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.geom.Point2D;
import java.util.HashMap;
import java.util.List;

import javax.swing.ButtonGroup;
import javax.swing.JRadioButton;
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
	/** Centroid mass options. */
	private JRadioButton mass;

	/** Centroid areas option. */
	private JRadioButton area;

	/** Centroid select option. */
	private JRadioButton select;

	/** The selected centroid. */
	private Point selectedCentroid;

	/** Last mouse position. */
	private final Point lastMousePos = new Point();

	/** Scaling factor. */
	private SpinnerNumberModel factor;

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

		if (this.mass.isSelected())
		{
			for (TensorBase tensor : tensors.keySet())
			{
				VecMath.add(centroid, tensor.getPosition());
			}
			VecMath.div(centroid, tensors.size());
		}
		else if (this.select.isSelected())
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
			VecMath.add(p, centroid);

			tensors.put(tensor, new Point((int) p.getX(), (int) p.getY()));
		}

		return true;
	}

	@Override
	public boolean doOnMouseMoved(MouseEvent e)
	{
		if (!this.select.isSelected())
		{
			return false;
		}

		this.lastMousePos.setLocation(e.getPoint());

		this.canvas.repaint();
		return true;
	}

	@Override
	public boolean doOnMouseClicked(MouseEvent e)
	{
		if (!this.select.isSelected())
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
		if (!this.select.isSelected())
		{
			return false;
		}

		if (this.selectedCentroid != null)
		{
			gfx.setColor(Color.blue);
			gfx.drawLine(
					this.selectedCentroid.x - 5,
					this.selectedCentroid.y,
					this.selectedCentroid.x + 5,
					this.selectedCentroid.y);
			gfx.drawLine(
					this.selectedCentroid.x,
					this.selectedCentroid.y - 5,
					this.selectedCentroid.x,
					this.selectedCentroid.y + 5);
		}

		gfx.setColor(Color.gray);
		gfx.drawLine(
				this.lastMousePos.x - 5,
				this.lastMousePos.y,
				this.lastMousePos.x + 5,
				this.lastMousePos.y);
		gfx.drawLine(
				this.lastMousePos.x,
				this.lastMousePos.y - 5,
				this.lastMousePos.x,
				this.lastMousePos.y + 5);

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

		ButtonGroup centroid = new ButtonGroup();
		this.mass = new JRadioButton(
				R.string.LAYOUT_SCALE_CENTROID_MASS.string());
		this.mass.setSelected(true);
		centroid.add(this.mass);
		panel.add(this.mass);

		this.area = new JRadioButton(
				R.string.LAYOUT_SCALE_CENTROID_AREA.string());
		centroid.add(this.area);
		panel.add(this.area);

		this.select = new JRadioButton(
				R.string.LAYOUT_SCALE_CENTROID_SELECT.string());
		centroid.add(this.select);
		panel.add(this.select);

	}
}
