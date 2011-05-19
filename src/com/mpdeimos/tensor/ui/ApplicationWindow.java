package com.mpdeimos.tensor.ui;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JToolBar;
import javax.swing.UIManager;
import javax.swing.WindowConstants;

import com.mpdeimos.tensor.action.ExitAction;
import com.mpdeimos.tensor.res.R;
import com.mpdeimos.tensor.util.Log;
import javax.swing.SwingConstants;
import javax.swing.JButton;
import javax.swing.ImageIcon;

/**
 * Represents the main application window
 * 
 * @author mpdeimos
 *
 */
public class ApplicationWindow extends JFrame implements WindowListener {
	/** log tag */
	private final static String LOG_TAG = "ApplicationWindow"; //$NON-NLS-1$
	
	/** singleton window instance */
	private static ApplicationWindow applicationWindow;

	/**
	 * Launch the application.
	 */
	public static void createAndDisplay() {
		EventQueue.invokeLater(new Runnable() {

			public void run() {
				applicationWindow = new ApplicationWindow();
				applicationWindow.setVisible(true);
			}
		});
	}
	
	/**
	 * @return the current application window. may be null if not created.
	 */
	public static ApplicationWindow getApplicationWindow() {
		return applicationWindow;
	}
	
	/**
	 * Create the application.
	 */
	public ApplicationWindow() {
		initialize();
	}
	
	/**
	 * Initializes the contents of the frame.
	 */
	private void initialize() {
		try {
			UIManager.setLookAndFeel(
			        UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {
			// just swallow and continue w/ ugly java look and feel
			Log.e(LOG_TAG, "setting native look failed", e); //$NON-NLS-1$
		}
		
		this.setTitle(R.strings.getString("window.main.title")); //$NON-NLS-1$
		this.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		this.addWindowListener(this);
		this.setBounds(50, 50, 400, 400);
//		GraphicsEnvironment env =
//			GraphicsEnvironment.getLocalGraphicsEnvironment();
//			        this.setMaximizedBounds(env.getMaximumWindowBounds());
//		this.setExtendedState(this.getExtendedState() | Frame.MAXIMIZED_BOTH);
		
		initializeMenu();
	}

	/**
	 * Initializes the applications's main menu.
	 */
	private void initializeMenu() {
		JMenuBar menuBar = new JMenuBar();
		
		// file menu
		JMenu menuFile = new JMenu(R.strings.getString("window.menu.file")); //$NON-NLS-1$
		
		ExitAction exitAction = new ExitAction();
		JMenuItem item = new JMenuItem(exitAction);
		menuFile.add(item);
		menuBar.add(menuFile);
		
		this.setJMenuBar(menuBar);
		
		JToolBar toolBar = new JToolBar();
		toolBar.setOrientation(SwingConstants.VERTICAL);
		getContentPane().add(toolBar, BorderLayout.WEST);
		
		JButton btnNewButton = new JButton("New button");
		btnNewButton.setAction(exitAction);
		toolBar.add(btnNewButton);
		
		JToolBar toolBar_1 = new JToolBar();
		getContentPane().add(toolBar_1, BorderLayout.NORTH);
	}

	/**
	 * Disposes the application window and quits the program.
	 */
	public void exit() {
		this.dispose();
	}

	@Override
	public void windowActivated(WindowEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void windowClosed(WindowEvent arg0) {
		Log.v(LOG_TAG, "Main window closed"); //$NON-NLS-1$
	}

	@Override
	public void windowClosing(WindowEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void windowDeactivated(WindowEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void windowDeiconified(WindowEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void windowIconified(WindowEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void windowOpened(WindowEvent arg0) {
		// TODO Auto-generated method stub
		
	}
}
