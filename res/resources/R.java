package resources;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Class for global resource access.
 * 
 * @author mpdeimos
 *
 */
public class R {
	/** resource bundle for strings */
	public static final ResourceBundle strings = ResourceBundle.getBundle("strings.strings"); //$NON-NLS-1$
	/** image resource provider */
	public static final ResourceProvider drawable = new ResourceProvider();
	
	/**
	 * Provides image resolution
	 */
	public static class ResourceProvider
	{
		/**
		 * @return the URL for the given image. prepends "/drawable/", appends ".png".
		 */
		public URL getURL(String name)
		{
			return R.class.getResource(String.format("/drawable/%s.png", name)); //$NON-NLS-1$
		}
	}
}
