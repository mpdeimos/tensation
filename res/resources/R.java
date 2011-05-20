package resources;

import java.net.URL;
import java.util.ResourceBundle;

import com.mpdeimos.tensor.util.Log;

/**
 * Class for global resource access.
 * 
 * @author mpdeimos
 *
 */
public class R {
	/** log tag */
	private static final String LOG_TAG = "R" ; //$NON-NLS-1$
	
	/** string resource provider */
	public static final StringResourceProvider strings = new StringResourceProvider();
	
	/** image resource provider */
	public static final DrawableResourceProvider drawable = new DrawableResourceProvider();
	
	/**
	 * Provides images
	 */
	public static class DrawableResourceProvider
	{
		/**
		 * @return the URL for the given image. prepends "/drawable/", appends ".png".
		 */
		public URL getURL(String name)
		{
			URL url = R.class.getResource(String.format("/drawable/%s.png", name)); //$NON-NLS-1$
			if (url == null)
			{
				Log.w(LOG_TAG, "Drawable not found: " + name); //$NON-NLS-1$
				url = R.class.getResource(String.format("/drawable/default.png", name)); //$NON-NLS-1$
			}
			
			return url;
		}
	}
	
	/**
	 * provides string resources
	 */
	public static class StringResourceProvider
	{
		/** default string if resource not found */
		private static final String NOT_FOUND = "## ERROR ##" ; //$NON-NLS-1$
		
		/** resource bundle for strings */
		public static final ResourceBundle resourceBundle = ResourceBundle.getBundle("strings.strings"); //$NON-NLS-1$

		/** @return the string for a given key */
		public String getString(String key)
		{
			String str = null;
			try
			{
				str = resourceBundle.getString(key);
			}
			catch (Exception e)
			{
				// Swallow, will be handled in finally
			}
			finally
			{
				if (str == null)
				{
					Log.w(LOG_TAG, "String not found: " + key); //$NON-NLS-1$
					str = NOT_FOUND;
				}
			}
			
			return str;
		}
	}
}
