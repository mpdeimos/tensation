package com.mpdeimos.tensation.util;

import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.util.Iterator;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.DocumentType;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

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

			// disable all kind of grammar resolving
			factory.setValidating(false);
			factory.setFeature("http://xml.org/sax/features/namespaces", false); //$NON-NLS-1$
			factory.setFeature("http://xml.org/sax/features/validation", false); //$NON-NLS-1$
			factory.setFeature(
					"http://apache.org/xml/features/nonvalidating/load-dtd-grammar", false); //$NON-NLS-1$
			factory.setFeature(
					"http://apache.org/xml/features/nonvalidating/load-external-dtd", false); //$NON-NLS-1$

			DocumentBuilder builder = factory.newDocumentBuilder();

			return builder;
		}
		catch (ParserConfigurationException e)
		{
			// shouldn't happen
			Log.e(XmlUtil.class, e);
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

			int indentAmount = 2;

			TransformerFactory factory = TransformerFactory.newInstance();
			factory.setAttribute("indent-number", indentAmount); //$NON-NLS-1$
			Transformer xformer = factory.newTransformer();
			xformer.setOutputProperty(OutputKeys.INDENT, "yes"); //$NON-NLS-1$
			xformer.setOutputProperty(
					"{http://xml.apache.org/xslt}indent-amount", //$NON-NLS-1$
					Integer.toString(indentAmount));

			DocumentType doctype = doc.getDoctype();
			if (doctype != null)
			{
				xformer.setOutputProperty(
						OutputKeys.DOCTYPE_PUBLIC,
						doctype.getPublicId());
				xformer.setOutputProperty(
						OutputKeys.DOCTYPE_SYSTEM,
						doctype.getSystemId());
			}

			xformer.transform(source, result);

			return true;
		}
		catch (TransformerConfigurationException e)
		{
			Log.e(XmlUtil.class, e);
		}
		catch (TransformerException e)
		{
			Log.e(XmlUtil.class, e);
		}

		return false;
	}

	/**
	 * Parses an XML Document from a given file.
	 * 
	 * @return the Document on success, null otherwise.
	 */
	public static Document parse(File file)
	{
		DocumentBuilder builder = XmlUtil.getBuilder();
		try
		{
			return builder.parse(file);
		}
		catch (SAXException e)
		{
			Log.e(XmlUtil.class, e);
		}
		catch (IOException e)
		{
			Log.e(XmlUtil.class, e);
		}

		return null;
	}

	/** Iterates over the child nodes of an element. */
	public static Iterable<Element> iterate(final Element e)
	{
		return iterate(e.getChildNodes());
	}

	/** Iterates over a NodeList. Does not permit deletions. */
	public static Iterable<Element> iterate(final NodeList node)
	{
		return new Iterable<Element>()
		{
			@Override
			public Iterator<Element> iterator()
			{
				return new Iterator<Element>()
				{
					/** position */
					private int i = 0;

					/** the next element. */
					Element next = null;

					@Override
					public boolean hasNext()
					{
						if (this.next != null)
							return true;

						while (this.i < node.getLength())
						{
							Node c = node.item(this.i++);
							if (c instanceof Element)
							{
								this.next = (Element) c;
								return true;
							}
						}
						return false;
					}

					@Override
					public Element next()
					{
						hasNext();
						Element r = this.next;
						this.next = null;
						return r;
					}

					@Override
					public void remove()
					{
						throw new IllegalStateException();
					}
				};
			}
		};
	}
}
