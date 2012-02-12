package com.mpdeimos.tensation.impex.serialize;

import com.mpdeimos.tensation.util.VecMath;

import java.awt.geom.Point2D;
import java.util.Locale;

/**
 * Serializer class for AWT Points.
 * 
 * @author mpdeimos
 */
public class Point2DSerializer implements IObjectSerializer<Point2D>
{
	@Override
	public Class<Point2D> getHandledClass()
	{
		return Point2D.class;
	}

	@Override
	public String serialize(Point2D t)
	{
		return String.format(Locale.US, "%f,%f", t.getX(), t.getY()); //$NON-NLS-1$
	}

	@Override
	public Point2D deserialize(String s, Class<?> type)
	{
		String[] splits = s.split(","); //$NON-NLS-1$
		double x = 0;
		double y = 0;
		if (splits.length >= 2)
		{
			x = Double.valueOf(splits[0]);
			y = Double.valueOf(splits[1]);
		}

		return VecMath.fresh(x, y);
	}
}
