package com.mpdeimos.tensor.action;

import com.mpdeimos.tensor.impex.Importer;
import com.mpdeimos.tensor.model.ModelRoot;
import com.mpdeimos.tensor.ui.Application;
import com.mpdeimos.tensor.util.Log;

import java.awt.event.ActionEvent;
import java.io.File;

import javax.swing.AbstractAction;
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
public class OpenAction extends AbstractAction
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
		Importer importer = new Importer();

		JFileChooser fc = new JFileChooser();
		fc.setFileFilter(new FileNameExtensionFilter(
				R.string.APP_EXTENSION_TDX.string(),
				SaveAsAction.XML_FILE_EXTENSION));
		int answer = fc.showOpenDialog(Application.getApp());

		if (answer != JFileChooser.APPROVE_OPTION)
			return;

		File selectedFile = fc.getSelectedFile();

		if (!selectedFile.exists())
		{
			Log.e(this, "open file failed, file does not exist..."); //$NON-NLS-1$
			// TODO show dialog
		}

		ModelRoot mr = importer.fromXml(selectedFile);
		// TODO check for null

		Application.getApp().setModel(mr);
		Application.getApp().setModelExportLocation(selectedFile);
	}
}
