package com.mpdeimos.tensation.util;

import java.io.File;

/**
 * Utility class for file functions.
 * 
 * @author mpdeimos
 * 
 */
public class FileUtil
{
	/** file extension for png files. */
	public static final String FILE_EXTENSION_PNG = "png"; //$NON-NLS-1$
	/** file extension for gif files. */
	public static final String FILE_EXTENSION_GIF = "gif"; //$NON-NLS-1$
	/** file extension for jpg files. */
	public static final String FILE_EXTENSION_JPG = "jpg"; //$NON-NLS-1$
	/** file extension for bmp files. */
	public static final String FILE_EXTENSION_BMP = "bmp"; //$NON-NLS-1$
	/** file extension for svg files. */
	public static final String FILE_EXTENSION_SVG = "svg"; //$NON-NLS-1$

	/** Separator for file extension and filename. */
	public static final String EXTENSION_SEPARATOR = "."; //$NON-NLS-1$

	/** @return the extension of a file. */
	public static String getExtension(File file)
	{
		int idx = file.getName().lastIndexOf(EXTENSION_SEPARATOR);
		return file.getName().substring(idx + 1);
	}
}
