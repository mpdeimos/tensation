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
			selectedEditPart.getBoundingRectangle();
			
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

	@Override
	public boolean doOnMousePressed(MouseEvent e) {
		super.doOnMousePressed(e);
		
		if (e.getButton() == MouseEvent.BUTTON1)
		{
			selectedEditPart = null;
			for (IEditPart editPart : drawingPanel.getEditParts())
			{
				if(editPart.isMouseOver(e.getPoint()))
				{
					selectedEditPart = editPart;
					break;
				}
			}
			
			if (selectedEditPart != null 
					&& selectedEditPart instanceof IMovableEditPart
					&& selectedEditPart.getBoundingRectangle().contains(e.getPoint()))
			{
				Point curPos = ((IMovableEditPart)selectedEditPart).getPosition();
				moveStartPointDelta = PointUtil.getDelta(curPos, e.getPoint());
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
		
		return false;
	}
	
	@Override
	public boolean doOnMouseReleased(MouseEvent e) {
		super.doOnMousePressed(e);
		
		moveStartPointDelta = null;
		
		return false;
	}

	@Override
	public boolean drawOverlay(Graphics2D gfx) {
		
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
			
			
			if (selectedEditPart instanceof IRotatableEditPart)
			{
				IRotatableEditPart rotatableEditPart = (IRotatableEditPart) selectedEditPart;
				try {
					Image img = ImageIO.read(R.drawable.getURL("circle-green")); //$NON-NLS-1$
					
					Point p = new Point(((int)r.getWidth()/2)+EDITPART_SELECTION_STROKE_OFFSET,
										(-(int)r.getHeight()/2)-EDITPART_SELECTION_STROKE_OFFSET);
					
					// FIXME
					PointUtil.rotate(p, rotatableEditPart.getRotation()/180*Math.PI);
					
					gfx.drawImage(img, (int)r.getCenterX() + p.x - 8, (int)r.getCenterY() + p.y - 8, null);
				} catch (IOException e) {
					Log.e(this, "Could not load image"); //$NON-NLS-1$
				}
			}
			return true;
		}
		
		return false;
	}
}
