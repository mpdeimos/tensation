package com.mpdeimos.tensor.layout;

import com.mpdeimos.tensor.model.IModelData;
import com.mpdeimos.tensor.model.ModelRoot;
import com.mpdeimos.tensor.model.TensorBase;
import com.mpdeimos.tensor.ui.Application;
import com.mpdeimos.tensor.ui.DividerLabel;
import com.mpdeimos.tensor.util.PointUtil;

import java.awt.Point;
import java.awt.Rectangle;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;

import javax.swing.ButtonGroup;
import javax.swing.JRadioButton;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;

import resources.R;

/**
 * Scaling a
 * 
 * @author mpdeimos
 */
public class ScaleLayouter extends LayouterBase
{
	/** Centroid mass options. */
	private JRadioButton mass;

	/** Centroid areas option. */
	private JRadioButton area;

	/** Scaling factor. */
	private SpinnerNumberModel factor;

	/** Constructor. */
	public ScaleLayouter()
	{
		super(R.string.LAYOUT_SCALE.string());
	}

	@Override
	void layout()
	{
		ModelRoot model = Application.getApp().getModel();

		List<TensorBase> tensors = new ArrayList<TensorBase>();
		Point2D centroid = new Point2D.Double(0, 0);
		for (IModelData child : model.getChildren())
		{
			if (child instanceof TensorBase)
			{
				TensorBase tensor = (TensorBase) child;
				tensors.add(tensor);
				if (this.mass.isSelected())
				{
					PointUtil.move(centroid, tensor.getPosition());
				}
			}
		}

		if (this.mass.isSelected())
		{
			PointUtil.scale(centroid, 1.0 / tensors.size());
		}
		else
		{
			Rectangle imageRectangle = this.canvas.getImageRectangle();
			centroid.setLocation(
					imageRectangle.getCenterX(),
					imageRectangle.getCenterY());
		}
		Point2D centroidInv = (Point2D) centroid.clone();
		PointUtil.scale(centroidInv, -1.0);

		for (TensorBase tensor : tensors)
		{
			Point2D p = new Point(tensor.getPosition());
			PointUtil.move(p, centroidInv);
			PointUtil.scale(p, this.factor.getNumber().doubleValue());
			PointUtil.move(p, centroid);

			tensor.setPosition(new Point((int) p.getX(), (int) p.getY()));
		}
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
		this.area = new JRadioButton(
				R.string.LAYOUT_SCALE_CENTROID_AREA.string());

		this.mass.setSelected(true);
		centroid.add(this.mass);
		panel.add(this.mass);

		centroid.add(this.area);
		panel.add(this.area);

	}
}
