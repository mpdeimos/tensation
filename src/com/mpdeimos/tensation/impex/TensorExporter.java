package com.mpdeimos.tensation.impex;

import com.mpdeimos.tensation.model.TensorBase;
import com.mpdeimos.tensation.model.TensorConnectionAnchor;
import com.mpdeimos.tensation.model.TensorConnectionAnchor.EDirection;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * Handles export of Tensors to valid XML data.
 * 
 * Helpers: (0) TensorBase, (1) id
 * 
 * @author mpdeimos
 * 
 */
public class TensorExporter implements IExporter
{
	@Override
	public Element export(Document xmlDoc, Object... helpers)
	{
		TensorBase tensor = (TensorBase) helpers[0];
		int id = (Integer) helpers[1];

		Element eTensor = xmlDoc.createElement(ETdgTensor.ELEMENT_TENSOR.$());

		eTensor.setAttribute(
				ETdgTensor.ATTRIB_ID.$(), Integer.toString(id));

		eTensor.setAttribute(
				ETdgTensor.ATTRIB_POS_X.$(),
				Integer.toString(tensor.getPosition().x));
		eTensor.setAttribute(
				ETdgTensor.ATTRIB_POS_Y.$(),
				Integer.toString(tensor.getPosition().y));
		eTensor.setAttribute(
				ETdgTensor.ATTRIB_ROTATION.$(),
				Integer.toString((int) (tensor.getRotation())));

		for (int i = 0; i < tensor.getAnchors().size(); i++)
		{
			TensorConnectionAnchor anchor = tensor.getAnchors().get(i);

			Element eAnchor = xmlDoc.createElement(ETdgTensor.ELEMENT_TENSOR_ANCHOR.$());
			eAnchor.setAttribute(
					ETdgTensor.ATTRIB_ANCHOR_ID.$(), Integer.toString(i));

			ETdgTensor direction = null;
			if (EDirection.SINK == anchor.getDirection())
				direction = ETdgTensor.VALUE_ANCHOR_SINK;
			else
				direction = ETdgTensor.VALUE_ANCHOR_SOURCE;

			eAnchor.setAttribute(
					ETdgTensor.ATTRIB_ANCHOR_DIRECTION.$(),
					direction.$());

			eTensor.appendChild(eAnchor);
		}

		KeyValueStoreExporter kve = new KeyValueStoreExporter();
		Element export = kve.export(
				xmlDoc,
				tensor.getAppearanceContainer().getValues(),
				"appearance"); //$NON-NLS-1$
		if (export != null)
		{
			eTensor.appendChild(export);
		}

		return eTensor;
	}
}
