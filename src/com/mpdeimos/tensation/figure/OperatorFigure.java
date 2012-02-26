package com.mpdeimos.tensation.figure;

import com.mpdeimos.tensation.editpart.EditPartBase;
import com.mpdeimos.tensation.editpart.IEditPart;
import com.mpdeimos.tensation.figure.ShapePack.EDrawingMode;
import com.mpdeimos.tensation.impex.svg.ESvg;
import com.mpdeimos.tensation.impex.svg.ESvgDefinitions;
import com.mpdeimos.tensation.model.Operator;
import com.mpdeimos.tensation.util.Gfx;

import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Shape;
import java.awt.geom.Ellipse2D;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * Drawing class for an Operator
 * 
 * @author mpdeimos
 * 
 */
public class OperatorFigure extends FigureBase
{

	/** size of the center circle in pixels */
	private static final int CENTER_CIRCLE_RADIUS = 8;

	/**
	 * Constructor.
	 */
	public OperatorFigure(IEditPart editPart)
	{
		super(editPart);
	}

	@Override
	protected void updateShapes()
	{
		super.updateShapes();

		List<Shape> lines = new ArrayList<Shape>(1);

		Operator tensor = (Operator) this.editPart.getModel();
		Point position = tensor.getPosition();
		int x = (int) position.getX();
		int y = (int) position.getY();

		Ellipse2D circle = new Ellipse2D.Double(
				x - CENTER_CIRCLE_RADIUS + 0.5,
				y - CENTER_CIRCLE_RADIUS + 0.5,
				2 * CENTER_CIRCLE_RADIUS - 1,
				2 * CENTER_CIRCLE_RADIUS - 1);
		lines.add(circle);

		ShapePack linePack = new ShapePack(EDrawingMode.STROKE, lines);
		this.shapePacks.add(linePack);
	}

	@Override
	public Element getSvgNode(Document doc, HashMap<String, Element> defs)
	{
		Operator operator = (Operator) this.editPart.getModel();
		Point position = operator.getPosition();
		AppearanceContainer appearance = ((EditPartBase)
				this.editPart).getAppearanceContainer();
		String def = getSvgDefName();

		Element use = doc.createElement(ESvg.ELEMENT_USE.$());
		use.setAttribute(
				ESvg.ATTRIB_TRANSFORM.$(),
				ESvg.VALUE_TRANSFORM_FUNC_TRANSLATE.$(
						position.getX(),
						position.getY())
				);

		use.setAttribute(ESvg.ATTRIB_XLINK_HREF.$(), ESvg.VALUE_REF.$(def));
		use.setAttribute(
				ESvg.ATTRIB_CLASS.$(),
				ESvgDefinitions.CLASS_OPERATOR.$());

		if (!defs.containsKey(def))
		{
			Element group = doc.createElement(ESvg.ELEMENT_GROUP.$());
			group.setAttribute(ESvg.ATTRIB_ID.$(), def);

			Element circle = doc.createElement(ESvg.ELEMENT_CIRCLE.$());
			group.appendChild(circle);

			circle.setAttribute(
					ESvg.ATTRIB_RADIUS.$(),
					Integer.toString(CENTER_CIRCLE_RADIUS));

			circle.setAttribute(
					ESvg.ATTRIB_CLASS.$(),
					ESvgDefinitions.CLASS_OPERATOR_CIRCLE.$());

			Element txt = doc.createElement(ESvg.ELEMENT_TEXT.$());
			txt.setTextContent(operator.getOperation().getLabel());
			txt.setAttribute(ESvg.ATTRIB_TEXT_DY.$(), "0.5ex"); //$NON-NLS-1$
			txt.setAttribute(
					ESvg.ATTRIB_TEXT_ANCHOR.$(),
					ESvg.VALUE_TEXT_ANCHOR_MIDDLE.$());
			group.appendChild(txt);

			defs.put(def, group);
		}

		appearance.applyAppearance(
				use);

		return use;
	}

	/** @return the string used as svg def. */
	private String getSvgDefName()
	{
		Operator operator = (Operator) this.editPart.getModel();

		String name = ESvgDefinitions.OPERATOR_DEF_PREFIX.$()
				+ operator.getOperation().ordinal();

		return name;
	}

	@Override
	public void draw(Graphics2D gfx)
	{
		super.draw(gfx);
		Font f = gfx.getFont();
		gfx.setFont(Gfx.SANS_SERIF_12);
		Gfx.drawTextCentered(
				gfx,
				((Operator) this.editPart.getModel()).getPosition(),
				((Operator) this.editPart.getModel()).getOperation().getLabel());
		gfx.setFont(f);
	}
}
