package com.mpdeimos.tensation.ui;

import com.mpdeimos.tensation.action.CloseTabAction;
import com.mpdeimos.tensation.action.CopyAction;
import com.mpdeimos.tensation.action.DeleteSelectionAction;
import com.mpdeimos.tensation.action.DuplicateAction;
import com.mpdeimos.tensation.action.DuplicateNewCanvasAction;
import com.mpdeimos.tensation.action.ExitAction;
import com.mpdeimos.tensation.action.ExportAction;
import com.mpdeimos.tensation.action.NewAction;
import com.mpdeimos.tensation.action.OpenAction;
import com.mpdeimos.tensation.action.PasteAction;
import com.mpdeimos.tensation.action.RedoAction;
import com.mpdeimos.tensation.action.ResetViewAction;
import com.mpdeimos.tensation.action.SaveAction;
import com.mpdeimos.tensation.action.SaveAsAction;
import com.mpdeimos.tensation.action.ScaleCanvasAction;
import com.mpdeimos.tensation.action.UndoAction;
import com.mpdeimos.tensation.action.canvas.DrawTensorAction;
import com.mpdeimos.tensation.action.canvas.GraphRestructureAction;
import com.mpdeimos.tensation.action.canvas.SelectEditPartAction;
import com.mpdeimos.tensation.action.canvas.TensorConnectAction;
import com.mpdeimos.tensation.layout.ForceDirectedPlacementLayouter;
import com.mpdeimos.tensation.layout.RandomLayouter;
import com.mpdeimos.tensation.layout.RotateLayouter;
import com.mpdeimos.tensation.layout.ScaleLayouter;
import com.mpdeimos.tensation.util.Log;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.Rectangle;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;

