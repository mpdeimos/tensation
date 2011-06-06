package com.mpdeimos.tensor.util;

import java.awt.geom.Point2D;

/**
 * Util class for points.
 * 
 * @author mpdeimos
 */
public class PointUtil {

	public static void rotate(Point2D p, double ang) {
		double sin = Math.sin(ang);
		double cos = Math.cos(ang);
		
		p.setLocation(
				p.getX()*cos  - p.getY()*sin,
				p.getX()*sin  + p.getY()*cos);
	}

	public static void move(Point2D p, int x, int y) {
		p.setLocation(p.getX() + x, p.getY() + y);
	}

}
