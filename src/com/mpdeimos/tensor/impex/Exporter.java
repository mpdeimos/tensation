package com.mpdeimos.tensor.impex;

import com.mpdeimos.tensor.model.IModelData;
import com.mpdeimos.tensor.model.ModelRoot;
import com.mpdeimos.tensor.model.TensorBase;
import com.mpdeimos.tensor.util.XmlUtil;

import javax.xml.parsers.DocumentBuilder;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * exports a root model into XML.
 * 
 * @author mpdeimos
 * 
 */
public class Exporter
{
	/** exports a root model to an XML document. */
	public Document toXml(ModelRoot modelRoot)
	{
		DocumentBuilder builder = XmlUtil.getBuilder();
		Document xmlDoc = builder.newDocument();

		Element eRoot = xmlDoc.createElement(EXmlGeneric.ELEMENT_ROOT.getName());
		xmlDoc.appendChild(eRoot);

		TensorExporter tensorExporter = new TensorExporter();
		Element eTensors = xmlDoc.createElement(EXmlGeneric.ELEMENT_TENSORS.getName());
		eRoot.appendChild(eTensors);

		int tensorIDs = 0;
		for (IModelData model : modelRoot.getChildren())
		{
			if (model instanceof TensorBase)
			{
				Element e = tensorExporter.export(xmlDoc, tensorIDs, model);
				eTensors.appendChild(e);
				tensorIDs++;
			}
		}

		// TensorExporter tensorExporter = new TensorExporter();
		Element eConnections = xmlDoc.createElement(EXmlGeneric.ELEMENT_CONNECTIONS.getName());
		eRoot.appendChild(eConnections);

		return xmlDoc;
	}
}
