package com.mpdeimos.tensor.ui;

import com.mpdeimos.tensor.action.ExitAction;
import com.mpdeimos.tensor.action.ExportAction;
import com.mpdeimos.tensor.action.NewAction;
import com.mpdeimos.tensor.action.OpenAction;
import com.mpdeimos.tensor.action.RedoAction;
import com.mpdeimos.tensor.action.SaveAction;
import com.mpdeimos.tensor.action.SaveAsAction;
import com.mpdeimos.tensor.action.ScaleCanvasAction;
import com.mpdeimos.tensor.action.UndoAction;
import com.mpdeimos.tensor.action.canvas.DrawTensorAction;
import com.mpdeimos.tensor.action.canvas.SelectEditPartAction;
import com.mpdeimos.tensor.action.canvas.TensorConnectAction;
import com.mpdeimos.tensor.layout.ForceDirectedPlacementLayouter;
import com.mpdeimos.tensor.layout.RandomLayouter;
import com.mpdeimos.tensor.layout.ScaleLayouter;
import com.mpdeimos.tensor.model.ModelRoot;
import com.mpdeimos.tensor.util.Log;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.Rectangle;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JToolBar;
import javax.swing.KeyStroke;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.WindowConstants;
import javax.swing.border.EtchedBorder;
import javax.swing.undo.CannotRedoException;
import javax.swing.undo.CannotUndoException;
import javax.swing.undo.UndoManager;
import javax.swing.undo.UndoableEdit;

import resources.R;

/**
 * Represents the main application window
 * 
 * @author mpdeimos
 * 
 */
public class Application extends JFrame
{
	/** singleton window instance */
	private static Application app;

	/** the drawing canvas of our app */
	private DrawingCanvas drawingCanvas;

	/** the default button for canvas actions. */
	private ToolBarButton selectEditPartButton;

	/** the context panel of our app. */
	private ContextPanel contextPanel;

	/** the undo manager of our app. */
	private final CanvasUndoManager undoManager;

	/** the global undo action. */
	private final AbstractAction undoAction;

	/** the global redo action. */
	private final AbstractAction redoAction;

	/** the model loadd. */
	private ModelRoot model;

	/** the export location of the model. */
	private File modelExportLocation;

	/** flag if the application has a gui or is still in command window mode. */
	// private boolean hasGui = false;

	/**
	 * Launch the application.
	 * 
	 * @param args
	 */
	public static void createAndDisplay(final String[] args)
	{
		EventQueue.invokeLater(new Runnable()
		{

			@Override
			public void run()
			{
				app = new Application();
				app.initializeGui();
				Commandline.evaluateArguments(args);
				// app.hasGui = true;
				app.setVisible(true);
			}
		});
	}

	/**
	 * @return the current application window. may be null if not created.
	 */
	public static Application getApp()
	{
		return app;
	}

	/**
	 * Create the application.
	 */
	public Application()
	{
		this.undoAction = new UndoAction();
		this.redoAction = new RedoAction();
		this.undoManager = new CanvasUndoManager();

		this.model = new ModelRoot();
	}

	/**
	 * Initializes the contents of the frame.
	 */
	private void initializeGui()
	{
		// this.hasGui = true;

		try
		{
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		}
		catch (Exception e)
		{
			// just swallow and continue w/ ugly java look and feel
			Log.e(this, "setting native look failed", e); //$NON-NLS-1$
		}

		this.setTitle(R.string.WINDOW_MAIN_TITLE.string());
		this.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		this.addWindowListener(new WindowListener());

		int x = Preferences.get().getInt(Preferences.WINDOW_X, 50);
		int y = Preferences.get().getInt(Preferences.WINDOW_Y, 50);
		int w = Preferences.get().getInt(Preferences.WINDOW_W, 600);
		int h = Preferences.get().getInt(Preferences.WINDOW_H, 400);
		this.setBounds(x, y, w, h);

		initializeContextPanel();
		initializeCanvas();
		initializeMenu();
		initializeToolbars();
		initializeKeystrokes();
	}

