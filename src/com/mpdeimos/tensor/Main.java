/**
 * 
 */
package com.mpdeimos.tensor;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;

import com.mpdeimos.tensor.util.Log;
import com.mpdeimos.tensor.util.Log.LogLevel;

/**
 * The main class for the Tensor program.
 * 
 * @author mpdeimos
 *
 */
public class Main
{
	public static final String LOG_TAG = "main";
	
	public static void main(String[] args)
	{
		Log.setLevel(LogLevel.DEBUG); // TODO add cli switch
		Log.d(LOG_TAG, "started...");
		
		// global exception handler 
		try
		{
			Display display = new Display();
			Shell shell = new Shell(display);
			Label label = new Label(shell, SWT.NONE);
			label.setText("Hello World");
			label.pack();
			shell.setSize(300, 400);
			shell.open();
			while (!shell.isDisposed()) {
				if (!display.readAndDispatch())
					display.sleep();
			}
			display.dispose();
		}
		catch (Exception e)
		{
			Log.e(LOG_TAG, "Catched uncought exception", e);
			System.exit(0);
		}
	}
}
