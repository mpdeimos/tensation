package com.mpdeimos.tensor.util;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * 
 * TODO: This is untested, needs investigation 
 * 
 * @author http://stackoverflow.com/questions/176527/how-can-i-enumerate-all-classes-in-a-package-and-add-them-to-a-list
 *
 */
public class PackageUtils {
	/** @return list of classes for a given package */
	@SuppressWarnings("nls")
	public static List<Class<?>> getClassesForPackage(Package pkg) {
		String pkgname = pkg.getName();
		ArrayList<Class<?>> classes = new ArrayList<Class<?>>();
		// Get a File object for the package
		File directory = null;
		String fullPath;
		String relPath = pkgname.replace('.', '/');

		URL resource = ClassLoader.getSystemClassLoader().getResource(relPath);
		if (resource == null) {
			throw new RuntimeException("No resource for " + relPath);
		}
		fullPath = resource.getFile();

		// try {
		directory = new File(fullPath);
		// } catch (URISyntaxException e) {
		// throw new RuntimeException(pkgname + " (" + resource +
		// ") does not appear to be a valid URL / URI.  Strange, since we got it from the system...",
		// e);
		// }

		if (directory.exists()) {
			// Get the list of the files contained in the package
			String[] files = directory.list();
			for (int i = 0; i < files.length; i++) {
				// we are only interested in .class files
				if (files[i].endsWith(".class")) {
					// removes the .class extension
					String className = pkgname + '.'
							+ files[i].substring(0, files[i].length() - 6);
					try {
						classes.add(Class.forName(className));
					} catch (ClassNotFoundException e) {
						throw new RuntimeException(
								"ClassNotFoundException loading " + className);
					}
				}
			}
		} else {
			try {
				String jarPath = fullPath.replaceFirst("[.]jar[!].*", ".jar")
						.replaceFirst("file:", "");
				JarFile jarFile = new JarFile(jarPath);
				Enumeration<JarEntry> entries = jarFile.entries();
				while (entries.hasMoreElements()) {
					JarEntry entry = entries.nextElement();
					String entryName = entry.getName();
					if (entryName.startsWith(relPath)
							&& entryName.length() > (relPath.length() + "/"
									.length())) {
						String className = entryName.replace('/', '.')
								.replace('\\', '.').replace(".class", "");
						try {
							classes.add(Class.forName(className));
						} catch (ClassNotFoundException e) {
							throw new RuntimeException(
									"ClassNotFoundException loading "
											+ className);
						}
					}
				}
			} catch (IOException e) {
				throw new RuntimeException(pkgname + " (" + directory
						+ ") does not appear to be a valid package", e);
			}
		}
		return classes;
	}
}