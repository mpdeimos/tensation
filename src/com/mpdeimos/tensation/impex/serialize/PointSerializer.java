package com.mpdeimos.tensation.impex.serialize;

import java.awt.Point;
import java.util.Locale;

/**
 * Serializer class for AWT Points.
 * 
 * @author mpdeimos
 */
public class PointSerializer implements IObjectSerializer<Point>
{
	@Override
	public Class<Point> getHandledClass()
	{
		return Point.class;
	}

	@Override
	public String serialize(Point t)
	{
		return String.format(Locale.US, "%d,%d", t.x, t.y); //$NON-NLS-1$
	}

	@Override
	public Point deserialize(String s, Class<?> type)
	{
		String[] splits = s.split(","); //$NON-NLS-1$
		int x = 0;
		int y = 0;
		if (splits.length >= 2)
		{
			x = Integer.valueOf(splits[0]);
			y = Integer.valueOf(splits[1]);
		}

		return new Point(x, y);
	}
}
