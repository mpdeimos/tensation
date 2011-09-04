package com.mpdeimos.tensor.impex;

import org.junit.Test;

/**
 * Tests model export.
 * 
 * @author mpdeimos
 * 
 */
public class TestExporter
{
	/** Tests an empty model. */
	@Test
	public void emptyTest()
	{
		// ModelRoot mr = new ModelRoot();
		//
		// Exporter exp = new Exporter();
		// Document doc = exp.toXml(mr);
		// String s = XmlUtil.writeDomDocumentToString(doc);

	}

	/** Tests a simple model. */
	@Test
	public void simpleTest()
	{
		// ModelRoot mr = new ModelRoot();
		// EpsilonTensor t1 = new EpsilonTensor(mr, EDirection.SINK);
		// t1.setPosition(new Point(10, 20));
		// t1.setRotation(90);
		// mr.addChild(t1);
		//
		// EpsilonTensor t2 = new EpsilonTensor(mr, EDirection.SOURCE);
		// t2.setPosition(new Point(30, 40));
		// t2.setRotation(45);
		// mr.addChild(t2);
		//
		// Exporter exp = new Exporter();
		// Document doc = exp.toXml(mr);
		// String s = XmlUtil.writeDomDocumentToString(doc);
		//
		// System.out.println(s);
	}
}
