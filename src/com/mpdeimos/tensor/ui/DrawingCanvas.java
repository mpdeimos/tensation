package com.mpdeimos.tensor.ui;

import com.mpdeimos.tensor.action.canvas.ICanvasAction;
import com.mpdeimos.tensor.editpart.EditPartFactory;
import com.mpdeimos.tensor.editpart.IEditPart;
import com.mpdeimos.tensor.model.IModelChangedListener;
import com.mpdeimos.tensor.model.IModelData;
import com.mpdeimos.tensor.model.ModelRoot;
import com.mpdeimos.tensor.util.ImmutableList;
import com.mpdeimos.tensor.util.Log;
import com.mpdeimos.tensor.util.PointUtil;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.io.File;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import javax.swing.BoundedRangeModel;
import javax.swing.JPanel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.MouseInputAdapter;

import resources.R;

/**
 * Holds the drawing canvas for our diagram.
 * 
 * @author mpdeimos
 */
public class DrawingCanvas extends JPanel
{
	/** the model loaded for this canvas. */
	private ModelRoot model;

	/** the EditPart factory */
	private final EditPartFactory editPartFactory = new EditPartFactory();

	/** list of all known EditParts. */
	private final List<IEditPart> editParts = new ArrayList<IEditPart>();

	/** the export location of the model. */
	private File modelExportLocation;

	/** the current drawing action */
	private ICanvasAction canvasAction = null;

	/** mouse event listener */
	private final MouseListener mouseListener;

	/** key event listener. */
	private final KeyListener keyListener;

	/** the model change listener. */
	private final IModelChangedListener modelChangedListener;

	/** the scaling factor of the canvas. */
	private double canvasScale = 1.0;

	/** horizontal scroll bar model. */
	private final BoundedRangeModel hScrollModel;

	/** vertical scroll bar model. */
	private final BoundedRangeModel vScrollModel;

	/** Canvas resize listener. */
	private final ComponentListener componentListener;

	/** the undo manager of our app. */
	private final CanvasUndoManager undoManager;

	/** the number of instantiated canvases till program start. */
	private static int canvasCount = 0;

	/** The canvas instance number. */
	private final int instanceNum;

	/** The currently selected EditParts. */
	private final List<IEditPart> selectedEditParts = new ArrayList<IEditPart>();

	/** Maximum Canvas Size. */
	private static final short MAX_CANVAS_SIZE = 4000;

	/**
	 * Create the panel.
	 * 
	 * @param vScrollModel
	 * @param hScrollModel
	 */
	/* package */DrawingCanvas(DrawingCanvasHolder holder)
	{
		this.instanceNum = canvasCount++;

		this.model = new ModelRoot(this);
		this.undoManager = new CanvasUndoManager();

		setBackground(Color.WHITE);
		this.mouseListener = new MouseListener();
		this.keyListener = new KeyListener();
		this.modelChangedListener = new ModelChangedListener();
		this.componentListener = new ComponentListener();

		this.hScrollModel = holder.getHorizontalScrollModel();
		this.hScrollModel.setMaximum(MAX_CANVAS_SIZE / 2 + getWidth());
		this.hScrollModel.setMinimum(-MAX_CANVAS_SIZE / 2);
		this.hScrollModel.addChangeListener(new ChangeListener()
		{
			@Override
			public void stateChanged(ChangeEvent arg0)
			{
				repaint();
			}
		});

		this.vScrollModel = holder.getVerticalScrollModel();
		this.vScrollModel.setMaximum(MAX_CANVAS_SIZE / 2 + getHeight());
		this.vScrollModel.setMinimum(-MAX_CANVAS_SIZE / 2);
		this.vScrollModel.addChangeListener(new ChangeListener()
		{
			@Override
			public void stateChanged(ChangeEvent arg0)
			{
				repaint();
			}
		});

		addMouseMotionListener(this.mouseListener);
		addMouseListener(this.mouseListener);
		addMouseWheelListener(this.mouseListener);
		addKeyListener(this.keyListener);
		addComponentListener(this.componentListener);

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

		Graphics2D g2 = (Graphics2D) g;

		g2.scale(this.canvasScale, this.canvasScale);

		g2.translate(
				-this.hScrollModel.getValue(),
				-this.vScrollModel.getValue());

		render(g2, true);
	}

