package com.mpdeimos.tensor.impex;

import com.mpdeimos.tensor.model.GenericTensor;
import com.mpdeimos.tensor.model.ModelRoot;
import com.mpdeimos.tensor.model.TensorBase;
import com.mpdeimos.tensor.model.TensorConnectionAnchor.EDirection;
import com.mpdeimos.tensor.util.Wrapper;

import java.awt.Point;

import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

/**
 * Imports an Tensor from XML Data.
 * 
 * @author mpdeimos
 * 
 */
public class TensorImporter implements IImporter
{
	@Override
	public TensorBase importNode(Element node, ModelRoot mr, Object... helpers)
	{
		@SuppressWarnings("unchecked")
		Wrapper<Integer> outId = (Wrapper<Integer>) helpers[0];

		NodeList anchors = node.getElementsByTagName(EXmlTensor.ELEMENT_TENSOR_ANCHOR.getName());
		EDirection[] directions = new EDirection[anchors.getLength()];
		for (int i = 0; i < anchors.getLength(); i++)
		{
			Element anchor = (Element) anchors.item(i);
			EDirection dir = EDirection.SOURCE;
			String dirName = anchor.getAttribute(EXmlTensor.ATTRIB_ANCHOR_DIRECTION.getName());
			if (EXmlTensor.VALUE_ANCHOR_SINK.getName().equals(dirName))
				dir = EDirection.SINK;

			directions[Integer.parseInt(anchor.getAttribute(EXmlTensor.ATTRIB_ANCHOR_ID.getName()))] = dir;
		}

		GenericTensor tensor = new GenericTensor(mr, directions);

		int id = Integer.parseInt(node.getAttribute(EXmlTensor.ATTRIB_ID.getName()));
		outId.set(id);

		int x = Integer.parseInt(node.getAttribute(EXmlTensor.ATTRIB_POS_X.getName()));
		int y = Integer.parseInt(node.getAttribute(EXmlTensor.ATTRIB_POS_Y.getName()));
		tensor.setPosition(new Point(x, y));

		double rot = Double.parseDouble(node.getAttribute(EXmlTensor.ATTRIB_ROTATION.getName()));
		tensor.setRotation(rot);

		return tensor;
	}
}