	/** Initializes global keystrokes. */
	private void initializeKeystrokes()
	{
		((JComponent) getContentPane()).registerKeyboardAction(
				this.undoAction,
				KeyStroke.getKeyStroke(KeyEvent.VK_Z, InputEvent.CTRL_DOWN_MASK),
				JComponent.WHEN_IN_FOCUSED_WINDOW);
		((JComponent) getContentPane()).registerKeyboardAction(
				this.redoAction,
				KeyStroke.getKeyStroke(KeyEvent.VK_Y, InputEvent.CTRL_DOWN_MASK),
				JComponent.WHEN_IN_FOCUSED_WINDOW);

		((JComponent) getContentPane()).registerKeyboardAction(
				new NewAction(),
				KeyStroke.getKeyStroke(KeyEvent.VK_N, InputEvent.CTRL_DOWN_MASK),
				JComponent.WHEN_IN_FOCUSED_WINDOW);
		((JComponent) getContentPane()).registerKeyboardAction(
				new OpenAction(),
				KeyStroke.getKeyStroke(KeyEvent.VK_O, InputEvent.CTRL_DOWN_MASK),
				JComponent.WHEN_IN_FOCUSED_WINDOW);
		((JComponent) getContentPane()).registerKeyboardAction(
				new SaveAction(),
				KeyStroke.getKeyStroke(KeyEvent.VK_S, InputEvent.CTRL_DOWN_MASK),
				JComponent.WHEN_IN_FOCUSED_WINDOW);
		((JComponent) getContentPane()).registerKeyboardAction(
				new SaveAsAction(),
				KeyStroke.getKeyStroke(KeyEvent.VK_S, InputEvent.CTRL_DOWN_MASK
						+ InputEvent.SHIFT_DOWN_MASK),
				JComponent.WHEN_IN_FOCUSED_WINDOW);
	}

	/**
	 * initializes the window toolbars
	 */
	private void initializeToolbars()
	{
		JToolBar sideToolBar = new JToolBar();
		sideToolBar.setOrientation(SwingConstants.VERTICAL);
		sideToolBar.setFloatable(false);
		getContentPane().add(sideToolBar, BorderLayout.WEST);

		this.selectEditPartButton = new ToolBarButton(
				sideToolBar,
				new SelectEditPartAction());
		sideToolBar.add(this.selectEditPartButton);
		JButton drawTensorButton = new ToolBarButton(
				sideToolBar,
				new DrawTensorAction());
		drawTensorButton.setHideActionText(true);
		sideToolBar.add(drawTensorButton);

		JButton connectButton = new ToolBarButton(
				sideToolBar,
				new TensorConnectAction());
		connectButton.setHideActionText(true);
		sideToolBar.add(connectButton);

		JButton exitButton = new ToolBarButton(sideToolBar, new ExitAction());
		exitButton.setHideActionText(true);
		sideToolBar.add(exitButton);

		startDefaultToolbarAction();

		JToolBar topToolBar = new JToolBar();
		getContentPane().add(topToolBar, BorderLayout.NORTH);
		topToolBar.setFloatable(false);
		topToolBar.setRollover(false);
		topToolBar.setBorderPainted(false);

		final JButton newButton = new ToolBarButton(
				topToolBar,
				new NewAction());
		newButton.setHideActionText(true);
		newButton.setIcon(new ImageIcon(R.drawable.DOCUMENT_NEW_24.url()));
		topToolBar.add(newButton);

		final JButton saveButton = new ToolBarButton(
				topToolBar,
				new SaveAction());
		saveButton.setHideActionText(true);
		saveButton.setIcon(new ImageIcon(R.drawable.DOCUMENT_SAVE_24.url()));
		topToolBar.add(saveButton);

		final JButton exportButton = new ToolBarButton(
				topToolBar,
				new ExportAction());
		exportButton.setHideActionText(true);
		exportButton.setIcon(new ImageIcon(R.drawable.DOCUMENT_EXPORT_24.url()));
		topToolBar.add(exportButton);

		topToolBar.add(new JToolBar.Separator());

		final JButton undoButton = new ToolBarButton(
				topToolBar,
				this.undoAction);
		undoButton.setHideActionText(true);
		undoButton.setIcon(new ImageIcon(R.drawable.EDIT_UNDO_24.url()));
		topToolBar.add(undoButton);
		final JButton redoButton = new ToolBarButton(
				topToolBar,
				this.redoAction);
		redoButton.setIcon(new ImageIcon(R.drawable.EDIT_REDO_24.url()));
		redoButton.setHideActionText(true);
		topToolBar.add(redoButton);
	}

