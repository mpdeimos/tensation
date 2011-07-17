/**
 * 
 */
package com.mpdeimos.tensor.ui;

import java.awt.Dimension;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import resources.R;

/**
 * The context panel.
 * 
 * @author mpdeimos
 * 
 */
public class ContextPanel extends JPanel
{
	/** The fixed width of this panel. */
	private static final int PANEL_WIDTH = 150;

	/** the content to display on this JPanel. */
	private JPanel content = null;

	// /** The linked application window. */
	// private final ApplicationWindow applicationWindow;

	/**
	 * Constructor.
	 * 
	 * @param applicationWindow
	 */
	public ContextPanel(ApplicationWindow applicationWindow)
	{
		// this.applicationWindow = applicationWindow;
		this.setPreferredSize(new Dimension(PANEL_WIDTH, 0));
		this.setBorder(new EmptyBorder(5, 5, 5, 5));
		update();
	}

	@Override
	public int getWidth()
	{
		return PANEL_WIDTH;
	}

	/** Sets the content for the panel. */
	public void setContent(JPanel content)
	{
		this.content = content;
		update();
	}

	/** Updates the content panel. */
	private void update()
	{
		this.removeAll();

		if (this.content != null)
			this.add(this.content);
		else
		{
			JLabel label = new JLabel(
					R.strings.getString("window_contextpanel_disabled")); //$NON-NLS-1$
			this.add(label);
		}
	}
}
