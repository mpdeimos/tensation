package com.mpdeimos.tensation.ui;

import javax.swing.JPanel;

/**
 * JPanel with a refresh method, intended for updating the UI from model data.
 * 
 * @author mpdeimos
 */
public abstract class RefreshablePanel extends JPanel
{
	/** Updates the UI from model data. */
	public abstract void refresh();
}