	/**
	 * initializes the painting canvas
	 */
	private void initializeCanvas()
	{
		JPanel drawingPanelOuter = new JPanel();
		drawingPanelOuter.setBorder(new EtchedBorder(
				EtchedBorder.LOWERED,
				null,
				null));
		getContentPane().add(drawingPanelOuter, BorderLayout.CENTER);
		drawingPanelOuter.setLayout(new BorderLayout(0, 0));

		this.drawingCanvas = new DrawingCanvas(this);
		drawingPanelOuter.add(this.drawingCanvas);
	}

	/**
	 * Initializes the applications's main menu.
	 */
	private void initializeMenu()
	{
		JMenuBar menuBar = new JMenuBar();

		// file menu
		JMenu menuFile = new JMenu(R.string.WINDOW_MENU_FILE.string());
		menuBar.add(menuFile);

		JMenuItem item = new JMenuItem(new NewAction());
		menuFile.add(item);
		item = new JMenuItem(new OpenAction());
		menuFile.add(item);

		item = new JMenuItem(new SaveAction());
		menuFile.add(item);
		item = new JMenuItem(new SaveAsAction());
		menuFile.add(item);
		item = new JMenuItem(new ExportAction());
		menuFile.add(item);

		menuFile.addSeparator();
		item = new JMenuItem(new ExitAction());
		menuFile.add(item);

		// edit menu
		JMenu menuEdit = new JMenu(R.string.WINDOW_MENU_EDIT.string());
		menuBar.add(menuEdit);

		item = new JMenuItem(this.undoAction);
		menuEdit.add(item);
		item = new JMenuItem(this.redoAction);
		menuEdit.add(item);

		// layout menu
		JMenu menuLayout = new JMenu(R.string.WINDOW_MENU_LAYOUT.string());
		menuBar.add(menuLayout);
		item = new JMenuItem(new ScaleLayouter());
		menuLayout.add(item);
		item = new JMenuItem(new RandomLayouter());
		menuLayout.add(item);
		item = new JMenuItem(new ForceDirectedPlacementLayouter());
		menuLayout.add(item);

		// options menu
		JMenu menuOptions = new JMenu(R.string.WINDOW_MENU_VIEW.string());
		menuBar.add(menuOptions);

		// options menu >> scale
		JMenu menuOptionsScale = new JMenu(
				R.string.WINDOW_MENU_VIEW_CANVASSCALE.string());
		menuOptions.add(menuOptionsScale);

		JCheckBoxMenuItem cbitem = new JCheckBoxMenuItem();
		cbitem.setAction(new ScaleCanvasAction(50, cbitem));
		menuOptionsScale.add(cbitem);

		cbitem = new JCheckBoxMenuItem();
		cbitem.setAction(new ScaleCanvasAction(75, cbitem));
		menuOptionsScale.add(cbitem);

		cbitem = new JCheckBoxMenuItem();
		cbitem.setAction(new ScaleCanvasAction(100, cbitem));
		cbitem.setSelected(true);
		menuOptionsScale.add(cbitem);

		cbitem = new JCheckBoxMenuItem();
		cbitem.setAction(new ScaleCanvasAction(200, cbitem));
		menuOptionsScale.add(cbitem);

		cbitem = new JCheckBoxMenuItem();
		cbitem.setAction(new ScaleCanvasAction(400, cbitem));
		menuOptionsScale.add(cbitem);

		this.setJMenuBar(menuBar);
	}

