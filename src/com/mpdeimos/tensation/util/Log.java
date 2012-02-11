/**
 * 
 */
package com.mpdeimos.tensation.util;

/**
 * Simple class with static logging functions. At the moment this is just
 * logging to the console.
 * 
 * @author mpdeimos
 * 
 */
public class Log
{
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
		private final String prefix;

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

	/** sets the global logging level from commandline arguments. */
	public static void setLevelFromCommandline(
			String[] args,
			LogLevel defaultValue)
	{
		setLevel(defaultValue);
		for (String arg : args)
		{
			if ("--verbose".equals(arg)) //$NON-NLS-1$
			{
				setLevel(LogLevel.VERBOSE);
			}
			else if ("--debug".equals(arg)) //$NON-NLS-1$
			{
				setLevel(LogLevel.DEBUG);
			}
			else if ("--info".equals(arg)) //$NON-NLS-1$
			{
				setLevel(LogLevel.INFO);
			}
			else if ("--warn".equals(arg)) //$NON-NLS-1$
			{
				setLevel(LogLevel.WARN);
			}
			else if ("--error".equals(arg)) //$NON-NLS-1$
			{
				setLevel(LogLevel.ERROR);
			}
		}
	}

	/**
	 * Logs a message with level VERBOSE.
	 * 
	 * @param tag
	 *            The message tag.
	 * @param message
	 *            The message to log.
	 */
	public static void v(Object tag, String message, Object... args)
	{
		log(LogLevel.VERBOSE, tag, message, args);
	}

	/**
	 * Logs a message with level DEBUG.
	 * 
	 * @param tag
	 *            The message tag.
	 * @param message
	 *            The message to log.
	 */
	public static void d(Object tag, String message, Object... args)
	{
		log(LogLevel.DEBUG, tag, message, args);
	}

	/**
	 * Logs a message with level INFO.
	 * 
	 * @param tag
	 *            The message tag.
	 * @param message
	 *            The message to log.
	 */
	public static void i(Object tag, String message, Object... args)
	{
		log(LogLevel.INFO, tag, message, args);
	}

	/**
	 * Logs a message with level INFO.
	 * 
	 * @param tag
	 *            The message tag.
	 * @param message
	 *            The message to log.
	 */
	public static void w(Object tag, String message, Object... args)
	{
		log(LogLevel.WARN, tag, message, args);
	}

	/**
	 * Logs a message with level ERROR.
	 * 
	 * @param tag
	 *            The message tag.
	 * @param message
	 *            The message to log.
	 */
	public static void e(Object tag, String message, Object... args)
	{
		log(LogLevel.ERROR, tag, message, args);
	}

	/**
	 * Logs an exception with level ERROR.
	 * 
	 * @param tag
	 *            The message tag.
	 * @param message
	 *            The message to log.
	 */
	public static void e(Object tag, Throwable t)
	{
		e(tag, null, t);
	}

	/**
	 * Logs an exception with level ERROR.
	 * 
	 * @param tag
	 *            The message tag.
	 * @param message
	 *            The message to log.
	 */
	public static void e(Object tag, String message, Throwable t)
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
	private static void log(
			LogLevel level,
			Object tag,
			String message,
			Object... args)
	{
		if (level.ordinal() < Log.level.ordinal())
			return;

		if (tag instanceof Class<?>)
		{
			tag = ((Class<?>) tag).getSimpleName();
		}
		else if (!(tag instanceof String))
		{
			tag = tag.getClass().getSimpleName();
		}

		String fmt = String.format(message, args);
		String out = String.format(
				" %7s | %16.16s | %18.18s |  %s", level.getPrefix(), Thread.currentThread().getName(), tag, fmt); //$NON-NLS-1$
		if (level.equals(LogLevel.ERROR))
			System.err.println(out);
		else
			System.out.println(out);
	}
}
