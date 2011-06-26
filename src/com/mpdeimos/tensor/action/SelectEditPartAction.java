package com.mpdeimos.tensor.action;

import java.awt.BasicStroke;
import java.awt.Cursor;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Stroke;
import java.awt.event.MouseEvent;
import java.awt.geom.Rectangle2D;

import javax.swing.ImageIcon;

import resources.R;

import com.mpdeimos.tensor.editpart.IEditPart;
import com.mpdeimos.tensor.editpart.feature.IFeature;
import com.mpdeimos.tensor.editpart.feature.IFeatureEditPart;
import com.mpdeimos.tensor.ui.DrawingCanvas;

/**
 * Action for drawing tensors
 * 
 * @author mpdeimos
 *
 */
public class SelectEditPartAction extends CanvasActionBase {
	/** The currently selected EditPart. */
	private IEditPart selectedEditPart;
	
	/** The currently highlighted EditPart. */
	private IEditPart highlightedEditPart;
	
	/** The stroke of selection rectangle. */
	private static BasicStroke EDITPART_SELECTION_STROKE = new BasicStroke(1.0f, BasicStroke.CAP_SQUARE, BasicStroke.JOIN_MITER, 1, new float[] {3f , 3f}, 0);
	
	/** the offset of the selection rectangle. */
	public static int EDITPART_SELECTION_STROKE_OFFSET = 3;

	/**
	 * Constructor.
	 */
	public SelectEditPartAction(DrawingCanvas canvas)
	{
		super(canvas, R.strings.getString("window_action_select"), new ImageIcon(R.drawable.getURL("select"))); //$NON-NLS-1$ //$NON-NLS-2$
	}
	
	@Override
	public void stopAction() {
		super.stopAction();

		// reset some stuff
		this.selectedEditPart = null;
		canvas.setCursor(Cursor.getDefaultCursor());
	}

	@Override
	public boolean doOnMouseMoved(MouseEvent e) {
		super.doOnMouseMoved(e);
		
		if (highlightedEditPart != null)
			highlightedEditPart.setHighlighted(false);
		
		if (handleFeaturesForMouseEvent(e, MouseEvent.MOUSE_MOVED))
			return true;
		
		highlightedEditPart = null;
		for (IEditPart editPart : canvas.getEditParts())
		{
			if (editPart == selectedEditPart)
				continue;
			
			boolean tmpOver = isMouseOver(editPart, e.getPoint());
			
			if (tmpOver && highlightedEditPart == null)
				highlightedEditPart = editPart;
			
			editPart.setHighlighted(false);
		}
		
		if (highlightedEditPart != null)
		{
			highlightedEditPart.setHighlighted(true);
			canvas.setCursor(new Cursor(Cursor.HAND_CURSOR));
		}
		else
			canvas.setCursor(Cursor.getDefaultCursor());
		
		canvas.repaint();
		return true;
	}

	@Override
	public boolean doOnMousePressed(MouseEvent e) {
		super.doOnMousePressed(e);
		
		if (selectedEditPart != null)
			selectedEditPart.setHighlighted(false);

		if (handleFeaturesForMouseEvent(e, MouseEvent.MOUSE_PRESSED))
			return true;
		
		if (e.getButton() == MouseEvent.BUTTON1)
		{
			if (selectedEditPart != null)
				selectedEditPart.setSelected(false);
			
			selectedEditPart = null;
			for (IEditPart editPart : canvas.getEditParts())
			{
				if(isMouseOver(editPart, e.getPoint()))
				{
					selectedEditPart = editPart;
					selectedEditPart.setSelected(true);
					
					canvas.repaint();
					
					handleFeaturesForMouseEvent(e, MouseEvent.MOUSE_PRESSED);
					
					return true;
				}
			}
			
			return false;
		}
		
		return false;
	}

	@Override
	public boolean doOnMouseDragged(MouseEvent e)
	{
		super.doOnMouseDragged(e);
		
		if (handleFeaturesForMouseEvent(e, MouseEvent.MOUSE_DRAGGED))
			return true;
		
		return false;
	}

	@Override
	public boolean doOnMouseReleased(MouseEvent e) {
		super.doOnMousePressed(e);
		
		if (handleFeaturesForMouseEvent(e, MouseEvent.MOUSE_RELEASED))
			return true;
		
		return false;
	}
	
	/** Handles the feature actions for a specific mouse event. */
	private boolean handleFeaturesForMouseEvent(MouseEvent e, int which) {
		if (selectedEditPart != null && selectedEditPart instanceof IFeatureEditPart)
		{
			IFeatureEditPart featureEditPart = (IFeatureEditPart) selectedEditPart;
			for (IFeature feature : featureEditPart.getFeatures())
			{
				boolean handled = false;
				switch (which)
				{
				case MouseEvent.MOUSE_CLICKED:
					handled = feature.doOnMouseClicked(canvas, e);
					break;
				case MouseEvent.MOUSE_DRAGGED:
					handled = feature.doOnMouseDragged(canvas, e);
					break;
				case MouseEvent.MOUSE_MOVED:
					handled = feature.doOnMouseMoved(canvas, e);
					break;
				case MouseEvent.MOUSE_PRESSED:
					handled = feature.doOnMousePressed(canvas, e);
					break;
				case MouseEvent.MOUSE_RELEASED:
					handled = feature.doOnMouseReleased(canvas, e);
					break;
				default:
					handled = false;
					break;
				}
				
				if (handled)
					return true;
			}
		}
		
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
			
			if (selectedEditPart != null && selectedEditPart instanceof IFeatureEditPart)
			{
				IFeatureEditPart featureEditPart = (IFeatureEditPart) selectedEditPart;
				for (IFeature feature : featureEditPart.getFeatures())
				{
					feature.drawOverlay(canvas, gfx);
				}
			}
			
			return true;
		}
		
		return false;
	}
	
	/** Tests whether the mouse is over a given EditPart. */
	private boolean isMouseOver(IEditPart editPart, Point point)
	{
		int offset = 2;
		Rectangle rect = new Rectangle(point.x - offset, point.y - offset, 4 , 4);
		return editPart.intersects(rect);
	}
}
