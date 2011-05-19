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
	/** log tag */
	public static final String LOG_TAG = "main"; //$NON-NLS-1$
	
	/**
	 * application entry point 
	 */
	public static void main(String[] args)
	{
		Log.setLevel(LogLevel.VERBOSE); // TODO add cli switch
		Log.d(LOG_TAG, "started..."); //$NON-NLS-1$
		
		ApplicationWindow.createAndDisplay();
	}
}
