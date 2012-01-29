package com.mpdeimos.tensation.impex;

import com.mpdeimos.tensation.model.ModelRoot;
import com.mpdeimos.tensation.model.TensorBase;
import com.mpdeimos.tensation.model.TensorConnection;
import com.mpdeimos.tensation.util.XmlUtil;

import java.util.HashMap;

import org.w3c.dom.Element;

/**
 * Imports an Tensor Connection from XML Data.
 * 
 * Helpers: (0) ModelRoot, (1) HashMap<Integer, TensorBase>
 * 
 * @author mpdeimos
 * 
 */
public class ConnectionImporter implements IImporter
{
	@Override
	public TensorConnection importNode(
			Element node,
			Object... helpers)
	{
		ModelRoot mr = (ModelRoot) helpers[0];
		@SuppressWarnings("unchecked")
		HashMap<Integer, TensorBase> tensors = (HashMap<Integer, TensorBase>) helpers[1];

		int sinkID = Integer.parseInt(node.getAttribute(ETdgConnection.ATTRIB_SINK.$()));
		int sinkAnchorID = Integer.parseInt(node.getAttribute(ETdgConnection.ATTRIB_SINK_ANCHOR.$()));
		double sinkAnchorDist = Double.parseDouble(node.getAttribute(ETdgConnection.ATTRIB_SINK_DISTANCE.$()));
		TensorBase sink = tensors.get(sinkID);

		int sourceID = Integer.parseInt(node.getAttribute(ETdgConnection.ATTRIB_SOURCE.$()));
		int sourceAnchorID = Integer.parseInt(node.getAttribute(ETdgConnection.ATTRIB_SOURCE_ANCHOR.$()));
		double sourceAnchorDist = Double.parseDouble(node.getAttribute(ETdgConnection.ATTRIB_SOURCE_DISTANCE.$()));
		TensorBase source = tensors.get(sourceID);

		TensorConnection connection = new TensorConnection(
				mr,
				source.getAnchors().get(sourceAnchorID),
				sink.getAnchors().get(sinkAnchorID));

		connection.setSinkControlPointDistance(sinkAnchorDist);
		connection.setSourceControlPointDistance(sourceAnchorDist);

		for (Element e : XmlUtil.iterate(node))
		{
			if ("appearance".equals(e.getAttribute(ETdgKeyValueStore.ATTRIB_GROUP_NAME.$()))) //$NON-NLS-1$
			{
				KeyValueStoreImporter kvi = new KeyValueStoreImporter();
				HashMap<String, Object> map = kvi.importNode(e);
				connection.getAppearanceContainer().setValues(map);
			}
		}

		return connection;
	}
}
