package com.mpdeimos.tensation.impex.tdg;

import com.mpdeimos.tensation.impex.IExporter;
import com.mpdeimos.tensation.model.TensorBase;
import com.mpdeimos.tensation.model.TensorConnection;

import java.util.HashMap;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * Handles export of Tensors to valid XML data.
 * 
 * Helpers:
 * 
 * (0) TensorConnection, (1) HashMap<TensorBase, Integer>
 * 
 * @author mpdeimos
 * 
 */
public class ConnectionExporter implements IExporter
{
	@Override
	public Element export(Document xmlDoc, Object... helpers)
	{
		TensorConnection con = (TensorConnection) helpers[0];

		@SuppressWarnings("unchecked")
		HashMap<TensorBase, Integer> tensorsToIds = (HashMap<TensorBase, Integer>) helpers[1];

		Element eCon = xmlDoc.createElement(ETdgConnection.ELEMENT_CONECTION.$());

		eCon.setAttribute(
				ETdgConnection.ATTRIB_SOURCE.$(),
				Integer.toString(tensorsToIds.get(con.getSource().getTensor())));
		eCon.setAttribute(
				ETdgConnection.ATTRIB_SOURCE_ANCHOR.$(),
				Integer.toString(con.getSource().getId()));

		eCon.setAttribute(
				ETdgConnection.ATTRIB_SINK.$(),
				Integer.toString(tensorsToIds.get(con.getSink().getTensor())));
		eCon.setAttribute(
				ETdgConnection.ATTRIB_SINK_ANCHOR.$(),
				Integer.toString(con.getSink().getId()));

		return eCon;
	}
}
