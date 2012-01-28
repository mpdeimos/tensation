package com.mpdeimos.tensation.impex;

import com.mpdeimos.tensation.model.ModelRoot;
import com.mpdeimos.tensation.model.TensorBase;
import com.mpdeimos.tensation.model.TensorConnection;

import java.util.HashMap;

import org.w3c.dom.Element;

/**
 * Imports an Tensor Connection from XML Data.
 * 
 * @author mpdeimos
 * 
 */
public class ConnectionImporter implements IImporter
{
	@Override
	public TensorConnection importNode(
			Element node,
			ModelRoot mr,
			Object... helpers)
	{
		@SuppressWarnings("unchecked")
		HashMap<Integer, TensorBase> tensors = (HashMap<Integer, TensorBase>) helpers[0];

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

		return connection;
	}
}
