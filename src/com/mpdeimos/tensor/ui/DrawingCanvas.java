package com.mpdeimos.tensor.ui;

import com.mpdeimos.tensor.action.canvas.ICanvasAction;
import com.mpdeimos.tensor.editpart.EditPartFactory;
import com.mpdeimos.tensor.editpart.IEditPart;
import com.mpdeimos.tensor.model.IModelChangedListener;
import com.mpdeimos.tensor.model.IModelData;
import com.mpdeimos.tensor.util.Log;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import javax.swing.JPanel;
import javax.swing.event.MouseInputAdapter;

/**
 * Holds the drawing canvas for our diagram.
 * 
 * @author mpdeimos
 */
public class DrawingCanvas extends JPanel
{

	/** the EditPart factory */
	private final EditPartFactory editPartFactory = new EditPartFactory();

	/** list of all known EditParts. */
	private final List<IEditPart> editParts = new ArrayList<IEditPart>();

	/** the current drawing action */
	private ICanvasAction canvasAction = null;

	/** mouse event listener */
	private final MouseListener mouseListener;

	/** key event listener. */
	private final KeyListener keyListener;

	/** the linked application window. */
	private final Application appWindow;

	/** the model change listener. */
	private final IModelChangedListener modelChangedListener;

	/**
	 * Create the panel.
	 */
	public DrawingCanvas(Application appWindow)
	{
		this.appWindow = appWindow;
		setBackground(Color.white);
		this.mouseListener = new MouseListener();
		this.keyListener = new KeyListener();
		this.modelChangedListener = new ModelChangedListener();

		addMouseMotionListener(this.mouseListener);
		addMouseListener(this.mouseListener);
		addKeyListener(this.keyListener);

		onModelExchanged();
	}

	@Override
	public boolean isFocusable()
	{
		return true;
	}

	@Override
	public void paintComponent(Graphics g)
	{
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

		for (IEditPart part : this.editParts)
		{
			part.draw(gfx);
		}

		if (this.canvasAction != null)
		{
			this.canvasAction.drawOverlay(gfx);
		}
	}

	/**
	 * listener class for mouse events
	 */
	private class MouseListener extends MouseInputAdapter
	{
		@Override
		public void mouseMoved(MouseEvent e)
		{
			super.mouseMoved(e);

			if (DrawingCanvas.this.canvasAction != null)
			{
				DrawingCanvas.this.canvasAction.doOnMouseMoved(e);
			}
		}

		@Override
		public void mouseClicked(MouseEvent e)
		{
			super.mouseClicked(e);

			if (DrawingCanvas.this.canvasAction != null)
			{
				if (DrawingCanvas.this.canvasAction.doOnMouseClicked(e))
					return;
			}

			if (e.getButton() == MouseEvent.BUTTON3)
			{
				stopCanvasAction();
			}
		}

		@Override
		public void mousePressed(MouseEvent e)
		{
			super.mousePressed(e);

			DrawingCanvas.this.requestFocusInWindow();

			if (DrawingCanvas.this.canvasAction != null)
			{
				DrawingCanvas.this.canvasAction.doOnMousePressed(e);
			}
		}

		@Override
		public void mouseReleased(MouseEvent e)
		{
			super.mouseReleased(e);

			if (DrawingCanvas.this.canvasAction != null)
			{
				DrawingCanvas.this.canvasAction.doOnMouseReleased(e);
			}
		}

		@Override
		public void mouseDragged(MouseEvent e)
		{
			super.mouseDragged(e);

			if (DrawingCanvas.this.canvasAction != null)
			{
				DrawingCanvas.this.canvasAction.doOnMouseDragged(e);
			}
		}

		@Override
		public void mouseEntered(MouseEvent e)
		{
			super.mouseDragged(e);

			if (DrawingCanvas.this.canvasAction != null)
			{
				DrawingCanvas.this.canvasAction.doOnMouseEntered(e);
			}
		}

		@Override
		public void mouseExited(MouseEvent e)
		{
			super.mouseDragged(e);

			if (DrawingCanvas.this.canvasAction != null)
			{
				DrawingCanvas.this.canvasAction.doOnMouseExited(e);
			}
		}
	}

	/**
	 * listener class for model data changes
	 */
	private class ModelChangedListener implements IModelChangedListener
	{

		@Override
		public void onModelChanged(IModelData model)
		{
			repaint();
		}

		@Override
		public void onChildAdded(IModelData child)
		{
			IEditPart part = DrawingCanvas.this.editPartFactory.createEditPart(child);
			if (part != null)
				DrawingCanvas.this.editParts.add(part);

			repaint();
		}

		@Override
		public void onChildRemoved(IModelData child)
		{
			List<IEditPart> toBeRemoved = new LinkedList<IEditPart>();
			for (IEditPart part : DrawingCanvas.this.editParts)
			{
				if (part.getModel() == child) // ref comp ok here
				{
					toBeRemoved.add(part);
				}
			}

			DrawingCanvas.this.editParts.removeAll(toBeRemoved);

			stopCanvasAction();

			repaint();
		}
	}

	/** Listener class for key events. */
	private class KeyListener extends KeyAdapter
	{
		@Override
		public void keyPressed(KeyEvent e)
		{
			if (DrawingCanvas.this.canvasAction != null)
			{
				DrawingCanvas.this.canvasAction.doOnKeyPressed(e);
			}
		}
	}

	/** performs an action on the canvas */
	public void startCanvasAction(ICanvasAction action)
	{
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
		if (this.stopCanvasActionMyChange || this.canvasAction == null)
			return;

		Log.d(this, "Stopped canvas action: %s", this.canvasAction); //$NON-NLS-1$

		this.stopCanvasActionMyChange = true;
		this.canvasAction.stopAction();
		if (startDefault)
			this.appWindow.startDefaultToolbarAction();
		this.stopCanvasActionMyChange = false;

		repaint();
	}

	/** @return the edit parts */
	public List<IEditPart> getEditParts()
	{
		return this.editParts;
	}

	/** @return the EditPartFactory of the canvas. */
	public EditPartFactory getEditPartFactory()
	{
		return this.editPartFactory;
	}

	/** Called every time the model got exchanged. */
	public void onModelExchanged()
	{
		this.editParts.clear();

		this.appWindow.getModel().addModelChangedListener(
				this.modelChangedListener);

		stopCanvasAction();
		repaint();
	}
}
