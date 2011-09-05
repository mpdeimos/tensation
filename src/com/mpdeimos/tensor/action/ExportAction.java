package com.mpdeimos.tensor.action;

import com.mpdeimos.tensor.ui.Application;
import com.mpdeimos.tensor.ui.ContextPanelContentBase;
import com.mpdeimos.tensor.ui.DividerLabel;
import com.mpdeimos.tensor.util.FileUtil;
import com.mpdeimos.tensor.util.LayoutUtil;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.Box;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;
import javax.swing.border.EmptyBorder;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileNameExtensionFilter;

import resources.R;

/**
 * Saves the current model to an xml file.
 * 
 * @author mpdeimos
 * 
 */
public class ExportAction extends ActionBase
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

		ContextPanel contextPanel = new ContextPanel();
		fc.setAccessory(contextPanel);
		fc.setDialogTitle(R.string.DLG_EXPORT_TITLE.string());

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

		double scale = ((Integer) contextPanel.model.getValue() / 100.0);
		Rectangle rect = Application.getApp().getDrawingCanvas().getImageRectangle();
		int width = (int) ((rect.width + Math.abs(rect.x)) * scale);
		int height = (int) ((rect.height + Math.abs(rect.y)) * scale);

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

		gfx.scale(scale, scale);
		gfx.translate(-rect.x, -rect.y);

		Application.getApp().getDrawingCanvas().render(gfx, false);

		gfx.dispose();

		bufferedImage = bufferedImage.getSubimage(
				0,
				0,
				(int) (rect.width * scale),
				(int) (rect.height * scale));

		try
		{
			ImageIO.write(bufferedImage, selectedExtension, selectedFile);
		}
		catch (IOException e1)
		{
			// TODO
		}
	}

	/** The Context Panel for the file browser. */
	private class ContextPanel extends ContextPanelContentBase
	{
		/** the number model. */
		private final SpinnerNumberModel model;

		/** Constructor. */
		public ContextPanel()
		{
			LayoutUtil.setWidth(this, 100);
			setBorder(new EmptyBorder(0, 10, 0, 0));
			setToolTipText(R.string.DLG_EXPORT_OPTION_SCALE_TOOLTIP.string());

			DividerLabel label = new DividerLabel(
						R.string.DLG_EXPORT_OPTION_SCALE.string());
			this.add(label);

			this.model = new SpinnerNumberModel(100, 10, 2000, 10);
			JSpinner spinner = new JSpinner(this.model);
			LayoutUtil.setHeight(spinner, 25);
			spinner.setToolTipText(R.string.DLG_EXPORT_OPTION_SCALE_TOOLTIP.string());
			this.add(spinner);

			this.add(Box.createVerticalGlue());
		}
	}

}
