package com.mpdeimos.tensor.util;

import java.awt.geom.Point2D;

/**
 * Vector Math operations for Point2D class.
 * 
 * Most functions are defined twice: with a result argument and one without. For
 * the latter the result is always stored in the first argument. For better
 * chaining all methods will return the result as well.
 * 
 * @author mpdeimos
 */
public class VecMath
{
	/** @return p1 + p2 */
	public static Point2D add(Point2D p1, Point2D p2, Point2D result)
	{
		return set(result, p1.getX() + p2.getX(), p1.getY() + p2.getY());
	}

	/** @return p1 + p2 */
	public static Point2D add(Point2D p1, Point2D p2)
	{
		return add(p1, p2, p1);
	}

	/** @return p1 + d */
	public static Point2D add(Point2D p1, double d, Point2D result)
	{
		return add(p1, fresh(d), result);
	}

	/** @return p1 + d */
	public static Point2D add(Point2D p1, double d)
	{
		return add(p1, d, p1);
	}

	/** @return p1 - p2 */
	public static Point2D sub(Point2D p1, Point2D p2, Point2D result)
	{
		return set(result, p1.getX() - p2.getX(), p1.getY() - p2.getY());
	}

	/** @return p1 - p2 */
	public static Point2D sub(Point2D p1, Point2D p2)
	{
		return sub(p1, p2, p1);
	}

	/** @return p1 - d */
	public static Point2D sub(Point2D p1, double d, Point2D result)
	{
		return sub(p1, fresh(d), result);
	}

	/** @return p1 - d */
	public static Point2D sub(Point2D p1, double d)
	{
		return sub(p1, d, p1);
	}

	/** @return p1 * p2 (pointwise) */
	public static Point2D mul(Point2D p1, Point2D p2, Point2D result)
	{
		return set(result, p1.getX() * p2.getX(), p1.getY() * p2.getY());
	}

	/** @return p1 * p2 (pointwise) */
	public static Point2D mul(Point2D p1, Point2D p2)
	{
		return mul(p1, p2, p1);
	}

	/** @return p1 * d */
	public static Point2D mul(Point2D p, double d, Point2D result)
	{
		return mul(result, fresh(d), result);
	}

	/** @return p1 * d */
	public static Point2D mul(Point2D p, double d)
	{
		return mul(p, d, p);
	}

	/** @return p1 / p2 (pointwise) */
	public static Point2D div(Point2D p1, Point2D p2, Point2D result)
	{
		return set(result, p1.getX() / p2.getX(), p1.getY() / p2.getY());
	}

	/** @return p1 / p2 (pointwise) */
	public static Point2D div(Point2D p1, Point2D p2)
	{
		return div(p1, p2, p1);
	}

	/** @return p1 / d */
	public static Point2D div(Point2D p, double d, Point2D result)
	{
		return div(p, fresh(d), result);
	}

	/** @return p1 / d */
	public static Point2D div(Point2D p, double d)
	{
		return div(p, d, p);
	}

	/** pointwise minium. */
	public static Point2D min(Point2D p1, Point2D p2, Point2D result)
	{
		return set(
				result,
				Math.min(p1.getX(), p2.getX()),
				Math.min(p1.getY(), p2.getY()));
	}

	/** pointwise minium. */
	public static Point2D min(Point2D p1, Point2D p2)
	{
		return min(p1, p2, p1);
	}

	/** pointwise minium. */
	public static Point2D min(Point2D p1, double d, Point2D result)
	{
		return min(p1, fresh(d), result);
	}

	/** pointwise minium. */
	public static Point2D min(Point2D p1, double d)
	{
		return min(p1, fresh(d), p1);
	}

	/** pointwise maximum. */
	public static Point2D max(Point2D p1, Point2D p2, Point2D result)
	{
		return set(
				result,
				Math.max(p1.getX(), p2.getX()),
				Math.max(p1.getY(), p2.getY()));
	}

	/** pointwise maximum. */
	public static Point2D max(Point2D p1, Point2D p2)
	{
		return max(p1, p2, p1);
	}

	/** pointwise maximum. */
	public static Point2D max(Point2D p1, double d, Point2D result)
	{
		return max(p1, fresh(d), result);
	}

	/** pointwise maximum. */
	public static Point2D max(Point2D p1, double d)
	{
		return max(p1, fresh(d), p1);
	}

	/** @return normalizes p */
	public static Point2D normalize(Point2D p, Point2D result)
	{
		return div(p, norm(p), result);
	}

	/** @return normalizes p */
	public static Point2D normalize(Point2D p)
	{
		return div(p, norm(p), p);
	}

	/** @return |p| */
	public static double norm(Point2D p)
	{
		return Math.sqrt(normSquared(p));
	}

	/** @return |p|^2 */
	public static double normSquared(Point2D p)
	{
		return (p.getX() * p.getX() + p.getY() * p.getY());
	}

	/** @return sets the position of p to (x,y) */
	public static Point2D set(Point2D p, double x, double y)
	{
		p.setLocation(x, y);
		return p;
	}

	/** @return sets the position of p1 to p2. */
	public static Point2D set(Point2D p1, Point2D p2)
	{
		return set(p1, p2.getX(), p2.getY());
	}

	/** Creates a new 2D point (0,0). */
	public static Point2D fresh()
	{
		return fresh(0);
	}

	/** Creates a new 2D point (d,d). */
	public static Point2D fresh(double d)
	{
		return fresh(d, d);
	}

	/** Creates a new 2D point (x,y). */
	public static Point2D fresh(double x, double y)
	{
		return new Point2D.Double(x, y);
	}

	/** Creates a new 2D point (p.x,p.y). */
	public static Point2D fresh(Point2D p)
	{
		return fresh(p.getX(), p.getY());
	}

	/** @return the distance between two points. */
	public static double distance(Point2D p1, Point2D p2)
	{
		return norm(sub(fresh(p1), p2));
	}

	/** @return the center between both points. */
	public static Point2D center(Point2D p1, Point2D p2, Point2D r)
	{
		sub(p1, p2, r);
		div(r, 2);
		add(r, p2);
		return r;
	}

	/** @return the center between both points. */
	public static Point2D center(Point2D p1, Point2D p2)
	{
		return center(p1, p2, p1);
	}

	/** @return the values to the next integer. */
	public static Point2D round(Point2D pos)
	{
		return round(pos, pos);
	}

	/** @return the values to the next integer. */
	public static Point2D round(Point2D pos, Point2D result)
	{
		return set(result, Math.round(pos.getX()), Math.round(pos.getY()));
	}

	/** @return the values to the bottom integer. */
	public static Point2D floor(Point2D pos)
	{
		return floor(pos, pos);
	}

	/** @return the values to the bottom integer. */
	public static Point2D floor(Point2D pos, Point2D result)
	{
		return set(result, Math.floor(pos.getX()), Math.floor(pos.getY()));
	}

	/** @return the signum of the vector. */
	public static Point2D signum(Point2D pos)
	{
		return signum(pos, pos);
	}

	/** @return the signum of the vector. */
	public static Point2D signum(Point2D pos, Point2D result)
	{
		return set(result, Math.signum(pos.getX()), Math.signum(pos.getY()));
	}
}
