package com.mpdeimos.tensation.action;

import com.mpdeimos.tensation.impex.TdgExporter;
import com.mpdeimos.tensation.model.ModelRoot;
import com.mpdeimos.tensation.ui.Application;
import com.mpdeimos.tensation.util.FileUtil;
import com.mpdeimos.tensation.util.Log;
import com.mpdeimos.tensation.util.XmlUtil;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.io.File;

import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.KeyStroke;
import javax.swing.filechooser.FileNameExtensionFilter;

import org.w3c.dom.Document;

import resources.R;

/**
 * Saves the current model to an xml file.
 * 
 * @author mpdeimos
 * 
 */
public class SaveAsAction extends ActionBase
{
	/** file extension of the exported xml. */
	public static final String XML_FILE_EXTENSION = "tdx"; //$NON-NLS-1$

	/**
	 * Constructor.
	 */
	public SaveAsAction()
	{
		super(
				R.string.WINDOW_MENU_FILE_SAVEAS.string(),
				new ImageIcon(R.drawable.DOCUMENT_SAVE_AS_16.url()),
				KeyStroke.getKeyStroke(
						KeyEvent.VK_S, ActionEvent.CTRL_MASK
								+ ActionEvent.SHIFT_MASK));
	}

	@Override
	public void actionPerformed(ActionEvent e)
	{
		JFileChooser fc = Application.getApp().createFileChooser();
		fc.setFileFilter(new FileNameExtensionFilter(
				R.string.APP_EXTENSION_TDX.string(),
				XML_FILE_EXTENSION));
		int answer = fc.showSaveDialog(Application.getApp());

		if (answer != JFileChooser.APPROVE_OPTION)
			return;

		saveFile(fc.getSelectedFile());
	}

	/** Saves the currently open file. */
	public static void saveFile(String filename)
	{
		saveFile(new File(filename));
	}

	/** Saves the currently open file. */
	public static void saveFile(File selectedFile)
	{
		ModelRoot model = Application.getApp().getActiveCanvas().getModel();
		TdgExporter ex = new TdgExporter();

		Document doc = ex.toXml(model);

		if (!selectedFile.getName().endsWith(XML_FILE_EXTENSION))
			selectedFile = new File(selectedFile.getPath()
					+ FileUtil.EXTENSION_SEPARATOR + XML_FILE_EXTENSION);

		if (selectedFile.exists())
		{
			// TODO commandline!
			final int answer2 = JOptionPane.showConfirmDialog(
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

		if (!XmlUtil.writeDomDocumentToFile(doc, selectedFile))
		{
			Log.e(
					SaveAsAction.class,
					"saving file '" + selectedFile.getPath() + "' failed..."); //$NON-NLS-1$ //$NON-NLS-2$
			// TODO show dialog
		}

		Application.getApp().getActiveCanvas().setModelExportLocation(
				selectedFile);
	}
}
