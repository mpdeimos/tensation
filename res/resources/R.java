package resources;

import com.mpdeimos.ant.resourcecompiler.DrawableResourceEnum;
import com.mpdeimos.ant.resourcecompiler.ILogger;
import com.mpdeimos.ant.resourcecompiler.RBase;
import com.mpdeimos.ant.resourcecompiler.StringResourceEnum;

/**
 * Class for global resource access.
 *
 * THIS CLASS IS CREATED DYNAMICALLY, DO NOT EDIT BY HAND!
 *
 * @author AntResourceCompiler <http://github.com/mpdeimos/AntResourceCompiler>
 *
 */
 @SuppressWarnings("all")
public class R extends RBase
{
	/** string resource provider */
	public static enum string implements StringResourceEnum
	{
		/** Value: Tensor */
		WINDOW_MAIN_TITLE,
		/** Value: foo */
		YET_ANOTHER_ONEE,
		/** Value: No adjustments */
		WINDOW_CONTEXTPANEL_DISABLED,
		/** Value: A connection consists of exactly one sink and one source */
		EXCEPTION_MODEL_CONNECTION_MISSMATCH,
		/** Value: Catched uncought exception */
		EXCEPTION_UNCOUGHT,
		/** Value: Select Item */
		WINDOW_ACTION_SELECT,
		/** Value: Select tensor */
		WINDOW_CONTEXTPANEL_DRAWTENSOR_SELECT,
		/** Value: Draw Tensor */
		WINDOW_ACTION_DRAWTENSOR,
		/** Value: Exit */
		WINDOW_MENU_FILE_EXIT,
		/** Value: Connect Tensors */
		WINDOW_ACTION_CONNECT,
		/** Value: File */
		WINDOW_MENU_FILE,
		;

		@Override
		public String string()
		{
			return StringResourceEnum.StringResourceEnumResolver.string(this);
		}
	}

	/** image resource provider */
	public static enum drawable implements DrawableResourceEnum
	{
		/** circle_green.png */
		CIRCLE_GREEN,
		/** draw_tensor.png */
		DRAW_TENSOR,
		/** window_exit.png */
		WINDOW_EXIT,
		/** default.png */
		DEFAULT,
		/** select.png */
		SELECT,
		/** action_connect.png */
		ACTION_CONNECT,
		/** overlay_rotate.png */
		OVERLAY_ROTATE,
		;

		@Override
		public java.net.URL url()
		{
			return DrawableResourceEnum.DrawableResourceEnumResolver.url(this);
		}
	}

	public static void setLogger(ILogger logger)
	{
		RBase.setLogger(logger);
	}
}
