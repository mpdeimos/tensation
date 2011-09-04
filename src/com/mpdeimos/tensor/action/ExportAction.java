package com.mpdeimos.tensor.action;

import com.mpdeimos.tensor.ui.Application;
import com.mpdeimos.tensor.util.FileUtil;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.AbstractAction;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileFilter;
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
	/**
	 * Constructor.
	 */
	public ExportAction()
	{
		super(
				R.string.WINDOW_MENU_EXPORT.string(),
				new ImageIcon(R.drawable.DOCUMENT_EXPORT_16.url()));
	}

	@Override
	public void actionPerformed(ActionEvent e)
	{
		JFileChooser fc = new JFileChooser();
		fc.setAcceptAllFileFilterUsed(false);
		FileFilter ffBmp = new FileNameExtensionFilter(
				R.string.APP_EXTENSION_BMP.string(),
				FileUtil.FILE_EXTENSION_BMP);
		fc.addChoosableFileFilter(ffBmp);

		FileFilter ffGif = new FileNameExtensionFilter(
				R.string.APP_EXTENSION_GIF.string(),
				FileUtil.FILE_EXTENSION_GIF);
		fc.addChoosableFileFilter(ffGif);

		FileNameExtensionFilter ffJpg = new FileNameExtensionFilter(
				R.string.APP_EXTENSION_JPG.string(),
				FileUtil.FILE_EXTENSION_JPG);
		fc.addChoosableFileFilter(ffJpg);

		FileFilter ffPng = new FileNameExtensionFilter(
				R.string.APP_EXTENSION_PNG.string(),
				FileUtil.FILE_EXTENSION_PNG);
		fc.addChoosableFileFilter(ffPng);

		int answer = fc.showSaveDialog(Application.getApp());

		if (answer != JFileChooser.APPROVE_OPTION)
			return;

		FileFilter ff = fc.getFileFilter();
		String selectedExtension = FileUtil.FILE_EXTENSION_PNG;

		// ref comp ok
		if (ff == ffGif)
			selectedExtension = FileUtil.FILE_EXTENSION_GIF;
		else if (ff == ffBmp)
			selectedExtension = FileUtil.FILE_EXTENSION_BMP;
		else if (ff == ffJpg)
			selectedExtension = FileUtil.FILE_EXTENSION_JPG;
		else if (ff == ffPng)
			selectedExtension = FileUtil.FILE_EXTENSION_PNG;

		File selectedFile = fc.getSelectedFile();
		if (!selectedFile.getName().endsWith(selectedExtension))
			selectedFile = new File(selectedFile.getPath()
					+ FileUtil.EXTENSION_SEPARATOR
					+ selectedExtension);

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

		Rectangle rect = Application.getApp().getDrawingCanvas().getImageRectangle();
		int width = rect.width + Math.abs(rect.x);
		int height = rect.height + Math.abs(rect.y);

		boolean hasTransparency = ff == ffPng;
		int imgType = hasTransparency ? BufferedImage.TYPE_INT_ARGB
				: BufferedImage.TYPE_INT_RGB;

		BufferedImage bufferedImage = new BufferedImage(
				width,
				height,
				imgType);

		Graphics2D gfx = bufferedImage.createGraphics();

		if (!hasTransparency)
		{
			gfx.setColor(Color.WHITE);
			gfx.fillRect(0, 0, width, height);
		}

		gfx.translate(-rect.x, -rect.y);

		Application.getApp().getDrawingCanvas().render(gfx, false);

		gfx.dispose();

		bufferedImage = bufferedImage.getSubimage(
				0,
				0,
				rect.width,
				rect.height);

		try
		{
			ImageIO.write(bufferedImage, selectedExtension, selectedFile);
		}
		catch (IOException e1)
		{
			// TODO
		}
	}
}
