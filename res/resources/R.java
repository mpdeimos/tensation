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
		/** Value: Connect Tensors */
		WINDOW_ACTION_CONNECT,
		/** Value: Do you want to save the current diagram before creating a new one? */
		DLG_QUESTION_SAVE_BEFORE_NEW,
		/** Value: Undo */
		WINDOW_MENU_EDIT_UNDO,
		/** Value: Do you want to save the current diagram before opening another one? */
		DLG_QUESTION_SAVE_BEFORE_OPEN,
		/** Value: Save As... */
		WINDOW_MENU_SAVEAS,
		/** Value: Adjust Connection */
		WINDOW_ACTION_CONNECT_ADJUST,
		/** Value: Catched uncought exception */
		EXCEPTION_UNCOUGHT,
		/** Value: Select tensor */
		WINDOW_CONTEXTPANEL_DRAWTENSOR_SELECT,
		/** Value: Edit */
		WINDOW_MENU_EDIT,
		/** Value: No adjustments */
		WINDOW_CONTEXTPANEL_DISABLED,
		/** Value: Select Item */
		WINDOW_ACTION_SELECT,
		/** Value: A connection consists of exactly one sink and one source */
		EXCEPTION_MODEL_CONNECTION_MISSMATCH,
		/** Value: Save current diagram? */
		DLG_QUESTION_SAVE_BEFORE_NEW_TITLE,
		/** Value: Rotate */
		WINDOW_ACTION_ROTATE,
		/** Value: Redo */
		WINDOW_MENU_EDIT_REDO,
		/** Value: Save */
		WINDOW_MENU_SAVE,
		/** Value: Exit */
		WINDOW_MENU_FILE_EXIT,
		/** Value: Tensor */
		WINDOW_MAIN_TITLE,
		/** Value: File */
		WINDOW_MENU_FILE,
		/** Value: Draw Tensor */
		WINDOW_ACTION_DRAWTENSOR,
		/** Value: Save current diagram? */
		DLG_QUESTION_SAVE_BEFORE_OPEN_TITLE,
		/** Value: Move */
		WINDOW_ACTION_MOVE,
		/** Value: New */
		WINDOW_MENU_NEW,
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
		/** document_save_16.png */
		DOCUMENT_SAVE_16,
		/** window_exit.png */
		WINDOW_EXIT,
		/** edit_redo_24.png */
		EDIT_REDO_24,
		/** default.png */
		DEFAULT,
		/** edit_undo_16.png */
		EDIT_UNDO_16,
		/** document_export_24.png */
		DOCUMENT_EXPORT_24,
		/** document_new_16.png */
		DOCUMENT_NEW_16,
		/** document_open_24.png */
		DOCUMENT_OPEN_24,
		/** document_new_24.png */
		DOCUMENT_NEW_24,
		/** edit_undo_24.png */
		EDIT_UNDO_24,
		/** select.png */
		SELECT,
		/** document_save_24.png */
		DOCUMENT_SAVE_24,
		/** action_connect.png */
		ACTION_CONNECT,
		/** document_save_as_16.png */
		DOCUMENT_SAVE_AS_16,
		/** document_open_16.png */
		DOCUMENT_OPEN_16,
		/** overlay_rotate.png */
		OVERLAY_ROTATE,
		/** edit_redo_16.png */
		EDIT_REDO_16,
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
