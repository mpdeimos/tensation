package com.mpdeimos.tensation.impex.serialize;

public class IntegerSerializer implements IObjectSerializer<Integer>
{
	@Override
	public Class<Integer> getHandledClass()
	{
		return Integer.class;
	}

	@Override
	public String serialize(Integer t)
	{
		return t.toString();
	}

	@Override
	public Integer deserialize(String s)
	{
		return Integer.valueOf(s);
	}
}
