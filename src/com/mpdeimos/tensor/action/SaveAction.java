package com.mpdeimos.tensor.action;

import com.mpdeimos.tensor.impex.Exporter;
import com.mpdeimos.tensor.model.ModelRoot;
import com.mpdeimos.tensor.ui.Application;
import com.mpdeimos.tensor.util.Log;
import com.mpdeimos.tensor.util.XmlUtil;

import java.awt.event.ActionEvent;
import java.io.File;

import javax.swing.AbstractAction;
import javax.swing.ImageIcon;

import org.w3c.dom.Document;

import resources.R;

/**
 * Saves the current model to an xml file.
 * 
 * @author mpdeimos
 * 
 */
public class SaveAction extends AbstractAction
{
	/**
	 * Constructor.
	 */
	public SaveAction()
	{
		super(
				R.string.WINDOW_MENU_SAVE.string(),
				new ImageIcon(R.drawable.DOCUMENT_SAVE_16.url()));
	}

	@Override
	public void actionPerformed(ActionEvent e)
	{
		File exportLocation = Application.getApp().getModelExportLocation();

		if (exportLocation == null)
		{
			new SaveAsAction().actionPerformed(e);
			return;
		}

		ModelRoot model = Application.getApp().getModel();
		Exporter ex = new Exporter();

		Document doc = ex.toXml(model);

		if (!XmlUtil.writeDomDocumentToFile(doc, exportLocation))
		{
			Log.e(this, "saving file failed..."); //$NON-NLS-1$
			// TODO show dialog
		}
	}
}
