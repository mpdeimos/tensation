/**
 * 
 */
package com.mpdeimos.tensor;

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
	public static final String LOG_TAG = "main";
	
	public static void main(String[] args)
	{
		Log.setLevel(LogLevel.DEBUG); // TODO add cli switch
		Log.d(LOG_TAG, "started...");
		
		// global exception handler 
		try
		{
			throw new NullPointerException();
		}
		catch (Exception e)
		{
			Log.e(LOG_TAG, "Catched uncought exception", e);
			System.exit(0);
		}
	}
}
