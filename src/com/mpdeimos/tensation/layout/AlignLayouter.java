package com.mpdeimos.tensation.layout;

import com.mpdeimos.tensation.model.TensorBase;
import com.mpdeimos.tensation.model.TensorConnection;
import com.mpdeimos.tensation.util.VecMath;

import java.awt.Point;
import java.awt.geom.Point2D;
import java.util.HashMap;
import java.util.Set;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;

import resources.R;

/**
 * Randomized Graph Layouting
 * 
 * @author mpdeimos
 */
public class AlignLayouter extends LayouterBase
{
	/** checkbox for toggling position randomization. */
	private DefaultComboBoxModel uiAlign;

	/** Constructor. */
	public AlignLayouter()
	{
		super(R.string.LAYOUT_ALIGN.string());
	}

	@Override
	public boolean layout(
			HashMap<TensorBase, Point2D> positions,
			HashMap<TensorBase, Double> rotations,
			Set<TensorConnection> connections)
	{
		Point2D pos = null;

		Object align = this.uiAlign.getSelectedItem();

		for (TensorBase tensor : positions.keySet())
		{
			if (pos == null)
			{
				pos = VecMath.fresh(tensor.getPosition());
			}
			else
			{
				if (align == R.string.LAYOUT_ALIGN_LEFT
						|| align == R.string.LAYOUT_ALIGN_TOP)
				{
					VecMath.min(pos, tensor.getPosition());
				}
				else
				{
					VecMath.max(pos, tensor.getPosition());
				}
			}
		}

		if (pos == null)
			return false;

		for (TensorBase tensor : positions.keySet())
		{
			if (align == R.string.LAYOUT_ALIGN_LEFT
					|| align == R.string.LAYOUT_ALIGN_RIGHT)
			{
				positions.put(
						tensor,
						new Point((int) pos.getX(), tensor.getPosition().y));
			}
			else
			{
				positions.put(
						tensor,
						new Point(tensor.getPosition().x, (int) pos.getY()));
			}
		}

		return true;
	}

	@Override
	public void onContextPanelInit(ContextPanel panel)
	{
		// panel.add(new DividerLabel(R.string.LAYOUT_ALIGN.string()));

		this.uiAlign = new DefaultComboBoxModel(new R.string[] {
				R.string.LAYOUT_ALIGN_LEFT,
				R.string.LAYOUT_ALIGN_TOP,
				R.string.LAYOUT_ALIGN_RIGHT,
				R.string.LAYOUT_ALIGN_BOTTOM });
		JComboBox bounds = new JComboBox(this.uiAlign);
		panel.add(bounds);
	}
}
