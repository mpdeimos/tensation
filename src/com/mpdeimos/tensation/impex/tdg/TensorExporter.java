package com.mpdeimos.tensation.impex.tdg;

import com.mpdeimos.tensation.impex.IExporter;
import com.mpdeimos.tensation.model.EDirection;
import com.mpdeimos.tensation.model.TensorBase;
import com.mpdeimos.tensation.model.TensorConnectionAnchor;

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

		return eTensor;
	}
}
