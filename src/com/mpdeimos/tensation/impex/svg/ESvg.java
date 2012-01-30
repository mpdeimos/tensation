package com.mpdeimos.tensation.impex.svg;

import com.mpdeimos.tensation.impex.INameEnum;

/**
 * Tags used for SVG writing.
 * 
 * @author mpdeimos
 * 
 */
public enum ESvg implements INameEnum
{
	/** the center-x attribute. */
	ATTRIB_CENTER_X("cx"), //$NON-NLS-1$

	/** the center-y attribute. */
	ATTRIB_CENTER_Y("cy"), //$NON-NLS-1$

	/** the css class attribute. */
	ATTRIB_CLASS("class"), //$NON-NLS-1$

	/** the css style attribute. */
	ATTRIB_STYLE("style"), //$NON-NLS-1$

	/** the color fill attribute. */
	ATTRIB_FILL("fill"), //$NON-NLS-1$

	/** the from-x attribute. */
	ATTRIB_FROM_X("x1"), //$NON-NLS-1$

	/** the from-y attribute. */
	ATTRIB_FROM_Y("y1"), //$NON-NLS-1$

	/** the height attribute. */
	ATTRIB_HEIGHT("height"), //$NON-NLS-1$

	/** the id attribute. */
	ATTRIB_ID("id"), //$NON-NLS-1$
	/** the radius attribute. */
	ATTRIB_RADIUS("r"), //$NON-NLS-1$

	/** the stroke color fill attribute. */
	ATTRIB_STROKE("stroke"), //$NON-NLS-1$
	/** the strike width attribute. */
	ATTRIB_STROKE_WIDTH("stroke-width"), //$NON-NLS-1$

	/** the to-x attribute. */
	ATTRIB_TO_X("x2"), //$NON-NLS-1$
	/** the to-y attribute. */
	ATTRIB_TO_Y("y2"), //$NON-NLS-1$

	/** the transform attribute. */
	ATTRIB_TRANSFORM("transform"), //$NON-NLS-1$

	/** the type attribute. */
	ATTRIB_TYPE("type"), //$NON-NLS-1$

	/** the width attribute. */
	ATTRIB_WIDTH("width"), //$NON-NLS-1$

	/** the x attribute. */
	ATTRIB_POS_X("x"), //$NON-NLS-1$

	/** the xlink href attribute. */
	ATTRIB_XLINK_HREF("xlink:href"), //$NON-NLS-1$

	/** the y attribute. */
	ATTRIB_POS_Y("y"), //$NON-NLS-1$

	/** marker end attribute. */
	ATTRIB_MARKER_END("marker-end"), //$NON-NLS-1$

	/** marker start attribute. */
	ATTRIB_MARKER_START("marker-start"), //$NON-NLS-1$

	/** marker height attribute. */
	ATTRIB_MARKER_HEIGHT("markerHeight"), //$NON-NLS-1$

	/** marker width attribute. */
	ATTRIB_MARKER_WIDTH("markerWidth"), //$NON-NLS-1$

	/** marker units attribute. */
	ATTRIB_MARKER_UNITS("markerUnits"), //$NON-NLS-1$

	/** marker x-reference attribute. */
	ATTRIB_MARKER_REF_X("refX"), //$NON-NLS-1$

	/** marker x-reference attribute. */
	ATTRIB_MARKER_REF_Y("refY"), //$NON-NLS-1$

	/** marker viewbox attribute. */
	ATTRIB_MARKER_VIEWBOX("viewBox"), //$NON-NLS-1$

	/** The path data attribute. */
	ATTRIB_PATH_DATA("d"), //$NON-NLS-1$

	/** The circle element. */
	ELEMENT_CIRCLE("circle"), //$NON-NLS-1$

	/** The defs element. */
	ELEMENT_DEFS("defs"), //$NON-NLS-1$

	/** The group element. */
	ELEMENT_GROUP("g"), //$NON-NLS-1$

	/** The line element. */
	ELEMENT_LINE("line"), //$NON-NLS-1$

	/** The path element. */
	ELEMENT_PATH("path"), //$NON-NLS-1$

	/** The root element. */
	ELEMENT_SVG("svg"), //$NON-NLS-1$

	/** The use element. */
	ELEMENT_USE("use"), //$NON-NLS-1$

	/** The marker element. */
	ELEMENT_MARKER("marker"), //$NON-NLS-1$

	/** color value for black. */
	VALUE_COLOR_BLACK("black"), //$NON-NLS-1$

	/** string format value for rotate transformations. */
	VALUE_TRANSFORM_FUNC_ROTATE("rotate(%f) "), //$NON-NLS-1$

	/** string format value for translate transformations. */
	VALUE_TRANSFORM_FUNC_TRANSLATE("translate(%f,%f) "), //$NON-NLS-1$

	/** reference to an url within the document. */
	VALUE_REF_URL("url(#%s)"), //$NON-NLS-1$

	/** reference to an element within the document. */
	VALUE_REF("#%s"), //$NON-NLS-1$

	/** The stroke width style value. */
	VALUE_STYLE_STROKE("stroke:%s;"), //$NON-NLS-1$
	/** The stroke width style value. */
	VALUE_STYLE_STROKE_WIDTH("stroke-width:%d;"), //$NON-NLS-1$
	/** The stroke width style value. */
	VALUE_STYLE_STROKE_DASHARRAY("stroke-dasharray:%s;"), //$NON-NLS-1$
	/** The element color. */
	VALUE_STYLE_FILL("fill:%s;"), //$NON-NLS-1$

	/** rgb value */
	VALUE_RGB("rgb(%d,%d,%d)"), //$NON-NLS-1$
	/** hex value */
	VALUE_HEX("#%x"), //$NON-NLS-1$

	/** user space unit value for markers. */
	VALUE_MARKER_UNITS_USERSPACE("userSpaceOnUse"), //$NON-NLS-1$
	/** marker viewbox w/ just height/width. */
	VALUE_MARKER_VIEWBOX_HW("0 0 %d %d"), //$NON-NLS-1$

	;

	/** the name of the xml tag. */
	private final String name;

	/** Constructor. */
	ESvg(String name)
	{
		this.name = name;
	}

	/** @return the name of the xml tag. */
	@Override
	public String $(Object... format)
	{
		return INameEnum.Impl.$(this.name, format);
	}
}
