package com.mpdeimos.tensor.ui;

import com.mpdeimos.tensor.action.ExitAction;
import com.mpdeimos.tensor.action.NewAction;
import com.mpdeimos.tensor.action.RedoAction;
import com.mpdeimos.tensor.action.SaveAction;
import com.mpdeimos.tensor.action.UndoAction;
import com.mpdeimos.tensor.action.canvas.DrawTensorAction;
import com.mpdeimos.tensor.action.canvas.SelectEditPartAction;
import com.mpdeimos.tensor.action.canvas.TensorConnectAction;
import com.mpdeimos.tensor.model.ModelRoot;
import com.mpdeimos.tensor.util.Log;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
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

	private ModelRoot model;

	/**
	 * Launch the application.
	 */
	public static void createAndDisplay()
	{
		EventQueue.invokeLater(new Runnable()
		{

			@Override
			public void run()
			{
				app = new Application();
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

		initialize();
	}

	/**
	 * Initializes the contents of the frame.
	 */
	private void initialize()
	{
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
		this.setBounds(50, 50, 600, 400);

		initializeContextPanel();
		initializeCanvas();
		initializeMenu();
		initializeToolbars();
		initializeKeystrokes();
	}

	/** Initializes global keystrokes. */
	private void initializeKeystrokes()
	{
		((JComponent) getContentPane()).registerKeyboardAction(this.undoAction,
				KeyStroke.getKeyStroke(KeyEvent.VK_Z, InputEvent.CTRL_MASK),
				JComponent.WHEN_IN_FOCUSED_WINDOW);
		((JComponent) getContentPane()).registerKeyboardAction(this.redoAction,
				KeyStroke.getKeyStroke(KeyEvent.VK_Y, InputEvent.CTRL_MASK),
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
				new SelectEditPartAction(this, this.drawingCanvas));
		sideToolBar.add(this.selectEditPartButton);
		JButton drawTensorButton = new ToolBarButton(
				sideToolBar,
				new DrawTensorAction(this, this.drawingCanvas));
		drawTensorButton.setHideActionText(true);
		sideToolBar.add(drawTensorButton);

		JButton connectButton = new ToolBarButton(
				sideToolBar,
				new TensorConnectAction(this, this.drawingCanvas));
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

		item = new JMenuItem(new SaveAction());
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

	/** sets the current main root model. */
	public void setModel(ModelRoot model)
	{
		this.model = model;
		this.drawingCanvas.onModelExchanged();
	}

	/** Starts the default toolbar action. */
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
}
