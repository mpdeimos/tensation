package com.mpdeimos.tensor.ui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;
import javax.swing.event.MouseInputAdapter;

import com.mpdeimos.tensor.action.ICanvasAction;
import com.mpdeimos.tensor.editpart.EditPartFactory;
import com.mpdeimos.tensor.editpart.IEditPart;
import com.mpdeimos.tensor.model.EpsilonTensor;
import com.mpdeimos.tensor.model.IModelChangedListener;
import com.mpdeimos.tensor.model.IModelData;
import com.mpdeimos.tensor.model.ModelRoot;
import com.mpdeimos.tensor.util.Log;

/**
 * Holds the drawing canvas for our diagram.
 * 
 * @author mpdeimos
 */
public class DrawingCanvas extends JPanel {

	/** the EditPart factory */
	private EditPartFactory editPartFactory = new EditPartFactory();
	
	/** list of all known EditParts. */
	private List<IEditPart> editParts = new ArrayList<IEditPart>();

	/** the current drawing action */
	private ICanvasAction canvasAction = null;
	
	/** root model */
	private ModelRoot root;

	/** mouse event listener */
	private MouseListener mouseListener;

	/** the linked application window. */
	private final ApplicationWindow appWindow;
	
	/**
	 * Create the panel.
	 */
	public DrawingCanvas(ApplicationWindow appWindow) {
        this.appWindow = appWindow;
		setBackground(Color.white);
        mouseListener = new MouseListener();
        addMouseMotionListener(mouseListener);
        addMouseListener(mouseListener);
        
        root = new ModelRoot();
        root.addModelChangedListener(new ModelChangedListener());
        
        root.addChild(new EpsilonTensor(root, new Point(30, 30)));
        root.addChild(new EpsilonTensor(root, new Point(80, 50)));
	}
	
	@Override
	public void paintComponent(Graphics g) {
    	super.paintComponent(g);
        Graphics2D gfx = (Graphics2D) g;
        gfx.setRenderingHint(
        		RenderingHints.KEY_ANTIALIASING,
        		RenderingHints.VALUE_ANTIALIAS_ON);
        gfx.setRenderingHint(
                RenderingHints.KEY_TEXT_ANTIALIASING,
                RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        gfx.setRenderingHint(
                RenderingHints.KEY_FRACTIONALMETRICS,
                RenderingHints.VALUE_FRACTIONALMETRICS_ON);
        gfx.setRenderingHint(
                RenderingHints.KEY_STROKE_CONTROL,
                RenderingHints.VALUE_STROKE_PURE);
        
        for (IEditPart part : editParts)
        {
        	part.draw(gfx);
        }
        
        if (canvasAction != null)
        {
        	canvasAction.drawOverlay(gfx);
        }
    }

    /**
     * listener class for mouse events
     */
    private class MouseListener extends MouseInputAdapter
    {
    	@Override
    	public void mouseMoved(MouseEvent e) {
    		super.mouseMoved(e);
    		
    		if (canvasAction != null)
    		{
    			canvasAction.doOnMouseMoved(e);
    		}
    	}

    	@Override
    	public void mouseClicked(MouseEvent e) {
    		super.mouseClicked(e);
    		
    		if (canvasAction != null)
    		{
    			if (canvasAction.doOnMouseClicked(e))
    				return;
    		}

    		if (e.getButton() == MouseEvent.BUTTON3)
    		{
    			stopCanvasAction();
    		}
    	}
    	
    	@Override
    	public void mousePressed(MouseEvent e) {
    		super.mousePressed(e);
    		
    		if (canvasAction != null)
    		{
    			canvasAction.doOnMousePressed(e);
    		}
    	}
    	
    	@Override
    	public void mouseReleased(MouseEvent e) {
    		super.mouseReleased(e);
    		
    		if (canvasAction != null)
    		{
    			canvasAction.doOnMouseReleased(e);
    		}
    	}
    	
    	@Override
    	public void mouseDragged(MouseEvent e) {
    		super.mouseDragged(e);
    		
    		if (canvasAction != null)
    		{
    			canvasAction.doOnMouseDragged(e);
    		}
    	}
    }
    
    /**
     * listener class for model data changes
     */
    private class ModelChangedListener implements IModelChangedListener
    {

		@Override
		public void onModelChanged(IModelData model) {
			repaint();
		}

		@Override
		public void onChildAdded(IModelData child) {
			Log.v(DrawingCanvas.this, "added new child to model"); //$NON-NLS-1$
			
			IEditPart part = editPartFactory.createEditPart(child);
			if (part != null)
				editParts.add(part);
			
			repaint();
		}

		@Override
		public void onChildRemoved(IModelData child) {
			// TODO !!
		}
    }

    /** performs an action on the canvas */ 
	public void startCanvasAction(ICanvasAction action) {
		stopCanvasAction(false);
		
		this.canvasAction = action;
		Log.d(this, "Started canvas action: %s", this.canvasAction); //$NON-NLS-1$
	}
	
	/** flag for determining if we've stopped the current action */
	private boolean stopCanvasActionMyChange = false;
	
	/** stops the current canvas action */
	public void stopCanvasAction()
	{
		stopCanvasAction(true);
	}
	
	/** stops the current canvas action */
	private void stopCanvasAction(boolean startDefault)
	{
		if (stopCanvasActionMyChange || this.canvasAction == null)
			return;
		
		Log.d(this, "Stopped canvas action: %s", this.canvasAction); //$NON-NLS-1$
		
		stopCanvasActionMyChange = true;
		this.canvasAction.stopAction();
		if (startDefault)
			this.appWindow.startDefaultToolbarAction();
		stopCanvasActionMyChange = false;
		
		repaint();
	}
	
	/** @return the model */
	public ModelRoot getModel()
	{
		// FIXME this is really bad practice!
		return root;
	}
	
	/** @return the edit parts */
	public List<IEditPart> getEditParts()
	{
		return this.editParts;
	}
}
