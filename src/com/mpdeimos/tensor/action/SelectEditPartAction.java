package com.mpdeimos.tensor.action;

import java.awt.BasicStroke;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Stroke;
import java.awt.event.MouseEvent;
import java.awt.geom.Rectangle2D;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

import resources.R;

import com.mpdeimos.tensor.editpart.IEditPart;
import com.mpdeimos.tensor.editpart.IMovableEditPart;
import com.mpdeimos.tensor.editpart.IRotatableEditPart;
import com.mpdeimos.tensor.ui.DrawingCanvas;
import com.mpdeimos.tensor.util.Log;
import com.mpdeimos.tensor.util.PointUtil;

/**
 * Action for drawing tensors
 * 
 * @author mpdeimos
 *
 */
public class SelectEditPartAction extends CanvasActionBase {
	/** The currently selected EditPart. */
	private IEditPart selectedEditPart;

	/** The offset to the EditPart position when in moving mode. */
	private Dimension moveStartPointDelta;
	
	/** The Point where the rotation indicator is shown */
	private Point rotationIndicator;
	
	/** The offset to the rotation indicator when in rotating mode. */
	private Dimension rotationStartPointDelta;
	
	/** The stroke of selection rectangle. */
	private static BasicStroke EDITPART_SELECTION_STROKE = new BasicStroke(1.0f, BasicStroke.CAP_SQUARE, BasicStroke.JOIN_MITER, 1, new float[] {3f , 3f}, 0);
	
	/** the offset of the selection rectangle. */
	private static int EDITPART_SELECTION_STROKE_OFFSET = 3;


	/**
	 * Constructor.
	 */
	public SelectEditPartAction(DrawingCanvas drawingPanel)
	{
		super(drawingPanel, R.strings.getString("window_action_select"), new ImageIcon(R.drawable.getURL("select"))); //$NON-NLS-1$ //$NON-NLS-2$
	}
	
	@Override
	public void stopAction() {
		super.stopAction();

		// reset some stuff
		this.selectedEditPart = null;
		this.moveStartPointDelta = null;
	}

	@Override
	public boolean doOnMouseMove(MouseEvent e) {
		
		if (selectedEditPart != null)
		{
			Rectangle r = selectedEditPart.getBoundingRectangle();
			
			if (rotationIndicator != null)
			{
				if (hasHitRotationIndicator(r, e))
				{
					drawingPanel.setCursor(new Cursor(Cursor.CROSSHAIR_CURSOR));
					drawingPanel.repaint();
					return true;
				}
			}
			
			if (selectedEditPart.getBoundingRectangle().contains(e.getPoint()))
			{
				drawingPanel.setCursor(new Cursor(Cursor.MOVE_CURSOR));
				drawingPanel.repaint();
				return true;
			}
		}
		
		
		boolean over = false;
		for (IEditPart editPart : drawingPanel.getEditParts())
		{
			over |= editPart.isMouseOver(e.getPoint());
		}
		
		if (over)
			drawingPanel.setCursor(new Cursor(Cursor.HAND_CURSOR));
		else
			drawingPanel.setCursor(Cursor.getDefaultCursor());
		
		drawingPanel.repaint();
		return true;
	}

	/** determines whether the rotation indicator has been hit by this mouse event. */
	private boolean hasHitRotationIndicator(Rectangle r, MouseEvent e) {
		return rotationIndicator.distance(e.getPoint().x - r.getCenterX(), e.getPoint().y - r.getCenterY()) < 10;
	}

