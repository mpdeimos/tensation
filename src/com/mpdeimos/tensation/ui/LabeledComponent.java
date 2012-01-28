package com.mpdeimos.tensation.ui;

import java.awt.Component;
import java.awt.Dimension;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * Combines a separator with a textlabel.
 * 
 * @author mpdeimos
 * 
 */
public class LabeledComponent extends JPanel
{
	/** The divider label. */
	private final JLabel label;

	/** Constructor. */
	public LabeledComponent(
			String text,
			Component component,
			int leftMargin,
			int rightMargin)
	{
		super();

		this.label = new JLabel(text);
		setLayout(new BoxLayout(this, BoxLayout.LINE_AXIS));
		if (leftMargin > 0)
			add(Box.createHorizontalStrut(leftMargin));
		add(this.label);
		add(Box.createHorizontalGlue());
		add(component);
		if (rightMargin > 0)
			add(Box.createHorizontalStrut(10));
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
