package com.mpdeimos.tensation.impex.tdg;

import com.mpdeimos.tensation.impex.IImporter;
import com.mpdeimos.tensation.model.ModelRoot;
import com.mpdeimos.tensation.model.TensorBase;
import com.mpdeimos.tensation.model.TensorConnection;

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
		TensorBase sink = tensors.get(sinkID);

		int sourceID = Integer.parseInt(node.getAttribute(ETdgConnection.ATTRIB_SOURCE.$()));
		int sourceAnchorID = Integer.parseInt(node.getAttribute(ETdgConnection.ATTRIB_SOURCE_ANCHOR.$()));
		TensorBase source = tensors.get(sourceID);

		TensorConnection connection = new TensorConnection(
				mr,
				source.getAnchors().get(sourceAnchorID),
				sink.getAnchors().get(sinkAnchorID));

		return connection;
	}
}