	/** Renders the canvas content to a given Graphics2D. */
	public void render(Graphics2D gfx, boolean overlays)
	{
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

		gfx.setColor(Color.BLACK);

		for (IEditPart part : this.editParts)
		{
			boolean highlighted = false;
			boolean selected = false;

			if (!overlays)
			{
				selected = part.isSelected();
				part.setSelected(false);
				highlighted = part.isHighlighted();
				part.setHighlighted(false);
			}

			part.draw(gfx);

			if (!overlays)
			{
				part.setHighlighted(highlighted);
				part.setSelected(selected);
			}
		}

		if (overlays && this.canvasAction != null)
		{
			this.canvasAction.drawOverlay(gfx);
		}
	}

	/**
	 * listener class for mouse events
	 */
	private class MouseListener extends MouseInputAdapter
	{
		/** creates a new mouse event with scaled point dimensions. */
		private MouseEvent createScaledMouseEvent(MouseEvent e)
		{
			Point p = PointUtil.scale(e.getPoint(),
					1 / DrawingCanvas.this.canvasScale);
			return new MouseEvent(
					(Component) e.getSource(),
					e.getID(),
					e.getWhen(),
					e.getModifiers(),
					p.x + DrawingCanvas.this.hScrollModel.getValue(),
					p.y + DrawingCanvas.this.vScrollModel.getValue(),
					e.getLocationOnScreen().x,
					e.getLocationOnScreen().y,
					e.getClickCount(),
					e.isPopupTrigger(),
					e.getButton());
		}

		@Override
		public void mouseMoved(MouseEvent e)
		{
			e = createScaledMouseEvent(e);
			super.mouseMoved(e);

			if (DrawingCanvas.this.canvasAction != null)
			{
				DrawingCanvas.this.canvasAction.doOnMouseMoved(e);
			}
		}

		@Override
		public void mouseClicked(MouseEvent e)
		{
			e = createScaledMouseEvent(e);

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
			e = createScaledMouseEvent(e);

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
			e = createScaledMouseEvent(e);

			super.mouseReleased(e);

			if (DrawingCanvas.this.canvasAction != null)
			{
				DrawingCanvas.this.canvasAction.doOnMouseReleased(e);
			}
		}

		@Override
		public void mouseDragged(MouseEvent e)
		{
			e = createScaledMouseEvent(e);

			super.mouseDragged(e);

			if (DrawingCanvas.this.canvasAction != null)
			{
				DrawingCanvas.this.canvasAction.doOnMouseDragged(e);
			}
		}

		@Override
		public void mouseEntered(MouseEvent e)
		{
			e = createScaledMouseEvent(e);

			super.mouseDragged(e);

			if (DrawingCanvas.this.canvasAction != null)
			{
				DrawingCanvas.this.canvasAction.doOnMouseEntered(e);
			}
		}

		@Override
		public void mouseExited(MouseEvent e)
		{
			e = createScaledMouseEvent(e);

			super.mouseDragged(e);

			if (DrawingCanvas.this.canvasAction != null)
			{
				DrawingCanvas.this.canvasAction.doOnMouseExited(e);
			}
		}

		@Override
		public void mouseWheelMoved(MouseWheelEvent e)
		{
			BoundedRangeModel m = DrawingCanvas.this.vScrollModel;
			if (e.isShiftDown())
				m = DrawingCanvas.this.hScrollModel;
			m.setValue(m.getValue()
					+ 5 * e.getUnitsToScroll());
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
			convertToEditPart(child);

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
					DrawingCanvas.this.removeSelectedEditPart(part);
				}
			}

			DrawingCanvas.this.editParts.removeAll(toBeRemoved);

			stopCanvasAction();