	@Override
	public boolean doOnMousePressed(MouseEvent e) {
		super.doOnMousePressed(e);
		
		if (e.getButton() == MouseEvent.BUTTON1)
		{
			if (selectedEditPart != null && rotationIndicator != null
					&& selectedEditPart instanceof IRotatableEditPart)
			{
				Rectangle r = selectedEditPart.getBoundingRectangle();
				
				if (hasHitRotationIndicator(r, e))
				{
					Point curPos = new Point(e.getPoint());
					PointUtil.move(curPos, -(int)r.getCenterX(), -(int)r.getCenterY());
					rotationStartPointDelta = PointUtil.getDelta(curPos, rotationIndicator);
					return true;
				}
			}
			
			if (selectedEditPart != null 
					&& selectedEditPart instanceof IMovableEditPart
					&& selectedEditPart.getBoundingRectangle().contains(e.getPoint()))
			{
				Point curPos = ((IMovableEditPart)selectedEditPart).getPosition();
				moveStartPointDelta = PointUtil.getDelta(curPos, e.getPoint());
			}
			else
			{
				selectedEditPart = null;
				for (IEditPart editPart : drawingPanel.getEditParts())
				{
					if(editPart.isMouseOver(e.getPoint()))
					{
						selectedEditPart = editPart;
						
						if (selectedEditPart instanceof IRotatableEditPart)
						{
							Rectangle r = selectedEditPart.getBoundingRectangle();
							IRotatableEditPart rotatableEditPart = (IRotatableEditPart) selectedEditPart;
							updateRoatationIndicator(r, rotatableEditPart.getRotation());
						}
						else
						{
							rotationIndicator = null;
						}
						
						// call us again, so we can do simple drag'n'drop
						return doOnMousePressed(e);
					}
				}
			}
			
			drawingPanel.repaint();
			return true;
		}
		
		
		return false;
	}
	
	@Override
	public boolean doOnMouseDragged(MouseEvent e)
	{
		if (selectedEditPart != null 
				&& selectedEditPart instanceof IMovableEditPart
				&& moveStartPointDelta != null)
		{
			Point curPos = e.getPoint();
			curPos.translate(moveStartPointDelta.width, moveStartPointDelta.height);
			((IMovableEditPart)selectedEditPart).setPosition(curPos);
			return true;
		}
		
		if (selectedEditPart != null 
				&& selectedEditPart instanceof IRotatableEditPart
				&& rotationStartPointDelta != null)
		{
			Rectangle r = selectedEditPart.getBoundingRectangle();
			
			Point curPos = e.getPoint();
			curPos.translate(-(int)r.getCenterX()+rotationStartPointDelta.width, -(int)r.getCenterY()+rotationStartPointDelta.height);
//			((IMovableEditPart)selectedEditPart).setPosition(curPos);
			double ang = Math.atan(curPos.getY()/curPos.getX())/Math.PI*180 + 45;
			if (curPos.getX() < 0)
				ang += 180;
			
			updateRoatationIndicator(r, ang);
			((IRotatableEditPart)selectedEditPart).setRotation(ang);
			
			
			return true;
		}
		
		return false;
	}

	/** updates the rotation indicator */
	private void updateRoatationIndicator(Rectangle r, double degrees) {
		rotationIndicator = new Point(((int)r.getWidth()/2)+EDITPART_SELECTION_STROKE_OFFSET,
				(-(int)r.getHeight()/2)-EDITPART_SELECTION_STROKE_OFFSET);

		PointUtil.rotate(rotationIndicator, degrees/180*Math.PI);
	}

	@Override
	public boolean doOnMouseReleased(MouseEvent e) {
		super.doOnMousePressed(e);
		
		moveStartPointDelta = null;
		
		return false;
	}

	@Override
	public boolean drawOverlay(Graphics2D gfx)
	{
		if (selectedEditPart != null)
		{
			Stroke s = gfx.getStroke();
			gfx.setStroke(EDITPART_SELECTION_STROKE);
			Rectangle r = selectedEditPart.getBoundingRectangle();
			Rectangle2D rect = new Rectangle2D.Double(
					r.getX()-EDITPART_SELECTION_STROKE_OFFSET+0.5,
					r.getY()-EDITPART_SELECTION_STROKE_OFFSET+0.5,
					r.getWidth()+2*EDITPART_SELECTION_STROKE_OFFSET,
					r.getHeight()+2*EDITPART_SELECTION_STROKE_OFFSET);
			gfx.draw(rect);
			
			gfx.setStroke(s);
			
			
			if (selectedEditPart instanceof IRotatableEditPart
					&& rotationIndicator != null)
			{
				try {
					Image img = ImageIO.read(R.drawable.getURL("circle-green")); //$NON-NLS-1$
					
					gfx.drawImage(img, (int)r.getCenterX() + rotationIndicator.x - 8, (int)r.getCenterY() + rotationIndicator.y - 8, null);
				} catch (IOException e) {
					Log.e(this, "Could not load image"); //$NON-NLS-1$
				}
			}
			return true;
		}
		
		return false;
	}
}
