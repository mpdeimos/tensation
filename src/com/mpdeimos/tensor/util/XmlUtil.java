package com.mpdeimos.tensor.util;

import java.io.File;
import java.io.StringWriter;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;

/**
 * Utility functions for XML actions.
 * 
 * @author mpdeimos
 * 
 */
public class XmlUtil
{
	/**
	 * @return an XML DOM document builder. May be null if an configuration
	 *         error occurs.
	 */
	public static DocumentBuilder getBuilder()
	{
		try
		{
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();

			return builder;
		}
		catch (ParserConfigurationException e)
		{
			// shouldn't happen
		}

		return null;
	}

	/**
	 * writes a dom document to a file.
	 * 
	 * @return true on success.
	 */
	public static boolean writeDomDocumentToFile(Document doc, File file)
	{
		Result result = new StreamResult(file);
		return writeDomDocumentToResult(doc, result);
	}

	/**
	 * writes a dom document to a file.
	 * 
	 * @return null on failure.
	 */
	public static String writeDomDocumentToString(Document doc)
	{
		StringWriter sw = new StringWriter();
		Result result = new StreamResult(sw);

		if (!writeDomDocumentToResult(doc, result))
			return null;

		return sw.toString();
	}

	/** writes a dom document to a result stream. */
	private static boolean writeDomDocumentToResult(Document doc, Result result)
	{
		try
		{
			Source source = new DOMSource(doc);

			Transformer xformer = TransformerFactory.newInstance().newTransformer();
			xformer.transform(source, result);

			return true;
		}
		catch (TransformerConfigurationException e)
		{
			// swallow
		}
		catch (TransformerException e)
		{
			// swallow
		}

		return false;
	}
}