	/** Initializes the application's context panel. */
	private void initializeContextPanel()
	{
		this.contextPanel = new ContextPanel(this);
		getContentPane().add(this.contextPanel, BorderLayout.EAST);
	}

	/** @return the drawing canvas of this application. */
	public DrawingCanvas getDrawingCanvas()
	{
		return this.drawingCanvas;
	}

	/** @return the context panel of this application. */
	public ContextPanel getContextPanel()
	{
		return this.contextPanel;
	}

	/** @return the global undo manager. */
	public UndoManager getUndoManager()
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
		this.modelExportLocation = null;
		this.drawingCanvas.onModelExchanged();
		this.undoManager.discardAllEdits();
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
		Preferences.get().put(Preferences.SAVE_LOCATION, location.getParent());
		this.modelExportLocation = location;
	}

	/**
	 * Starts the default toolbar action.
	 */
	protected void startDefaultToolbarAction()
	{
		this.selectEditPartButton.doClick();
	}

	/**
	 * Disposes the application window and quits the program.
	 */
	public void exit()
	{
		this.dispose();
	}

	/**
	 * Listener for window events
	 */
	private class WindowListener extends WindowAdapter
	{
		@Override
		public void windowClosing(WindowEvent arg0)
		{
			Rectangle r = Application.this.getBounds();
			Preferences.get().putInt(Preferences.WINDOW_X, r.x);
			Preferences.get().putInt(Preferences.WINDOW_Y, r.y);
			Preferences.get().putInt(Preferences.WINDOW_W, r.width);
			Preferences.get().putInt(Preferences.WINDOW_H, r.height);
			Preferences.save();
		}

		@Override
		public void windowClosed(WindowEvent arg0)
		{
			Log.v(Application.this, "Main window closed"); //$NON-NLS-1$
		}
	}

	/** Extended UndoManager */
	private class CanvasUndoManager extends UndoManager
	{
		/** Constructor. */
		public CanvasUndoManager()
		{
			updateActions();
		}

		@Override
		public synchronized void undo() throws CannotUndoException
		{
			super.undo();

			updateActions();
		}

		@Override
		public synchronized void redo() throws CannotRedoException
		{
			super.redo();

			updateActions();
		}

		@Override
		public synchronized void discardAllEdits()
		{
			super.discardAllEdits();

			updateActions();
		}

		@Override
		public synchronized boolean addEdit(UndoableEdit anEdit)
		{
			boolean addEdit = super.addEdit(anEdit);

			updateActions();

			return addEdit;
		}

		/** updates the action states. */
		private void updateActions()
		{
			Application.this.undoAction.setEnabled(canUndo());
			Application.this.undoAction.putValue(
					Action.NAME,
					getUndoPresentationName());
			Application.this.undoAction.putValue(
					Action.SHORT_DESCRIPTION,
					getUndoPresentationName());
			Application.this.redoAction.setEnabled(canRedo());
			Application.this.redoAction.putValue(
					Action.NAME,
					getRedoPresentationName());
			Application.this.redoAction.putValue(
					Action.SHORT_DESCRIPTION,
					getRedoPresentationName());
		}
	}

	/**
	 * @return a new file chooser opened at the last save location.
	 */
	public JFileChooser createFileChooser()
	{
		String saveLocation = Preferences.get().get(
				Preferences.SAVE_LOCATION,
				null);
		return new JFileChooser(saveLocation);
	}
}
