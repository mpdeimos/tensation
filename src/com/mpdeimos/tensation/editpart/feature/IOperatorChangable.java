package com.mpdeimos.tensation.editpart.feature;

import com.mpdeimos.tensation.action.canvas.SelectEditPartAction;
import com.mpdeimos.tensation.model.EOperation;
import com.mpdeimos.tensation.ui.Application;
import com.mpdeimos.tensation.ui.ContextPanelContentBase;
import com.mpdeimos.tensation.ui.DividerLabel;
import com.mpdeimos.tensation.ui.RefreshablePanel;
import com.mpdeimos.tensation.util.ListUtil;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;

import resources.R;

/** feature for changing the operator. */
public interface IOperatorChangable extends IFeatureEditPart
{
	/** Sets the operator of this object. */
	public void setOperation(EOperation operation);

	/** @return the operator of this object. */
	public EOperation getOperator();

	/** feature class for EditParts with customizable operators. */
	public class Feature extends
				SelectEditPartAction.IContextFeature<IOperatorChangable>
	{
		/** the context panel for this feature. */
		private static RefreshablePanel contextPanel = null;

		/** Constructor. */
		public Feature(IOperatorChangable editPart)
		{
			super(editPart);
			createContextPanel();
		}

		@Override
		public RefreshablePanel getStaticContextPanel()
		{
			return contextPanel;
		}

		/** Inits the context panel. */
		private static void createContextPanel()
		{
			if (contextPanel != null)
				return;

			contextPanel = new ContextPanel();
		}

		/** @ignore */
		private static class ContextPanel extends ContextPanelContentBase
		{
			/** the currently selected customizables. */
			private List<IOperatorChangable> customizables = new ArrayList<IOperatorChangable>();

			/** the operation combobox model. */
			private final DefaultComboBoxModel uiOperation;

			/** flag for bypassing refresh triggers. */
			private boolean byPassRefresh = false;

			/** action listener that triggers the update actions. */
			private final ActionListener updateTrigger = new ActionListener()
				{
					@Override
					public void actionPerformed(ActionEvent e)
					{
						updateEditParts();
					}
				};

			/** Constructor. */
			private ContextPanel()
			{
				this.add(new DividerLabel(
							R.string.WINDOW_CONTEXTPANEL_OPERATOR.string()));

				this.uiOperation = new DefaultComboBoxModel();
				this.uiOperation.addElement(null);
				for (Object o : EOperation.values())
					this.uiOperation.addElement(o);
				JComboBox combo = new JComboBox(this.uiOperation);
				combo.addActionListener(this.updateTrigger);
				this.add(combo);
			}

			@Override
			public void refresh()
			{
				this.byPassRefresh = true;

				this.customizables = ListUtil.filterByClass(
							Application.getApp().getActiveCanvas().getSelectedEditParts(),
							IOperatorChangable.class);

				// Line Style
				Object commonOperation = null;

				for (IOperatorChangable o : this.customizables)
				{
					if (commonOperation == null)
					{
						commonOperation = o.getOperator();
					}
					else
					{
						if (commonOperation != o.getOperator())
						{
							commonOperation = null;
						}
					}
				}
				this.uiOperation.setSelectedItem(commonOperation);

				this.byPassRefresh = false;
			}

			/** Updates the EditParts with the current ui data. */
			private void updateEditParts()
			{
				if (this.byPassRefresh)
					return;

				EOperation commonOperation = (EOperation) ContextPanel.this.uiOperation.getSelectedItem();
				if (commonOperation == null)
					return;

				for (IOperatorChangable c : this.customizables)
				{
					c.setOperation(commonOperation);
				}

				Application.getApp().getActiveCanvas().repaint();
				refresh();
			}
		}
	}
}