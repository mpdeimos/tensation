/**
 * 
 */
package com.mpdeimos.tensor;

import com.mpdeimos.tensor.ui.ApplicationWindow;
import com.mpdeimos.tensor.util.Log;
import com.mpdeimos.tensor.util.Log.LogLevel;

/**
 * The main class for the Tensor Program.
 * 
 * @author mpdeimos
 *
 */
public class Main
{
	/**
	 * application entry point 
	 */
	public static void main(String[] args)
	{
		Log.setLevel(LogLevel.VERBOSE); // TODO add cli switch
		Log.v(Main.class, "started..."); //$NON-NLS-1$
		
		ApplicationWindow.createAndDisplay();
	}
}
