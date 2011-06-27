package com.mpdeimos.tensor.util;

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
	public static Point2D move(Point2D p, int x, int y)
	{
		p.setLocation(p.getX() + x, p.getY() + y);
		return p;
	}

	/**
	 * @return the distance between two points as dimension
	 */
	public static Dimension getDelta(Point p1, Point p2)
	{
		return new Dimension(p1.x - p2.x, p1.y - p2.y);
	}
}
