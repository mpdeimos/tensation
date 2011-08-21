package com.mpdeimos.tensor.impex;

import com.mpdeimos.tensor.model.IModelData;
import com.mpdeimos.tensor.model.TensorConnection;

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
	public Element export(Document xmlDoc, int id, IModelData data)
	{
		TensorConnection con = (TensorConnection) data;

		Element eCon = xmlDoc.createElement(EXmlConnection.ELEMENT_CONECTION.getName());

		eCon.setAttribute(
				EXmlConnection.ATTRIB_ID.getName(), Integer.toString(id));

		eCon.setAttribute(
				EXmlConnection.ATTRIB_SOURCE.getName(),
				Integer.toString(0)); // TODO
		eCon.setAttribute(
				EXmlConnection.ATTRIB_SOURCE_ANCHOR.getName(),
				Integer.toString(con.getSource().getId()));
		eCon.setAttribute(
				EXmlConnection.ATTRIB_SOURCE_DISTANCE.getName(),
				Double.toString(con.getSourceDistance()));

		eCon.setAttribute(
				EXmlConnection.ATTRIB_SINK.getName(),
				Integer.toString(0)); // TODO
		eCon.setAttribute(
				EXmlConnection.ATTRIB_SINK_ANCHOR.getName(),
				Integer.toString(con.getSink().getId()));
		eCon.setAttribute(
				EXmlConnection.ATTRIB_SINK_DISTANCE.getName(),
				Double.toString(con.getSinkDistance()));

		return eCon;
	}
}
