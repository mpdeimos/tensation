package com.mpdeimos.tensation.util;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.font.FontRenderContext;
import java.awt.geom.AffineTransform;
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

	/** Sans Serif Font, 12pt. */
	public static final Font SANS_SERIF_12 = new Font(
			Font.SANS_SERIF,
			Font.PLAIN,
			12);
	/** Sans Serif Font, 10pt. */
	public static final Font SANS_SERIF_10 = new Font(
			Font.SANS_SERIF,
			Font.PLAIN,
			10);

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

	/**
	 * Creates a stroke with the given width and pattern multiplication (0 = no
	 * pattern).
	 */
	public static BasicStroke createStroke(float width, float patternMultiplier)
	{
		return new BasicStroke(
				width,
				BasicStroke.CAP_BUTT,
				BasicStroke.JOIN_MITER,
				1,
				createPatternArray(width, patternMultiplier),
				0);
	}

	/** Creates a stroke pattern array. */
	public static float[] createPatternArray(
			float width,
			float patternMultiplier)
	{
		return patternMultiplier * width > 0 ? new float[] {
				width * patternMultiplier,
				width * patternMultiplier } : null;
	}

	/** @return a brighter variant of this color. */
	public static Color brighten(Color c)
	{
		int sum = c.getRed() + c.getGreen() + c.getBlue();
		if (sum < 384)
			return new Color(
					Math.min(255, c.getRed() + 100),
					Math.min(255, c.getGreen() + 100),
					Math.min(255, c.getBlue() + 100));
		return new Color(
				Math.max(0, c.getRed() - 100),
				Math.max(0, c.getGreen() - 100),
				Math.max(0, c.getBlue() - 100));

	}

	/** Aligns the text centered around the given point, no drawing. */
	public static Dimension alignTextCentered(
			Graphics2D gfx,
			Point2D center,
			String text)
	{
		if (text == null)
			return null;

		FontMetrics fm = gfx.getFontMetrics();
		Dimension bounds = new Dimension(fm.stringWidth(text), fm.getAscent());
		return bounds;

	}

	/** Draws text centered around the given point. */
	public static Dimension drawTextCentered(
			Graphics2D gfx,
			Point2D center,
			String text)
	{
		Dimension bounds = alignTextCentered(gfx, center, text);

		if (bounds == null)
			return null;

		gfx.drawString(
				text,
				(float) (center.getX() - (bounds.getWidth() / 2)),
				(float) (center.getY() + (bounds.getHeight() / 4)));
		return bounds;

	}

	/** Approximates the font width of a given text. */
	public static Dimension approximateTextWidth(Font font, String text)
	{
		FontRenderContext frc = new FontRenderContext(
				new AffineTransform(),
				true,
				true);
		Rectangle2D stringBounds = font.getStringBounds(text, frc);

		return new Dimension(
				(int) stringBounds.getWidth(),
				(int) stringBounds.getHeight());
	}
}
