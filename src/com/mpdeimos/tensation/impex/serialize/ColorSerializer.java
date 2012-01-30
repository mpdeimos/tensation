package com.mpdeimos.tensation.impex.serialize;

import java.awt.Color;

/**
 * Serializer class for AWT Colors.
 * 
 * @author mpdeimos
 */
public class ColorSerializer implements IObjectSerializer<Color>
{
	@Override
	public Class<Color> getHandledClass()
	{
		return Color.class;
	}

	@Override
	public String serialize(Color t)
	{
		return String.format(
				"%d,%d,%d,%d", t.getRed(), t.getGreen(), t.getBlue(), t.getAlpha()); //$NON-NLS-1$
	}

	@Override
	public Color deserialize(String s, Class<?> type)
	{
		String[] splits = s.split(","); //$NON-NLS-1$
		int alpha = 0;
		int red = 0;
		int green = 0;
		int blue = 0;

		if (splits.length >= 4)
		{
			alpha = Integer.valueOf(splits[3]);
		}
		if (splits.length >= 3)
		{
			red = Integer.valueOf(splits[0]);
			green = Integer.valueOf(splits[1]);
			blue = Integer.valueOf(splits[2]);
		}

		return new Color(red, green, blue, alpha);
	}
}
