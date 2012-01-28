package com.mpdeimos.tensation.ui;

import com.mpdeimos.tensation.action.ExportAction;
import com.mpdeimos.tensation.action.OpenAction;
import com.mpdeimos.tensation.action.SaveAsAction;
import com.mpdeimos.tensation.util.Log;
import com.mpdeimos.tensation.util.StringUtil;

import java.util.Arrays;

import resources.R;

/**
 * Enumeration of all Commandline Tasks
 *
 * @author mpdeimos
 */
public enum Commandline
{
	/** Open commandline action. */
	OPEN(new CommandlineTask()
	{
		@Override
		public int run(String[] args)
		{
			OpenAction.openFile(args[0]);
			return 1;
		}

	}, R.string.CMD_OPEN.string(), "-o", "--open"), //$NON-NLS-1$//$NON-NLS-2$

	/** Open commandline action. */
	SAVE(new CommandlineTask()
	{
		@Override
		public int run(String[] args)
		{
			SaveAsAction.saveFile(args[0]);
			return 1;
		}

	}, R.string.CMD_SAVE.string(), "-s", "--save"), //$NON-NLS-1$//$NON-NLS-2$

	/** Open commandline action. */
	EXPORT(new CommandlineTask()
	{
		@Override
		public int run(String[] args)
		{
			ExportAction.exportFile(args[0]);
			return 1;
		}

	}, R.string.CMD_EXPORT.string(), "-x", "--export"), //$NON-NLS-1$//$NON-NLS-2$

	/** Quit commandline action. */
	QUIT(new CommandlineTask()
	{
		@Override
		public int run(String[] args)
		{
			System.exit(0);
			return 0;
		}

	}, R.string.CMD_QUIT.string(), "-q", "--quit"), //$NON-NLS-1$//$NON-NLS-2$

	/** Help commandline action. */
	HELP(new CommandlineTask()
	{
		@Override
		public int run(String[] args)
		{
			String argFmt = "%-16.16s %s"; //$NON-NLS-1$
			System.out.println(R.string.CMD_HELP_CONTENT_PRE.string());
			System.out.println();
			for (Commandline cmd : Commandline.values())
			{
				System.out.println(
						String.format(
								argFmt,
								StringUtil.flatten(" ", cmd.switches), cmd.description)); //$NON-NLS-1$
			}
			System.out.println();
			System.out.println(R.string.CMD_HELP_CONTENT_POST.string());
			System.exit(0);
			return 0;
		}

	}, R.string.CMD_HELP.string(), "-h", "--help"), //$NON-NLS-1$//$NON-NLS-2$

	;

	/** the commandline task */
	private final CommandlineTask task;

	/** the commandline switches. */
	private final String[] switches;

	/** commandline task help description. */
	private final String description;

	/** Constructor. */
	Commandline(CommandlineTask task, String description, String... switches)
	{
		this.task = task;
		this.description = description;
		this.switches = switches;
	}

	/** runs the task and returns the number of processed arguments. */
	private int run(String[] args)
	{
		try
		{
			return this.task.run(args);
		}
		catch (Exception e)
		{
			Log.e(this, e);
			return 0;
		}
	}

	/**
	 * Commandline task interface
	 */
	interface CommandlineTask
	{
		/** runs the task and returns the number of processed arguments. */
		public abstract int run(String[] args);
	}

	/** Evaluates command-line arguments. */
	public static void evaluateArguments(String[] args)
	{
		for (int i = 0; i < args.length; i++)
		{
			String arg = args[i];

			for (Commandline cmd : Commandline.values())
			{
				if (cmd.canHandleSwicth(arg))
				{
					i += cmd.run(Arrays.copyOfRange(args, i + 1, args.length));
				}
			}
		}
	}

	/** Checks whether the switch is handled by this commandline action. */
	private boolean canHandleSwicth(String arg)
	{
		for (String swtch : this.switches)
		{
			if (swtch.equalsIgnoreCase(arg))
			{
				return true;
			}
		}
		return false;
	}
}
