package com.mpdeimos.tensation.impex.tdg;

import com.mpdeimos.tensation.figure.AppearanceContainer.IAppearanceHolder;
import com.mpdeimos.tensation.impex.export.ExportHandler;
import com.mpdeimos.tensation.model.ModelDataBase;
import com.mpdeimos.tensation.model.ModelRoot;
import com.mpdeimos.tensation.model.Operator;
import com.mpdeimos.tensation.model.TensorBase;
import com.mpdeimos.tensation.model.TensorConnection;
import com.mpdeimos.tensation.util.Wrapper;
import com.mpdeimos.tensation.util.XmlUtil;

import java.io.File;
import java.util.HashMap;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
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
		NodeList operatorNodes = root.getElementsByTagName(ETdgGeneric.ELEMENT_OPERATOR.$());

		ModelRoot mr = new ModelRoot();

		TensorImporter tensorImporter = new TensorImporter();
		Wrapper<Integer> outId = new Wrapper<Integer>(-1);
		HashMap<Integer, TensorBase> tensorIDs = new HashMap<Integer, TensorBase>();

		for (Element item : XmlUtil.iterate(tensorNodes))
		{
			TensorBase data = tensorImporter.importNode(
						item,
						mr,
						outId);

			commonImport(item, data);

			tensorIDs.put(outId.get(), data);
			mr.addChild(data);
		}

		ConnectionImporter connectionImporter = new ConnectionImporter();
		for (Element item : XmlUtil.iterate(connectionNodes))
		{
			TensorConnection data = connectionImporter.importNode(
						item,
						mr,
						tensorIDs);

			commonImport(item, data);

			mr.addChild(data);
		}

		for (Element item : XmlUtil.iterate(operatorNodes))
		{
			Operator data = new Operator(mr);
			commonImport(item, data);
			mr.addChild(data);
		}

		return mr;
	}

	/** Common import actions. */
	private void commonImport(Element node, ModelDataBase model)
	{
		KeyValueStoreImporter kvi = new KeyValueStoreImporter();
		for (Element e : XmlUtil.iterate(node))
		{
			if ("this".equals(e.getAttribute(ETdgKeyValueStore.ATTRIB_GROUP_NAME.$()))) //$NON-NLS-1$
			{
				ExportHandler exp = new ExportHandler(model);
				HashMap<String, Object> map = kvi.importNode(e);
				exp.setValues(map);
			}
			if (model instanceof IAppearanceHolder
					&& "appearance".equals(e.getAttribute(ETdgKeyValueStore.ATTRIB_GROUP_NAME.$()))) //$NON-NLS-1$
			{
				HashMap<String, Object> map = kvi.importNode(e);
				((IAppearanceHolder) model).getAppearanceContainer().setValues(
						map);
			}
		}
	}
}
