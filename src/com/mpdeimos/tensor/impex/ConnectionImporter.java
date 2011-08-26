package com.mpdeimos.tensor.impex;

import com.mpdeimos.tensor.model.ModelRoot;
import com.mpdeimos.tensor.model.TensorBase;
import com.mpdeimos.tensor.model.TensorConnection;

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

		int sinkID = Integer.parseInt(node.getAttribute(EXmlConnection.ATTRIB_SINK.getName()));
		int sinkAnchorID = Integer.parseInt(node.getAttribute(EXmlConnection.ATTRIB_SINK_ANCHOR.getName()));
		double sinkAnchorDist = Double.parseDouble(node.getAttribute(EXmlConnection.ATTRIB_SINK_DISTANCE.getName()));
		TensorBase sink = tensors.get(sinkID);

		int sourceID = Integer.parseInt(node.getAttribute(EXmlConnection.ATTRIB_SOURCE.getName()));
		int sourceAnchorID = Integer.parseInt(node.getAttribute(EXmlConnection.ATTRIB_SOURCE_ANCHOR.getName()));
		double sourceAnchorDist = Double.parseDouble(node.getAttribute(EXmlConnection.ATTRIB_SOURCE_DISTANCE.getName()));
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
