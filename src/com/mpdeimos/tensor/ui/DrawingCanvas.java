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

import javax.swing.AbstractAction;
import javax.swing.JPanel;

import com.mpdeimos.tensor.action.DrawTensorAction;
import com.mpdeimos.tensor.editpart.EditPartFactory;
import com.mpdeimos.tensor.editpart.IEditPart;
import com.mpdeimos.tensor.figure.IFigure;
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
	private AbstractAction action = null;
	
	/** the EditPart corresponding to the current action TODO maybe create action base class that 'knows' what to do */
	private IEditPart actionPart = null;

	/** root model */
	private ModelRoot root;
	
	/**
	 * Create the panel.
	 */
	public DrawingCanvas() {
        setBackground(Color.white);
        addMouseMotionListener(new MouseMotionListener());
        addMouseListener(new MouseListener());
        
        root = new ModelRoot();
        root.addModelChangedListener(new ModelChangedListener());
        
        root.addChild(new EpsilonTensor(new Point(30, 30)));
        root.addChild(new EpsilonTensor(new Point(80, 50)));
	}
	
	@Override
	public void paint(Graphics g) {
    	super.paint(g);
        Graphics2D gfx = (Graphics2D) g;
        gfx.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        for (IEditPart part : editParts)
        {
        	part.draw(gfx);
        }
        
        if (actionPart != null)
        {
        	actionPart.draw(gfx);
        }
    }

    /**
     * listener class for mouse motion events
     */
    private class MouseMotionListener extends MouseMotionAdapter
    {
    	@Override
    	public void mouseMoved(MouseEvent e) {
    		super.mouseMoved(e);
    		
    		if (action instanceof DrawTensorAction)
    		{
	    		actionPart = editPartFactory.createEditPart(new EpsilonTensor(e.getPoint()));
    		}
    		else
    		{
    			actionPart = null;
    		}
    		DrawingCanvas.this.repaint();
    	}
    }
    
    /**
     * listener class for mouse events
     */
    private class MouseListener extends MouseAdapter
    {
    	@Override
    	public void mouseClicked(MouseEvent e) {
    		super.mouseClicked(e);
    		
    		if (e.getButton() == MouseEvent.BUTTON3)
    		{
    			action = null;
    			return;
    		}
    		
    		if (action instanceof DrawTensorAction && e.getButton() == MouseEvent.BUTTON1)
    		{
    			root.addChild(new EpsilonTensor(e.getPoint()));
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
	public void performAction(AbstractAction action) {
		this.action  = action;
	}
}
