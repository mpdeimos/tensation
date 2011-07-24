package com.mpdeimos.tensor.ui;

import com.mpdeimos.tensor.action.DrawTensorAction;
import com.mpdeimos.tensor.action.ExitAction;
import com.mpdeimos.tensor.action.SelectEditPartAction;
import com.mpdeimos.tensor.action.TensorConnectAction;
import com.mpdeimos.tensor.util.Log;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JToolBar;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.WindowConstants;
import javax.swing.border.EtchedBorder;

import resources.R;

/**
 * Represents the main application window
 * 
 * @author mpdeimos
 * 
 */
public class ApplicationWindow extends JFrame
{
	/** singleton window instance */
	private static ApplicationWindow applicationWindow;

	/** the drawing canvas of our app */
	private DrawingCanvas drawingCanvas;

	/** the default button for canvas actions. */
	private ToolBarButton selectEditPartButton;

	/** the context panel of our app. */
	private ContextPanel contextPanel;

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
				applicationWindow = new ApplicationWindow();
				applicationWindow.setVisible(true);
			}
		});
	}

	/**
	 * @return the current application window. may be null if not created.
	 */
	public static ApplicationWindow getApplicationWindow()
	{
		return applicationWindow;
	}

	/**
	 * Create the application.
	 */
	public ApplicationWindow()
	{
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
	}

	/**
	 * initializes the window toolbars
	 */
	private void initializeToolbars()
	{
		JToolBar sideToolBar = new JToolBar();
		sideToolBar.setOrientation(SwingConstants.VERTICAL);
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

		JMenuItem item = new JMenuItem(new ExitAction());
		menuFile.add(item);
		menuBar.add(menuFile);

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
			Log.v(ApplicationWindow.this, "Main window closed"); //$NON-NLS-1$
		}
	}
}
