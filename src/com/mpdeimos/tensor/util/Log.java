/**
 * 
 */
package com.mpdeimos.tensor.util;

/**
 * Simple class with static logging functions. At the moment this is just logging to the console. 
 * 
 * @author mpdeimos
 *
 */
public class Log {
	public static enum LogLevel
	{
		DEBUG("DEBUG"),
		INFO("INFO"),
		WARN("WARN"),
		ERROR("ERROR");
		
		private String prefix;

		LogLevel(String prefix)
		{
			this.prefix = prefix;
		}
		
		protected String getPrefix()
		{
			return this.prefix;
		}
	}

	// internal log level
	private static LogLevel level = LogLevel.WARN;
	
	public static void setLevel(LogLevel level)
	{
		Log.level = level;
	}
	
	/**
	 * Logs a message with level DEBUG.
	 * 
	 * @param tag		The message tag.
	 * @param message	The message to log.
	 */
	public static void d(String tag, String message)
	{
		log(LogLevel.DEBUG, tag, message);
	}
	
	/**
	 * Logs a message with level INFO.
	 * 
	 * @param tag		The message tag.
	 * @param message	The message to log.
	 */
	public static void i(String tag, String message)
	{
		log(LogLevel.INFO, tag, message);
	}
	
	/**
	 * Logs a message with level INFO.
	 * 
	 * @param tag		The message tag.
	 * @param message	The message to log.
	 */
	public static void w(String tag, String message)
	{
		log(LogLevel.WARN, tag, message);
	}
	
	/**
	 * Logs a message with level ERROR.
	 * 
	 * @param tag		The message tag.
	 * @param message	The message to log.
	 */
	public static void e(String tag, String message)
	{
		log(LogLevel.ERROR, tag, message);
	}
	
	/**
	 * Logs an exception with level ERROR.
	 * 
	 * @param tag		The message tag.
	 * @param message	The message to log.
	 */
	public static void e(String tag, Throwable t)
	{
		StringBuilder message = new StringBuilder();
		message.append(t.getMessage());
		message.append(t.getStackTrace());
		log(LogLevel.ERROR, tag, message.toString());
	}
	
	/**
	 * Logs an exception with level ERROR.
	 * 
	 * @param tag		The message tag.
	 * @param message	The message to log.
	 */
	public static void e(String tag, String message, Throwable t)
	{
		StringBuilder messageBuilder = new StringBuilder();
		if (!StringUtil.isNullOrEmpty(message))
		{
			messageBuilder.append(message);
			messageBuilder.append("\n");
		}
		messageBuilder.append(t.getClass().getName());
		messageBuilder.append("\n");
		if (!StringUtil.isNullOrEmpty(t.getMessage()))
		{
			messageBuilder.append(t.getMessage());
			messageBuilder.append("\n");
		}
		for (StackTraceElement e : t.getStackTrace())
		{
			messageBuilder.append("\t" + e.toString());
			messageBuilder.append("\n");
		}
		
		log(LogLevel.ERROR, tag, messageBuilder.toString());
	}
	
	/**
	 * Internal logging.
	 */
	private static void log(LogLevel level, String tag, String message)
	{
		if (level.ordinal() < Log.level.ordinal())
			return;
		
		String out = String.format("[%s] [%s] %s", level.getPrefix(), tag, message);
		if (level.equals(LogLevel.ERROR))
			System.err.println(out);
		else
			System.out.println(out);
	}

}
