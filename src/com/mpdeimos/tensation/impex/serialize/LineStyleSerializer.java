package com.mpdeimos.tensation.impex.serialize;

import com.mpdeimos.tensation.model.ELineStyle;

public class LineStyleSerializer implements IObjectSerializer<ELineStyle>
{
	@Override
	public Class<ELineStyle> getHandledClass()
	{
		return ELineStyle.class;
	}

	@Override
	public String serialize(ELineStyle t)
	{
		return t.name();
	}

	@Override
	public ELineStyle deserialize(String s)
	{
		return ELineStyle.valueOf(s);
	}

}
