package com.mpdeimos.tensor.action;

import com.mpdeimos.tensor.ui.Application;
import com.mpdeimos.tensor.util.FileUtil;

import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.AbstractAction;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileNameExtensionFilter;

import resources.R;

/**
 * Saves the current model to an xml file.
 * 
 * @author mpdeimos
 * 
 */
public class ExportAction extends AbstractAction
{
	/** file extension of the exported xml. */
	public static final String PNG_FILE_EXTENSION = "png"; //$NON-NLS-1$

	/**
	 * Constructor.
	 */
	public ExportAction()
	{
		super(
				R.string.WINDOW_MENU_EXPORT.string(),
				new ImageIcon(R.drawable.DOCUMENT_SAVE_AS_16.url()));
	}

	@Override
	public void actionPerformed(ActionEvent e)
	{
		JFileChooser fc = new JFileChooser();
		fc.setFileFilter(new FileNameExtensionFilter(
				R.string.APP_EXTENSION_TDX.string(),
				PNG_FILE_EXTENSION));
		int answer = fc.showSaveDialog(Application.getApp());

		if (answer != JFileChooser.APPROVE_OPTION)
			return;

		File selectedFile = fc.getSelectedFile();
		if (!selectedFile.getName().endsWith(PNG_FILE_EXTENSION))
			selectedFile = new File(selectedFile.getPath()
					+ FileUtil.EXTENSION_SEPARATOR + PNG_FILE_EXTENSION);

		if (selectedFile.exists())
		{
			int answer2 = JOptionPane.showConfirmDialog(
					Application.getApp(),
					String.format(
							R.string.DLG_QUESTION_SAVE_OVERWRITE.string(),
							selectedFile.getName()),
					R.string.DLG_QUESTION_SAVE_OVERWRITE_TITLE.string(),
					JOptionPane.YES_NO_OPTION,
					JOptionPane.QUESTION_MESSAGE);

			if (answer2 != JOptionPane.YES_OPTION)
				return;
		}

		// FIXME
		int width = 200;
		int height = 200;

		BufferedImage bi = new BufferedImage(
				width,
				height,
				BufferedImage.TYPE_INT_ARGB);

		Graphics2D gfx = bi.createGraphics();
		Application.getApp().getDrawingCanvas().render(gfx, false);

		try
		{
			ImageIO.write(bi, PNG_FILE_EXTENSION, selectedFile);
		}
		catch (IOException e1)
		{
			// TODO
		}
	}
}
