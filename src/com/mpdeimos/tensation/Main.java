/**
 * 
 */
package com.mpdeimos.tensation;

import com.mpdeimos.ant.resourcecompiler.ILogger;
import com.mpdeimos.tensation.ui.Application;
import com.mpdeimos.tensation.util.Log;
import com.mpdeimos.tensation.util.Log.LogLevel;

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
		Log.setLevelFromCommandline(args, LogLevel.ERROR);

		R.setLogger(new ILogger()
		{
			@Override
			public void log(String msg)
			{
				Log.w(R.class, msg);

			}
		});

		Application.createAndDisplay(args);
	}
}
