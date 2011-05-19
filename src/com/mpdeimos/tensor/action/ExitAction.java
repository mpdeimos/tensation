package com.mpdeimos.tensor.action;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

import com.mpdeimos.tensor.res.R;
import com.mpdeimos.tensor.ui.ApplicationWindow;


/**
 * Action for exiting the program.
 * 
 * @author mpdeimos
 *
 */
public class ExitAction extends AbstractAction {

	/**
	 * Constructor.
	 */
	public ExitAction() {
		super(R.strings.getString("window.menu.file.exit")); //$NON-NLS-1$
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		ApplicationWindow.getApplicationWindow().exit();
	}

}
