package com.mpdeimos.tensation.editpart.feature;

import com.mpdeimos.tensation.action.canvas.SelectEditPartAction;
import com.mpdeimos.tensation.ui.Application;
import com.mpdeimos.tensation.ui.ContextPanelContentBase;
import com.mpdeimos.tensation.ui.DividerLabel;
import com.mpdeimos.tensation.ui.RefreshablePanel;
import com.mpdeimos.tensation.util.Log;

import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import resources.R;

/**
 * Interface for EditParts that have customizable labels.
 * 
 * @author mpdeimos
 * 
 */
public interface ILabeled extends IFeatureEditPart
{
	/** Sets the label of this object. */
	public void setLabel(String label);

	/** @return the label of this object. */
	public String getLabel();

	/** feature class for EditParts with customizable labels. */
	public class Feature extends
			SelectEditPartAction.IContextFeature<ILabeled>
	{
		/** the context panel for this feature. */
		private RefreshablePanel contextPanel = null;

		/** Constructor. */
		public Feature(ILabeled editPart)
		{
			super(editPart);
			createContextPanel();
		}

		@Override
		public RefreshablePanel getStaticContextPanel()
		{
			// this feature just works for single selections
			if (Application.getApp().getActiveCanvas().getSelectedEditParts().size() != 1)
				return null;

			return this.contextPanel;
		}

		/** Inits the context panel. */
		private void createContextPanel()
		{
			if (this.contextPanel != null)
				return;

			this.contextPanel = new ContextPanel();
		}

		/** @ignore */
		private class ContextPanel extends ContextPanelContentBase
		{
			/** the ui text field for setting a label. */
			private final JTextField uiLabel;

			/** Constructor. */
			private ContextPanel()
			{
				this.add(new DividerLabel(
						R.string.WINDOW_CONTEXTPANEL_LABEL.string()));

				this.uiLabel = new JTextField();
				this.uiLabel.getDocument().addDocumentListener(
						new DocumentListener()
				{

					@Override
					public void removeUpdate(DocumentEvent e)
					{
						updateModel();
					}

					@Override
					public void insertUpdate(DocumentEvent e)
					{
						updateModel();
					}

					@Override
					public void changedUpdate(DocumentEvent e)
					{
						updateModel();
					}
				});
				this.add(this.uiLabel);

			}

			/** bypassing flag. */
			private boolean myChange = false;

			/** Updates the model data. */
			public void updateModel()
			{
				if (this.myChange)
					return;

				Log.v(this, "Label changed to %s", this.uiLabel.getText()); //$NON-NLS-1$
				Feature.this.editPart.setLabel(this.uiLabel.getText());
			}

			@Override
			public void refresh()
			{
				this.myChange = true;
				this.uiLabel.setText(Feature.this.editPart.getLabel());
				this.myChange = false;
			}
		}
	}
}
