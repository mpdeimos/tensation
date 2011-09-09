package com.mpdeimos.tensor.impex;

import com.mpdeimos.tensor.editpart.IEditPart;
import com.mpdeimos.tensor.figure.IFigure;
import com.mpdeimos.tensor.ui.Application;
import com.mpdeimos.tensor.ui.DrawingCanvas;
import com.mpdeimos.tensor.util.Log;
import com.mpdeimos.tensor.util.XmlUtil;

import java.awt.Rectangle;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

/**
 * Exports editparts (or model data) to SVG.
 * 
 * @author mpdeimos
 * 
 */
public class SvgExporter
{
	/** the svg prototype file. */
	private static final String SVG_PROTOTYPE_XML = "svg-prototype.xml"; //$NON-NLS-1$

	/** @return an XML SVG Document. */
	public Document toSvg(List<IEditPart> parts)
	{
		InputStream resource = SvgExporter.class.getResourceAsStream(SVG_PROTOTYPE_XML);
		DocumentBuilder builder = XmlUtil.getBuilder();
		Document xmlDoc = null;
		try
		{
			xmlDoc = builder.parse(resource);
		}
		catch (Exception e)
		{
			Log.e(this, "Error parsing svg prototype", e); //$NON-NLS-1$
			return null;
		}

		Element eRoot = xmlDoc.getDocumentElement();
		DrawingCanvas canvas = Application.getApp().getDrawingCanvas();
		Rectangle r = canvas.getImageRectangle(); // TODO calculate real rect
		eRoot.setAttribute(
				ESvg.ATTRIB_WIDTH.$(),
				Integer.toString(Math.max(0, r.x) + r.width));
		eRoot.setAttribute(
				ESvg.ATTRIB_HEIGHT.$(),
				Integer.toString(Math.max(0, r.y) + r.height));

		HashMap<String, Element> definitions = new HashMap<String, Element>();
		NodeList defsList = eRoot.getElementsByTagName(ESvg.ELEMENT_DEFS.$());
		Element defs = null;
		if (defsList.getLength() > 0)
		{
			defs = (Element) defsList.item(0);
		}
		else
		{
			defs = xmlDoc.createElement(ESvg.ELEMENT_DEFS.$());
			eRoot.appendChild(defs);
		}

		for (IEditPart part : parts)
		{
			IFigure figure = part.getFigure();
			Element svgNode = figure.getSvgNode(xmlDoc, definitions);
			if (svgNode != null)
				eRoot.appendChild(svgNode);
		}

		for (Element def : definitions.values())
		{
			defs.appendChild(def);
		}

		return xmlDoc;
	}
}
