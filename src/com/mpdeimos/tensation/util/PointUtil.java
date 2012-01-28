package com.mpdeimos.tensation.util;

import java.awt.Dimension;
import java.awt.Point;
import java.awt.geom.Point2D;

/**
 * Util class for points.
 * 
 * @author mpdeimos
 */
public class PointUtil
{

	/**
	 * rotates a given point by an angle in radians.
	 * 
	 * @return the rotated point
	 */
	public static Point2D rotate(Point2D p, double ang)
	{
		double sin = Math.sin(ang);
		double cos = Math.cos(ang);

		p.setLocation(
				p.getX() * cos - p.getY() * sin,
				p.getX() * sin + p.getY() * cos);
		return p;
	}

	/**
	 * moves a given point by the specified offset.
	 * 
	 * @return the rotated point
	 */
	public static Point2D move(Point2D p, double x, double y)
	{
		p.setLocation(p.getX() + x, p.getY() + y);
		return p;
	}

	/**
	 * @return the distance between two points as dimension
	 */
	public static Dimension getDelta(Point2D p1, Point2D p2)
	{
		return new Dimension(
				(int) (p1.getX() - p2.getX()),
				(int) (p1.getY() - p2.getY()));
	}

	/** @return the distance vector between two points. */
	public static void sub(Point2D p1, Point2D p2, Point2D result)
	{
		result.setLocation(p1);
		move(result, -p2.getX(), -p2.getY());
	}

	/** the scaled point. */
	public static Point scale(Point point, double scale)
	{
		point.setLocation(point.getX() * scale, point.getY() * scale);
		return point;
	}
}
