package com.mpdeimos.tensation.impex;

import com.mpdeimos.tensation.model.IModelData;
import com.mpdeimos.tensation.model.TensorBase;
import com.mpdeimos.tensation.model.TensorConnection;

import java.util.HashMap;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * Handles export of Tensors to valid XML data.
 * 
 * @author mpdeimos
 * 
 */
public class ConnectionExporter implements IExporter
{
	@Override
	public Element export(Document xmlDoc, IModelData data, Object... helpers)
	{
		TensorConnection con = (TensorConnection) data;

		@SuppressWarnings("unchecked")
		HashMap<TensorBase, Integer> tensorsToIds = (HashMap<TensorBase, Integer>) helpers[0];

		Element eCon = xmlDoc.createElement(ETdgConnection.ELEMENT_CONECTION.$());

		eCon.setAttribute(
				ETdgConnection.ATTRIB_SOURCE.$(),
				Integer.toString(tensorsToIds.get(con.getSource().getTensor())));
		eCon.setAttribute(
				ETdgConnection.ATTRIB_SOURCE_ANCHOR.$(),
				Integer.toString(con.getSource().getId()));
		eCon.setAttribute(
				ETdgConnection.ATTRIB_SOURCE_DISTANCE.$(),
				Double.toString(con.getSourceDistance()));

		eCon.setAttribute(
				ETdgConnection.ATTRIB_SINK.$(),
				Integer.toString(tensorsToIds.get(con.getSink().getTensor())));
		eCon.setAttribute(
				ETdgConnection.ATTRIB_SINK_ANCHOR.$(),
				Integer.toString(con.getSink().getId()));
		eCon.setAttribute(
				ETdgConnection.ATTRIB_SINK_DISTANCE.$(),
				Double.toString(con.getSinkDistance()));

		return eCon;
	}
}
