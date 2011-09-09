package com.mpdeimos.tensor.impex;

import com.mpdeimos.tensor.model.ModelRoot;
import com.mpdeimos.tensor.model.TensorBase;
import com.mpdeimos.tensor.model.TensorConnection;
import com.mpdeimos.tensor.util.Wrapper;
import com.mpdeimos.tensor.util.XmlUtil;

import java.io.File;
import java.util.HashMap;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * imports a root model from XML.
 * 
 * @author mpdeimos
 * 
 */
public class TdgImporter
{
	/** exports a root model to an XML document. */
	public ModelRoot fromXml(File file)
	{
		Document xmlDoc = XmlUtil.parse(file);

		Element root = xmlDoc.getDocumentElement();

		NodeList tensorNodes = root.getElementsByTagName(ETdgTensor.ELEMENT_TENSOR.$());
		NodeList connectionNodes = root.getElementsByTagName(ETdgConnection.ELEMENT_CONECTION.$());

		ModelRoot mr = new ModelRoot();

		TensorImporter tensorImporter = new TensorImporter();
		Wrapper<Integer> outId = new Wrapper<Integer>(-1);
		HashMap<Integer, TensorBase> tensorIDs = new HashMap<Integer, TensorBase>();

		for (int i = 0; i < tensorNodes.getLength(); i++)
		{
			Node item = tensorNodes.item(i);
			if (item instanceof Element)
			{
				TensorBase data = tensorImporter.importNode(
						(Element) item,
						mr,
						outId);

				tensorIDs.put(outId.get(), data);
				mr.addChild(data);
			}
		}

		ConnectionImporter connectionImporter = new ConnectionImporter();
		for (int i = 0; i < connectionNodes.getLength(); i++)
		{
			Node item = connectionNodes.item(i);
			if (item instanceof Element)
			{
				TensorConnection data = connectionImporter.importNode(
						(Element) item,
						mr,
						tensorIDs);

				mr.addChild(data);
			}
		}

		return mr;
	}
}
