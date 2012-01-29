package com.mpdeimos.tensation.impex;

import com.mpdeimos.tensation.model.GenericTensor;
import com.mpdeimos.tensation.model.ModelRoot;
import com.mpdeimos.tensation.model.TensorBase;
import com.mpdeimos.tensation.model.TensorConnectionAnchor.EDirection;
import com.mpdeimos.tensation.util.Wrapper;
import com.mpdeimos.tensation.util.XmlUtil;

import java.awt.Point;
import java.util.HashMap;

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
	public TensorBase importNode(Element node, Object... helpers)
	{
		ModelRoot mr = (ModelRoot) helpers[0];
		@SuppressWarnings("unchecked")
		Wrapper<Integer> outId = (Wrapper<Integer>) helpers[1];

		NodeList anchors = node.getElementsByTagName(ETdgTensor.ELEMENT_TENSOR_ANCHOR.$());
		EDirection[] directions = new EDirection[anchors.getLength()];
		for (int i = 0; i < anchors.getLength(); i++)
		{
			Element anchor = (Element) anchors.item(i);
			EDirection dir = EDirection.SOURCE;
			String dirName = anchor.getAttribute(ETdgTensor.ATTRIB_ANCHOR_DIRECTION.$());
			if (ETdgTensor.VALUE_ANCHOR_SINK.$().equals(dirName))
				dir = EDirection.SINK;

			directions[Integer.parseInt(anchor.getAttribute(ETdgTensor.ATTRIB_ANCHOR_ID.$()))] = dir;
		}

		GenericTensor tensor = new GenericTensor(mr, directions);

		int id = Integer.parseInt(node.getAttribute(ETdgTensor.ATTRIB_ID.$()));
		outId.set(id);

		int x = Integer.parseInt(node.getAttribute(ETdgTensor.ATTRIB_POS_X.$()));
		int y = Integer.parseInt(node.getAttribute(ETdgTensor.ATTRIB_POS_Y.$()));
		tensor.setPosition(new Point(x, y));

		double rot = Double.parseDouble(node.getAttribute(ETdgTensor.ATTRIB_ROTATION.$()));
		tensor.setRotation(rot);

		for (Element e : XmlUtil.iterate(node))
		{
			if ("appearance".equals(e.getAttribute(ETdgKeyValueStore.ATTRIB_GROUP_NAME.$()))) //$NON-NLS-1$
			{
				KeyValueStoreImporter kvi = new KeyValueStoreImporter();
				HashMap<String, Object> map = kvi.importNode(e);
				tensor.getAppearanceContainer().setValues(map);
			}
		}

		return tensor;
	}
}
