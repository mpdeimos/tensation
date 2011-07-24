/**
 * 
 */
package com.mpdeimos.tensor;

import com.mpdeimos.ant.resourcecompiler.ILogger;
import com.mpdeimos.tensor.ui.ApplicationWindow;
import com.mpdeimos.tensor.util.Log;
import com.mpdeimos.tensor.util.Log.LogLevel;

import resources.R;

/**
 * The main class for the Tensor Program.
 * 
 * @author mpdeimos
 */
public class Main
{
	/**
	 * application entry point
	 */
	public static void main(String[] args)
	{
		Log.setLevel(LogLevel.VERBOSE); // TODO add cli switch

		R.setLogger(new ILogger()
		{
			@Override
			public void log(String msg)
			{
				Log.w(R.class, msg);

			}
		});

		ApplicationWindow.createAndDisplay();
	}
}
