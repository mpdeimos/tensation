package com.mpdeimos.tensor.action;

import com.mpdeimos.tensor.impex.TdgImporter;
import com.mpdeimos.tensor.model.ModelRoot;
import com.mpdeimos.tensor.ui.Application;
import com.mpdeimos.tensor.util.Log;

import java.awt.event.ActionEvent;
import java.io.File;

import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;

import resources.R;

/**
 * Opens a model from an xml file.
 * 
 * @author mpdeimos
 * 
 */
public class OpenAction extends ActionBase
{
	/**
	 * Constructor.
	 */
	public OpenAction()
	{
		super(
				R.string.WINDOW_MENU_OPEN.string(),
				new ImageIcon(R.drawable.DOCUMENT_OPEN_16.url()));
	}

	@Override
	public void actionPerformed(ActionEvent e)
	{
		JFileChooser fc = new JFileChooser();
		fc.setFileFilter(new FileNameExtensionFilter(
				R.string.APP_EXTENSION_TDX.string(),
				SaveAsAction.XML_FILE_EXTENSION));
		int answer = fc.showOpenDialog(Application.getApp());

		if (answer != JFileChooser.APPROVE_OPTION)
			return;

		openFile(fc.getSelectedFile());
	}

	/** Opens a file by filename */
	public static void openFile(String filename)
	{
		openFile(new File(filename));
	}

	/** Opens a file via file descriptor. */
	public static void openFile(File file)
	{
		if (!file.exists())
		{
			Log.e(
					OpenAction.class,
					"open file failed, file '" + file.getPath() + "' does not exist..."); //$NON-NLS-1$ //$NON-NLS-2$
			// TODO show dialog?

			return;
		}

		TdgImporter importer = new TdgImporter();

		ModelRoot mr = importer.fromXml(file);
		// TODO check for null

		Application.getApp().setModel(mr);
		Application.getApp().setModelExportLocation(file);
	}
}
