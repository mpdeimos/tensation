package com.mpdeimos.tensation.ui;

import java.util.prefs.BackingStoreException;

/**
 * Application Preferences
 * 
 * @author mpdeimos
 */
public class Preferences
{
	/** window x position */
	public static final String WINDOW_X = "wnd_x"; //$NON-NLS-1$
	/** window y position */
	public static final String WINDOW_Y = "wnd_y"; //$NON-NLS-1$
	/** window height */
	public static final String WINDOW_H = "wnd_h"; //$NON-NLS-1$
	/** window width */
	public static final String WINDOW_W = "wnd_w"; //$NON-NLS-1$
	/** app save location */
	public static final String SAVE_LOCATION = "save_location"; //$NON-NLS-1$
	/** window maximized state */
	public static final String WINDOW_MAXIMIZED = "wnd_maximized"; //$NON-NLS-1$

	/** @return the app preferences */
	public static java.util.prefs.Preferences get()
	{
		return java.util.prefs.Preferences.userNodeForPackage(Preferences.class);
	}

	/** saves the app preferences */
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
