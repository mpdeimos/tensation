package com.mpdeimos.tensation.action;

import com.mpdeimos.tensation.impex.tdg.TdgImporter;
import com.mpdeimos.tensation.model.ModelRoot;
import com.mpdeimos.tensation.ui.Application;
import com.mpdeimos.tensation.util.Log;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.io.File;

import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.KeyStroke;
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
				R.string.WINDOW_MENU_FILE_OPEN.string(),
				new ImageIcon(R.drawable.DOCUMENT_OPEN_16.url()),
				KeyStroke.getKeyStroke(
						KeyEvent.VK_O, ActionEvent.CTRL_MASK));
	}

	@Override
	public void actionPerformed(ActionEvent e)
	{
		JFileChooser fc = Application.getApp().createFileChooser();
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

		Application app = Application.getApp();

		app.createNewCanvas();
		app.getActiveCanvas().setModel(mr);
		app.getActiveCanvas().setModelExportLocation(file);
	}
}
