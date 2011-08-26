package com.mpdeimos.tensor.impex;

import com.mpdeimos.tensor.model.IModelData;
import com.mpdeimos.tensor.model.TensorBase;
import com.mpdeimos.tensor.model.TensorConnectionAnchor;
import com.mpdeimos.tensor.model.TensorConnectionAnchor.EDirection;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * Handles export of Tensors to valid XML data.
 * 
 * @author mpdeimos
 * 
 */
public class TensorExporter implements IExporter
{
	@Override
	public Element export(Document xmlDoc, IModelData data, Object... helpers)
	{
		TensorBase tensor = (TensorBase) data;

		int id = (Integer) helpers[0];

		Element eTensor = xmlDoc.createElement(EXmlTensor.ELEMENT_TENSOR.getName());

		eTensor.setAttribute(
				EXmlTensor.ATTRIB_ID.getName(), Integer.toString(id));

		eTensor.setAttribute(
				EXmlTensor.ATTRIB_POS_X.getName(),
				Integer.toString(tensor.getPosition().x));
		eTensor.setAttribute(
				EXmlTensor.ATTRIB_POS_Y.getName(),
				Integer.toString(tensor.getPosition().y));
		eTensor.setAttribute(
				EXmlTensor.ATTRIB_ROTATION.getName(),
				Integer.toString((int) (tensor.getRotation())));

		for (int i = 0; i < tensor.getAnchors().size(); i++)
		{
			TensorConnectionAnchor anchor = tensor.getAnchors().get(i);

			Element eAnchor = xmlDoc.createElement(EXmlTensor.ELEMENT_TENSOR_ANCHOR.getName());
			eAnchor.setAttribute(
					EXmlTensor.ATTRIB_ANCHOR_ID.getName(), Integer.toString(i));

			EXmlTensor direction = null;
			if (EDirection.SINK == anchor.getDirection())
				direction = EXmlTensor.VALUE_ANCHOR_SINK;
			else
				direction = EXmlTensor.VALUE_ANCHOR_SOURCE;

			eAnchor.setAttribute(
					EXmlTensor.ATTRIB_ANCHOR_DIRECTION.getName(),
					direction.getName());

			eTensor.appendChild(eAnchor);
		}

		return eTensor;
	}
}
