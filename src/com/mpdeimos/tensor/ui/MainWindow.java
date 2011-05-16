package com.mpdeimos.tensor.ui;

import java.awt.EventQueue;
import java.awt.FlowLayout;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.UIManager;

import com.mpdeimos.tensor.util.Log;

public class MainWindow {
	
	private final String LOG_TAG = "MainWindow";
	
	public MainWindow()
	{
		// try to look a bit native.
        try {
			UIManager.setLookAndFeel(
			        UIManager.getSystemLookAndFeelClassName());
		} catch (Exception e) {
			Log.e(LOG_TAG, "setting native look failed", e);
		}
	}

	public void display()
	{
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                JFrame f = new JFrame("Swing Example Window");
                f.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

                f.setLayout(new FlowLayout());
 
                f.add(new JLabel("This is just a test"));
 
                f.pack();
                f.setVisible(true);
            }
        });
	}

}