			repaint();
		}
	}

	/**
	 * Listener class for component changes.
	 */
	private class ComponentListener extends ComponentAdapter
	{
		@Override
		public void componentResized(ComponentEvent e)
		{
			super.componentResized(e);

			DrawingCanvas.this.hScrollModel.setMaximum(MAX_CANVAS_SIZE / 2
					+ DrawingCanvas.this.getWidth());
			DrawingCanvas.this.vScrollModel.setMaximum(MAX_CANVAS_SIZE / 2
					+ DrawingCanvas.this.getHeight());
			DrawingCanvas.this.hScrollModel.setExtent(DrawingCanvas.this.getWidth());
			DrawingCanvas.this.vScrollModel.setExtent(DrawingCanvas.this.getHeight());

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

		@Override
		public void keyReleased(KeyEvent e)
		{
			if (DrawingCanvas.this.canvasAction != null)
			{
				DrawingCanvas.this.canvasAction.doOnKeyReleased(e);
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

	/** restarts the last canvas action or the default toolbar action */
	public void startCanvasAction()
	{
		if (this.canvasAction == null)
		{
			Application.getApp().startDefaultToolbarAction();
		}
		else
		{
			this.canvasAction.actionPerformed(null);
		}
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
			Application.getApp().startDefaultToolbarAction();
		this.stopCanvasActionMyChange = false;

		repaint();
	}

	/** @return the edit parts */
	public List<IEditPart> getEditParts()
	{
		return this.editParts;
	}

	/**
	 * @param model
	 *            to search the editpart for.
	 * @return the editpart for a given model. null if not found.
	 */
	public IEditPart getEditPartForModelData(IModelData model)
	{
		for (IEditPart editPart : this.editParts)
		{
			if (editPart.getModel() == model) // ref comp
			{
				return editPart;
			}
		}
		return null;
	}

	/** @return the EditPartFactory of the canvas. */
	public EditPartFactory getEditPartFactory()
	{
		return this.editPartFactory;
	}

	/** Converts a data item to an edit part and adds it to the editpart list. */
	private void convertToEditPart(IModelData data)
	{
		IEditPart part = DrawingCanvas.this.editPartFactory.createEditPart(data);
		if (part != null)
			this.editParts.add(part);
	}

	/** Called every time the model got exchanged. */
	private void onModelExchanged()
	{
		this.editParts.clear();

		this.model.addModelChangedListener(
				this.modelChangedListener);

		for (IModelData data : this.model.getChildren())
		{
			convertToEditPart(data);
		}

		stopCanvasAction();
		repaint();
	}

	/** @return the size and offset of the editparts. */
	public Rectangle getImageRectangle()
	{
		Rectangle rect = null;

		for (IEditPart part : this.editParts)
		{
			Rectangle r = part.getBoundingRectangle();
			if (rect == null)
				rect = r;
			else
				rect.add(r);
		}

		if (rect == null)
		{
			rect = new Rectangle();
		}

		return rect;
	}

	/** sets the scaling factor of the canvas. */
	public void setScale(double scale)
	{
		this.canvasScale = scale;
		repaint();
	}

	/** @return the scaling of the canvas. */
	public double getScale()
	{
		return this.canvasScale;
	}

	/** Sets the canvas scrolling. */
	public void setScroll(int h, int v)
	{
		this.hScrollModel.setValue(h);
		this.vScrollModel.setValue(v);
	}

	/** @return the scroll position of the canvas. */
	public Point getScroll()
	{
		return new Point(
				this.hScrollModel.getValue(),
				this.vScrollModel.getValue());
	}

	/** @return the human readable name of this canvas. */
	@Override
	public String getName()
	{
		if (this.modelExportLocation != null)
			return this.modelExportLocation.getName();

		return String.format(
				R.string.WINDOW_MAIN_TAB.string(),
				1 + this.instanceNum);
	}

	/** @return the canvas undo manager. */
	public CanvasUndoManager getUndoManager()
	{
		return this.undoManager;
	}

	/** @return the main root model. */
	public ModelRoot getModel()
	{
		return this.model;
	}

	/** sets the current main root model. the export location is reset to null */
	public void setModel(ModelRoot model)
	{
		this.model = model;
		this.model.setDrawingCanvas(this);
		this.modelExportLocation = null;
		this.undoManager.discardAllEdits();
		onModelExchanged();
	}

	/** @return the export location of the model. may be null if not saved yet. */
	public File getModelExportLocation()
	{
		return this.modelExportLocation;
	}

	/**
	 * sets the filename of the current model export.
	 */
	public void setModelExportLocation(File location)
	{
		this.modelExportLocation = location;
		Application.getApp().setModelExportLocation(location);
		Application.getApp().updateActiveTabName();
	}

	/** marks the given editparts as selected and resets the previous selection. */
	public void setSelectedEditParts(List<IEditPart> parts)
	{
		clearSelectedEditParts();

		for (IEditPart part : parts)
		{
			addSelectedEditPart(part);
		}
	}

	/** Clears the EditPart selection. */
	public void clearSelectedEditParts()
	{
		for (IEditPart part : this.selectedEditParts)
		{
			part.setSelected(false);
		}
		this.selectedEditParts.clear();
	}

	/** adds an editpart to the current selection. */
	public void addSelectedEditPart(IEditPart part)
	{
		part.setSelected(true);
		this.selectedEditParts.add(part);
	}

	/** adds an editpart to the current selection. */
	public void removeSelectedEditPart(IEditPart part)
	{
		part.setSelected(false);
		this.selectedEditParts.remove(part);
	}

	/** @return a list of all selected aditparts. */
	public ImmutableList<IEditPart> getSelectedEditParts()
	{
		return new ImmutableList<IEditPart>(this.selectedEditParts);
	}
}
