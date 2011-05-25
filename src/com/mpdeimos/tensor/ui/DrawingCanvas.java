package com.mpdeimos.tensor.ui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
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
	/** log tag */
	public static final String LOG_TAG = "DrawingCanvas"; //$NON-NLS-1$
	
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
	
	/**
	 * Create the panel.
	 */
	public DrawingCanvas() {
        setBackground(Color.white);
        mouseListener = new MouseListener();
        addMouseMotionListener(mouseListener);
        addMouseListener(mouseListener);
        
        root = new ModelRoot();
        root.addModelChangedListener(new ModelChangedListener());
        
        root.addChild(new EpsilonTensor(new Point(30, 30)));
        root.addChild(new EpsilonTensor(new Point(80, 50)));
	}
	
	@Override
	public void paintComponent(Graphics g) {
    	super.paintComponent(g);
        Graphics2D gfx = (Graphics2D) g;
        gfx.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
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
    			canvasAction.doOnMouseMove(e);
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
    }
    
    /**
     * listener class for model data changes
     */
    private class ModelChangedListener implements IModelChangedListener
    {

		@Override
		public void onModelChanged(IModelData model) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onChildAdded(IModelData child) {
			Log.v(LOG_TAG, "added new child to model"); //$NON-NLS-1$
			
			IEditPart part = editPartFactory.createEditPart(child);
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
		this.canvasAction  = action;
		Log.d(LOG_TAG, "Started canvas action: %s", this.canvasAction); //$NON-NLS-1$
	}
	
	/** stops the current canvas action */
	public void stopCanvasAction()
	{
		Log.d(LOG_TAG, "Stopped canvas action: %s", this.canvasAction); //$NON-NLS-1$
		this.canvasAction = null;
		repaint();
	}
	
	/** @return the model */
	public ModelRoot getModel()
	{
		// FIXME this is really bad practice!
		return root;
	}
}
