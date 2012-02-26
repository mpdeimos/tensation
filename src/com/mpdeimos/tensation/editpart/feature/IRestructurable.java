package com.mpdeimos.tensation.editpart.feature;

import com.mpdeimos.tensation.action.canvas.GraphRestructureAction;
import com.mpdeimos.tensation.action.canvas.ICanvasAction;
import com.mpdeimos.tensation.restructure.ERestruction;
import com.mpdeimos.tensation.util.Gfx;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.HashMap;
import java.util.Set;

/**
 * Interface for EditParts with Graph-Restruction abilities.
 * 
 * @author mpdeimos
 * 
 */
public interface IRestructurable extends IFeatureEditPart
{
	/**
	 * @return a list of points where the restrcution operation labels should be
	 *         drawn.
	 */
	public Set<Point2D> getRestructionLabelPositions();

	/** feature class for EditParts with abilities to restructure a graph. */
	public class Feature extends
			GraphRestructureAction.IFeature<IRestructurable>
	{
		/** ellipse spacing. */
		private static final double SPACING = 3;

		/** the drawn overlays. */
		private final HashMap<Ellipse2D, ERestruction> overlays = new HashMap<Ellipse2D, ERestruction>();

		/** Constructor. */
		public Feature(IRestructurable editPart)
		{
			super(editPart);
		}

		@Override
		public boolean doOnMouseMoved(ICanvasAction action, MouseEvent e)
		{
			for (Ellipse2D ellipse : this.overlays.keySet())
			{
				if (ellipse.contains(e.getPoint()))
				{
					action.getCanvas().setCursor(
							new Cursor(Cursor.HAND_CURSOR));

					return true;
				}
			}

			action.getCanvas().setCursor(
					new Cursor(Cursor.DEFAULT_CURSOR));

			return false;
		}

		@Override
		public boolean doOnMouseReleased(ICanvasAction action, MouseEvent e)
		{
			for (Ellipse2D ellipse : this.overlays.keySet())
			{
				if (ellipse.contains(e.getPoint()))
				{
					this.overlays.get(ellipse).getRestruction().restruct(
							this.editPart);
					return true;
				}
			}
			return false;
		}

		@Override
		public boolean drawOverlay(ICanvasAction action, Graphics2D gfx)
		{
			this.overlays.clear();
			for (ERestruction restruction : ERestruction.values())
			{
				if (!restruction.getRestruction().isRestructable(this.editPart))
					continue;

				for (Point2D pos : this.editPart.getRestructionLabelPositions())
				{
					Rectangle2D rect = Gfx.alignTextCentered(
							gfx,
							pos,
							restruction.getRestruction().getDisplayName());

					rect = Gfx.enlargeRectangle(rect, SPACING, SPACING);

					Ellipse2D ellipse = new Ellipse2D.Double();
					ellipse.setFrame(rect);

					gfx.setColor(Color.GREEN);
					gfx.fill(ellipse);

					gfx.setColor(Color.BLACK);
					Gfx.drawTextCentered(
							gfx,
							pos,
							restruction.getRestruction().getDisplayName());

					this.overlays.put(ellipse, restruction);
				}
			}
			return false;
		}

	}
}
