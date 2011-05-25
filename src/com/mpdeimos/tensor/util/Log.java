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
	/**
	 * Enumeration of different logging levels, sorted by severity.
	 */
	public static enum LogLevel
	{
		/** verbose logging level */
		VERBOSE("VERBOSE"), //$NON-NLS-1$
		
		/** debug logging level */
		DEBUG("DEBUG"), //$NON-NLS-1$
		
		/** info logging level */
		INFO("INFO"), //$NON-NLS-1$
		
		/** warning logging level */
		WARN("WARN"), //$NON-NLS-1$
		
		/** error logging level */
		ERROR("ERROR"); //$NON-NLS-1$
		
		/** string representation of logging level */
		private String prefix;

		/**
		 * Constructor.
		 */
		LogLevel(String prefix)
		{
			this.prefix = prefix;
		}
		
		/**
		 * @return the human readable string representation of a logging level 
		 */
		protected String getPrefix()
		{
			return this.prefix;
		}
	}

	/** internal logging level */
	private static LogLevel level = LogLevel.WARN;
	
	/** sets the global logging level */
	public static void setLevel(LogLevel level)
	{
		Log.level = level;
	}
	
	/**
	 * Logs a message with level VERBOSE.
	 * 
	 * @param tag		The message tag.
	 * @param message	The message to log.
	 */
	public static void v(String tag, String message, Object... args)
	{
		log(LogLevel.VERBOSE, tag, message, args);
	}
	
	/**
	 * Logs a message with level DEBUG.
	 * 
	 * @param tag		The message tag.
	 * @param message	The message to log.
	 */
	public static void d(String tag, String message, Object... args)
	{
		log(LogLevel.DEBUG, tag, message, args);
	}
	
	/**
	 * Logs a message with level INFO.
	 * 
	 * @param tag		The message tag.
	 * @param message	The message to log.
	 */
	public static void i(String tag, String message, Object... args)
	{
		log(LogLevel.INFO, tag, message, args);
	}
	
	/**
	 * Logs a message with level INFO.
	 * 
	 * @param tag		The message tag.
	 * @param message	The message to log.
	 */
	public static void w(String tag, String message, Object... args)
	{
		log(LogLevel.WARN, tag, message, args);
	}
	
	/**
	 * Logs a message with level ERROR.
	 * 
	 * @param tag		The message tag.
	 * @param message	The message to log.
	 */
	public static void e(String tag, String message, Object... args)
	{
		log(LogLevel.ERROR, tag, message, args);
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
			messageBuilder.append(StringUtil.NEWLINE);
		}
		messageBuilder.append(t.getClass().getName());
		messageBuilder.append(StringUtil.NEWLINE);
		if (!StringUtil.isNullOrEmpty(t.getMessage()))
		{
			messageBuilder.append(t.getMessage());
			messageBuilder.append(StringUtil.NEWLINE);
		}
		for (StackTraceElement e : t.getStackTrace())
		{
			messageBuilder.append(StringUtil.TABULATOR + e.toString());
			messageBuilder.append(StringUtil.NEWLINE);
		}
		
		log(LogLevel.ERROR, tag, messageBuilder.toString());
	}
	
	/**
	 * Internal logging.
	 */
	private static void log(LogLevel level, String tag, String message, Object... args)
	{
		if (level.ordinal() < Log.level.ordinal())
			return;
		
		String fmt = String.format(message, args);
		String out = String.format("%7s | %18.18s | %18.18s  ||  %s", level.getPrefix(), Thread.currentThread().getName(), tag, fmt); //$NON-NLS-1$
		if (level.equals(LogLevel.ERROR))
			System.err.println(out);
		else
			System.out.println(out);
	}
}
