package com.mpdeimos.tensation.impex.tdg;

import com.mpdeimos.tensation.figure.AppearanceContainer.IAppearanceHolder;
import com.mpdeimos.tensation.impex.export.ExportHandler;
import com.mpdeimos.tensation.model.IModelData;
import com.mpdeimos.tensation.model.ModelRoot;
import com.mpdeimos.tensation.model.Operator;
import com.mpdeimos.tensation.model.TensorBase;
import com.mpdeimos.tensation.model.TensorConnection;
import com.mpdeimos.tensation.util.XmlUtil;

import java.util.HashMap;

import javax.xml.parsers.DocumentBuilder;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * exports a root model into XML.
 * 
 * @author mpdeimos
 * 
 */
public class TdgExporter
{
	/** exports a root model to an XML document. */
	public Document toXml(ModelRoot modelRoot)
	{
		DocumentBuilder builder = XmlUtil.getBuilder();
		Document xmlDoc = builder.newDocument();
		xmlDoc.setXmlStandalone(true);

		Element eRoot = xmlDoc.createElement(ETdgGeneric.ELEMENT_ROOT.$());
		xmlDoc.appendChild(eRoot);

		TensorExporter tensorExporter = new TensorExporter();
		Element eTensors = xmlDoc.createElement(ETdgGeneric.ELEMENT_TENSORS.$());
		eRoot.appendChild(eTensors);

		HashMap<TensorBase, Integer> tensorsToIds = new HashMap<TensorBase, Integer>();
		int tensorIDs = 0;
		for (IModelData model : modelRoot.getChildren())
		{
			if (model instanceof TensorBase)
			{
				tensorsToIds.put((TensorBase) model, tensorIDs);
				Element e = tensorExporter.export(xmlDoc, model, tensorIDs);
				commonExport(xmlDoc, e, model);
				eTensors.appendChild(e);
				tensorIDs++;
			}
		}

		ConnectionExporter connectionExporter = new ConnectionExporter();
		Element eConnections = xmlDoc.createElement(ETdgGeneric.ELEMENT_CONNECTIONS.$());
		eRoot.appendChild(eConnections);

		for (IModelData model : modelRoot.getChildren())
		{
			if (model instanceof TensorConnection)
			{
				Element e = connectionExporter.export(
						xmlDoc, model, tensorsToIds);
				commonExport(xmlDoc, e, model);
				eConnections.appendChild(e);
			}
		}

		Element eOperators = xmlDoc.createElement(ETdgGeneric.ELEMENT_OPERATORS.$());
		eRoot.appendChild(eOperators);

		for (IModelData model : modelRoot.getChildren())
		{
			if (model instanceof Operator)
			{
				Element e = xmlDoc.createElement(ETdgGeneric.ELEMENT_OPERATOR.$());
				commonExport(xmlDoc, e, model);
				eOperators.appendChild(e);
			}
		}

		return xmlDoc;
	}

	/** Performs common export actions. */
	private void commonExport(Document xmlDoc, Element e, IModelData model)
	{
		KeyValueStoreExporter kve = new KeyValueStoreExporter();
		ExportHandler exp = new ExportHandler(model);
		Element export = kve.export(xmlDoc, exp.getValues(), "this"); //$NON-NLS-1$
		if (export != null)
		{
			e.appendChild(export);
		}
		if (model instanceof IAppearanceHolder)
		{
			export = kve.export(
					xmlDoc,
					((IAppearanceHolder) model).getAppearanceContainer().getValues(),
					"appearance"); //$NON-NLS-1$
			if (export != null)
			{
				e.appendChild(export);
			}
		}
	}
}
