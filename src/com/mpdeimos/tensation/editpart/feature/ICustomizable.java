package com.mpdeimos.tensation.editpart.feature;

import com.mpdeimos.tensation.action.canvas.SelectEditPartAction;
import com.mpdeimos.tensation.ui.Application;
import com.mpdeimos.tensation.ui.ContextPanelContentBase;
import com.mpdeimos.tensation.ui.DividerLabel;
import com.mpdeimos.tensation.ui.RefreshablePanel;
import com.mpdeimos.tensation.util.LayoutUtil;
import com.mpdeimos.tensation.util.ListUtil;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JPanel;

import resources.R;

/**
 * Interface for EditParts that have customizable appearance attributes.
 * 
 * @author mpdeimos
 * 
 */
public interface ICustomizable extends IFeatureEditPart
{
	/** @return the color of the editpart */
	public Color getColor();

	/** sets the color of the editpart. */
	public void setColor(Color color);

	/** feature class for EditParts with connection sources. */
	public class Feature extends
			SelectEditPartAction.IContextFeature<ICustomizable>
	{
		/** the context panel for this feature. */
		private static RefreshablePanel contextPanel = null;

		/** Constructor. */
		public Feature(ICustomizable editPart)
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
			List<ICustomizable> customizables = new ArrayList<ICustomizable>();

			/** The common color of all editparts. */
			Color commonColor = null;

			/** the color preview rect. */
			private final JPanel uiColorPreview;

			/** Constructor. */
			private ContextPanel()
			{
				this.add(new DividerLabel(
						R.string.WINDOW_CONTEXTPANEL_CUSTOMIZE_APPEARANCE.string()));

				this.uiColorPreview = new JPanel();
				LayoutUtil.setSize(this.uiColorPreview, 24, 12);
				JButton colorChooser = new JButton(new AbstractAction()
				{
					@Override
					public void actionPerformed(ActionEvent arg0)
					{
						Color color = JColorChooser.showDialog(
								ContextPanel.this,
								R.string.WINDOW_CONTEXTPANEL_CUSTOMIZE_APPEARANCE_COLOR_TITLE.string(),
								ContextPanel.this.commonColor);

						if (color != null)
						{
							ContextPanel.this.commonColor = color;
							updateEditParts();
						}
					}
				});
				colorChooser.add(this.uiColorPreview);
				this.add(label(
						R.string.WINDOW_CONTEXTPANEL_CUSTOMIZE_APPEARANCE_COLOR.string(),
						colorChooser));
			}

			@Override
			public void refresh()
			{
				this.customizables = ListUtil.filterByClass(
						Application.getApp().getActiveCanvas().getSelectedEditParts(),
						ICustomizable.class);

				this.commonColor = null;
				for (ICustomizable c : this.customizables)
				{
					if (this.commonColor == null)
					{
						this.commonColor = c.getColor();
					}

					if (this.commonColor == null
							|| !this.commonColor.equals(c.getColor()))
					{
						this.commonColor = null;
						break;
					}
				}
				this.uiColorPreview.setBackground(this.commonColor);
			}

			/** Updates the EditParts with the current ui data. */
			private void updateEditParts()
			{
				if (this.commonColor != null)
				{
					for (ICustomizable c : this.customizables)
					{
						c.setColor(this.commonColor);
					}
				}
				Application.getApp().getActiveCanvas().repaint();
				refresh();
			}
		}
	}
}
