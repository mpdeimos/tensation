package com.mpdeimos.tensation.ui;

import com.mpdeimos.tensation.editpart.IEditPart;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;

/**
 * ListCellRenderer implementation for rendering EditParts.
 * 
 * @author mpdeimos
 * 
 */
public class EditPartListCellRenderer implements ListCellRenderer
{
	/** The dimensions of this table cell. */
	private final Dimension cellSize;

	/** Constructor. */
	public EditPartListCellRenderer(int width, int height)
	{
		this.cellSize = new Dimension(width, height);
	}

	@Override
	public Component getListCellRendererComponent(
				JList list,
				final Object obj,
				int index,
				boolean isSelected,
				boolean hasFocus)
	{
		JLabel label = new JLabel()
		{
			@Override
			public void paint(java.awt.Graphics g)
			{
				Graphics2D gfx = (Graphics2D) g;

				gfx.setRenderingHint(
						RenderingHints.KEY_ANTIALIASING,
						RenderingHints.VALUE_ANTIALIAS_ON);
				gfx.setRenderingHint(
						RenderingHints.KEY_TEXT_ANTIALIASING,
						RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
				gfx.setRenderingHint(
						RenderingHints.KEY_FRACTIONALMETRICS,
						RenderingHints.VALUE_FRACTIONALMETRICS_ON);

				super.paint(gfx);

				IEditPart editPart = (IEditPart) obj;
				editPart.draw(gfx);
			}
		};

		label.setMinimumSize(this.cellSize);
		label.setPreferredSize(this.cellSize);
		label.setMaximumSize(this.cellSize);

		if (isSelected)
		{
			label.setBackground(list.getSelectionBackground());
			label.setForeground(list.getSelectionForeground());
			label.setOpaque(true);
		}
		else
		{
			label.setBackground(list.getBackground());
			label.setForeground(list.getForeground());
			label.setOpaque(false);
		}

		label.setEnabled(list.isEnabled());
		label.setFont(list.getFont());

		return label;
	}
}
