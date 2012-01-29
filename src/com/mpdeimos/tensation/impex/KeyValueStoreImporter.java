package com.mpdeimos.tensation.impex;

import com.mpdeimos.tensation.impex.serialize.Serialized;
import com.mpdeimos.tensation.impex.serialize.Serializer;
import com.mpdeimos.tensation.util.XmlUtil;

import java.util.HashMap;

import org.w3c.dom.Element;

/**
 * Imports an Tensor Connection from XML Data.
 * 
 * No helpers required.
 * 
 * @author mpdeimos
 * 
 */
public class KeyValueStoreImporter implements IImporter
{
	@Override
	public HashMap<String, Object> importNode(
			Element node,
			Object... helpers)
	{
		HashMap<String, Object> map = new HashMap<String, Object>();
		for (Element item : XmlUtil.iterate(node))
		{
			String key = item.getAttribute(ETdgKeyValueStore.ATTRIB_ITEM_KEY.$());
			String value = item.getAttribute(ETdgKeyValueStore.ATTRIB_ITEM_VALUE.$());
			String type = item.getAttribute(ETdgKeyValueStore.ATTRIB_ITEM_TYPE.$());
			Serialized s = new Serialized(value, type);
			map.put(key, Serializer.deserialize(s));
		}
		return map;
	}
}
