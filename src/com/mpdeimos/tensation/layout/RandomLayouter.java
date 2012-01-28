package com.mpdeimos.tensation.layout;

import com.mpdeimos.tensation.model.TensorBase;
import com.mpdeimos.tensation.model.TensorConnection;
import com.mpdeimos.tensation.ui.DividerLabel;

import java.awt.Point;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.HashMap;
import java.util.Set;

import javax.swing.JCheckBox;

import resources.R;

/**
 * Randomized Graph Layouting
 * 
 * @author mpdeimos
 */
public class RandomLayouter extends LayouterBase
{
	/** checkbox for toggling position randomization. */
	private JCheckBox uiPosition;

	/** checkbox for toggling rotation randomization. */
	private JCheckBox uiRotation;

	/** Constructor. */
	public RandomLayouter()
	{
		super(R.string.LAYOUT_RANDOM.string());
	}

	@Override
	public boolean layout(
			HashMap<TensorBase, Point2D> positions,
			HashMap<TensorBase, Double> rotations,
			Set<TensorConnection> connections)
	{
		Rectangle2D imageRectangle = new Rectangle2D.Double(
				this.canvas.getScroll().getX()
						+ ForceDirectedPlacementLayouter.SCREEN_MARGIN,
				this.canvas.getScroll().getY()
						+ ForceDirectedPlacementLayouter.SCREEN_MARGIN,
				this.canvas.getBounds().getWidth() - 2
						* ForceDirectedPlacementLayouter.SCREEN_MARGIN,
				this.canvas.getBounds().getHeight() - 2
						* ForceDirectedPlacementLayouter.SCREEN_MARGIN);

		for (TensorBase tensor : positions.keySet())
		{
			if (this.uiRotation.isSelected())
			{
				rotations.put(tensor, Math.random() * 360.0);
			}
			if (this.uiPosition.isSelected())
			{
				int x = (int) (Math.random() * imageRectangle.getWidth() + imageRectangle.getX());
				int y = (int) (Math.random() * imageRectangle.getHeight() + imageRectangle.getY());
				positions.put(tensor, new Point(x, y));
			}
		}

		return true;
	}

	@Override
	public void onContextPanelInit(ContextPanel panel)
	{
		panel.add(new DividerLabel(R.string.LAYOUT_RANDOM_WHAT.string()));

		this.uiPosition = new JCheckBox(
				R.string.LAYOUT_RANDOM_WHAT_POSITION.string(),
				true);
		panel.add(this.uiPosition);

		this.uiRotation = new JCheckBox(
				R.string.LAYOUT_RANDOM_WHAT_ROTATION.string(),
				true);
		panel.add(this.uiRotation);
	}
}
