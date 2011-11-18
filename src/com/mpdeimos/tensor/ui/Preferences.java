package com.mpdeimos.tensor.ui;

import java.util.prefs.BackingStoreException;

/**
 * Application Preferences
 * 
 * @author mpdeimos
 */
public class Preferences
{
	public static final String WINDOW_X = "wnd_x";
	public static final String WINDOW_Y = "wnd_y";
	public static final String WINDOW_H = "wnd_h";
	public static final String WINDOW_W = "wnd_w";
	public static final String SAVE_LOCATION = "save_location";

	public static java.util.prefs.Preferences get()
	{
		return java.util.prefs.Preferences.userNodeForPackage(Preferences.class);
	}

	public static void save()
	{
		try
		{
			get().flush();
			get().sync();
		}
		catch (BackingStoreException e)
		{
			// swallow
		}
	}
}
