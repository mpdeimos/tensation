package com.mpdeimos.tensation.action;

import com.mpdeimos.tensation.impex.TdgExporter;
import com.mpdeimos.tensation.model.ModelRoot;
import com.mpdeimos.tensation.ui.Application;
import com.mpdeimos.tensation.util.Log;
import com.mpdeimos.tensation.util.XmlUtil;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.io.File;

import javax.swing.ImageIcon;
import javax.swing.KeyStroke;

import org.w3c.dom.Document;

import resources.R;

/**
 * Saves the current model to an xml file.
 * 
 * @author mpdeimos
 * 
 */
public class SaveAction extends ActionBase
{
	/**
	 * Constructor.
	 */
	public SaveAction()
	{
		super(
				R.string.WINDOW_MENU_FILE_SAVE.string(),
				new ImageIcon(R.drawable.DOCUMENT_SAVE_16.url()),
				KeyStroke.getKeyStroke(
						KeyEvent.VK_S, ActionEvent.CTRL_MASK));
	}

	@Override
	public void actionPerformed(ActionEvent e)
	{
		File exportLocation = Application.getApp().getActiveCanvas().getModelExportLocation();

		if (exportLocation == null)
		{
			new SaveAsAction().actionPerformed(e);
			return;
		}

		ModelRoot model = Application.getApp().getActiveCanvas().getModel();
		TdgExporter ex = new TdgExporter();

		Document doc = ex.toXml(model);

		if (!XmlUtil.writeDomDocumentToFile(doc, exportLocation))
		{
			Log.e(this, "saving file failed..."); //$NON-NLS-1$
			// TODO show dialog
		}
	}
}
