package com.mpdeimos.tensor.ui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionAdapter;
import java.awt.event.MouseMotionListener;
import java.awt.geom.Ellipse2D;

import javax.swing.JPanel;

import com.mpdeimos.tensor.util.Log;
import javax.swing.border.EtchedBorder;

/**
 * Holds the drawing canvas for our diagram.
 * 
 * @author mpdeimos
 */
public class DrawingCanvas extends JPanel {
	/** log tag */
	public static final String LOG_TAG = "DrawingCanvas"; //$NON-NLS-1$
	
	/** just for testing purpose - moving stuff */
	private int x, y = 0;

	/**
	 * Create the panel.
	 */
	public DrawingCanvas() {
        setBackground(Color.white);
        addMouseMotionListener(new MouseMotionListener());
        addMouseListener(new MouseListener());
	}
	
    public void paint(Graphics g) {
    	super.paint(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        Dimension d = getSize();
        
        g2.draw(new Ellipse2D.Double(x, y, 20, 20));
       
    }

    /**
     * listener class for mouse motion events
     */
    private class MouseMotionListener extends MouseMotionAdapter
    {
    	@Override
    	public void mouseMoved(MouseEvent e) {
    		super.mouseMoved(e);
    		
    		x = e.getX();
    		y = e.getY();
    		
    		DrawingCanvas.this.repaint();
    	}
    }
    
    /**
     * listener class for mouse events
     */
    private class MouseListener extends MouseAdapter
    {
    	// nothing todo yet
    }
}
