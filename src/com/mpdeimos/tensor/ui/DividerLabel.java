package com.mpdeimos.tensor.ui;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.SwingConstants;

/**
 * Combines a separator with a textlabel.
 * 
 * @author mpdeimos
 * 
 */
public class DividerLabel extends JPanel
{
	/** The divider label. */
	private final JLabel label;

	/** Constructor. */
	public DividerLabel(String text)
	{
		super();

		setLayout(new GridBagLayout());

		GridBagConstraints c = new GridBagConstraints();
		c.gridy = 0;
		c.weightx = 0;
		c.insets = new Insets(2, 0, 2, 0);

		this.label = new JLabel(text);
		c.fill = GridBagConstraints.NONE;
		c.gridx = 0;
		c.ipadx = 5;

		this.add(this.label, c);

		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 1;
		c.weightx = 1;
		this.add(new JSeparator(SwingConstants.HORIZONTAL), c);
	}

	/** Sets the text of the divider label. */
	public void setText(String text)
	{
		this.label.setText(text);
	}

	@Override
	public Dimension getMaximumSize()
	{
		Dimension d = super.getPreferredSize();
		d.width = Short.MAX_VALUE;

		return d;
	}
}
