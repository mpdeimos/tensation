package com.mpdeimos.tensation.impex;

import com.mpdeimos.tensation.impex.serialize.Serialized;
import com.mpdeimos.tensation.impex.serialize.Serializer;

import java.util.HashMap;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * Handles export of Tensors to valid XML data.
 * 
 * Helpers:
 * 
 * (0) HashMap<String, Object> - the key value store, (1) String - the group
 * name
 * 
 * @author mpdeimos
 * 
 */
public class KeyValueStoreExporter implements IExporter
{
	@Override
	public Element export(Document xmlDoc, Object... helpers)
	{
		@SuppressWarnings("unchecked")
		HashMap<String, Object> store = (HashMap<String, Object>) helpers[0];
		String group = (String) helpers[1];

		if (store.size() == 0)
			return null;

		Element eGroup = xmlDoc.createElement(ETdgKeyValueStore.ELEMENT_ATTRIBUTES.$());
		eGroup.setAttribute(ETdgKeyValueStore.ATTRIB_GROUP_NAME.$(), group);

		for (String key : store.keySet())
		{
			Serialized serialize = Serializer.serialize(store.get(key));
			Element eItem = xmlDoc.createElement(ETdgKeyValueStore.ELEMENT_ITEM.$());
			eItem.setAttribute(ETdgKeyValueStore.ATTRIB_ITEM_KEY.$(), key);
			eItem.setAttribute(
					ETdgKeyValueStore.ATTRIB_ITEM_VALUE.$(),
					serialize.getData());
			eItem.setAttribute(
					ETdgKeyValueStore.ATTRIB_ITEM_TYPE.$(),
					serialize.getTypeName());
			eGroup.appendChild(eItem);
		}

		return eGroup;
	}
}
