/**
 * 
 */
package com.mpdeimos.tensor.util;

import static org.junit.Assert.assertEquals;

import java.awt.geom.Point2D;

import org.junit.Test;

/**
 * @author mpdeimos
 * 
 */
public class VecMathTest
{
	/** Epsilon threshold. */
	private static final double EPSILON = 0.001;
	/** (0,0) */
	final Point2D z0z0 = VecMath.fresh(0, 0);
	/** (0,1) */
	final Point2D z0p1 = VecMath.fresh(0, 1);
	/** (1,1) */
	final Point2D p1p1 = VecMath.fresh(1, 1);
	/** (1,0) */
	final Point2D p1z0 = VecMath.fresh(1, 0);
	/** (-1,1) */
	final Point2D n1p1 = VecMath.fresh(-1, 1);
	/** (-1,0) */
	final Point2D n1z0 = VecMath.fresh(-1, 0);
	/** (-1,-1) */
	final Point2D n1n1 = VecMath.fresh(-1, -1);
	/** (0,-1) */
	final Point2D z0n1 = VecMath.fresh(0, -1);
	/** (1,-1) */
	final Point2D p1n1 = VecMath.fresh(1, -1);

	/**
	 * Test method for
	 * {@link com.mpdeimos.tensor.util.VecMath#dot(java.awt.geom.Point2D, java.awt.geom.Point2D)}
	 * .
	 */
	@Test
	public void testDot()
	{
		assertEquals(0, VecMath.dot(this.z0z0, this.z0z0), EPSILON);
		assertEquals(0, VecMath.dot(this.z0z0, this.p1p1), EPSILON);
		assertEquals(2, VecMath.dot(this.p1p1, this.p1p1), EPSILON);
		assertEquals(0, VecMath.dot(this.p1p1, this.p1n1), EPSILON);
		assertEquals(-2, VecMath.dot(this.p1p1, this.n1n1), EPSILON);
	}

	/**
	 * Test method for
	 * {@link com.mpdeimos.tensor.util.VecMath#ang(java.awt.geom.Point2D, java.awt.geom.Point2D)}
	 * .
	 */
	@Test
	public void testAng()
	{
		assertEquals(0, radToGrad(VecMath.ang(this.p1z0, this.p1z0)), EPSILON);
		assertEquals(45, radToGrad(VecMath.ang(this.p1z0, this.p1p1)), EPSILON);
		assertEquals(90, radToGrad(VecMath.ang(this.p1z0, this.z0p1)), EPSILON);
		assertEquals(135, radToGrad(VecMath.ang(this.p1z0, this.n1p1)), EPSILON);
		assertEquals(180, radToGrad(VecMath.ang(this.p1z0, this.n1z0)), EPSILON);
		assertEquals(225, radToGrad(VecMath.ang(this.p1z0, this.n1n1)), EPSILON);
		assertEquals(270, radToGrad(VecMath.ang(this.p1z0, this.z0n1)), EPSILON);
		assertEquals(315, radToGrad(VecMath.ang(this.p1z0, this.p1n1)), EPSILON);

		assertEquals(270, radToGrad(VecMath.ang(this.n1p1, this.p1p1)), EPSILON);
	}

	/** @return the grad value of an angle in radians. */
	private static double radToGrad(double rad)
	{
		return 180 * rad / Math.PI;
	}

}