import javax.swing.AbstractAction;
import javax.swing.Box;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JTabbedPane;
import javax.swing.JToolBar;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.WindowConstants;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

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

	/** the tabbed pane for holding various canvases */
	private JTabbedPane tabbedPane;

	/** the default button for canvas actions. */
	private ToolBarButton selectEditPartButton;

	/** the context panel of our app. */
	private ContextPanel contextPanel;

	/** the export location of the model. */
	private File modelExportLocation;

	/** flag if the application has a gui or is still in command window mode. */
	// private boolean hasGui = false;

	/** action for closing an tab */
	public final AbstractAction ACTION_CLOSE_TAB = new CloseTabAction();

	/** the global undo action. */
	public final AbstractAction ACTION_UNDO = new UndoAction();

	/** the global redo action. */
	public final AbstractAction ACTION_REDO = new RedoAction();

	/** the global new action. */
	public final AbstractAction ACTION_NEW = new NewAction();

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

				if (app.tabbedPane.getTabCount() == 0)
				{
					app.createNewCanvas();
				}

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
		// nothing for now
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

		initializeContextPanel();
		initializeTabs();
		initializeMenu();
		initializeToolbars();

		this.pack();

		int x = Preferences.get().getInt(Preferences.WINDOW_X, 50);
		int y = Preferences.get().getInt(Preferences.WINDOW_Y, 50);
		int w = Preferences.get().getInt(Preferences.WINDOW_W, 600);
		int h = Preferences.get().getInt(Preferences.WINDOW_H, 400);
		this.setBounds(x, y, w, h);

		if (Preferences.get().getBoolean(Preferences.WINDOW_MAXIMIZED, false))
		{
			this.setExtendedState(MAXIMIZED_BOTH);
		}
	}

	/** Initializes the Tabbed canvas switches. */
	private void initializeTabs()
	{
		this.tabbedPane = new JTabbedPane(
				SwingConstants.BOTTOM,
				JTabbedPane.SCROLL_TAB_LAYOUT);
		getContentPane().add(this.tabbedPane, BorderLayout.CENTER);

		this.tabbedPane.addChangeListener(new ChangeListener()
		{
			@Override
			public void stateChanged(ChangeEvent arg0)
			{
				getActiveCanvas().getUndoManager().updateActions();
				getActiveCanvas().startCanvasAction();
			}
		});
	}

	/**
	 * initializes the window toolbars
	 */
	private void initializeToolbars()
	{
		// side toolbar

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

		JButton epsilonDeltaButton = new ToolBarButton(
				sideToolBar,
				new GraphRestructureAction());
		epsilonDeltaButton.setHideActionText(true);
		sideToolBar.add(epsilonDeltaButton);

		sideToolBar.add(Box.createVerticalGlue());

		JButton tabClose = new ToolBarButton(sideToolBar, this.ACTION_CLOSE_TAB);
		tabClose.setHideActionText(true);
		sideToolBar.add(tabClose);

		// top toolbar

		JToolBar topToolBar = new JToolBar();
		getContentPane().add(topToolBar, BorderLayout.NORTH);
		topToolBar.setFloatable(false);
		topToolBar.setRollover(false);
		topToolBar.setBorderPainted(false);

		final JButton newButton = new ToolBarButton(
				topToolBar,
				this.ACTION_NEW);
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
				this.ACTION_UNDO);
		undoButton.setHideActionText(true);
		undoButton.setIcon(new ImageIcon(R.drawable.EDIT_UNDO_24.url()));
		topToolBar.add(undoButton);
		final JButton redoButton = new ToolBarButton(
				topToolBar,
				this.ACTION_REDO);
		redoButton.setIcon(new ImageIcon(R.drawable.EDIT_REDO_24.url()));
		redoButton.setHideActionText(true);
		topToolBar.add(redoButton);
	}

	/**
	 * initializes the painting canvas
	 */
	public void createNewCanvas()
	{
		DrawingCanvasHolder holder = new DrawingCanvasHolder();
		this.tabbedPane.addTab(holder.getDrawingCanvas().getName(), holder);
		this.tabbedPane.setSelectedIndex(this.tabbedPane.getTabCount() - 1);
		this.ACTION_CLOSE_TAB.setEnabled(this.tabbedPane.getTabCount() > 1);
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

		JMenuItem item = new JMenuItem(this.ACTION_NEW);
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

		item = new JMenuItem(this.ACTION_CLOSE_TAB);
		menuFile.add(item);

		menuFile.addSeparator();

		menuFile.add(new ExitAction());

		// edit menu
		JMenu menuEdit = new JMenu(R.string.WINDOW_MENU_EDIT.string());
		menuBar.add(menuEdit);

		item = new JMenuItem(this.ACTION_UNDO);
		menuEdit.add(item);
		item = new JMenuItem(this.ACTION_REDO);
		menuEdit.add(item);
		menuEdit.addSeparator();
		menuEdit.add(new CopyAction());
		menuEdit.add(new PasteAction());
		menuEdit.add(new DuplicateAction());
		menuEdit.add(new DuplicateNewCanvasAction());
		menuEdit.addSeparator();
		menuEdit.add(new DeleteSelectionAction());

		// layout menu
		JMenu menuLayout = new JMenu(R.string.WINDOW_MENU_LAYOUT.string());
		menuBar.add(menuLayout);
		item = new JMenuItem(new ScaleLayouter());
		menuLayout.add(item);
		item = new JMenuItem(new RandomLayouter());
		menuLayout.add(item);
		item = new JMenuItem(new ForceDirectedPlacementLayouter());
		menuLayout.add(item);
		item = new JMenuItem(new RotateLayouter());
		menuLayout.add(item);

		// options menu
		JMenu menuOptions = new JMenu(R.string.WINDOW_MENU_VIEW.string());
		menuBar.add(menuOptions);

		item = new JMenuItem(new ResetViewAction());
		menuOptions.add(item);

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
		this.contextPanel = new ContextPanel();
		getContentPane().add(this.contextPanel, BorderLayout.EAST);
	}

	/** @return the active drawing canvas of this application. */
	public DrawingCanvas getActiveCanvas()
	{
		DrawingCanvasHolder selected = (DrawingCanvasHolder) this.tabbedPane.getSelectedComponent();

		if (selected == null)
			throw new IllegalStateException();

		return selected.getDrawingCanvas();
	}

	/** @return the context panel of this application. */
	public ContextPanel getContextPanel()
	{
		return this.contextPanel;
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
			if (Application.getApp().getExtendedState() == MAXIMIZED_BOTH)
				Preferences.get().putBoolean(Preferences.WINDOW_MAXIMIZED, true);
			else
			{
				Preferences.get().putBoolean(
						Preferences.WINDOW_MAXIMIZED,
						false);

				Rectangle r = Application.this.getBounds();
				Preferences.get().putInt(Preferences.WINDOW_X, r.x);
				Preferences.get().putInt(Preferences.WINDOW_Y, r.y);
				Preferences.get().putInt(Preferences.WINDOW_W, r.width);
				Preferences.get().putInt(Preferences.WINDOW_H, r.height);
			}
			Preferences.save();
		}

		@Override
		public void windowClosed(WindowEvent arg0)
		{
			Log.v(Application.this, "Main window closed"); //$NON-NLS-1$
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

	/** Closes a canvas tab. */
	public void closeActiveCanvas()
	{
		if (!this.ACTION_CLOSE_TAB.isEnabled())
			return;

		this.tabbedPane.remove(this.tabbedPane.getSelectedIndex());

		this.ACTION_CLOSE_TAB.setEnabled(this.tabbedPane.getTabCount() > 1);
	}

	/** @return the export location of the model. may be null if not saved yet. */
	/* package */File getModelExportLocation()
	{
		return this.modelExportLocation;
	}

	/**
	 * sets the filename of the current model export.
	 */
	/* package */void setModelExportLocation(File location)
	{
		if (location != null)
		{
			Preferences.get().put(
					Preferences.SAVE_LOCATION,
					location.getParent());
		}
		this.modelExportLocation = location;
	}

	/** Updates the active tab name. */
	public void updateActiveTabName()
	{
		this.tabbedPane.setTitleAt(
				this.tabbedPane.getSelectedIndex(),
				getActiveCanvas().getName());
	}
}
