/**
 * 
 */
package com.mpdeimos.swt.loader;

import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Properties;

import org.eclipse.jdt.internal.jarinjarloader.RsrcURLStreamHandlerFactory;

/**
 * SWT cross platform loader according to stackoverflow.com/questions/2706222/create-cross-platform-java-swt-application/5784073#5784073
 * 
 * @author mpdeimos
 * 
 */
public class SWTLoader {
	private static String className;
	private static String jarName;

	public static void main(String[] args) throws Throwable {
		Properties props = new Properties();
		InputStream propsStream = SWTLoader.class.getResourceAsStream("/swtloader.properties");
		if (propsStream == null)
		{
			System.err.println("Launch failed: Failed to find swtloader.properties");
			System.exit(0);
		}
		
		props.load(propsStream);
		
		jarName = props.getProperty("jar");
		System.err.println(jarName);
		if (jarName == null)
		{
			System.err.println("Launch failed: jar property not set");
			System.exit(0);
		}	
		
		className = props.getProperty("class");
		if (className == null)
		{
			System.err.println("Launch failed: class property not set");
			System.exit(0);
		}	
		
		ClassLoader cl = getSWTClassloader();
		Thread.currentThread().setContextClassLoader(cl);
		
		try {
			try {
				Class<?> c = Class.forName(className, true, cl);
				Method main = c.getMethod("main",
						new Class[] { args.getClass() });
				main.invoke((Object) null, new Object[] { args });
			} catch (InvocationTargetException ex) {
				if (ex.getCause() instanceof UnsatisfiedLinkError) {
					System.err.println("Launch failed: (UnsatisfiedLinkError)");
					String arch = getArch();
					if ("32".equals(arch)) {
						System.err
								.println("Try adding '-d64' to your command line arguments");
					} else if ("64".equals(arch)) {
						System.err
								.println("Try adding '-d32' to your command line arguments");
					}
				} else {
					throw ex;
				}
			}
		} catch (ClassNotFoundException ex) {
			System.err
					.println("Launch failed: Failed to find main class " + className);
		} catch (NoSuchMethodException ex) {
			System.err.println("Launch failed: Failed to find main method");
		} catch (InvocationTargetException ex) {
			Throwable th = ex.getCause();
			if ((th.getMessage() != null)
					&& th.getMessage().toLowerCase()
							.contains("invalid thread access")) {
				System.err
						.println("Launch failed: (SWTException: Invalid thread access)");
				System.err
						.println("Try adding '-XstartOnFirstThread' to your command line arguments");
			} else {
				throw th;
			}
		}
	}

	private static ClassLoader getSWTClassloader() {
		ClassLoader parent = SWTLoader.class.getClassLoader();
		URL.setURLStreamHandlerFactory(new RsrcURLStreamHandlerFactory(parent));
		String swtFileName = getSwtJarName();
		try {
			URL wrappedJarUrl = new URL("rsrc:" + jarName);
			URL swtFileUrl = new URL("rsrc:" + swtFileName);
			System.err.println("Using SWT Jar: " + swtFileName);
			ClassLoader cl = new URLClassLoader(new URL[] { wrappedJarUrl,
					swtFileUrl }, parent);
			
			try {
				// Check we can now load the SWT class
				Class.forName("org.eclipse.swt.widgets.Layout", true, cl);
			} catch (ClassNotFoundException exx) {
				System.err
						.println("Launch failed: Failed to load SWT class from jar: "
								+ swtFileName);
				throw new RuntimeException(exx);
			}

			return cl;
		} catch (MalformedURLException exx) {
			throw new RuntimeException(exx);
		}
	}

	private static String getSwtJarName() {
		// Detect OS
		String osName = System.getProperty("os.name").toLowerCase();
		String swtFileNameOsPart = osName.contains("win") ? "win" : osName
				.contains("mac") ? "osx" : osName.contains("linux")
				|| osName.contains("nix") ? "linux" : "";
		if ("".equals(swtFileNameOsPart)) {
			throw new RuntimeException("Launch failed: Unknown OS name: "
					+ osName);
		}

		// Detect 32bit vs 64 bit
		String swtFileNameArchPart = getArch();

		String swtFileName = "swt-" + swtFileNameOsPart + swtFileNameArchPart
				+ "-3.6.2.jar";
		return swtFileName;
	}

	private static String getArch() {
		// Detect 32bit vs 64 bit
		String jvmArch = System.getProperty("os.arch").toLowerCase();
		String arch = (jvmArch.contains("64") ? "64" : "32");
		return arch;
	}
}
