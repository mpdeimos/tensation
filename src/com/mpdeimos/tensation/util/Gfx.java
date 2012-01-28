package com.mpdeimos.tensation.util;

import java.awt.Graphics2D;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;

/**
 * Utility class for common drawing functions.
 * 
 * @author mpdeimos
 */
public class Gfx
{
	/** Antialiasing correction offset. */
	private static final double AA_CORRECTION = .5;

	/** Draws a crosshair at the given position with antialiasing correction. */
	public static void drawCrosshair(Graphics2D gfx, Point2D pos)
	{
		// ensure we draw at .5 positions (antialiasing prevention)
		pos = aaCorrect(pos);

		gfx.draw(new Line2D.Double(
				pos.getX() - 10,
				pos.getY(),
				pos.getX() + 10,
				pos.getY()));
		gfx.draw(new Line2D.Double(
				pos.getX(),
				pos.getY() - 10,
				pos.getX(),
				pos.getY() + 10));
	}

	/** corrects antialiasing. */
	private static Point2D aaCorrect(Point2D pos)
	{
		pos = VecMath.fresh(
				(int) pos.getX() - AA_CORRECTION,
				(int) pos.getY() - AA_CORRECTION);

		return pos;
	}

	/** Draws a rectangle with antialiasing correction. */
	public static void drawRect(Graphics2D gfx, Rectangle2D rect)
	{
		Point2D pos = aaCorrect(VecMath.fresh(rect.getX(), rect.getY()));

		gfx.draw(new Rectangle2D.Double(
				pos.getX(),
				pos.getY(),
				rect.getWidth(),
				rect.getHeight()));
	}
}
